package com.github.moviesapi.repository.movies_list

import com.github.moviesapi.network.Result
import com.github.moviesapi.network.response.MoviesResponse
import timber.log.Timber

import javax.inject.Inject

class MoviesListRepository @Inject constructor(private val dataSource: MoviesListDataSource) {
    suspend fun fetchMovies(page: Int, callback: (Result<MoviesResponse>) -> Unit) {
        val response = dataSource.discoverMovies(page)
        if (response.status == Result.Status.SUCCESS) {
            callback(response)
        } else if (response.status == Result.Status.ERROR) {
            Timber.d(response.message)
        }
    }
}