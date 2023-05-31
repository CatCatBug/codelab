package com.umeox.paging3

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.umeox.paging3.data.ArticleRepository
import com.umeox.paging3.db.AppDatabase
import com.umeox.paging3.viewmodel.ViewModelFactory

object Injection {

    // 使用 单例 创建 repository
    private fun provideArticleRepository(context: Context): ArticleRepository = ArticleRepository(
        AppDatabase.getDatabase(context).articleDao()
    )


    fun provideViewModelFactory(owner: SavedStateRegistryOwner, context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideArticleRepository(context))
    }
}