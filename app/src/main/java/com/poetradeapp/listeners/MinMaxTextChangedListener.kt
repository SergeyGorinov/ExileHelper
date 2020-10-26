package com.poetradeapp.listeners

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.poetradeapp.models.enums.IFilter
import com.poetradeapp.models.enums.ViewFilters
import com.poetradeapp.models.requestmodels.ItemRequestModelFields.Filters
import com.poetradeapp.models.requestmodels.ItemRequestModelFields.MinMax
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MinMaxTextChangedListener(
    private val item: IFilter,
    private val filterMin: TextInputEditText,
    private val filterMax: TextInputEditText,
    private val filters: Filters
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val min = filterMin.text?.toString()?.toIntOrNull()
        val max = filterMax.text?.toString()?.toIntOrNull()
        val minMax = MinMax(min, max)
        if (item is ViewFilters.WeaponFilters) {
            val filter = filters.weapon_filters.filters
            when (item) {
                ViewFilters.WeaponFilters.Damage -> {
                    filter.damage = minMax
                }
                ViewFilters.WeaponFilters.APS -> {
                    filter.aps = minMax
                }
                ViewFilters.WeaponFilters.CritChance -> {
                    filter.crit = minMax
                }
                ViewFilters.WeaponFilters.DPS -> {
                    filter.dps = minMax
                }
                ViewFilters.WeaponFilters.PDPS -> {
                    filter.pdps = minMax
                }
                ViewFilters.WeaponFilters.EDPS -> {
                    filter.edps = minMax
                }
            }
        }
        if (item is ViewFilters.ArmourFilters) {
            val filter = filters.armour_filters.filters
            when (item) {
                ViewFilters.ArmourFilters.Armour -> {
                    filter.ar = minMax
                }
                ViewFilters.ArmourFilters.Evasion -> {
                    filter.ev = minMax
                }
                ViewFilters.ArmourFilters.EnergyShield -> {
                    filter.es = minMax
                }
                ViewFilters.ArmourFilters.Block -> {
                    filter.block = minMax
                }
            }
        }
        if (item is ViewFilters.ReqFilters) {
            val filter = filters.req_filters.filters
            when (item) {
                ViewFilters.ReqFilters.Level -> {
                    filter.lvl = minMax
                }
                ViewFilters.ReqFilters.Strength -> {
                    filter.str = minMax
                }
                ViewFilters.ReqFilters.Dexterity -> {
                    filter.dex = minMax
                }
                ViewFilters.ReqFilters.Intelligence -> {
                    filter.int = minMax
                }
            }
        }
        if (item is ViewFilters.MapFilters) {
            val filter = filters.map_filters.filters
            when (item) {
                ViewFilters.MapFilters.MapTier -> {
                    filter.mapTier = minMax
                }
                ViewFilters.MapFilters.MapPacksize -> {
                    filter.mapPacksize = minMax
                }
                ViewFilters.MapFilters.MapIIQ -> {
                    filter.mapIiq = minMax
                }
                ViewFilters.MapFilters.MapIIR -> {
                    filter.mapIir = minMax
                }
            }
        }
        if (item is ViewFilters.MiscFilters) {
            val filter = filters.misc_filters.filters
            when (item) {
                ViewFilters.MiscFilters.Quality -> {
                    filter.quality = minMax
                }
                ViewFilters.MiscFilters.ItemLevel -> {
                    filter.ilvl = minMax
                }
                ViewFilters.MiscFilters.GemLevel -> {
                    filter.gem_level = minMax
                }
                ViewFilters.MiscFilters.GemExperience -> {
                    filter.gem_level_progress = minMax
                }
                ViewFilters.MiscFilters.TalismanTier -> {
                    filter.talisman_tier = minMax
                }
                ViewFilters.MiscFilters.StoredExperience -> {
                    filter.stored_experience = minMax
                }
                ViewFilters.MiscFilters.StackSize -> {
                    filter.stack_size = minMax
                }
            }
        }
        if (item is ViewFilters.HeistFilters) {
            val filter = filters.heist_filters.filters
            when (item) {
                ViewFilters.HeistFilters.AgilityLevel -> {
                    filter.heist_agility = minMax
                }
                ViewFilters.HeistFilters.AreaLevel -> {
                    filter.area_level = minMax
                }
                ViewFilters.HeistFilters.BruteForceLevel -> {
                    filter.heist_brute_force = minMax
                }
                ViewFilters.HeistFilters.CounterThaumLevel -> {
                    filter.heist_counter_thaumaturgy = minMax
                }
                ViewFilters.HeistFilters.DeceptionLevel -> {
                    filter.heist_deception = minMax
                }
                ViewFilters.HeistFilters.DemolitionLevel -> {
                    filter.heist_demolition = minMax
                }
                ViewFilters.HeistFilters.EngineeringLevel -> {
                    filter.heist_engineering = minMax
                }
                ViewFilters.HeistFilters.EscapeRoutesRevealed -> {
                    filter.heist_escape_routes = minMax
                }
                ViewFilters.HeistFilters.LockpickingLevel -> {
                    filter.heist_lockpicking = minMax
                }
                ViewFilters.HeistFilters.PerceptionLevel -> {
                    filter.heist_perception = minMax
                }
                ViewFilters.HeistFilters.RewardRoomsRevealed -> {
                    filter.heist_reward_rooms = minMax
                }
                ViewFilters.HeistFilters.TotalEscapeRoutes -> {
                    filter.heist_max_escape_routes = minMax
                }
                ViewFilters.HeistFilters.TotalRewardRooms -> {
                    filter.heist_max_reward_rooms = minMax
                }
                ViewFilters.HeistFilters.TotalWings -> {
                    filter.heist_max_wings = minMax
                }
                ViewFilters.HeistFilters.WingsRevealed -> {
                    filter.heist_max_wings = minMax
                }
                ViewFilters.HeistFilters.TrapDisarmamentLevel -> {
                    filter.heist_trap_disarmament = minMax
                }
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) = Unit
}