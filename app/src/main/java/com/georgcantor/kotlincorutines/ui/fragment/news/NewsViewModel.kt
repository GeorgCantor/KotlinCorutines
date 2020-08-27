package com.georgcantor.kotlincorutines.ui.fragment.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgcantor.kotlincorutines.model.response.Article
import com.georgcantor.kotlincorutines.repository.Repository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: Repository
) : ViewModel() {

    val news = MutableLiveData<List<Article>>()
    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val error = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    fun getNews(query: String?, page: Int) {
        viewModelScope.launch(exceptionHandler) {
            repository.getNews(query ?: "", page).apply {
                errorBody()?.let { error.postValue(it.toString()) }
                news.postValue(body()?.articles)
            }
            isProgressVisible.postValue(false)
        }
    }
}