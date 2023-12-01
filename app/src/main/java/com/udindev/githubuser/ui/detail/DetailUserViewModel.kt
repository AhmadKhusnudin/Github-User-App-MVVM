package com.udindev.githubuser.ui.detail

import androidx.lifecycle.ViewModel
import com.udindev.githubuser.repository.UserRepository

class DetailUserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getDetailUser(username: String) = userRepository.getDetailUser(username)

    fun getFollowers(username: String) = userRepository.getFollowers(username)

    fun getFollowing(username: String) = userRepository.getFollowing(username)

}
