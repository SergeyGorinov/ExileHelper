package com.sgorinov.exilehelper.charts_feature.data

import com.sgorinov.exilehelper.charts_feature.data.models.OverviewResponse
import com.sgorinov.exilehelper.charts_feature.domain.models.CurrencyData
import com.sgorinov.exilehelper.charts_feature.domain.models.OverviewData
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.File

class FeatureRepositoryTest {

    private lateinit var repository: FeatureRepository

    @Mock
    private lateinit var api: PoeNinjaChartsApi

    @Mock
    private lateinit var mockResponse: OverviewResponse

    private val league = "testLeague"
    private val type = "testType"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = FeatureRepository(api)
    }

    @Test
    fun getCurrenciesOverview() {
        val mockJsonPath =
            javaClass.classLoader!!.getResource("getCurrenciesOverviewMockResponse.json").file
        val mockJson = File(mockJsonPath).readText()
        val mockResponse = Json.decodeFromString(OverviewResponse.serializer(), mockJson)
        val mockData = listOf(
            OverviewData(
                "22",
                "Mirror of Kalandra",
                null,
                "https://web.poecdn.com/image/Art/2DItems/Currency/CurrencyDuplicate.png?scale=1&w=1&h=1",
                "mirror",
                CurrencyData(129, 1 / 0.000011621904),
                CurrencyData(124, 91229.3918918919),
                listOf(0f, 2.04f, 6.56f, 7.45f, 11.16f, 11.65f, 11.8f),
                listOf(),
                11.8f,
                0f
            )
        )

        runBlocking {
            Mockito.`when`(api.getCurrenciesOverview(league, type)).thenReturn(mockResponse)
            repository.getCurrenciesOverview(league, type)
            assertEquals(mockData, repository.overviewData)
        }
    }

    @Test
    fun getCurrenciesOverview_emptyLines() {
        runBlocking {
            Mockito.`when`(api.getCurrenciesOverview(league, type)).thenReturn(mockResponse)
            Mockito.`when`(this@FeatureRepositoryTest.mockResponse.lines).thenReturn(listOf())
            repository.getCurrenciesOverview(league, type)
            assertEquals(emptyList<OverviewData>(), repository.overviewData)
        }
    }

    @Test
    fun getItemsOverview() {
        val mockJsonPath =
            javaClass.classLoader!!.getResource("getItemsOverviewMockResponse.json").file
        val mockJson = File(mockJsonPath).readText()
        val mockResponse = Json.parseToJsonElement(mockJson).jsonObject
        val mockData = listOf(
            OverviewData(
                "36712",
                "Misinformation",
                null,
                "https://web.poecdn.com/image/Art/2DItems/Currency/Strongholds/IvoryWatchstone3.png?v=18db05af11a9b8104cc3cf97729dd004&w=1&h=1&scale=1",
                "misinformation-ivory-watchstone-1",
                null,
                CurrencyData(13, 26149.98),
                null,
                listOf(),
                null,
                0f
            )
        )
        runBlocking {
            Mockito.`when`(api.getItemsOverview(league, type)).thenReturn(mockResponse)
            repository.getItemsOverview(league, type)
            assertEquals(mockData, repository.overviewData)
        }
    }
}