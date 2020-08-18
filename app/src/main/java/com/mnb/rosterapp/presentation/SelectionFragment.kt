package com.mnb.rosterapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.mnb.rosterapp.R

class SelectionFragment : Fragment() {

    private val stateModel by activityViewModels<StateViewModel>()

    companion object {
        const val ORIGIN = "selection_fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stateModel.state.observe(this, Observer {
            // no-op?
        })
        val view = inflater.inflate(R.layout.fragment_selection, container, false)
        if (view != null) {
            init(view)
        }
        return view
    }

    private fun init(view: View) {
        view.findViewById<Button>(R.id.button_top_home)?.setOnClickListener {
            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
            Navigation.findNavController(view).navigate(
                R.id.action_selectionFragment_to_armySelectFragment,
                argBundle
            )
        }
        view.findViewById<Button>(R.id.button_bottom_home)?.setOnClickListener {
            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
            Navigation.findNavController(view).navigate(
                R.id.action_selectionFragment_to_codexSelectFragment,
                argBundle
            )
        }
    }
}
