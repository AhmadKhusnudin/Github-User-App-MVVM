package com.udindev.githubuser.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udindev.githubuser.data.local.entity.User
import com.udindev.githubuser.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getAllUser() = userRepository.getListUser()

    fun searchUser(username: String) = userRepository.searchUser(username)

    fun showSnackBar() = userRepository.snackBar

    fun addToFavorite(user: User) {
        viewModelScope.launch {
            userRepository.addRemoveFavoriteUser(user, true)
        }
    }

    fun removeFromFavorite(user: User) {
        viewModelScope.launch {
            userRepository.addRemoveFavoriteUser(user, false)
        }
    }
}

