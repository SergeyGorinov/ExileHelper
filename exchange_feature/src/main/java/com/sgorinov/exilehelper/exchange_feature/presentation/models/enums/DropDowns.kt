package com.sgorinov.exilehelper.exchange_feature.presentation.models.enums

@Suppress("unused")
internal class DropDowns {

    enum class YesNo(override val id: String?, override val text: String) : IEnum {
        Any(null, "Any"),
        Yes("true", "Yes"),
        No("false", "No")
    }

    enum class ItemCategory(override val id: String?, override val text: String) : IEnum {
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
        Currency("currency_feature", "Any Currency"),
        UniqueFragment("currency_feature.piece", "Unique Fragment"),
        Resonator("currency_feature.resonator", "Resonator"),
        Fossil("currency_feature.fossil", "Fossil"),
        Incubator("currency_feature.incubator", "Incubator"),
        HeistTarget("currency_feature.heistobjective", "Heist Target")
    }

    enum class ItemRarity(override val id: String?, override val text: String) : IEnum {
        Any(null, "Any"),
        Normal("normal", "Normal"),
        Magic("magic", "Magic"),
        Rare("rare", "Rare"),
        Unique("unique", "Unique"),
        UniqueRelic("uniquefoil", "Unique (Relic)"),
        NonUnique("nonunique", "Any Non-Unique")
    }

    enum class ContractObjectiveValue(override val id: String?, override val text: String) : IEnum {
        Any(null, "Any"),
        Moderate("moderate", "Moderate Value"),
        High("high", "High Value"),
        Precious("precious", "Precious"),
        Priceless("priceless", "Priceless")
    }

    enum class MapRegion(override val id: String?, override val text: String) : IEnum {
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

    enum class MapSeries(override val id: String?, override val text: String) : IEnum {
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

    enum class GemQualityType(override val id: String?, override val text: String) : IEnum {
        Any(null, "Any"),
        Superior("0", "Superior (Default)"),
        Alternate("", "Any Alternate"),
        Anomalous("1", "Anomalous"),
        Divergent("2", "Divergent"),
        Phantasmal("3", "Phantasmal")
    }

    enum class Listed(override val id: String?, override val text: String) : IEnum {
        Any(null, "Any"),
        OneDayAgo("1day", "Up to a Day Ago"),
        ThreeDaysAgo("3days", "Up to 3 Days Ago"),
        OneWeekAgo("1week", "Up to a Week Ago"),
        TwoWeeksAgo("2weeks", "Up to 2 Weeks Ago"),
        OneMonthAgo("1month", "Up to 1 Month Ago"),
        OneMonthsAgo("2months", "Up to 2 Months Ago")
    }

    enum class SaleType(override val id: String?, override val text: String) : IEnum {
        Any(null, "Any"),
        Priced("priced", "Buyout or Fixed Price"),
        Unpriced("unpriced", "No Listed Price")
    }

    enum class BuyoutPrice(override val id: String?, override val text: String) : IEnum {
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

    enum class ChallengeType(override val id: String?, override val text: String) : IEnum {
        Any(null, "Any"),
        Exterminate("Exterminate", "Defeat Waves of Enemies"),
        Survival("Survival", "Survive"),
        Defense("Defense", "Protect the Altar"),
        Conquer("Conquer", "Stand in the Stone Circles")
    }

    enum class RewardType(override val id: String?, override val text: String) : IEnum {
        Any(null, "Any"),
        DoubleCurrency("DoubleCurrency", "Currency"),
        DoubleDivCards("DoubleDivCards", "Divination Cards"),
        MirrorRare("MirrorRare", "Mirrored Rare Item"),
        ExchangeUnique("ExchangeUnique", "Unique Item")
    }
}