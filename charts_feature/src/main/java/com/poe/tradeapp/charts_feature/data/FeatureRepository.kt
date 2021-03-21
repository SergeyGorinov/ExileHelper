package com.poe.tradeapp.charts_feature.data

import com.poe.tradeapp.charts_feature.data.models.CurrenciesOverviewResponse
import com.poe.tradeapp.charts_feature.data.models.CurrencyHistoryModel
import com.poe.tradeapp.charts_feature.data.models.GraphData
import com.poe.tradeapp.charts_feature.data.models.ItemGroup
import kotlinx.serialization.json.JsonObject
import retrofit2.await

internal class FeatureRepository(private val api: PoeNinjaChartsApi) : BaseFeatureRepository() {

    override suspend fun getCurrenciesOverview(
        league: String,
        type: String
    ): CurrenciesOverviewResponse {
        return api.getCurrenciesOverview(league, type).await()
    }

    override suspend fun getItemsOverview(league: String, type: String): JsonObject {
        return api.getItemsOverview(league, type).await()
    }

    override suspend fun getCurrencyHistory(
        league: String,
        type: String,
        id: String
    ): CurrencyHistoryModel {
        return api.getCurrencyHistory(league, type, id).await()
    }

    override suspend fun getItemHistory(league: String, type: String, id: String): List<GraphData> {
        return api.getItemHistory(league, type, id).await()
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