package com.chirikualii.materidb.data.repository

import android.util.Log
import com.chirikualii.materidb.data.local.MovieDbImpl
import com.chirikualii.materidb.data.local.dao.MovieDao
import com.chirikualii.materidb.data.local.entity.MovieEntity
import com.chirikualii.materidb.data.model.Movie
import com.chirikualii.materidb.data.model.MovieType
import com.chirikualii.materidb.data.remote.ApiService
import com.google.gson.Gson

class MovieRepoImpl(
    private val service: ApiService,
    private val movieDbImpl: MovieDbImpl): MovieRepo {

    override suspend fun getPopularMovie(): List<Movie> {
       try {
           val response = service.getPopularMovie()

           if(response.isSuccessful){
               val listMovie = response.body()

               val lisMovieEntity = listMovie?.results?.map {
                   MovieEntity(
                       movieId = it.id.toString(),
                       title = it.title,
                       releaseDate = it.releaseDate,
                       imagePoster = it.posterPath,
                       overview = it.overview,
                       backdrop = it.backdropPath,
                       typeMovie = MovieType.popular
                   )

               }

               //insert to db
               lisMovieEntity?.forEach{
                   movieDbImpl.getDatabase().movieDao().insertMovie(it)
               }

               Log.d(MovieRepoImpl::class.simpleName,
                   "getNowPlayingMovie : ${Gson().toJsonTree(listMovie)}")
               return getPopularMovieLocal()

           }else{
               Log.e(MovieRepoImpl::class.simpleName,
                   "getPopularMovie error code: ${response.code()}", )
               return getPopularMovieLocal()
           }
       }catch (e:Exception){
           Log.e(MovieRepoImpl::class.simpleName, "getPopularMovie error :${e.message} ", )
           return getPopularMovieLocal()
       }
    }

    override suspend fun getNowPlayingMovie(): List<Movie> {
        try {
            val response = service.getNowPlayingMovie()

            if(response.isSuccessful){
                val listMovie = response.body()


                val lisMovieEntity = listMovie?.results?.map {
                    MovieEntity(
                        movieId = it.id.toString(),
                        title = it.title,
                        releaseDate = it.releaseDate,
                        imagePoster = it.posterPath,
                        overview = it.overview,
                        backdrop = it.backdropPath,
                        typeMovie = MovieType.nowPlaying
                    )

                }

                //insert to db
                lisMovieEntity?.forEach{
                    movieDbImpl.getDatabase().movieDao().insertMovie(it)
                }

                Log.d(MovieRepoImpl::class.simpleName,
                    "getNowPlayingMovie : ${Gson().toJsonTree(listMovie)}")
                return getNowPlayingMovieLocal() ?: emptyList()
            }else{
                Log.e(MovieRepoImpl::class.simpleName,
                    "getNowPlayingMovie error code: ${response.code()}", )
                return getNowPlayingMovieLocal()
            }
        }catch (e:Exception){
            Log.e(MovieRepoImpl::class.simpleName, "getNowPlayingMovie error :${e.message} ", )
            return getNowPlayingMovieLocal()
        }
    }

    override suspend fun getPopularMovieLocal(): List<Movie> {
        var listData = listOf<Movie>()
        try {
            listData = movieDbImpl.getDatabase().movieDao().getListMoviePopular(
                MovieType.popular
            ).map {
                Movie(
                    movieId = it.movieId,
                    title = it.title,
                    releaseDate = it.releaseDate,
                    imagePoster = it.imagePoster,
                    backdrop = it.backdrop,
                    overview = it.overview,
                    bookmark = it.bookmark
                )
            }
            return listData
        }catch (e:Exception){
            Log.e(movieDbImpl::class.simpleName, "getPopularMovieLocal: ${e.message}", )
            return listData
        }
    }

    override suspend fun getNowPlayingMovieLocal(): List<Movie> {
        var listData = listOf<Movie>()
        try {
            listData = movieDbImpl.getDatabase().movieDao().getListMovieNowPlaying(
                MovieType.nowPlaying
            ).map {
                Movie(
                    movieId = it.movieId,
                    title = it.title,
                    releaseDate = it.releaseDate,
                    imagePoster = it.imagePoster,
                    backdrop = it.backdrop,
                    overview = it.overview,
                    bookmark = it.bookmark
                )
            }
            return listData
        }catch (e:Exception){
            Log.e(movieDbImpl::class.simpleName, "getPopularMovieLocal: ${e.message}", )
            return listData
        }
    }
}