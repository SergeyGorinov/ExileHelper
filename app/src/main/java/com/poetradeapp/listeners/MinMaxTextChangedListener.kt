package com.poetradeapp.listeners

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.poetradeapp.models.EnumFilters
import com.poetradeapp.models.FiltersEnum
import com.poetradeapp.models.requestmodels.ItemRequestModelFields.Filters
import com.poetradeapp.models.requestmodels.ItemRequestModelFields.MinMax
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MinMaxTextChangedListener(
    private val item: FiltersEnum,
    private val filterMin: TextInputEditText,
    private val filterMax: TextInputEditText,
    private val filters: Filters
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val min = filterMin.text?.toString()?.toIntOrNull()
        val max = filterMax.text?.toString()?.toIntOrNull()
        val minMax = MinMax(min, max)
        if (item is EnumFilters.WeaponFilters) {
            val filter = filters.weapon_filters.filters
            when (item) {
                EnumFilters.WeaponFilters.Damage -> {
                    filter.damage = minMax
                }
                EnumFilters.WeaponFilters.APS -> {
                    filter.aps = minMax
                }
                EnumFilters.WeaponFilters.CritChance -> {
                    filter.crit = minMax
                }
                EnumFilters.WeaponFilters.DPS -> {
                    filter.dps = minMax
                }
                EnumFilters.WeaponFilters.PDPS -> {
                    filter.pdps = minMax
                }
                EnumFilters.WeaponFilters.EDPS -> {
                    filter.edps = minMax
                }
            }
        }
        if (item is EnumFilters.ArmourFilters) {
            val filter = filters.armour_filters.filters
            when (item) {
                EnumFilters.ArmourFilters.Armour -> {
                    filter.ar = minMax
                }
                EnumFilters.ArmourFilters.Evasion -> {
                    filter.ev = minMax
                }
                EnumFilters.ArmourFilters.EnergyShield -> {
                    filter.es = minMax
                }
                EnumFilters.ArmourFilters.Block -> {
                    filter.block = minMax
                }
            }
        }
        if (item is EnumFilters.ReqFilter) {
            val filter = filters.req_filters.filters
            when (item) {
                EnumFilters.ReqFilter.Level -> {
                    filter.lvl = minMax
                }
                EnumFilters.ReqFilter.Strength -> {
                    filter.str = minMax
                }
                EnumFilters.ReqFilter.Dexterity -> {
                    filter.dex = minMax
                }
                EnumFilters.ReqFilter.Intelligence -> {
                    filter.int = minMax
                }
            }
        }
        if (item is EnumFilters.MapFilter) {
            val filter = filters.map_filters.filters
            when (item) {
                EnumFilters.MapFilter.MapTier -> {
                    filter.mapTier = minMax
                }
                EnumFilters.MapFilter.MapPacksize -> {
                    filter.mapPacksize = minMax
                }
                EnumFilters.MapFilter.MapIIQ -> {
                    filter.mapIiq = minMax
                }
                EnumFilters.MapFilter.MapIIR -> {
                    filter.mapIir = minMax
                }
            }
        }
        if (item is EnumFilters.MiscFilter) {
            val filter = filters.misc_filters.filters
            when (item) {
                EnumFilters.MiscFilter.Quality -> {
                    filter.quality = minMax
                }
                EnumFilters.MiscFilter.ItemLevel -> {
                    filter.ilvl = minMax
                }
                EnumFilters.MiscFilter.GemLevel -> {
                    filter.gem_level = minMax
                }
                EnumFilters.MiscFilter.GemExperience -> {
                    filter.gem_level_progress = minMax
                }
                EnumFilters.MiscFilter.TalismanTier -> {
                    filter.talisman_tier = minMax
                }
                EnumFilters.MiscFilter.StoredExperience -> {
                    filter.stored_experience = minMax
                }
                EnumFilters.MiscFilter.StackSize -> {
                    filter.stack_size = minMax
                }
            }
        }
        if (item is EnumFilters.HeistFilter) {
            val filter = filters.heist_filters.filters
            when (item) {
                EnumFilters.HeistFilter.AgilityLevel -> {
                    filter.heist_agility = minMax
                }
                EnumFilters.HeistFilter.AreaLevel -> {
                    filter.area_level = minMax
                }
                EnumFilters.HeistFilter.BruteForceLevel -> {
                    filter.heist_brute_force = minMax
                }
                EnumFilters.HeistFilter.CounterThaumLevel -> {
                    filter.heist_counter_thaumaturgy = minMax
                }
                EnumFilters.HeistFilter.DeceptionLevel -> {
                    filter.heist_deception = minMax
                }
                EnumFilters.HeistFilter.DemolitionLevel -> {
                    filter.heist_demolition = minMax
                }
                EnumFilters.HeistFilter.EngineeringLevel -> {
                    filter.heist_engineering = minMax
                }
                EnumFilters.HeistFilter.EscapeRoutesRevealed -> {
                    filter.heist_escape_routes = minMax
                }
                EnumFilters.HeistFilter.LockpickingLevel -> {
                    filter.heist_lockpicking = minMax
                }
                EnumFilters.HeistFilter.PerceptionLevel -> {
                    filter.heist_perception = minMax
                }
                EnumFilters.HeistFilter.RewardRoomsRevealed -> {
                    filter.heist_reward_rooms = minMax
                }
                EnumFilters.HeistFilter.TotalEscapeRoutes -> {
                    filter.heist_max_escape_routes = minMax
                }
                EnumFilters.HeistFilter.TotalRewardRooms -> {
                    filter.heist_max_reward_rooms = minMax
                }
                EnumFilters.HeistFilter.TotalWings -> {
                    filter.heist_max_wings = minMax
                }
                EnumFilters.HeistFilter.WingsRevealed -> {
                    filter.heist_max_wings = minMax
                }
                EnumFilters.HeistFilter.TrapDisarmamentLevel -> {
                    filter.heist_trap_disarmament = minMax
                }
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) = Unit
}