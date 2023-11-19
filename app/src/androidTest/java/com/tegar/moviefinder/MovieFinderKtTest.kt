package com.tegar.moviefinder

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tegar.moviefinder.data.local.MovieDao
import com.tegar.moviefinder.data.local.MovieDatabase
import com.tegar.moviefinder.data.model.Movie
import com.tegar.moviefinder.di.AppModule
import com.tegar.moviefinder.helper.TestIdlingResource
import com.tegar.moviefinder.helper.assertCurrentRouteName
import com.tegar.moviefinder.helper.onNodeWithStringId
import com.tegar.moviefinder.mockserver.MockWebServerDispatcher
import com.tegar.moviefinder.ui.navigation.Screen
import com.tegar.moviefinder.ui.theme.MovieFinderTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class)
@RunWith(AndroidJUnit4::class)
class MovieFinderKtTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    private val mockWebServer = MockWebServer()

    @Inject
    lateinit var database: MovieDatabase
    private lateinit var movieDao: MovieDao


    @Before
    fun setup() {
        hiltRule.inject()
        movieDao = database.movieDao()
        mockWebServer.start(8080)
        composeTestRule.activity.setContent {
            MovieFinderTheme {
                navController = TestNavHostController(LocalContext.current)
                mockWebServer.dispatcher = MockWebServerDispatcher().RequestDispatcher()
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                MovieFinder(navController = navController)
            }
        }

        IdlingRegistry.getInstance().register(TestIdlingResource.countingIdlingResource)
    }

    @Test
    fun navHost_bottomNavigation_working() {
        composeTestRule.onNodeWithStringId(R.string.about).performClick()
        assertEquals(navController.currentDestination?.route, Screen.About.route)
        composeTestRule.onNodeWithStringId(R.string.favorite).performClick()
        assertEquals(navController.currentDestination?.route, Screen.Favourite.route)
        composeTestRule.onNodeWithStringId(R.string.movie).performClick()
        assertEquals(navController.currentDestination?.route, Screen.Movie.route)
    }

    @Test
    fun testInsertToDatabaseAndDeleteWithTrueId() = runBlocking {
        val movie = Movie(
            "The Killer", "/e7Jvsry47JJQruuezjU2X1Z6J77.jpg", "2023-10-25", 800158
        )
        movieDao.insertMovie(movie)
        val movieFromDb = movieDao.isFavorite(800158).first()
        assertTrue(movieFromDb)

        val delete = movieDao.deleteMovie(800158)
        assertTrue(delete)
    }

    @Test
    fun testInsertToDatabaseAndDeleteWithFalseId() = runBlocking {
        val movie = Movie(
            "The Marvels", "/tUtgLOESpCx7ue4BaeCTqp3vn1b.jpg", "2023-11-08", 609681
        )
        movieDao.insertMovie(movie)
        val movieFromDb = movieDao.isFavorite(609681).first()
        assertTrue(movieFromDb)

        val delete = movieDao.deleteMovie(709681)
        assertFalse(delete)
    }

    @Test
    fun navHost_clickItem_navigatesToDetailAndBack() {
        composeTestRule.onNodeWithText("Oppenheimer", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("Oppenheimer", useUnmergedTree = true).performClick()

        mockWebServer.dispatcher = MockWebServerDispatcher().RequestDispatcher()
        navController.assertCurrentRouteName(Screen.DetailMovie.route)
        composeTestRule.onNodeWithTag("backButton").performClick()
        navController.assertCurrentRouteName(Screen.Movie.route)

    }

    @Test
    fun searchAndShowData() {
        val searchText = "marvels"
        composeTestRule.onNodeWithTag("SearchField").performClick()
        composeTestRule.onNodeWithTag("SearchField").performTextInput(searchText)
        composeTestRule.onNodeWithTag("SearchField").performImeAction()
        mockWebServer.dispatcher = MockWebServerDispatcher().RequestDispatcher()
        composeTestRule.onNodeWithText("The Marvels").assertIsDisplayed()


    }

    @Test
    fun navHost_clickItem_navigatesToDetailAndInsertToDatabase() = runBlocking {
        val movie = Movie(
            "Oppenheimer", "/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg", "2023-07-19", 872585
        )
        composeTestRule.onNodeWithText("Oppenheimer", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("Oppenheimer", useUnmergedTree = true).performClick()
        navController.assertCurrentRouteName(Screen.DetailMovie.route)

        mockWebServer.dispatcher = MockWebServerDispatcher().RequestDispatcher()

        composeTestRule.onNodeWithText("Oppenheimer").assertIsDisplayed()
        composeTestRule.onNodeWithTag("FavoriteIcon").performClick()

        val movieFromDb = movieDao.isFavorite(movie.id).first()
        assertTrue(movieFromDb)
    }


    @After
    fun teardown() {
        database.close()
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(TestIdlingResource.countingIdlingResource)
    }
}