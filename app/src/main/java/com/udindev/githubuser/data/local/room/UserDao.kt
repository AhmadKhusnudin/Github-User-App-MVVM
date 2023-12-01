package com.udindev.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.udindev.githubuser.data.local.entity.User
import com.udindev.githubuser.util.Result

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE favorite = 1")
    fun getFavoriteUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(user: List<User>)

    @Update
    suspend fun addAndRemoveUserFavorite(user: User)

    @Query("DELETE FROM users WHERE favorite = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM users WHERE login = :login AND favorite = 1)")
    suspend fun isUserFavorite(login: String): Boolean

}