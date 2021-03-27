package com.poe.tradeapp.exchange.presentation.models.enums

internal class ViewFilters {

    @Suppress("unused")
    enum class AllFilters(val id: String, val text: String, val values: Array<*>) {
        TypeFilter("type_filters", "Type Filters", TypeFilters.values() as Array<*>),
        WeaponFilter("weapon_filters", "Weapon Filters", WeaponFilters.values() as Array<*>),
        ArmourFilter("armour_filters", "Armour Filters", ArmourFilters.values() as Array<*>),
        SocketFilter("socket_filters", "Socket Filters", SocketFilters.values() as Array<*>),
        ReqFilter("req_filters", "Requirements", ReqFilters.values() as Array<*>),
        MapFilter("map_filters", "Map Filters", MapFilters.values() as Array<*>),
        HeistFilter("misc_filters", "Heist Filters", HeistFilters.values() as Array<*>),
        MiscFilter("heist_filters", "Miscellaneous", MiscFilters.values() as Array<*>),
        TradeFilter("trade_filters", "Trade Filters", TradeFilters.values() as Array<*>)
    }

    enum class TypeFilters(
        override val id: String?,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: Array<*>?
    ) : IFilter {
        Category("category", "Item Category", ViewType.Dropdown, DropDowns.ItemCategory.values()),
        Rarity("rarity", "Item Rarity", ViewType.Dropdown, DropDowns.ItemRarity.values())
    }

    enum class WeaponFilters(
        override val id: String?,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: Array<*>?
    ) : IFilter {
        Damage("damage", "Damage", ViewType.Minmax, null),
        APS("aps", "Attacks per Second", ViewType.Minmax, null),
        CritChance("crit", "Critical Chance", ViewType.Minmax, null),
        DPS("dps", "Damage per Second", ViewType.Minmax, null),
        PDPS("pdps", "Physical DPS", ViewType.Minmax, null),
        EDPS("edps", "Elemental DPS", ViewType.Minmax, null)
    }

    enum class ArmourFilters(
        override val id: String?,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: Array<*>?
    ) : IFilter {
        Armour("ar", "Armour", ViewType.Minmax, null),
        Evasion("ev", "Evasion", ViewType.Minmax, null),
        EnergyShield("es", "Energy Shield", ViewType.Minmax, null),
        Block("block", "Block", ViewType.Minmax, null)
    }

    enum class SocketFilters(
        override val id: String?,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: Array<*>?
    ) : IFilter {
        Sockets("sockets", "Sockets", ViewType.Socket, null),
        Links("links", "Links", ViewType.Socket, null)
    }

    enum class ReqFilters(
        override val id: String?,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: Array<*>?
    ) : IFilter {
        Level("lvl", "Level", ViewType.Minmax, null),
        Strength("str", "Strength", ViewType.Minmax, null),
        Dexterity("dex", "Dexterity", ViewType.Minmax, null),
        Intelligence("int", "Intelligence", ViewType.Minmax, null)
    }

    enum class MapFilters(
        override val id: String?,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: Array<*>?
    ) : IFilter {
        MapTier("map_tier", "Map Tier", ViewType.Minmax, null),
        MapPacksize("map_packsize", "Map Packsize", ViewType.Minmax, null),
        MapIIQ("map_iiq", "Map IIQ", ViewType.Minmax, null),
        MapIIR("map_iir", "Map IIR", ViewType.Minmax, null),
        ShapedMap("map_shaped", "Shaped Map", ViewType.Dropdown, DropDowns.YesNo.values()),
        ElderMap("map_elder", "Elder Map", ViewType.Dropdown, DropDowns.YesNo.values()),
        BlightedMap("map_blighted", "Blighted Map", ViewType.Dropdown, DropDowns.YesNo.values()),
        MapRegion("map_region", "Map Region", ViewType.Dropdown, DropDowns.MapRegion.values()),
        MapSeries("map_series", "Map Series", ViewType.Dropdown, DropDowns.MapSeries.values())
    }

    enum class HeistFilters(
        override val id: String?,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: Array<*>?
    ) : IFilter {
        ContractObjectiveValue(
            "heist_objective_value",
            "Contract Objective Value",
            ViewType.Dropdown,
            DropDowns.ContractObjectiveValue.values()
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
        override val id: String?,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: Array<*>?
    ) : IFilter {
        Quality("quality", "Quality", ViewType.Minmax, null),
        ItemLevel("ilvl", "Item Level", ViewType.Minmax, null),
        GemLevel("gem_level", "Gem Level", ViewType.Minmax, null),
        GemExperience("gem_level_progress", "Gem Experience %", ViewType.Minmax, null),
        GemQualityType(
            "gem_alternate_quality",
            "Gem Quality Type",
            ViewType.Dropdown,
            DropDowns.GemQualityType.values()
        ),
        ShaperInfluence(
            "shaper_item",
            "Shaper Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values()
        ),
        ElderInfluence(
            "elder_item",
            "Elder Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values()
        ),
        CrusaderInfluence(
            "crusader_item",
            "Crusader Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values()
        ),
        RedeemerInfluence(
            "redeemer_item",
            "Redeemer Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values()
        ),
        HunterInfluence(
            "hunter_item",
            "Hunter Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values()
        ),
        WarlordInfluence(
            "warlord_item",
            "Warlord Influence",
            ViewType.Dropdown,
            DropDowns.YesNo.values()
        ),
        FracturedItem(
            "fractured_item",
            "Fractured Item",
            ViewType.Dropdown,
            DropDowns.YesNo.values()
        ),
        SynthesisedItem(
            "synthesised_item",
            "Synthesised Item",
            ViewType.Dropdown,
            DropDowns.YesNo.values()
        ),
        AlternateArt(
            "alternate_art",
            "Alternate Art",
            ViewType.Dropdown,
            DropDowns.GemQualityType.values()
        ),
        Identified("identified", "Identified", ViewType.Dropdown, DropDowns.YesNo.values()),
        Corrupted("corrupted", "Corrupted", ViewType.Dropdown, DropDowns.YesNo.values()),
        Mirrored("mirrored", "Mirrored", ViewType.Dropdown, DropDowns.YesNo.values()),
        Crafted("crafted", "Crafted", ViewType.Dropdown, DropDowns.YesNo.values()),
        Veiled("veiled", "Veiled", ViewType.Dropdown, DropDowns.YesNo.values()),
        Enchanted("enchanted", "Enchanted", ViewType.Dropdown, DropDowns.YesNo.values()),
        TalismanTier("talisman_tier", "Talisman Tier", ViewType.Minmax, null),
        StoredExperience("stored_experience", "Stored Experience", ViewType.Minmax, null),
        StackSize("stack_size", "Stack Size", ViewType.Minmax, null)
    }

    enum class TradeFilters(
        override val id: String?,
        override val text: String,
        override val viewType: ViewType,
        override val dropDownValues: Array<*>?
    ) : IFilter {
        SellerAccount("account", "Seller Account", ViewType.Account, null),
        Listed("indexed", "Listed", ViewType.Dropdown, DropDowns.Listed.values()),
        SaleType("sale_type", "Sale Type", ViewType.Dropdown, DropDowns.SaleType.values()),
        BuyoutPrice("price", "Buyout Price", ViewType.Buyout, DropDowns.BuyoutPrice.values())
    }
}