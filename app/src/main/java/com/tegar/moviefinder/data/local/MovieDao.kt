package com.tegar.moviefinder.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tegar.moviefinder.data.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getAllMovie(): Flow<List<Movie>>

    @Insert
    suspend fun insertMovie(movie: Movie)

    @Query("DELETE FROM movie WHERE id = :id")
    suspend fun deleteByIdMovie(id: Int): Int

    suspend fun deleteMovie(id: Int): Boolean {
        return deleteByIdMovie(id) > 0
    }


    @Query("SELECT EXISTS(SELECT * FROM movie WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>
}