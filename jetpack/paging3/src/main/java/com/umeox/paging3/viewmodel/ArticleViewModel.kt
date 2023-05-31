package com.umeox.paging3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.umeox.paging3.data.Article
import com.umeox.paging3.data.ArticleRepository
import com.umeox.paging3.data.firstArticleCreatedTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEMS_PER_PAGE = 50
class ArticleViewModel(
    private val repository: ArticleRepository,
) : ViewModel(){

    val allArticlePagingData: Flow<PagingData<Article>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            maxSize = 200
        ),
        // 通过 room 获取 PagingSource
        pagingSourceFactory = { repository.getAllArticleById() }
    )
        .flow
        .cachedIn(viewModelScope)


    private val articleList = (0..500).map {
        Article(
            //id = it,
            id = 0,
            title = "Article $it",
            description = "This describes article $it",
            // minusDays(int n) 生成当前日期之前 n 天的日期
            created = firstArticleCreatedTime.minusDays(it.toLong())
        )
    }

    fun addLocalList() {
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                //处理耗时操作
                repository.addAllList(
                    articleList
                )
            }
        }
    }


    fun delete(article: Article) {
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                //处理耗时操作
                repository.delete(article)
            }
        }

    }
}