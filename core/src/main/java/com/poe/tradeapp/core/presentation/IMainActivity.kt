package com.poe.tradeapp.core.presentation

interface IMainActivity {
    val leagues: List<String>
    fun goToCurrencyExchange(wantItemId: String? = null, haveItemId: String? = null)
    fun showBottomNavBarIfNeeded()
    fun signOut()
}