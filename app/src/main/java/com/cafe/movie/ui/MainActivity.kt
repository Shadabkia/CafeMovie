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
                } else {
                    Toast.makeText(this@MainActivity, "null", Toast.LENGTH_SHORT).show()
                }
            }

        }

        findViewById<TextView>(R.id.text).setOnClickListener {
            testMoshi()
        }

        Timber.tag("moooshi").d("onCreate: ")

    }

    private fun testMoshi() {
        val d = Log.d("moooshi", "testMoshi: ")

        val movie = Movie(
            adult = false,
            backdropPath = null,
            genreIds = null,
            id = 0,
            originalLanguage = null,
            originalTitle = null,
            overview = null,
            popularity = 0.0,
            posterPath = null,
            releaseDate = null,
            title = null,
            video = false,
            voteAverage = 0.0,
            voteCount = 0
        )

        val movieListResponse = MovieListResponse(
            page = 90,
            movies = List(1) { movie },
            totalPages = 0,
            totalResults = 0
        )

        val moshiHelper = MoshiHelper

        var json =
            moshiHelper.getInstance().moshiToJson(movieListResponse, MovieListResponse::class.java)

        Log.d("moooshi", "json: $json")
    }
}