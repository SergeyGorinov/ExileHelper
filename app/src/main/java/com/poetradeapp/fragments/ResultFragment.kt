package com.poetradeapp.fragments

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.poetradeapp.R
import com.poetradeapp.MainActivity
import com.poetradeapp.adapters.CurrencyListAdapter
import com.poetradeapp.adapters.CurrencyResultAdapter
import com.poetradeapp.adapters.CurrencyResultViewHolder
import com.poetradeapp.models.ExchangeCurrencyResponseModel
import com.poetradeapp.models.MainViewModel
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflanter = TransitionInflater.from(requireContext())
        exitTransition = inflanter.inflateTransition(R.transition.fragment_slide)
        enterTransition = inflanter.inflateTransition(R.transition.fragment_slide)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(
            context as MainActivity,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        results.setItemViewCacheSize(20)
        results.layoutManager = LinearLayoutManager(view.context)
        results.adapter = CurrencyResultAdapter(viewModel.getCurrencyResultsData()?.result ?: listOf())
    }
}