package com.poetradeapp.fragments.item

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.poetradeapp.R
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.adapters.ItemsResultAdapter
import com.poetradeapp.models.viewmodels.ItemsSearchViewModel
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ItemsSearchResultFragment : Fragment() {

    private lateinit var viewModel: ItemsSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflanter = TransitionInflater.from(requireContext())
        exitTransition = inflanter.inflateTransition(R.transition.fragment_slide)
        enterTransition = inflanter.inflateTransition(R.transition.fragment_slide)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(
            context as ItemsSearchActivity,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(ItemsSearchViewModel::class.java)
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
        results.adapter = ItemsResultAdapter(viewModel.getItemsResultData())
        results.setItemViewCacheSize(20)
        results.setHasFixedSize(true)

        results.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                (requireActivity() as ItemsSearchActivity).closeResultsLoader()
                results.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}