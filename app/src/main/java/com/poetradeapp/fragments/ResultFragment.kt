package com.poetradeapp.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.poetradeapp.R
import com.poetradeapp.adapters.CurrencyListAdapter
import com.poetradeapp.adapters.CurrencyResultAdapter
import com.poetradeapp.adapters.CurrencyResultViewHolder
import com.poetradeapp.models.MainViewModel
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflanter = TransitionInflater.from(requireContext())
        exitTransition = inflanter.inflateTransition(R.transition.fragment_slide)
        enterTransition = inflanter.inflateTransition(R.transition.fragment_slide)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        results.apply {
            setItemViewCacheSize(20)
            layoutManager = LinearLayoutManager(activity)
            adapter = CurrencyResultAdapter(viewModel.getCurrencyResultsData()?.result ?: listOf())
        }
    }
}