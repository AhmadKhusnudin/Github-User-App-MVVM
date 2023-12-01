package com.udindev.githubuser.repository

import androidx.lifecycle.LiveData
import com.udindev.githubuser.data.local.entity.User
import com.udindev.githubuser.data.local.room.UserDao
import com.udindev.githubuser.data.remote.retrofit.ApiService
import com.udindev.githubuser.util.AppExecutors

class FavoriteRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val executors: AppExecutors
) {

    fun getFavoriteUsers(): LiveData<List<User>> {
        return userDao.getFavoriteUsers()
    }

    suspend fun removeUserFromFavorite(user: User, favoriteState: Boolean) {
        user.isFavorite = favoriteState
        userDao.addAndRemoveUserFavorite(user)
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            executors: AppExecutors
        ): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(apiService, userDao, executors)
            }.also { instance = it }
    }
}