package com.poe.tradeapp.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.terrakok.cicerone.*
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.github.terrakok.cicerone.androidx.TransactionInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.poe.tradeapp.MyFirebaseMessaging
import com.poe.tradeapp.R
import com.poe.tradeapp.charts_feature.presentation.fragments.ChartsMainFragment
import com.poe.tradeapp.core.DI
import com.poe.tradeapp.core.presentation.IMainActivity
import com.poe.tradeapp.currency.presentation.fragments.CurrencyExchangeMainFragment
import com.poe.tradeapp.databinding.ActivityMainBinding
import com.poe.tradeapp.exchange.presentation.fragments.ItemsSearchMainFragment
import com.poe.tradeapp.presentation.fragments.LoaderFragment
import com.poe.tradeapp.presentation.fragments.StartFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.inject

@ExperimentalCoroutinesApi
class MainActivity : FragmentActivity(), IMainActivity {

    private val viewModel by viewModel<MainActivityViewModel>()

    private val navigatorHolder by DI.inject<NavigatorHolder>()
    private val router by DI.inject<Router>()

    private lateinit var viewBinding: ActivityMainBinding

    private var isApiConnectionChecked = false
    private var itemSearchType: String? = null
    private var itemSearchName: String? = null
    private var withNotificationRequest = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == MyFirebaseMessaging.currencyMessage) {
                Log.d(
                    "CURRENCY PAYLOAD",
                    intent.getStringExtra(MyFirebaseMessaging.currencyMessagePayload) ?: ""
                )
            }
        }
    }

    private val navigator = object : AppNavigator(this@MainActivity, R.id.mainContainer) {

        override fun forward(command: Forward) {
            val screen = command.screen
            if (screen is FragmentScreen) {
                val type =
                    if (command.clearContainer) TransactionInfo.Type.REPLACE else TransactionInfo.Type.ADD
                commitNewFragmentScreenWithCommand(command, screen, type, true)
            }
        }

        override fun replace(command: Replace) {
            val screen = command.screen
            if (screen is FragmentScreen) {
                if (localStackCopy.isNotEmpty()) {
                    fragmentManager.popBackStack()
                    val removed = localStackCopy.removeAt(localStackCopy.lastIndex)
                    commitNewFragmentScreenWithCommand(command, screen, removed.type, true)
                } else {
                    commitNewFragmentScreenWithCommand(
                        command,
                        screen,
                        TransactionInfo.Type.REPLACE,
                        false
                    )
                }
            }
        }

        private fun commitNewFragmentScreenWithCommand(
            command: Command,
            screen: FragmentScreen,
            type: TransactionInfo.Type,
            addToBackStack: Boolean
        ) {
            val fragment = screen.createFragment(fragmentFactory)
            val transaction = fragmentManager.beginTransaction()
            transaction.setReorderingAllowed(true)
            setupFragmentTransactionWithCommand(
                command,
                transaction
            )
            when (type) {
                TransactionInfo.Type.ADD -> transaction.add(containerId, fragment, screen.screenKey)
                TransactionInfo.Type.REPLACE -> transaction.replace(
                    containerId,
                    fragment,
                    screen.screenKey
                )
            }
            if (addToBackStack) {
                val transactionInfo = TransactionInfo(screen.screenKey, type)
                transaction.addToBackStack(transactionInfo.toString())
                localStackCopy.add(transactionInfo)
            }
            transaction.commit()
        }

        private fun setupFragmentTransactionWithCommand(
            command: Command,
            fragmentTransaction: FragmentTransaction
        ) {
            when (command) {
                is Forward -> {
                    fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.slide_out,
                        R.anim.pop_in,
                        R.anim.pop_out
                    )
                }
                is Back -> {
                    fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.slide_out,
                        R.anim.pop_in,
                        R.anim.pop_out
                    )
                }
                is Replace -> {
                    fragmentTransaction.setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        router.replaceScreen(LoaderFragment.newInstance())
        viewBinding.bottomNavBar.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.currencyExchangeMenu -> {
                    router.newRootScreen(CurrencyExchangeMainFragment.newInstance())
                    true
                }
                R.id.itemsSearchMenu -> {
                    router.newRootScreen(
                        ItemsSearchMainFragment.newInstance(
                            itemSearchType,
                            itemSearchName,
                            withNotificationRequest
                        )
                    )
                    itemSearchType = null
                    itemSearchName = null
                    withNotificationRequest = false
                    true
                }
                R.id.chartsMenu -> {
                    router.newRootScreen(ChartsMainFragment.newInstance())
                    true
                }
                else -> {
                    false
                }
            }
        }
        viewBinding.bottomNavBar.setOnNavigationItemReselectedListener {
            return@setOnNavigationItemReselectedListener
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
        val intentFilter = IntentFilter().apply {
            addAction(MyFirebaseMessaging.currencyMessage)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
        if (currentFragment is ItemsSearchMainFragment && currentFragment.closeToolbarSearchLayoutIfNeeded()) {
            return
        }
        super.onBackPressed()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusedView = currentFocus
        if (focusedView is AutoCompleteTextView && ev?.action == MotionEvent.ACTION_UP) {
            val outRect = Rect()
            focusedView.getGlobalVisibleRect(outRect)
            if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                focusedView.clearFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun getLeagues(): List<String> {
        return viewModel.getLeagues()
    }

    override fun showBottomNavBarIfNeeded() {
        if (viewBinding.bottomNavBar.visibility == View.GONE) {
            viewBinding.bottomNavBar.visibility = View.VISIBLE
        }
    }

    override fun signOut() {
        Firebase.auth.signOut()
        router.newRootScreen(StartFragment.newInstance())
    }

    override fun goToCurrencyExchange(
        wantItemId: String?,
        haveItemId: String?,
        firstTime: Boolean,
        withNotificationRequest: Boolean
    ) {
        val savedExchangeCurrencyFragmentState =
            viewModel.currencyExchangeMainFragmentState ?: bundleOf()

        savedExchangeCurrencyFragmentState.putAll(
            bundleOf(
                CurrencyExchangeMainFragment.WANT_ITEM_ID_KEY to wantItemId,
                CurrencyExchangeMainFragment.HAVE_ITEM_ID_KEY to haveItemId,
                CurrencyExchangeMainFragment.WITH_NOTIFICATION_REQUEST_KEY to withNotificationRequest
            )
        )

        if (firstTime) {
            router.newRootScreen(CurrencyExchangeMainFragment.newInstance())
        } else {
            viewBinding.bottomNavBar.selectedItemId = R.id.currencyExchangeMenu
        }
    }

    override fun goToItemsSearch(
        itemType: String,
        itemName: String?,
        withNotificationRequest: Boolean
    ) {
        itemSearchType = itemType
        itemSearchName = itemName
        this.withNotificationRequest = withNotificationRequest
        viewBinding.bottomNavBar.selectedItemId = R.id.itemsSearchMenu
    }

    override fun checkApiConnection() {
        if (FirebaseAuth.getInstance().currentUser != null && !isApiConnectionChecked) {
            lifecycleScope.launch {
                val result = withContext(Dispatchers.IO) {
                    try {
                        return@withContext viewModel.addToken()
                    } catch (e: Exception) {
                        false
                    }
                }
                if (!result) {
                    AlertDialog.Builder(this@MainActivity, R.style.AppTheme_AlertDialog)
                        .setTitle("Error")
                        .setMessage("Cannot connect to server!\nNotification requests were not synced.")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                isApiConnectionChecked = true
            }
        }
    }

    override fun saveCurrencyExchangeFragmentState(state: Bundle) {
        viewModel.currencyExchangeMainFragmentState = state
    }

    override fun saveItemsSearchFragmentState(state: Bundle) {
        viewModel.itemsSearchMainFragmentState = state
    }

    override fun restoreCurrencyExchangeFragmentState(): Bundle? {
        return viewModel.currencyExchangeMainFragmentState
    }

    override fun restoreItemsSearchFragmentState(): Bundle? {
        return viewModel.itemsSearchMainFragmentState
    }
}
