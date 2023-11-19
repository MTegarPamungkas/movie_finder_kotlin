package com.tegar.moviefinder.utils

import com.tegar.moviefinder.BuildConfig

object ImageTmdbQuality {

    private const val apiImage = BuildConfig.API_IMAGE

    fun quality200(image: String): String {
        return "$apiImage/w200$image"
    }

    fun qualityOriginal(image: String): String {
        return "$apiImage/original$image"
    }
}