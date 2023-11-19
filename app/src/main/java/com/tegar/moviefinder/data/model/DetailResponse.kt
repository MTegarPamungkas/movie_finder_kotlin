package com.tegar.moviefinder.data.model

import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("adult")
	val adult: Boolean,

	@field:SerializedName("title")
	val title: String = "",

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,


	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("runtime")
	val runtime: Int = 0,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("release_date")
	val releaseDate: String = "",

	@field:SerializedName("vote_average")
	val voteAverage: Double? = null,
)

data class GenresItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

