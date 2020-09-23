package com.poetradeapp.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.poetradeapp.R
import com.poetradeapp.adapters.SwipePagerAdapter
import com.poetradeapp.fragments.currency.CurrencyExchangeFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.math.abs
import kotlin.math.max

class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflanter = TransitionInflater.from(requireContext())
        exitTransition = inflanter.inflateTransition(R.transition.fragment_fade)
        enterTransition = inflanter.inflateTransition(R.transition.fragment_fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentContainer.setPageTransformer { page, position ->
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> {
                        alpha = 0f
                    }
                    position <= 1 -> {
                        val scaleFactor = max(0.5, 1 - abs(position).toDouble())
                        val verticalMargin = pageHeight * (1 - scaleFactor) / 2
                        val horizontalMargin = pageWidth * (1 - scaleFactor) / 2
                    }
                }
            }
        }
        val adapter = SwipePagerAdapter(requireActivity())
        adapter.addFragment(CurrencyExchangeFragment())
        adapter.addFragment(ItemExchangeFragment())
        fragmentContainer.adapter = adapter
        testButton.setOnClickListener {
            adapter.removeFragment(0)
        }
    }
}