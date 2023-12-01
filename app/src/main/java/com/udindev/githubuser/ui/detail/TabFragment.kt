package com.udindev.githubuser.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.udindev.githubuser.databinding.FragmentTabBinding
import com.udindev.githubuser.ui.adapter.FollowAdapter
import com.udindev.githubuser.util.Result


class TabFragment : Fragment() {

    private var _binding: FragmentTabBinding? = null
    private val binding get() = _binding
    private val viewModel: DetailUserViewModel by viewModels {
        DetailUserViewModelFactory.getInstance(requireActivity())
    }

    private var username: String = ""
    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_SECTION_NUMBER)
            username = it.getString(ARG_USERNAME).toString()
        }

        if (position == 1) {
            viewModel.getFollowers(username)
            setUserFollowers()
        } else {
            viewModel.getFollowing(username)
            setUserFollowing()
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }

    private fun setUserFollowers() {
        val userFollowersAdapter = FollowAdapter()
        binding?.rvFollowUser?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = userFollowersAdapter

            val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            binding?.rvFollowUser?.addItemDecoration(itemDecoration)
        }

        viewModel.getFollowers(username).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireActivity(), "Error Get Data", Toast.LENGTH_SHORT).show()
                }

                Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    userFollowersAdapter.submitList(result.data)
                }
            }
        }
    }

    private fun setUserFollowing() {
        val userFollowingAdapter = FollowAdapter()
        binding?.rvFollowUser?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = userFollowingAdapter
        }

        viewModel.getFollowing(username).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    showLoading(false)
                }

                Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    userFollowingAdapter.submitList(result.data)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}