package com.poetradeapp.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.poetradeapp.R
import com.google.android.material.button.MaterialButton
import com.poetradeapp.models.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflanter = TransitionInflater.from(requireContext())
        exitTransition = inflanter.inflateTransition(R.transition.fragment_fade_in)
        enterTransition = inflanter.inflateTransition(R.transition.fragment_fade_in)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.findViewById<MaterialButton>(R.id.testButton).setOnClickListener {
            GlobalScope.launch {
                activity?.let {
                    ViewModelProvider(
                        it,
                        ViewModelProvider.AndroidViewModelFactory(it.application)
                    ).get(MainViewModel::class.java).channel.send(1)
                }
            }
        }
        return view
    }
}