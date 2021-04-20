package com.sgorinov.exilehelper.exchange.presentation.models.enums

import com.sgorinov.exilehelper.core.presentation.FragmentScopes
import com.sgorinov.exilehelper.core.presentation.scopedViewModel
import com.sgorinov.exilehelper.exchange.presentation.ItemsSearchViewModel

internal object ViewFilters {

    private val viewModel by scopedViewModel<ItemsSearchViewModel>(
        FragmentScopes.EXCHANGE_FEATURE.scopeId,
        FragmentScopes.EXCHANGE_FEATURE
    )

    private val ultimatumFilters = listOf(
        FilterField(
            "ultimatum_challenge",
            "Challenge Type",
            ViewType.Dropdown,
            DropDowns.ChallengeType.values().toList()
        ),
        FilterField(
            "ultimatum_reward",
            "Reward Type",
            ViewType.Dropdown,
            DropDowns.RewardType.values().toList()
        ),
        FilterField(
            "ultimatum_input",
            "Required Item",
            ViewType.Dropdown,
            viewModel.ultimatumInput
        ),
        FilterField("ultimatum_output", "Reward Unique", ViewType.Dropdown, viewModel.uniques)
    )

    val allFilters = listOf(
        Filter("type_filters", "Type Filters", TypeFilters.values().toList()),
        Filter("weapon_filters", "Weapon Filters", WeaponFilters.values().toList()),
        Filter("armour_filters", "Armour Filters", ArmourFilters.values().toList()),
        Filter("socket_filters", "Socket Filters", SocketFilters.values().toList()),
        Filter("req_filters", "Requirements", ReqFilters.values().toList()),
        Filter("map_filters", "Map Filters", MapFilters.values().toList()),
        Filter("misc_filters", "Heist Filters", HeistFilters.values().toList()),
        Filter("ultimatum_filters", "Ultimatum Filters", ultimatumFilters),
        Filter("heist_filters", "Miscellaneous", MiscFilters.values().toList()),
        Filter("trade_filters", "Trade Filters", TradeFilters.values().toList())
    )

    enum class TypeFilters(
        override val id: String,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: List<IEnum>?
    ) : IFilter {
        Category(
            "category",
            "Item Category",
            ViewType.Dropdown,
            DropDowns.ItemCategory.values().toList()
        ),
        Rarity("rarity", "Item Rarity", ViewType.Dropdown, DropDowns.ItemRarity.values().toList())
    }

    enum class WeaponFilters(
        override val id: String,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: List<IEnum>?
    ) : IFilter {
        Damage("damage", "Damage", ViewType.Minmax, null),
        APS("aps", "Attacks per Second", ViewType.Minmax, null),
        CritChance("crit", "Critical Chance", ViewType.Minmax, null),
        DPS("dps", "Damage per Second", ViewType.Minmax, null),
        PDPS("pdps", "Physical DPS", ViewType.Minmax, null),
        EDPS("edps", "Elemental DPS", ViewType.Minmax, null)
    }

    enum class ArmourFilters(
        override val id: String,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: List<IEnum>?
    ) : IFilter {
        Armour("ar", "Armour", ViewType.Minmax, null),
        Evasion("ev", "Evasion", ViewType.Minmax, null),
        EnergyShield("es", "Energy Shield", ViewType.Minmax, null),
        Block("block", "Block", ViewType.Minmax, null)
    }

    enum class SocketFilters(
        override val id: String,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: List<IEnum>?
    ) : IFilter {
        Sockets("sockets", "Sockets", ViewType.Socket, null),
        Links("links", "Links", ViewType.Socket, null)
    }

    enum class ReqFilters(
        override val id: String,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: List<IEnum>?
    ) : IFilter {
        Level("lvl", "Level", ViewType.Minmax, null),
        Strength("str", "Strength", ViewType.Minmax, null),
        Dexterity("dex", "Dexterity", ViewType.Minmax, null),
        Intelligence("int", "Intelligence", ViewType.Minmax, null)
    }

    enum class MapFilters(
        override val id: String,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: List<IEnum>?
    ) : IFilter {
        MapTier("map_tier", "Map Tier", ViewType.Minmax, null),
        MapPacksize("map_packsize", "Map Packsize", ViewType.Minmax, null),
        MapIIQ("map_iiq", "Map IIQ", ViewType.Minmax, null),
        MapIIR("map_iir", "Map IIR", ViewType.Minmax, null),
        ShapedMap("map_shaped", "Shaped Map", ViewType.Dropdown, DropDowns.YesNo.values().toList()),
        ElderMap("map_elder", "Elder Map", ViewType.Dropdown, DropDowns.YesNo.values().toList()),
        BlightedMap(
            "map_blighted",
            "Blighted Map",
            ViewType.Dropdown,
            DropDowns.YesNo.values().toList()
        ),
        MapRegion(
            "map_region",
            "Map Region",
            ViewType.Dropdown,
            DropDowns.MapRegion.values().toList()
        ),
        MapSeries(
            "map_series",
            "Map Series",
            ViewType.Dropdown,
            DropDowns.MapSeries.values().toList()
        )
    }

    enum class HeistFilters(
        override val id: String,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: List<IEnum>?
    ) : IFilter {
        ContractObjectiveValue(
            "heist_objective_value",
            "Contract Objective Value",
            ViewType.Dropdown,
            DropDowns.ContractObjectiveValue.values().toList()
        ),
        WingsRevealed("heist_wings", "Wings Revealed", ViewType.Minmax, null),
        TotalWings("heist_max_wings", "Total Wings", ViewType.Minmax, null),
        EscapeRoutesRevealed(
            "heist_escape_routes",
            "Escape Routes Revealed",
            ViewType.Minmax,
            null
        ),
        TotalEscapeRoutes("heist_max_escape_routes", "Total Escape Routes", ViewType.Minmax, null),
        RewardRoomsRevealed("heist_reward_rooms", "Reward Rooms Revealed", ViewType.Minmax, null),
        TotalRewardRooms("heist_max_reward_rooms", "Total Reward Rooms", ViewType.Minmax, null),
        AreaLevel("area_level", "Area Level", ViewType.Minmax, null),
        LockpickingLevel("heist_lockpicking", "Lockpicking Level", ViewType.Minmax, null),
        BruteForceLevel("heist_brute_force", "Brute Force Level", ViewType.Minmax, null),
        PerceptionLevel("heist_perception", "Perception Level", ViewType.Minmax, null),
        DemolitionLevel("heist_demolition", "Demolition Level", ViewType.Minmax, null),
        CounterThaumLevel(
            "heist_counter_thaumaturgy",
            "Counter-Thaum. Level",
            ViewType.Minmax,
            null
        ),
        TrapDisarmamentLevel(
            "heist_trap_disarmament",
            "Trap Disarmament Level",
            ViewType.Minmax,
            null
        ),
        AgilityLevel("heist_agility", "Agility Level", ViewType.Minmax, null),
        DeceptionLevel("heist_deception", "Deception Level", ViewType.Minmax, null),
        EngineeringLevel("heist_engineering", "Engineering Level", ViewType.Minmax, null)
    }

    enum class MiscFilters(
        override val id: String,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: List<IEnum>?
    ) : IFilter {
        Quality("quality", "Quality", ViewType.Minmax, null),
        ItemLevel("ilvl", "Item Level", ViewType.Minmax, null),
        GemLevel("gem_level", "Gem Level", ViewType.Minmax, null),
        GemExperience("gem_level_progress", "Gem Experience %", ViewType.Minmax, null),
        GemQualityType(
            "gem_alternate_quality",
            "Gem Quality Type",
            ViewType.Dropdown,
            DropDowns.GemQualityType.values().toList()
        ),
        ShaperInfluence(
            "shaper_item",
            "Shaper Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values().toList()
        ),
        ElderInfluence(
            "elder_item",
            "Elder Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values().toList()
        ),
        CrusaderInfluence(
            "crusader_item",
            "Crusader Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values().toList()
        ),
        RedeemerInfluence(
            "redeemer_item",
            "Redeemer Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values().toList()
        ),
        HunterInfluence(
            "hunter_item",
            "Hunter Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values().toList()
        ),
        WarlordInfluence(
            "warlord_item",
            "Warlord Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values().toList()
        ),
        FracturedItem(
            "fractured_item",
            "Fractured Item",
            ViewType.Dropdown,
            DropDowns.YesNo.values().toList()
        ),
        SynthesisedItem(
            "synthesised_item",
            "Synthesised Item",
            ViewType.Dropdown,
            DropDowns.YesNo.values().toList()
        ),
        AlternateArt(
            "alternate_art",
            "Alternate Art",
            ViewType.Dropdown,
            DropDowns.GemQualityType.values().toList()
        ),
        Identified(
            "identified",
            "Identified",
            ViewType.Dropdown,
            DropDowns.YesNo.values().toList()
        ),
        Corrupted("corrupted", "Corrupted", ViewType.Dropdown, DropDowns.YesNo.values().toList()),
        Mirrored("mirrored", "Mirrored", ViewType.Dropdown, DropDowns.YesNo.values().toList()),
        Crafted("crafted", "Crafted", ViewType.Dropdown, DropDowns.YesNo.values().toList()),
        Veiled("veiled", "Veiled", ViewType.Dropdown, DropDowns.YesNo.values().toList()),
        Enchanted("enchanted", "Enchanted", ViewType.Dropdown, DropDowns.YesNo.values().toList()),
        TalismanTier("talisman_tier", "Talisman Tier", ViewType.Minmax, null),
        StoredExperience("stored_experience", "Stored Experience", ViewType.Minmax, null),
        StackSize("stack_size", "Stack Size", ViewType.Minmax, null)
    }

    enum class TradeFilters(
        override val id: String,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: List<IEnum>?
    ) : IFilter {
        SellerAccount("account", "Seller Account", ViewType.Account, null),
        Listed("indexed", "Listed", ViewType.Dropdown, DropDowns.Listed.values().toList()),
        SaleType("sale_type", "Sale Type", ViewType.Dropdown, DropDowns.SaleType.values().toList()),
        BuyoutPrice(
            "price",
            "Buyout Price",
            ViewType.Buyout,
            DropDowns.BuyoutPrice.values().toList()
        )
    }

    data class Filter(
        val id: String,
        val text: String,
        val values: List<IFilter>
    )

    data class FilterField(
        override val id: String,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: List<IEnum>?
    ) : IFilter

    data class ItemDropdownFilter(override val id: String?, override val text: String) : IEnum
}