package com.tegar.moviefinder.mockserver

import com.tegar.moviefinder.helper.FileReader
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockWebServerDispatcher {

    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/trending/movie/day?language=en-US&page=1" ->
                    MockResponse().setResponseCode(200)
                        .setBody(FileReader.readStringFromFile("trending.json"))
                "/search/movie?query=marvels" ->
                    MockResponse().setResponseCode(200)
                        .setBody(FileReader.readStringFromFile("search.json"))
                "/movie/872585" ->
                    MockResponse().setResponseCode(200)
                        .setBody(FileReader.readStringFromFile("detail.json"))
                else -> MockResponse().setResponseCode(400)
            }
        }
    }
}