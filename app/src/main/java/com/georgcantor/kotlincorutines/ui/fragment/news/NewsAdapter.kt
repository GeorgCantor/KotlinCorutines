package com.georgcantor.kotlincorutines.ui.fragment.news

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.georgcantor.kotlincorutines.R
import com.georgcantor.kotlincorutines.model.response.Article
import com.georgcantor.kotlincorutines.util.*
import com.georgcantor.kotlincorutines.util.Constants.ANIM_PLAYBACK_SPEED

class NewsAdapter(
    private val context: Context,
    private val articles: MutableList<Article>,
    private val transitionFunc: (View, View, FrameLayout) -> Unit
) : RecyclerView.Adapter<NewsAdapter.ListViewHolder>() {

    private val originalBg: Int by bindColor(context, R.color.list_item_bg_collapsed)
    private val expandedBg: Int by bindColor(context, R.color.list_item_bg_expanded)

    private val listItemHorizontalPadding: Float by bindDimen(context, R.dimen.list_item_horizontal_padding)
    private val listItemVerticalPadding: Float by bindDimen(context, R.dimen.list_item_vertical_padding)
    private val originalWidth = context.screenWidth - 48.dp
    private val expandedWidth = context.screenWidth - 24.dp
    private var originalHeight = -1
    private var expandedHeight = -1

    private val listItemExpandDuration: Long get() = (300L / ANIM_PLAYBACK_SPEED).toLong()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private lateinit var recyclerView: RecyclerView
    private var expandedArticle: Article? = null
    private var isScaledDown = false

    override fun getItemCount(): Int = articles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(inflater.inflate(R.layout.item_article, parent, false))

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val article = articles[position]

        expandItem(holder, article == expandedArticle, animate = false)
        scaleDownItem(holder, position, isScaledDown)

        with(holder) {
            title.text = article.title
            description.text = article.description

            context.loadImage(article.urlToImage, imageSmall)
            context.loadImage(article.urlToImage, image)

            cardContainer.setOnClickListener {
                when (expandedArticle) {
                    null -> {
                        // expand clicked view
                        expandItem(this, expand = true, animate = true)
                        expandedArticle = article
                        transitionFunc(imageSmall, image, rootLayout)
                        imageSmall.visibility = INVISIBLE
                        image.visibility = VISIBLE
                    }
                    article -> {
                        // collapse clicked view
                        expandItem(this, expand = false, animate = true)
                        expandedArticle = null
                        transitionFunc(image, imageSmall, rootLayout)
                        imageSmall.visibility = VISIBLE
                    }
                    else -> {
                        // collapse previously expanded view
                        val expandedModelPosition = articles.indexOf(expandedArticle!!)
                        val oldViewHolder =
                            recyclerView.findViewHolderForAdapterPosition(expandedModelPosition) as? ListViewHolder
                        if (oldViewHolder != null) expandItem(
                            oldViewHolder,
                            expand = false,
                            animate = true
                        )
                        image.visibility = INVISIBLE

                        // expand clicked view
                        expandItem(this, expand = true, animate = true)
                        expandedArticle = article
                        Handler().postDelayed({
                            transitionFunc(imageSmall, image, rootLayout)
                            imageSmall.visibility = INVISIBLE
                            image.visibility = VISIBLE
                        }, 300)
                    }
                }
            }
        }
    }

    fun updateArticles(articles: List<Article>) {
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    private fun expandItem(holder: ListViewHolder, expand: Boolean, animate: Boolean) {
        if (animate) {
            val animator = getValueAnimator(
                expand, listItemExpandDuration, AccelerateDecelerateInterpolator()
            ) { progress -> setExpandProgress(holder, progress) }

            if (expand) animator.doOnStart { holder.expandView.isVisible = true }
            else animator.doOnEnd { holder.expandView.isVisible = false }

            animator.start()
        } else {
            // show expandView only if we have expandedHeight (onViewAttached)
            holder.expandView.isVisible = expand && expandedHeight >= 0
            setExpandProgress(holder, if (expand) 1f else 0f)
        }
    }

    override fun onViewAttachedToWindow(holder: ListViewHolder) {
        super.onViewAttachedToWindow(holder)
        // get originalHeight & expandedHeight if not gotten before
        if (expandedHeight < 0) {
            expandedHeight = 0 // so that this block is only called once

            holder.cardContainer.doOnLayout { view ->
                originalHeight = view.height
                // show expandView and record expandedHeight in next layout pass
                // (doOnPreDraw) and hide it immediately. We use onPreDraw because
                // it's called after layout is done. doOnNextLayout is called during
                // layout phase which causes issues with hiding expandView.
                holder.expandView.isVisible = true
                view.doOnPreDraw {
                    expandedHeight = view.height
                    holder.expandView.isVisible = false
                }
            }
        }
    }

    private fun setExpandProgress(holder: ListViewHolder, progress: Float) {
        if (expandedHeight > 0 && originalHeight > 0) {
            holder.cardContainer.layoutParams.height =
                (originalHeight + (expandedHeight - originalHeight) * progress).toInt()
        }
        holder.cardContainer.layoutParams.width =
            (originalWidth + (expandedWidth - originalWidth) * progress).toInt()

        holder.cardContainer.setBackgroundColor(blendColors(originalBg, expandedBg, progress))
        holder.cardContainer.requestLayout()

        holder.chevron.rotation = 90 * progress
    }

    /**Scale Down Animation*/
    private inline val LinearLayoutManager.visibleItemsRange: IntRange
        get() = findFirstVisibleItemPosition()..findLastVisibleItemPosition()

    fun getScaleDownAnimator(isScaledDown: Boolean): ValueAnimator {
        val lm = recyclerView.layoutManager as LinearLayoutManager

        val animator = getValueAnimator(
            isScaledDown,
            duration = 300L,
            interpolator = AccelerateDecelerateInterpolator()
        ) { progress ->
            // Get viewHolder for all visible items and animate attributes
            for (i in lm.visibleItemsRange) {
                val holder = recyclerView.findViewHolderForLayoutPosition(i) as ListViewHolder
                setScaleDownProgress(holder, i, progress)
            }
        }

        // Set adapter variable when animation starts so that newly binded views in
        // onBindViewHolder will respect the new size when they come into the screen
        animator.doOnStart { this.isScaledDown = isScaledDown }

        // For all the non visible items in the layout manager, notify them to adjust the
        // view to the new size
        animator.doOnEnd {
            repeat(lm.itemCount) { if (it !in lm.visibleItemsRange) notifyItemChanged(it) }
        }
        return animator
    }

    private fun setScaleDownProgress(holder: ListViewHolder, position: Int, progress: Float) {
        val itemExpanded = position >= 0 && articles[position] == expandedArticle
        holder.cardContainer.layoutParams.apply {
            width = ((if (itemExpanded) expandedWidth else originalWidth) * (1 - 0.1f * progress)).toInt()
            height = ((if (itemExpanded) expandedHeight else originalHeight) * (1 - 0.1f * progress)).toInt()
        }
        holder.cardContainer.requestLayout()

        holder.scaleContainer.scaleX = 1 - 0.05f * progress
        holder.scaleContainer.scaleY = 1 - 0.05f * progress

        holder.scaleContainer.setPadding(
            (listItemHorizontalPadding * (1 - 0.2f * progress)).toInt(),
            (listItemVerticalPadding * (1 - 0.2f * progress)).toInt(),
            (listItemHorizontalPadding * (1 - 0.2f * progress)).toInt(),
            (listItemVerticalPadding * (1 - 0.2f * progress)).toInt()
        )

        holder.listItemFg.alpha = progress
    }

    private fun scaleDownItem(holder: ListViewHolder, position: Int, isScaleDown: Boolean) {
        setScaleDownProgress(holder, position, if (isScaleDown) 1f else 0f)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootLayout: FrameLayout by bindView(R.id.root_layout)
        val title: TextView by bindView(R.id.title)
        val description: TextView by bindView(R.id.description)
        val imageSmall: ImageView by bindView(R.id.image_small)
        val image: ImageView by bindView(R.id.image)
        val expandView: View by bindView(R.id.expand_view)
        val chevron: View by bindView(R.id.chevron)
        val cardContainer: View by bindView(R.id.card_container)
        val scaleContainer: View by bindView(R.id.scale_container)
        val listItemFg: View by bindView(R.id.list_item_fg)
    }
}