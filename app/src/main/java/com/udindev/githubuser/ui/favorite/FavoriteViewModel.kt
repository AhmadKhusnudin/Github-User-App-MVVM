package com.udindev.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udindev.githubuser.data.local.entity.User
import com.udindev.githubuser.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    fun getFavoriteUser() = favoriteRepository.getFavoriteUsers()

    fun removeUserFromFavorite(user: User) {
        viewModelScope.launch {
            favoriteRepository.removeUserFromFavorite(user, false)
        }
    }
}