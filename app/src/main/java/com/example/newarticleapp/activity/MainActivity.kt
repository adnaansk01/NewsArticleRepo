package com.example.newarticleapp.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.newarticleapp.adapter.NewsAdapter
import com.example.newarticleapp.databinding.ActivityMainBinding
import com.example.newarticleapp.utils.Resource
import com.example.newarticleapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var adapter: NewsAdapter

    // Provide API key from BuildConfig or secure source // set in build.gradle: buildConfigField("String", "NEWS_API_KEY", "\"your_key\"")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        observeUiState()

        // trigger loading
        viewModel.fetchTopHeadlines()
    }

    private fun setupRecycler() {
        adapter = NewsAdapter { article ->
            // item click
            // e.g. open article.url in Custom Tab or WebView
            article.url?.let { url ->
                // open in Custom Tab (omitted) or use Intent
            }
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.uiState.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.visibility = View.GONE
                            adapter.submitList(resource.data ?: emptyList())
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.visibility = View.VISIBLE
                            binding.tvError.text = resource.message ?: "Unknown error"
                            adapter.submitList(emptyList())
                        }
                    }
                }
            }
        }
    }
}

