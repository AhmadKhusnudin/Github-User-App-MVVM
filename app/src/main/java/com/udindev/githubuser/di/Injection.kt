package com.udindev.githubuser.di

import android.content.Context
import com.udindev.githubuser.data.local.room.UserDatabase
import com.udindev.githubuser.data.remote.retrofit.ApiConfig
import com.udindev.githubuser.repository.FavoriteRepository
import com.udindev.githubuser.repository.UserRepository
import com.udindev.githubuser.util.AppExecutors

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getUserFavoriteDatabase(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }

    fun provideFavoriteRepository(context: Context): FavoriteRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getUserFavoriteDatabase(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        return FavoriteRepository.getInstance(apiService, dao, appExecutors)
    }
}