package com.poetradeapp.fragments.currency

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.poetradeapp.R
import com.poetradeapp.activities.CurrencyExchangeActivity
import com.poetradeapp.adapters.CurrencyResultAdapter
import com.poetradeapp.models.view.CurrencyExchangeViewModel
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CurrencyExchangeResultFragment : Fragment() {

    private lateinit var viewModel: CurrencyExchangeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflanter = TransitionInflater.from(requireContext())
        exitTransition = inflanter.inflateTransition(R.transition.fragment_slide)
        enterTransition = inflanter.inflateTransition(R.transition.fragment_slide)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(
            context as CurrencyExchangeActivity,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(CurrencyExchangeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        results.layoutManager = LinearLayoutManager(view.context)
        results.adapter = CurrencyResultAdapter(viewModel.getCurrencyResultData())
        results.setItemViewCacheSize(20)
        results.setHasFixedSize(true)

        results.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                (requireActivity() as CurrencyExchangeActivity).closeResultsLoader()
                results.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}