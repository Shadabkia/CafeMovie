package com.cafe.movie.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.cafe.movie.databinding.ActivityMainBinding
import com.cafe.movie.ui.adapter.MovieListener
import com.cafe.movie.ui.adapter.MoviesAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MovieListener {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    private val movieAdapter = MoviesAdapter(this)

    private var animationSet: AnimationSet? = null

    private var spanCount = 3;
    private val layoutManager = GridLayoutManager(this, spanCount)


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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setOrientationSpanCount(newConfig.orientation)
    }

    private fun setOrientationSpanCount(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 6; // Set span count for landscape orientation
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 3; // Set span count for portrait orientation
        }

        layoutManager.spanCount = spanCount
        binding.rvMovies.layoutManager = layoutManager
    }

    private fun initViews() {

        setOrientationSpanCount(resources.configuration.orientation)
        initListeners()

        lifecycleScope.launch {
            viewModel.movies.collect {
                if (it != null) {
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
            }

            // Manage loadings and retry buttons
            lifecycleScope.launch {
                movieAdapter.loadStateFlow.collectLatest {
                    Timber.tag("loadStateFlow")
                        .d("append ${it.append} prepend ${it.prepend} refresh ${it.refresh}")

                    clLoadMoreError.isVisible = it.append is LoadState.Error
                    pbLoadMore.isVisible = it.append is LoadState.Loading

                    if (it.refresh is LoadState.Error) {
                        clMainError.isVisible =
                            (it.refresh as LoadState.Error).error.message == "No Data"
                    } else {
                        clMainError.isVisible = false
                    }

                    if (it.refresh is LoadState.Loading) {
                        clLogoLoading.isVisible = true
                    } else {
                        // prevent repeat animation
                        if (animationSet == null && !clMainError.isVisible) {
                            animateIvLogo()
                        }
                    }
                }
            }
        }
    }

    private fun animateIvLogo() {
        binding.apply {
            // Get the position of ivLogo
            val location1 = IntArray(2)
            ivLogo.getLocationOnScreen(location1)
            val x1 = location1[0].toFloat()
            val y1 = location1[1].toFloat()

            // Get the position of ivAppbarLogo
            val location2 = IntArray(2)
            ivAppbarLogo.getLocationOnScreen(location2)

            val x2 = location2[0].toFloat()
            val y2 = location2[1].toFloat()

            val translateAnimation = TranslateAnimation(
                Animation.ABSOLUTE, 0f, Animation.ABSOLUTE,
                (0.96 * x2).toFloat(), Animation.ABSOLUTE, 0f, Animation.ABSOLUTE,
                (2.12 * (y2 - y1)).toFloat()
            ).apply {
                duration = 1000 // 1 second
                fillAfter = true // Keeps the result after the animation
            }

            // Define a ScaleAnimation
            val scaleAnimation = ScaleAnimation(
                1f, 0.5f, // X scaling
                1f, 0.5f, // Y scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X
                Animation.RELATIVE_TO_SELF, 0.5f // Pivot Y
            ).apply {
                duration = 1000 // 1 second
                fillAfter = true // Keeps the result after the animation
            }

            animationSet = AnimationSet(true).apply {
                addAnimation(translateAnimation)
                addAnimation(scaleAnimation)
            }

            ivLogo.startAnimation(animationSet)
            animationSet?.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    clLogoLoading.isVisible = false
                    animationSet = null;
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })
        }

    }

    private fun initListeners() {
        binding.apply {
            btLoadMoreTry.setOnClickListener {
                movieAdapter.retry()
            }

            btRetry.setOnClickListener {
                movieAdapter.refresh()
            }

            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                movieAdapter.refresh()
            }
        }
    }

    override fun onMovieClicked(view: View, movieTitle: String?) {
        val snackBar = Snackbar
            .make(view, movieTitle + "", Snackbar.LENGTH_LONG)
        snackBar.show()
    }
}