package com.cafe.movie.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cafe.movie.R
import com.cafe.movie.data.network.dto.Movie
import com.cafe.movie.data.network.dto.MovieListResponse
import com.cafe.movie.utils.MoshiHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            viewModel.movies.collect { list ->
                if (list != null) {
                    Toast.makeText(this@MainActivity, "list", Toast.LENGTH_SHORT).show()
                }
            }

        }
        Timber.tag("moooshi").d("onCreate: ")
    }
}