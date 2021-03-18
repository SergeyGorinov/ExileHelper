package com.poe.tradeapp.core.presentation

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.github.terrakok.cicerone.Router
import com.poe.tradeapp.core.DI
import org.koin.core.component.inject

interface IBaseFragment {
    var viewBinding: ViewBinding?
}

abstract class BaseFragment(resId: Int) : Fragment(resId), IBaseFragment {

    protected val router by DI.inject<Router>()

    override var viewBinding: ViewBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    inline fun <reified T : ViewBinding> getBinding() = viewBinding as T
}