package com.udindev.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.udindev.githubuser.data.remote.response.FollowResponseItem
import com.udindev.githubuser.databinding.ItemUserFollowVerticalBinding

class FollowAdapter :
    ListAdapter<FollowResponseItem, FollowAdapter.FollowViewHolder>(DIFF_CALLBACK) {
    class FollowViewHolder(private val binding: ItemUserFollowVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FollowResponseItem) {
            binding.tvName.text = user.login

            Glide.with(binding.ivAvatarFollow)
                .load(user.avatarUrl)
                .into(binding.ivAvatarFollow)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val binding = ItemUserFollowVerticalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FollowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowResponseItem>() {
            override fun areContentsTheSame(
                oldItem: FollowResponseItem,
                newItem: FollowResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: FollowResponseItem,
                newItem: FollowResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}