package com.sgorinov.exilehelper.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
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
import com.sgorinov.exilehelper.MyFirebaseMessaging
import com.sgorinov.exilehelper.R
import com.sgorinov.exilehelper.charts_feature.presentation.fragments.ChartsMainFragment
import com.sgorinov.exilehelper.core.DI
import com.sgorinov.exilehelper.core.presentation.IMainActivity
import com.sgorinov.exilehelper.currency.data.models.CurrencyRequest
import com.sgorinov.exilehelper.currency.presentation.fragments.CurrencyExchangeMainFragment
import com.sgorinov.exilehelper.databinding.ActivityMainBinding
import com.sgorinov.exilehelper.exchange.data.models.ItemsRequestModel
import com.sgorinov.exilehelper.exchange.presentation.fragments.ItemsSearchMainFragment
import com.sgorinov.exilehelper.presentation.fragments.LoaderFragment
import com.sgorinov.exilehelper.presentation.fragments.StartFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.inject

class MainActivity : FragmentActivity(), IMainActivity {

    private val viewModel by viewModel<MainActivityViewModel>()

    private val navigatorHolder by DI.inject<NavigatorHolder>()
    private val router by DI.inject<Router>()
    private val notificationUtil by DI.inject<NotificationUtil>()

    private lateinit var viewBinding: ActivityMainBinding

    private var isApiConnectionChecked = false

    private val receiver = NotificationBroadcastReceiver()

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
        notificationUtil.apply {
            this.onGetNotification = { title, body, payload ->
                generateNotification(title, body, payload)
            }
            this.onGetPayload = { type, payload ->
                processNotificationPayload(type, payload, false)
            }
        }
        viewBinding.bottomNavBar.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.currencyExchangeMenu -> {
                    router.newRootScreen(CurrencyExchangeMainFragment.newInstance())
                    true
                }
                R.id.itemsSearchMenu -> {
                    router.newRootScreen(ItemsSearchMainFragment.newInstance())
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
        if (intent.hasExtra("data") && intent.hasExtra("type")) {
            val type = intent.getStringExtra("type")
            val payload = intent.getStringExtra("data")
            processNotificationPayload(type, payload, true)
        } else {
            router.replaceScreen(LoaderFragment.newInstance())
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
        val intentFilter = IntentFilter().apply {
            addAction(MyFirebaseMessaging.FIREBASE_NOTIFICATION)
            addAction(MyFirebaseMessaging.APP_NOTIFICATION)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
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

        if (viewBinding.bottomNavBar.selectedItemId == R.id.currencyExchangeMenu) {
            router.newRootScreen(CurrencyExchangeMainFragment.newInstance())
            return
        }

        viewBinding.bottomNavBar.selectedItemId = R.id.currencyExchangeMenu
    }

    override fun goToItemsSearch(
        itemType: String,
        itemName: String?,
        withExchangeRequest: Boolean,
        withNotificationRequest: Boolean
    ) {
        val savedItemsSearchFragmentState = viewModel.itemsSearchMainFragmentState ?: bundleOf()

        savedItemsSearchFragmentState.putAll(
            bundleOf(
                ItemsSearchMainFragment.SAVED_ITEM_TYPE to itemType,
                ItemsSearchMainFragment.SAVED_ITEM_NAME to itemName,
                ItemsSearchMainFragment.WITH_EXCHANGE_REQUEST_KEY to withExchangeRequest,
                ItemsSearchMainFragment.WITH_NOTIFICATION_REQUEST_KEY to withNotificationRequest
            )
        )

        if (viewBinding.bottomNavBar.selectedItemId == R.id.itemsSearchMenu) {
            router.newRootScreen(ItemsSearchMainFragment.newInstance())
            return
        }

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

    private fun generateNotification(
        title: String,
        message: String,
        data: Pair<String?, String?>
    ) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, NotificationBroadcastReceiver::class.java).apply {
            action = MyFirebaseMessaging.APP_NOTIFICATION
            putExtra(MyFirebaseMessaging.NOTIFICATION_TYPE, data.first)
            putExtra(MyFirebaseMessaging.NOTIFICATION_PAYLOAD, data.second)
        }
        val notificationBuilder = NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nc = NotificationChannel(
                DEFAULT_CHANNEL_ID,
                "Price notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(nc)
        }
        notificationBuilder.setSmallIcon(R.mipmap.launcher_icon)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setContentText(message)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setContentIntent(
            PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
        notificationManager.notify("Exile Helper".hashCode(), notificationBuilder.build())
    }

    private fun processNotificationPayload(type: String?, payload: String?, firstTime: Boolean) {
        if (type == null || payload == null) {
            return
        }
        when (type) {
            CurrencyExchangeMainFragment.NOTIFICATION_REQUESTS_TYPE -> {
                val data = Json.decodeFromString<CurrencyRequest>(payload)
                goToCurrencyExchange(
                    data.exchange.want.firstOrNull(),
                    data.exchange.have.firstOrNull(),
                    firstTime
                )
            }
            ItemsSearchMainFragment.NOTIFICATION_REQUESTS_TYPE -> {
                val data = Json {
                    isLenient = true
                }.decodeFromString(ItemsRequestModel.serializer(), payload)
                goToItemsSearch(data.query.type ?: "", data.query.name)
            }
        }
    }

    class NotificationUtil {
        var onGetNotification: ((String, String, Pair<String?, String?>) -> Unit)? = null
        var onGetPayload: ((String, String) -> Unit)? = null
    }

    class NotificationBroadcastReceiver : BroadcastReceiver() {

        private val notificationUtil by DI.inject<NotificationUtil>()

        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                MyFirebaseMessaging.FIREBASE_NOTIFICATION -> {
                    val title =
                        intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_MESSAGE_TITLE)
                    val body = intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_MESSAGE_BODY)
                    val payload = intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_PAYLOAD)
                    val type = intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_TYPE)
                    if (title != null && body != null) {
                        notificationUtil.onGetNotification?.invoke(title, body, type to payload)
                    }
                }
                MyFirebaseMessaging.APP_NOTIFICATION -> {
                    val payload = intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_PAYLOAD)
                    val type = intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_TYPE)
                    if (payload != null && type != null) {
                        notificationUtil.onGetPayload?.invoke(type, payload)
                    }
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_CHANNEL_ID = "default_channel_id"
    }
}
