package com.umeox.paging3.data

import androidx.paging.PagingSource
import com.umeox.paging3.dao.ArticleDao
import com.umeox.paging3.paging.ArticlePagingSource
import java.time.LocalDateTime

val firstArticleCreatedTime = LocalDateTime.now()

// 定义 Repository 类
class ArticleRepository(val articleDao: ArticleDao) {

    // 创建 flow 数据流
/*    val articleStream: Flow<List<Article>> = flowOf(
        (0..500).map {
            Article(
                id = it,
                title = "Article $it",
                description = "This describes article $it",
                // minusDays(int n) 生成当前日期之前 n 天的日期
                created = firstArticleCreatedTime.minusDays(it.toLong())
            )
    })*/

    // 使用 自定义 PagingSource
    fun articlePagingSource() = ArticlePagingSource()

    // 通过 room 获取 PagingSource
    fun getAllArticleById(): PagingSource<Int, Article> {
        return articleDao.getAllArticleById()
    }

    suspend fun addAllList(articleList: List<Article>){
        articleDao.addAllList(articleList)
    }

    suspend fun delete(article: Article){
        articleDao.deleteArticle(article)
    }

}
