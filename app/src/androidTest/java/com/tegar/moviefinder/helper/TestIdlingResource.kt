package com.tegar.moviefinder.helper

import androidx.test.espresso.idling.CountingIdlingResource

object TestIdlingResource {
    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

}
