package com.udindev.githubuser.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.udindev.githubuser.R
import com.udindev.githubuser.data.local.entity.User
import com.udindev.githubuser.databinding.ItemUserVerticalBinding
import com.udindev.githubuser.ui.detail.DetailUserActivity

class ListUserAdapter(private val onItemClicked: (User) -> Unit) :
    ListAdapter<User, ListUserAdapter.UserViewHolder>(DIFF_CALLBACK) {

    inner class UserViewHolder(val binding: ItemUserVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {

            binding.tvName.text = user.login
            binding.tvType.text = user.type?.uppercase()

            Glide.with(itemView.context)
                .load(user.avatar_url)
                .into(binding.ivAvatar)

            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_USER, user.login)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            ItemUserVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        val ivFavorite = holder.binding.ivFavorite
        if (user.isFavorite) {
            ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    ivFavorite.context,
                    R.drawable.ic_favorite
                )
            )
        } else {
            ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    ivFavorite.context,
                    R.drawable.ic_favorite_border
                )
            )
        }

        ivFavorite.setOnClickListener {
            onItemClicked(user)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areContentsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}