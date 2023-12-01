package com.udindev.githubuser.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.udindev.githubuser.databinding.FragmentHomeBinding
import com.udindev.githubuser.ui.adapter.ListUserAdapter
import com.udindev.githubuser.ui.favorite.FavoriteActivity
import com.udindev.githubuser.ui.setting.SettingActivity
import com.udindev.githubuser.util.Result

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.contentSearch?.searchBar?.setOnClickListener {
            searchUserByUsername()
        }

        binding?.contentSearch?.ibFavoritePage?.setOnClickListener {
            val intent = Intent(requireActivity(), FavoriteActivity::class.java)
            startActivity(intent)
        }

        binding?.contentSearch?.ibSetting?.setOnClickListener {
            val intent = Intent(requireActivity(), SettingActivity::class.java)
            startActivity(intent)
        }

        setListUser()
    }

    private fun setListUser() {
        val userListAdapter = ListUserAdapter { user ->
            if (user.isFavorite) {
                viewModel.removeFromFavorite(user)
            } else {
                viewModel.addToFavorite(user)
            }

        }

        binding?.rvUser?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = userListAdapter
        }

        viewModel.getAllUser().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            requireActivity(),
                            "Load Data Error ${result.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        val listUser = result.data
                        userListAdapter.submitList(listUser)
                    }
                }
            }
        }
    }

    private fun searchUserByUsername() {
        val userListAdapter = ListUserAdapter { user ->
            if (user.isFavorite) {
                viewModel.removeFromFavorite(user)
            } else {
                viewModel.addToFavorite(user)
            }
        }
        binding?.rvUser?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = userListAdapter
        }
        with(binding?.contentSearch) {
            binding?.contentSearch?.searchView?.setupWithSearchBar(binding?.contentSearch?.searchBar)
            this?.searchView
                ?.editText
                ?.setOnEditorActionListener { _, _, _ ->
                    searchBar.text = searchView.text
                    viewModel.searchUser(searchBar.text.toString())
                        .observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Result.Error -> {
                                    showLoading(false)
                                    Log.d("Username Null", "searchUserByUsername: ${result.error}")
                                }

                                Result.Loading -> {
                                    showLoading(true)
                                }

                                is Result.Success -> {
                                    showLoading(false)
                                    userListAdapter.submitList(result.data)
                                    if (result.data.isEmpty()) {
                                        viewModel.showSnackBar().observe(viewLifecycleOwner) {
                                            it.getUserNotFound().let {
                                                Snackbar.make(
                                                    requireActivity().window.decorView.rootView,
                                                    "User Not Found",
                                                    Snackbar.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    searchView.hide()
                    false
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