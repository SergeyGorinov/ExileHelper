package com.poe.tradeapp.core.presentation

interface IMainActivity {
    val leagues: List<String>
    fun goToCurrencyExchange(wantItemId: String?, haveItemId: String?)
    fun showBottomNavBarIfNeeded()
}