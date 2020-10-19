package com.poetradeapp.fragments

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.poetradeapp.PoeTradeApplication
import com.poetradeapp.R
import com.poetradeapp.activities.CurrencyExchangeActivity
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.helpers.StaticDataLoader
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var staticDataInstance: StaticDataLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as PoeTradeApplication).getDaggerComponent().inject(this)

        val transitionInflanter = TransitionInflater.from(context)
        enterTransition = transitionInflanter.inflateTransition(R.transition.fragment_fade)
        exitTransition = transitionInflanter.inflateTransition(R.transition.fragment_fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goToItemChange.setOnClickListener {
            startActivity(Intent(context, ItemsSearchActivity::class.java))
        }

        goToCurrencyChange.setOnClickListener {
            startActivity(Intent(context, CurrencyExchangeActivity::class.java))
        }
    }
}