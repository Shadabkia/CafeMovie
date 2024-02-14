package com.cafe.movie.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowMetrics
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cafe.movie.R
import com.cafe.movie.data.network.dto.Movie
import com.cafe.movie.data.network.dto.MovieListResponse
import com.cafe.movie.databinding.ActivityMainBinding
import com.cafe.movie.ui.adapter.MovieListener
import com.cafe.movie.ui.adapter.MoviesAdapter
import com.cafe.movie.utils.MoshiHelper
import com.cafe.movie.utils.PagingLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MovieListener{

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    val movieAdapter = MoviesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainActivityEvents.collect {
                    when (it) {
                        MainActivityEvents.InitViews -> initViews()

                    }
                }
            }
        }
        viewModel.activityCreated()

        Timber.tag("moooshi").d("onCreate: ")
    }

    private fun initViews() {
        lifecycleScope.launch {
            viewModel.movies.collect {
                if (it != null) {
                    Toast.makeText(this@MainActivity, "list", Toast.LENGTH_SHORT).show()
                    movieAdapter.submitData(it)
                }
            }
        }

        binding.apply {
            rvMovies.apply {
                adapter = movieAdapter
                itemAnimator = null
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }

                adapter = movieAdapter.withLoadStateFooter(
                    footer = PagingLoadStateAdapter(movieAdapter)
                )
            }
        }
    }

    override fun onMovieClicked(view: View, movieId: Int?) {

    }
}