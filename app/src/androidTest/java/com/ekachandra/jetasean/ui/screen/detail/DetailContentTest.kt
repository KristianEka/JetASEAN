package com.ekachandra.jetasean.ui.screen.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.ekachandra.jetasean.R
import com.ekachandra.jetasean.model.Country
import com.ekachandra.jetasean.ui.theme.JetASEANTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeCountryDetail = Country(
        id = 1,
        image = R.drawable.flag_of_indonesia_flat_round,
        title = "Indonesia",
        capital = "Jakarta",
        membership = "08 August 1967",
        contribution = "As the largest country in ASEAN, Indonesia has an important role in leading ASEAN and promoting economic and political integration in the Southeast Asian region. Indonesia also has great influence in terms of maritime security and environmental cooperation in ASEAN."
    )

    @Before
    fun setUp() {
        composeTestRule.setContent {
            JetASEANTheme {
                DetailContent(
                    image = fakeCountryDetail.image,
                    title = fakeCountryDetail.title,
                    capital = fakeCountryDetail.capital,
                    membership = fakeCountryDetail.membership,
                    contribution = fakeCountryDetail.contribution,
                    onBackClick = {},
                    favoriteStatus = false,
                    updateFavoriteStatus = {}
                )
            }
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun detailContent_isDisplayed() {
        composeTestRule.onNodeWithText(fakeCountryDetail.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.capital,
                fakeCountryDetail.capital
            )
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.membership,
                fakeCountryDetail.membership
            )
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeCountryDetail.contribution).assertIsDisplayed()
    }
}