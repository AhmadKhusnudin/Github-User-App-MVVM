package com.udindev.githubuser.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.udindev.githubuser.databinding.ActivityFavoriteBinding
import com.udindev.githubuser.ui.adapter.ListUserFavoriteAdapter

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListFavoriteUser()
    }

    private fun setListFavoriteUser() {
        val userListAdapter = ListUserFavoriteAdapter { user ->
            if (user.isFavorite) {
                viewModel.removeUserFromFavorite(user)
            }
        }
        binding.rvFavoriteUser.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = userListAdapter
        }
        viewModel.getFavoriteUser().observe(this) { users ->
            binding.progressBar.visibility = View.GONE
            userListAdapter.submitList(users)
        }
    }
}