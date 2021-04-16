package com.sgorinov.exilehelper.charts_feature.data

import com.sgorinov.exilehelper.charts_feature.data.models.GraphData
import com.sgorinov.exilehelper.charts_feature.data.models.HistoryModel
import com.sgorinov.exilehelper.charts_feature.data.models.ItemGroup
import com.sgorinov.exilehelper.charts_feature.domain.models.CurrencyData
import com.sgorinov.exilehelper.charts_feature.domain.models.OverviewData
import kotlinx.serialization.json.*

internal class FeatureRepository(private val api: PoeNinjaChartsApi) : BaseFeatureRepository() {

    override var overviewData = listOf<OverviewData>()

    override suspend fun getCurrenciesOverview(
        league: String,
        type: String
    ) {
        val result = api.getCurrenciesOverview(league, type)
        val currenciesOverview = result.lines
        val currenciesDetails = result.currencyDetails
        overviewData = currenciesOverview.map { currencyOverview ->
            val sellingData = if (currencyOverview.pay != null) {
                CurrencyData(currencyOverview.pay.listing_count, 1 / currencyOverview.pay.value)
            } else {
                null
            }
            val buyingData =
                CurrencyData(currencyOverview.receive.listing_count, currencyOverview.receive.value)
            val currencyDetail =
                currenciesDetails.firstOrNull { it.id == currencyOverview.receive.get_currency_id }
            OverviewData(
                currencyOverview.receive.get_currency_id.toString(),
                currencyOverview.currencyTypeName,
                null,
                currencyDetail?.icon,
                currencyDetail?.tradeId ?: currencyOverview.detailsId,
                sellingData,
                buyingData,
                if (currencyOverview.receiveSparkLine.data.any { it == null }) {
                    null
                } else {
                    currencyOverview.receiveSparkLine.data.filterNotNull()
                },
                if (currencyOverview.paySparkLine.data.any { it == null }) {
                    listOf()
                } else {
                    currencyOverview.paySparkLine.data.filterNotNull()
                },
                currencyOverview.receiveSparkLine.totalChange,
                currencyOverview.paySparkLine.totalChange
            )
        }
    }

    override suspend fun getItemsOverview(league: String, type: String) {
        val result = api.getItemsOverview(league, type)
        overviewData = result.getValue("lines").jsonArray.mapNotNull { itemOverview ->
            try {
                val id = itemOverview.jsonObject.getValue("id").jsonPrimitive.content
                val name = itemOverview.jsonObject.getValue("name").jsonPrimitive.content
                val baseType =
                    if (itemOverview.jsonObject.getValue("baseType").jsonPrimitive.isString) {
                        itemOverview.jsonObject.getValue("baseType").jsonPrimitive.content
                    } else {
                        null
                    }
                val icon = itemOverview.jsonObject.getValue("icon").jsonPrimitive.content
                val tradeId = itemOverview.jsonObject.getValue("detailsId").jsonPrimitive.content
                val listingCount =
                    itemOverview.jsonObject.getValue("listingCount").jsonPrimitive.intOrNull ?: 0
                val chaosValue =
                    itemOverview.jsonObject.getValue("chaosValue").jsonPrimitive.doubleOrNull ?: 0.0
                val sparkLine = itemOverview.jsonObject.getValue("sparkline").jsonObject
                val totalChange = sparkLine.getValue("totalChange").jsonPrimitive.floatOrNull ?: 0f
                val sparkLineData = sparkLine.getValue("data").jsonArray.map {
                    it.jsonPrimitive.content.toFloatOrNull()
                }
                OverviewData(
                    id,
                    name,
                    baseType,
                    icon,
                    tradeId,
                    null,
                    CurrencyData(listingCount, chaosValue),
                    null,
                    if (sparkLineData.any { it == null }) {
                        listOf()
                    } else {
                        sparkLineData.filterNotNull()
                    },
                    null,
                    totalChange
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun getCurrencyHistory(
        league: String,
        type: String,
        id: String
    ): HistoryModel {
        return api.getCurrencyHistory(league, type, id)
    }

    override suspend fun getItemHistory(league: String, type: String, id: String): List<GraphData> {
        return api.getItemHistory(league, type, id)
    }

    override fun getItemsGroups() = listOf(
        ItemGroup(
            "Currency",
            "https://web.poecdn.com/image/Art/2DItems/Currency/CurrencyRerollRare.png",
            true,
            "Currency"
        ),
        ItemGroup(
            "Fragments",
            "https://web.poecdn.com/image/Art/2DItems/Maps/AtlasMaps/FragmentPhoenix.png",
            true,
            "Fragment"
        ),
        ItemGroup(
            "Invitations",
            "https://web.poecdn.com/image/Art/2DItems/Currency/Atlas/NullVoid5.png",
            false,
            "Invitation"
        ),
        ItemGroup(
            "Delirium Orbs",
            "https://web.poecdn.com/image/Art/2DItems/Currency/Delirium/DeliriumOrbScarabs.png",
            false,
            "DeliriumOrb"
        ),
        ItemGroup(
            "Watchstones",
            "https://web.poecdn.com/image/Art/2DItems/Currency/Strongholds/IvoryWatchstone5.png",
            false,
            "Watchstone"
        ),
        ItemGroup(
            "Oils",
            "https://web.poecdn.com/image/blight/items/OpalescentOil.png",
            false,
            "Oil"
        ),
        ItemGroup(
            "Incubators",
            "https://web.poecdn.com/image/Art/2DItems/Currency/Incubation/IncubationAbyss.png",
            false,
            "Incubator"
        ),
        ItemGroup(
            "Scarabs",
            "https://web.poecdn.com/image/Art/2DItems/Currency/Scarabs/GreaterScarabBreach.png",
            false,
            "Scarab"
        ),
        ItemGroup(
            "Fossils",
            "https://web.poecdn.com/image/Art/2DItems/Currency/Delve/SanctifiedFossil.png",
            false,
            "Fossil"
        ),
        ItemGroup(
            "Resonators",
            "https://web.poecdn.com/image/Art/2DItems/Currency/Delve/Reroll2x2A.png",
            false,
            "Resonator"
        ),
        ItemGroup(
            "Essences",
            "https://web.poecdn.com/image/Art/2DItems/Currency/Essence/Woe7.png",
            false,
            "Essence"
        ),
        ItemGroup(
            "Divination Cards",
            "https://web.poecdn.com/image/Art/2DItems/Divination/InventoryIcon.png",
            false,
            "DivinationCard"
        ),
        ItemGroup(
            "Prophecies",
            "https://web.poecdn.com/image/Art/2DItems/Currency/ProphecyOrbRed.png",
            false,
            "Prophecy"
        ),
        ItemGroup(
            "Skill Gems",
            "https://web.poecdn.com/image/Art/2DItems/Gems/Portal.png",
            false,
            "SkillGem"
        ),
        ItemGroup(
            "Base Types",
            "https://web.poecdn.com/image/Art/2DItems/Rings/OpalRing.png",
            false,
            "BaseType"
        ),
        ItemGroup(
            "Helmet Enchants",
            "https://web.poecdn.com/image/Art/2DItems/Gems/ClusterBurst.png",
            false,
            "HelmetEnchant"
        ),
        ItemGroup(
            "Unique Maps",
            "https://web.poecdn.com/image/Art/2DItems/Maps/UndeadSiege.png",
            false,
            "UniqueMap"
        ),
        ItemGroup(
            "Maps",
            "https://web.poecdn.com/image/Art/2DItems/Maps/AtlasMaps/Gorge3.png",
            false,
            "Map"
        ),
        ItemGroup(
            "Unique Jewels",
            "https://web.poecdn.com/image/Art/2DItems/Jewels/unique7.png",
            false,
            "UniqueJewel"
        ),
        ItemGroup(
            "Unique Flasks",
            "https://web.poecdn.com/gen/image/WzksMTQseyJmIjoiMkRJdGVtc1wvRmxhc2tzXC9UYXN0ZU9mSGF0ZSIsInciOjEsImgiOjIsInNjYWxlIjp0cnVlLCJsZXZlbCI6MX1d/4727ad7a3a/Item.png",
            false,
            "UniqueFlask"
        ),
        ItemGroup(
            "Unique Weapons",
            "https://web.poecdn.com/image/Art/2DItems/Weapons/OneHandWeapons/OneHandSwords/Varunastra.png",
            false,
            "UniqueWeapon"
        ),
        ItemGroup(
            "Unique Armours",
            "https://web.poecdn.com/image/Art/2DItems/Armours/Boots/Skyforth.png",
            false,
            "UniqueArmour"
        ),
        ItemGroup(
            "Unique Accessories",
            "https://web.poecdn.com/image/Art/2DItems/Amulets/AgateAmuletUnique.png",
            false,
            "UniqueAccessory"
        ),
        ItemGroup(
            "Beasts",
            "https://web.poecdn.com/image/Art/2DItems/Currency/BestiaryOrbFull.png",
            false,
            "Beast"
        ),
        ItemGroup(
            "Vials",
            "https://web.poecdn.com/image/Art/2DItems/Currency/VialTemperedFlesh.png",
            false,
            "Vial"
        )
    )
}