package com.udindev.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.udindev.githubuser.R
import com.udindev.githubuser.databinding.ActivityDetailUserBinding
import com.udindev.githubuser.ui.adapter.SectionPagerAdapter
import com.udindev.githubuser.util.Result

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel: DetailUserViewModel by viewModels {
        DetailUserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USER) ?: ""

        viewModel.getDetailUser(username).observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        this,
                        "Terjadi Kesalahan ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    binding.tvUserLogin.text = result.data.login
                    binding.tvNumberFollowers.text = result.data.followers.toString()
                    binding.tvNumberFollowing.text = result.data.following.toString()
                    binding.tvName.text = result.data.name
                    binding.tvNumberPublicRepository.text = result.data.publicRepos.toString()
                    binding.tvNumberPublicGists.text = result.data.publicGists.toString()

                    Glide.with(this)
                        .load(result.data.avatarUrl)
                        .into(binding.ivAvatar)

                    binding.btnShare.setOnClickListener {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, result.data.url)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(intent, null)
                        startActivity(shareIntent)
                    }
                }
            }
        }

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = username
        val viewPager = binding.viewPager
        viewPager.adapter = sectionPagerAdapter

        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private val TAB_TITLE = intArrayOf(
            R.string.followers,
            R.string.following
        )

        const val EXTRA_USER = "user"
    }
}