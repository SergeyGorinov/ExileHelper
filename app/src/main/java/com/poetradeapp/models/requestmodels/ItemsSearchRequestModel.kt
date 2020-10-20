package com.poetradeapp.models.requestmodels

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.Delegates

interface NullCheckable {
    @JsonIgnore
    fun isEmpty(): Boolean
}

@JsonInclude
data class ItemRequestModel(
    val query: ItemRequestModelFields.Query = ItemRequestModelFields.Query(),
    val sort: ItemRequestModelFields.Sorting = ItemRequestModelFields.Sorting()
)

class ItemRequestModelFields {
    @JsonInclude
    data class Query(
        var status: Status = Status(),
        val stats: MutableList<Stat> = mutableListOf(Stat()),
        @JsonInclude(JsonInclude.Include.NON_NULL)
        var name: String? = null,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        var type: String? = null,
        @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
        val filters: Filters = Filters()
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Sorting(
        var price: String? = "asc",
        var quality: String? = null,
        var pdamage: String? = null,
        var crit: String? = null,
        var aps: String? = null,
        var ilvl: String? = null,
        var pdps: String? = null,
        var edamage: String? = null,
        var edps: String? = null,
        var dps: String? = null,
        var ev: String? = null,
        var ar: String? = null,
        var es: String? = null,
        var block: String? = null
    )

    @JsonInclude
    data class Status(
        var option: String = "online"
    )

    @JsonInclude
    data class Stat(
        var type: String = "and",
        var filters: MutableList<StatFilter> = mutableListOf()
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class StatFilter(
        var id: String,
        var value: MinMax?,
        var disabled: Boolean
    )

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
    data class Filters(
        val map_filters: MapFilter = MapFilter(),
        val misc_filters: MiscFilter = MiscFilter(),
        val socket_filters: SocketFilter = SocketFilter(),
        val armour_filters: ArmourFilter = ArmourFilter(),
        val weapon_filters: WeaponFilter = WeaponFilter(),
        val type_filters: TypeFilter = TypeFilter(),
        val req_filters: ReqFilter = ReqFilter(),
        val trade_filters: TradeFilter = TradeFilter(),
        val heist_filters: HeistFilter = HeistFilter()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return map_filters.filters.isEmpty() &&
                    misc_filters.filters.isEmpty() &&
                    socket_filters.filters.isEmpty() &&
                    armour_filters.filters.isEmpty() &&
                    weapon_filters.filters.isEmpty() &&
                    type_filters.filters.isEmpty() &&
                    req_filters.filters.isEmpty() &&
                    trade_filters.filters.isEmpty() &&
                    heist_filters.filters.isEmpty()
        }
    }

    @JsonInclude
    data class MapFilter(
        var disabled: Boolean = true,
        @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
        var filters: MapFilters = MapFilters()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return filters.isEmpty()
        }
    }

    @JsonInclude
    data class MiscFilter(
        var disabled: Boolean = true,
        @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
        val filters: MiscFilters = MiscFilters()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return filters.isEmpty()
        }
    }

    @JsonInclude
    data class SocketFilter(
        var disabled: Boolean = true,
        @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
        val filters: SocketFilters = SocketFilters()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return filters.isEmpty()
        }
    }

    @JsonInclude
    data class ArmourFilter(
        var disabled: Boolean = true,
        @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
        val filters: ArmourFilters = ArmourFilters()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return filters.isEmpty()
        }
    }

    @JsonInclude
    data class WeaponFilter(
        var disabled: Boolean = true,
        @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
        val filters: WeaponFilters = WeaponFilters()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return filters.isEmpty()
        }
    }

    @JsonInclude
    data class TypeFilter(
        var disabled: Boolean = false,
        @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
        val filters: TypeFilters = TypeFilters()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return filters.isEmpty()
        }
    }

    @JsonInclude
    data class ReqFilter(
        var disabled: Boolean = true,
        @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
        val filters: ReqFilters = ReqFilters()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return filters.isEmpty()
        }
    }

    @JsonInclude
    data class TradeFilter(
        var disabled: Boolean = true,
        @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
        val filters: TradeFilters = TradeFilters()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return filters.isEmpty()
        }
    }

    @JsonInclude
    data class HeistFilter(
        var disabled: Boolean = true,
        @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
        val filters: HeistFilters = HeistFilters()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return filters.isEmpty()
        }
    }

    @ExperimentalCoroutinesApi
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
    data class MapFilters(
        private var _map_tier: MinMax? = null,
        private var _map_iiq: MinMax? = null,
        private var _map_shaped: DropDown? = null,
        private var _map_blighted: DropDown? = null,
        private var _map_series: DropDown? = null,
        private var _map_packsize: MinMax? = null,
        private var _map_iir: MinMax? = null,
        private var _map_elder: DropDown? = null,
        private var _map_region: DropDown? = null
    ) : NullCheckable {

        val isEmptyState = MutableStateFlow(isEmpty())

        var mapTier: MinMax? by Delegates.observable(_map_tier) { _, _, new ->
            _map_tier = new
            isEmptyState.value = isEmpty()
        }
        var mapIiq: MinMax? by Delegates.observable(_map_iir) { _, _, new ->
            _map_iir = new
            isEmptyState.value = isEmpty()
        }
        var mapShaped: DropDown? by Delegates.observable(_map_shaped) { _, _, new ->
            _map_shaped = new
            isEmptyState.value = isEmpty()
        }
        var mapBlighted: DropDown? by Delegates.observable(_map_blighted) { _, _, new ->
            _map_blighted = new
            isEmptyState.value = isEmpty()
        }
        var mapSeries: DropDown? by Delegates.observable(_map_series) { _, _, new ->
            _map_series = new
            isEmptyState.value = isEmpty()
        }
        var mapPacksize: MinMax? by Delegates.observable(_map_packsize) { _, _, new ->
            _map_packsize = new
            isEmptyState.value = isEmpty()
        }
        var mapIir: MinMax? by Delegates.observable(_map_iir) { _, _, new ->
            _map_iir = new
            isEmptyState.value = isEmpty()
        }
        var mapElder: DropDown? by Delegates.observable(_map_elder) { _, _, new ->
            _map_elder = new
            isEmptyState.value = isEmpty()
        }
        var mapRegion: DropDown? by Delegates.observable(_map_region) { _, _, new ->
            _map_region = new
            isEmptyState.value = isEmpty()
        }

        override fun isEmpty(): Boolean {
            return listOfNotNull(
                _map_tier,
                _map_iiq,
                _map_shaped,
                _map_blighted,
                _map_series,
                _map_packsize,
                _map_iir,
                _map_elder,
                _map_region
            ).all { a -> a.isEmpty() }
        }
    }

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
    data class MiscFilters(
        var quality: MinMax? = null,
        var gem_level: MinMax? = null,
        var shaper_item: DropDown? = null,
        var crusader_item: DropDown? = null,
        var hunter_item: DropDown? = null,
        var fractured_item: DropDown? = null,
        var alternate_art: DropDown? = null,
        var corrupted: DropDown? = null,
        var crafted: DropDown? = null,
        var enchanted: DropDown? = null,
        var stored_experience: MinMax? = null,
        var durability: MinMax? = null,
        var ilvl: MinMax? = null,
        var gem_level_progress: MinMax? = null,
        var gem_alternate_quality: DropDown? = null,
        var elder_item: DropDown? = null,
        var redeemer_item: DropDown? = null,
        var warlord_item: DropDown? = null,
        var synthesised_item: DropDown? = null,
        var identified: DropDown? = null,
        var mirrored: DropDown? = null,
        var veiled: DropDown? = null,
        var talisman_tier: MinMax? = null,
        var stack_size: MinMax? = null
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return listOfNotNull(
                quality,
                gem_level,
                shaper_item,
                crusader_item,
                hunter_item,
                fractured_item,
                alternate_art,
                corrupted,
                crafted,
                enchanted,
                stored_experience,
                durability,
                ilvl,
                gem_level_progress,
                elder_item,
                redeemer_item,
                warlord_item,
                synthesised_item,
                identified,
                mirrored,
                veiled,
                talisman_tier,
                stack_size
            ).all { a -> a.isEmpty() }
        }
    }

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
    data class SocketFilters(
        var sockets: Sockets = Sockets(),
        var links: Sockets = Sockets()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return sockets.isEmpty() && links.isEmpty()
        }
    }

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
    data class ArmourFilters(
        var ar: MinMax? = null,
        var es: MinMax? = null,
        var ev: MinMax? = null,
        var block: MinMax? = null
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return listOfNotNull(ar, es, ev, block).all { a -> a.isEmpty() }
        }
    }

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
    data class WeaponFilters(
        var damage: MinMax? = null,
        var crit: MinMax? = null,
        var pdps: MinMax? = null,
        var aps: MinMax? = null,
        var dps: MinMax? = null,
        var edps: MinMax? = null
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return listOfNotNull(damage, crit, pdps, aps, dps, edps).all { a -> a.isEmpty() }
        }
    }

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
    data class TypeFilters(
        var category: DropDown? = null,
        var rarity: DropDown? = null
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return listOfNotNull(category, rarity).all { a -> a.isEmpty() }
        }
    }

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
    data class ReqFilters(
        var lvl: MinMax? = null,
        var dex: MinMax? = null,
        var str: MinMax? = null,
        var int: MinMax? = null
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return listOfNotNull(lvl, dex, str, int).all { a -> a.isEmpty() }
        }
    }

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
    data class TradeFilters(
        var account: String? = null,
        var indexed: DropDown? = null,
        var sale_type: DropDown? = null,
        var price: Price = Price()
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return listOfNotNull(
                indexed,
                sale_type,
                price
            ).all { a -> a.isEmpty() } && account == null
        }
    }

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = AllFieldsNullFilter::class)
    data class HeistFilters(
        var heist_wings: MinMax? = null,
        var heist_max_wings: MinMax? = null,
        var heist_escape_routes: MinMax? = null,
        var heist_max_escape_routes: MinMax? = null,
        var heist_reward_rooms: MinMax? = null,
        var heist_max_reward_rooms: MinMax? = null,
        var area_level: MinMax? = null,
        var heist_lockpicking: MinMax? = null,
        var heist_brute_force: MinMax? = null,
        var heist_perception: MinMax? = null,
        var heist_demolition: MinMax? = null,
        var heist_counter_thaumaturgy: MinMax? = null,
        var heist_trap_disarmament: MinMax? = null,
        var heist_agility: MinMax? = null,
        var heist_deception: MinMax? = null,
        var heist_engineering: MinMax? = null,
        var heist_objective_value: DropDown? = null
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return listOfNotNull(
                heist_wings,
                heist_max_wings,
                heist_escape_routes,
                heist_max_escape_routes,
                heist_reward_rooms,
                heist_max_reward_rooms,
                area_level,
                heist_lockpicking,
                heist_brute_force,
                heist_perception,
                heist_demolition,
                heist_counter_thaumaturgy,
                heist_trap_disarmament,
                heist_agility,
                heist_deception,
                heist_engineering,
                heist_objective_value
            ).all { a -> a.isEmpty() }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Sockets(
        var r: Int? = null,
        var g: Int? = null,
        var b: Int? = null,
        var w: Int? = null,
        var min: Int? = null,
        var max: Int? = null
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return listOfNotNull(r, g, b, w, min, max).isEmpty()
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class MinMax(
        var min: Int?,
        var max: Int?
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return listOfNotNull(min, max).isEmpty()
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class DropDown(
        var option: String?
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return option == null
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Price(
        var min: Int? = null,
        var max: Int? = null,
        var option: String? = null
    ) : NullCheckable {
        override fun isEmpty(): Boolean {
            return listOfNotNull(min, max, option).isEmpty()
        }
    }

    class AllFieldsNullFilter {
        override fun equals(other: Any?): Boolean {
            if (other == null)
                return true
            if (other is NullCheckable) {
                return other.isEmpty()
            }
            return false
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }
}