package com.poetradeapp.models.enums

class ViewFilters {
    enum class AllFilters(val text: String) {
        WeaponFilter("Weapon Filters"),
        ArmourFilter("Armour Filters"),
        ReqFilter("Requirements"),
        MapFilter("Map Filters"),
        HeistFilter("Heist Filters"),
        MiscFilter("Miscellaneous")
    }

    enum class TypeFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : IFilter {
        Category("category", "Item Category", true),
        Rarity("rarity", "Item Rarity", true)
    }

    enum class WeaponFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : IFilter {
        Damage("damage", "Damage", false),
        APS("aps", "Attacks per Second", false),
        CritChance("crit", "Critical Chance", false),
        DPS("dps", "Damage per Second", false),
        PDPS("pdps", "Physical DPS", false),
        EDPS("edps", "Elemental DPS", false)
    }

    enum class ArmourFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : IFilter {
        Armour("ar", "Armour", false),
        Evasion("ev", "Evasion", false),
        EnergyShield("es", "Energy Shield", false),
        Block("block", "Block", false)
    }

    enum class SocketFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : IFilter {
        Sockets("sockets", "Sockets", false),
        Links("links", "Links", false)
    }

    enum class SocketTypes {
        R,
        G,
        B,
        W,
        MIN,
        MAX
    }

    enum class ReqFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : IFilter {
        Level("lvl", "Level", false),
        Strength("str", "Strength", false),
        Dexterity("dex", "Dexterity", false),
        Intelligence("int", "Intelligence", false)
    }

    enum class MapFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : IFilter {
        MapTier("map_tier", "Map Tier", false),
        MapPacksize("map_packsize", "Map Packsize", false),
        MapIIQ("map_iiq", "Map IIQ", false),
        MapIIR("map_iir", "Map IIR", false),
        ShapedMap("map_shaped", "Shaped Map", true),
        ElderMap("map_elder", "Elder Map", true),
        BlightedMap("map_blighted", "Blighted Map", true),
        MapRegion("map_region", "Map Region", true),
        MapSeries("map_series", "Map Series", true)
    }

    enum class HeistFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : IFilter {
        ContractObjectiveValue("heist_objective_value", "Contract Objective Value", true),
        WingsRevealed("heist_wings", "Wings Revealed", false),
        TotalWings("heist_max_wings", "Total Wings", false),
        EscapeRoutesRevealed("heist_escape_routes", "Escape Routes Revealed", false),
        TotalEscapeRoutes("heist_max_escape_routes", "Total Escape Routes", false),
        RewardRoomsRevealed("heist_reward_rooms", "Reward Rooms Revealed", false),
        TotalRewardRooms("heist_max_reward_rooms", "Total Reward Rooms", false),
        AreaLevel("area_level", "Area Level", false),
        LockpickingLevel("heist_lockpicking", "Lockpicking Level", false),
        BruteForceLevel("heist_brute_force", "Brute Force Level", false),
        PerceptionLevel("heist_perception", "Perception Level", false),
        DemolitionLevel("heist_demolition", "Demolition Level", false),
        CounterThaumLevel("heist_counter_thaumaturgy", "Counter-Thaum. Level", false),
        TrapDisarmamentLevel("heist_trap_disarmament", "Trap Disarmament Level", false),
        AgilityLevel("heist_agility", "Agility Level", false),
        DeceptionLevel("heist_deception", "Deception Level", false),
        EngineeringLevel("heist_engineering", "Engineering Level", false)
    }

    enum class MiscFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : IFilter {
        Quality("quality", "Quality", false),
        ItemLevel("ilvl", "Item Level", false),
        GemLevel("gem_level", "Gem Level", false),
        GemExperience("gem_level_progress", "Gem Experience %", false),
        GemQualityType("gem_alternate_quality", "Gem Quality Type", true),
        ShaperInfluence("shaper_item", "Shaper Influence", true),
        ElderInfluence("elder_item", "Elder Influence", true),
        CrusaderInfluence("crusader_item", "Crusader Influence", true),
        RedeemerInfluence("redeemer_item", "Redeemer Influence", true),
        HunterInfluence("hunter_item", "Hunter Influence", true),
        WarlordInfluence("warlord_item", "Warlord Influence", true),
        FracturedItem("fractured_item", "Fractured Item", true),
        SynthesisedItem("synthesised_item", "Synthesised Item", true),
        AlternateArt("alternate_art", "Alternate Art", true),
        Identified("identified", "Identified", true),
        Corrupted("corrupted", "Corrupted", true),
        Mirrored("mirrored", "Mirrored", true),
        Crafted("crafted", "Crafted", true),
        Veiled("veiled", "Veiled", true),
        Enchanted("enchanted", "Enchanted", true),
        TalismanTier("talisman_tier", "Talisman Tier", false),
        StoredExperience("stored_experience", "Stored Experience", false),
        StackSize("stack_size", "Stack Size", false)
    }

    enum class TradeFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : IFilter {
        SellerAccount("account", "Seller Account", false),
        Listed("indexed", "Listed", true),
        SaleType("sale_type", "Sale Type", true),
        BuyoutPrice("price", "Buyout Price", true)
    }
}