package com.chirikualii.materidb.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chirikualii.materidb.data.local.entity.MovieEntity
import com.chirikualii.materidb.data.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    suspend fun insertMovie(movie:MovieEntity)

    //get movie dengan type movie = Now Palying
    @Query("SELECT * FROM movie WHERE type_movie = (:typeMovie)")
    suspend fun getListMovieNowPlaying(
        typeMovie: String
    ):List<MovieEntity>

    @Query("SELECT * FROM movie WHERE type_movie = (:typeMovie)")
    suspend fun getListMoviePopular(
        typeMovie: String
    ):List<MovieEntity>

    //Update Movie
    @Update(entity =MovieEntity::class)
    suspend fun updateMovie(movie: MovieEntity)

    @Query("UPDATE movie SET bookmark= :bookmark WHERE movieId = :MovieId")
    suspend fun updateMovieWithQuery(MovieId :String,bookmark :Int)
}