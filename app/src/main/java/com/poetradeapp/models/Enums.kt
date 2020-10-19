package com.poetradeapp.models

interface GenericEnum {
    val id: String?
    val text: String
}

interface FiltersEnum : GenericEnum {
    val isDropDown: Boolean
}

class EnumFilters {

    enum class TypeFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : FiltersEnum {
        Category("category", "Item Category", true),
        Rarity("rarity", "Item Rarity", true)
    }

    enum class WeaponFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : FiltersEnum {
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
    ) : FiltersEnum {
        Armour("ar", "Armour", false),
        Evasion("ev", "Evasion", false),
        EnergyShield("es", "Energy Shield", false),
        Block("block", "Block", false)
    }

    enum class SocketFilters(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : FiltersEnum {
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

    enum class ReqFilter(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : FiltersEnum {
        Level("lvl", "Level", false),
        Strength("str", "Strength", false),
        Dexterity("dex", "Dexterity", false),
        Intelligence("int", "Intelligence", false)
    }

    enum class MapFilter(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : FiltersEnum {
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

    enum class HeistFilter(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : FiltersEnum {
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

    enum class MiscFilter(
        override val id: String?,
        override val text: String,
        override val isDropDown: Boolean
    ) : FiltersEnum {
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
    ) : FiltersEnum {
        SellerAccount("account", "Seller Account", false),
        Listed("indexed", "Listed", true),
        SaleType("sale_type", "Sale Type", true),
        BuyoutPrice("price", "Buyout Price", true)
    }
}

class Enums {
    enum class Filters(val text: String) {
        TypeFilter("Type Filters"),
        WeaponFilter("Weapon Filters"),
        ArmourFilter("Armour Filters"),
        SocketFilter("Socket Filters"),
        ReqFilter("Requirements"),
        MapFilter("Map Filters"),
        HeistFilter("Heist Filters"),
        MiscFilter("Miscellaneous"),
        TradeFilter("Trade Filters")
    }

    enum class YesNo(override val id: String?, override val text: String) : GenericEnum {
        Any(null, "Any"),
        Yes("true", "Yes"),
        No("false", "No")
    }

    enum class ItemCategory(override val id: String?, override val text: String) : GenericEnum {
        Any(null, "Any"),
        Weapon("weapon", "Any Weapon"),
        WeaponOne("weapon.one", "One-Handed Weapon"),
        WeaponOneMelee("weapon.onemelee", "One-Handed Melee Weapon"),
        WeaponTwoMelee("weapon.twomelee", "Two-Handed Melee Weapon"),
        WeaponBow("weapon.bow", "Bow"),
        WeaponClaw("weapon.claw", "Claw"),
        WeaponDagger("weapon.dagger", "Any Dagger"),
        WeaponBaseDagger("weapon.basedagger", "Base Dagger"),
        WeaponRuneDagger("weapon.runedagger", "Rune Dagger"),
        WeaponOneAxe("weapon.oneaxe", "One-Handed Axe"),
        WeaponOneMace("weapon.onemace", "One-Handed Mace"),
        WeaponOneSword("weapon.onesword", "One-Handed Sword"),
        WeaponSceptre("weapon.sceptre", "Sceptre"),
        WeaponStaff("weapon.staff", "Any Staff"),
        WeaponBaseStaff("weapon.basestaff", "Base Staff"),
        WeaponWarStaff("weapon.warstaff", "Warstaff"),
        WeaponTwoAxe("weapon.twoaxe", "Two-Handed Axe"),
        WeaponTwoMace("weapon.twomace", "Two-Handed Mace"),
        WeaponTwoSword("weapon.twosword", "Two-Handed Sword"),
        WeaponWand("weapon.wand", "Wand"),
        WeaponRod("weapon.rod", "Fishing Rod"),
        Armour("armour", "Any Armour"),
        ArmourChest("armour.chest", "Body Armour"),
        ArmourBoots("armour.boots", "Boots"),
        ArmourGloves("armour.gloves", "Gloves"),
        ArmourHelmet("armour.helmet", "Helmet"),
        ArmourShield("armour.shield", "Shield"),
        ArmourQuiver("armour.quiver", "Quiver"),
        Accessory("accessory.", "Any Accessory"),
        AccessoryAmulet("accessory.amulet", "Amulet"),
        AccessoryBelt("accessory.Belt", "Belt"),
        AccessoryRing("accessory.ring", "Ring"),
        AccessoryTrinket("accessory.trinket", "Trinket"),
        Gem("gem", "Any Gem"),
        GemSkill("gem.activegem", "Skill Gem"),
        GemSupport("gem.supportgem", "Support Gem"),
        GemAwakenedSupport("gem.supportgemplus", "Awakened Support Gem"),
        Jewel("jewel", "Any Jewel"),
        JewelBase("jewel.base", "Base Jewel"),
        JewelAbyss("jewel.abyss", "Abyss Jewel"),
        JewelCluster("jewel.cluster", "Cluster Jewel"),
        Flask("flask", "Flask"),
        Map("map", "Map"),
        MapFragment("map.fragment", "Map Fragment"),
        MapScarab("map.scarab", "Scarab"),
        Watchstone("watchstone", "Watchstone"),
        Leaguestone("leaguestone", "Leaguestone"),
        Prophecy("prophecy", "Prophecy"),
        Card("card", "Card"),
        CapturedBeast("monster.beast", "Captured Beast"),
        MetamorphSample("monster.sample", "Metamorph Sample"),
        Heist("heistequipment", "Any Heist Equipment"),
        HeistGear("heistequipment.heistweapon", "Heist Gear"),
        HeistTool("heistequipment.heisttool", "Heist Tool"),
        HeistCloak("heistequipment.heistutility", "Heist Cloak"),
        HeistBrooch("heistequipment.heistreward", "Heist Brooch"),
        HeistMission("heistmission", "Any Heist Mission"),
        HeistContract("heistmission.contract", "Heist Contract"),
        HeistBlueprint("heistmission.blueprint", "Heist Blueprint"),
        Currency("currency", "Any Currency"),
        UniqueFragment("currency.piece", "Unique Fragment"),
        Resonator("currency.resonator", "Resonator"),
        Fossil("currency.fossil", "Fossil"),
        Incubator("currency.incubator", "Incubator"),
        HeistTarget("currency.heistobjective", "Heist Target")
    }

    enum class ItemRarity(override val id: String?, override val text: String) : GenericEnum {
        Any(null, "Any"),
        Normal("normal", "Normal"),
        Magic("magic", "Magic"),
        Rare("rare", "Rare"),
        Unique("unique", "Unique"),
        UniqueRelic("uniquefoil", "Unique (Relic)"),
        NonUnique("nonunique", "Any Non-Unique")
    }

    enum class ContractObjectiveValue(override val id: String?, override val text: String) :
        GenericEnum {
        Any(null, "Any"),
        Moderate("moderate", "Moderate Value"),
        High("high", "High Value"),
        Precious("precious", "Precious"),
        Priceless("priceless", "Priceless")
    }

    enum class MapRegion(override val id: String?, override val text: String) : GenericEnum {
        Any(null, "Any"),
        HaewarkHamlet("otl", "Haewark Hamlet"),
        TirnsEnd("itl", "Tirn's End"),
        LexProxima("itr", "Lex Proxima"),
        LexEjoris("otr", "Lex Ejoris"),
        NewVastir("obl", "New Vastir"),
        GlennachCairns("ibl", "Glennach Cairns"),
        ValdosRest("ibr", "Valdo's Rest"),
        LiraArthain("obr", "Lira Arthain")
    }

    enum class MapSeries(override val id: String?, override val text: String) : GenericEnum {
        Any(null, "Any"),
        Current("current", "Current"),
        Harvest("harvest", "Harvest"),
        Delirium("delirium", "Delirium"),
        Metamorph("metamorph", "Metamorph"),
        Blight("blight", "Blight"),
        Legion("legion", "Legion"),
        Synthesis("synthesis", "Synthesis"),
        Betrayal("betrayal", "Betrayal"),
        WarForTheAtlas("warfortheatlas", "War for the Atlas"),
        AtlasOfWorlds("atlasofworlds", "Atlas of Worlds"),
        TheAwakening("theawakening", "The Awakening"),
        Legacy("original", "Legacy")
    }

    enum class GemQualityType(override val id: String?, override val text: String) : GenericEnum {
        Any(null, "Any"),
        Superior("0", "Superior (Default)"),
        Alternate("", "Any Alternate"),
        Anomalous("1", "Anomalous"),
        Divergent("2", "Divergent"),
        Phantasmal("3", "Phantasmal")
    }

    enum class Listed(override val id: String?, override val text: String) : GenericEnum {
        Any(null, "Any"),
        OneDayAgo("1day", "Up to a Day Ago"),
        ThreeDaysAgo("3days", "Up to 3 Days Ago"),
        OneWeekAgo("1week", "Up to a Week Ago"),
        TwoWeeksAgo("2weeks", "Up to 2 Weeks Ago"),
        OneMonthAgo("1month", "Up to 1 Month Ago"),
        OneMonthsAgo("2months", "Up to 2 Months Ago")
    }

    enum class SaleType(override val id: String?, override val text: String) : GenericEnum {
        Any(null, "Any"),
        Priced("priced", "Buyout or Fixed Price"),
        Unpriced("unpriced", "No Listed Price")
    }

    enum class BuyoutPrice(override val id: String?, override val text: String) : GenericEnum {
        ChaosEquivalent(null, "Chaos Orb Equivalent"),
        BlessedOrb("blessed", "Blessed Orb"),
        CartographersChisel("chisel", "Cartographer's Chisel"),
        ChaosOrb("chaos", "Chaos Orb"),
        ChromaticOrb("chrom", "Chromatic Orb"),
        DivineOrb("divine", "Divine Orb"),
        ExaltedOrb("exa", "Exalted Orb"),
        GemcuttersPrism("gcp", "Gemcutter's Prism"),
        JewellersOrb("jew", "Jeweller's Orb"),
        OrbOfScouring("scour", "Orb of Scouring"),
        OrbOfRegret("regret", "Orb of Regret"),
        OrbOfFusing("fusing", "Orb of Fusing"),
        OrbOfChance("chance", "Orb of Chance"),
        OrbOfAlteration("alt", "Orb of Alteration"),
        OrbOfAlchemy("alch", "Orb of Alchemy"),
        RegalOrb("regal", "Regal Orb"),
        VaalOrb("vaal", "Vaal Orb")
    }
}