package com.poetradeapp.fragments.item

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poetradeapp.R
import com.poetradeapp.adapters.ItemsResultAdapter
import com.poetradeapp.models.view.ItemsSearchViewModel
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class ItemsSearchResultFragment : Fragment() {

    internal val viewModel: ItemsSearchViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fragment_slide)
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        val adapter = ItemsResultAdapter(viewModel.fetchedItems)

        adapter.setHasStableIds(true)

        results.layoutManager = layoutManager
        results.adapter = adapter
        results.setHasFixedSize(true)
        results.setItemViewCacheSize(100)
        results.scrollToPosition(0)

        results.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var isLoading = false

            private var totalCount = viewModel.itemsResultData?.result?.size ?: 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalCurrentItems = layoutManager.itemCount
                if (totalCurrentItems == totalCount)
                    return
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && (totalCurrentItems - 1) == lastVisiblePosition) {
                    isLoading = true
                    recyclerView.post {
                        adapter.addLoader()
                    }
                    GlobalScope.launch(Dispatchers.Default) {
                        viewModel.fetchPartialResults(totalCurrentItems)
                    }.invokeOnCompletion {
                        GlobalScope.launch(Dispatchers.Main) {
                            adapter.addFetchedItems(viewModel.fetchedItems)
                            isLoading = false
                        }
                    }
                }
            }
        })
    }
}