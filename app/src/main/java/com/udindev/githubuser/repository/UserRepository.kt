package com.udindev.githubuser.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.udindev.githubuser.data.local.entity.User
import com.udindev.githubuser.data.local.room.UserDao
import com.udindev.githubuser.data.remote.response.DetailUserResponse
import com.udindev.githubuser.data.remote.response.FollowResponseItem
import com.udindev.githubuser.data.remote.retrofit.ApiConfig
import com.udindev.githubuser.data.remote.retrofit.ApiService
import com.udindev.githubuser.util.AppExecutors
import com.udindev.githubuser.util.Event
import com.udindev.githubuser.util.Result

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val appExecutors: AppExecutors
) {
    companion object {
        private const val USER = "a"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, appExecutors)
            }.also { instance = it }
    }


    private val _snackBar = MutableLiveData<Event<String>>()
    val snackBar: LiveData<Event<String>> = _snackBar

    fun getListUser(): LiveData<Result<List<User>>> = liveData {
        emit(Result.Loading)
        try {
            val response = ApiConfig.getApiService().getAllUser(USER)
            val users = response.items
            val usersList = users.map { user ->
                val isFavorite = userDao.isUserFavorite(user.login)
                User(
                    user.nodeId,
                    user.login,
                    user.type,
                    user.avatarUrl,
                    isFavorite
                )
            }
            userDao.deleteAll()
            userDao.insertUsers(usersList)

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }

        val localData: LiveData<Result<List<User>>> =
            userDao.getAllUsers().map { Result.Success(it) }
        emitSource(localData)
    }

    fun searchUser(username: String): LiveData<Result<List<User>>> = liveData {
        emit(Result.Loading)
        try {
            val response = ApiConfig.getApiService().searchUser(username)
            val users = response.items

            if (users.isEmpty()) {
                val message = "User Not Found"
                _snackBar.value = Event(message)
            }

            val usersList = users.map { user ->
                val isFavorite = userDao.isUserFavorite(user.login)
                User(
                    user.nodeId,
                    user.login,
                    user.type,
                    user.avatarUrl,
                    isFavorite
                )
            }
            userDao.deleteAll()
            userDao.insertUsers(usersList)

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }

        val localData: LiveData<Result<List<User>>> =
            userDao.getAllUsers().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getDetailUser(username: String): LiveData<Result<DetailUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = ApiConfig.getApiService().getDetailUser(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }


    }

    fun getFollowers(username: String): LiveData<Result<List<FollowResponseItem>>> = liveData {
        emit(Result.Loading)
        val response = ApiConfig.getApiService().getFollowers(username)
        try {
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFollowing(username: String): LiveData<Result<List<FollowResponseItem>>> = liveData {
        emit(Result.Loading)
        val response = ApiConfig.getApiService().getFollowing(username)
        try {
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun addRemoveFavoriteUser(user: User, favoriteState: Boolean) {
        user.isFavorite = favoriteState
        userDao.addAndRemoveUserFavorite(user)
    }
}