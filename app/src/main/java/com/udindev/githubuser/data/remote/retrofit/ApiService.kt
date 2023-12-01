package com.udindev.githubuser.data.remote.retrofit

import com.udindev.githubuser.data.remote.response.DetailUserResponse
import com.udindev.githubuser.data.remote.response.FollowResponseItem
import com.udindev.githubuser.data.remote.response.GitHubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun getAllUser(
        @Query("q") q: String
    ): GitHubResponse

    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String
    ): GitHubResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): DetailUserResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String
    ): List<FollowResponseItem>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String
    ): List<FollowResponseItem>
}