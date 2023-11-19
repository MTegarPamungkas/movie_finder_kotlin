package com.tegar.moviefinder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "movie")
data class Movie(

    @field:SerializedName("title")
    val title: String = "",

    @field:SerializedName("poster_path")
    val posterPath: String? = null,

    @field:SerializedName("release_date")
    val releaseDate: String = "",

    @field:SerializedName("id")
    @field:PrimaryKey
    val id: Int = 0,

    )
