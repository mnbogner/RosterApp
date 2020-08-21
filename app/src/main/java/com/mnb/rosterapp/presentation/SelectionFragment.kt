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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stateModel.state.observe(this, Observer {
            // no-op?
            System.out.println(ORIGIN + " state observer called")
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_selection, container, false)
    }

    override fun onResume() {
        super.onResume()

        val view = view
        if (view == null) {
            return
        }

        view.findViewById<Button>(R.id.button_top_home)?.setOnClickListener {
            stateModel.handleEvent(Event.SelectionArmySelect())
            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
            Navigation.findNavController(view).navigate(
                R.id.action_selectionFragment_to_armySelectFragment,
                argBundle
            )
        }
        view.findViewById<Button>(R.id.button_bottom_home)?.setOnClickListener {
            stateModel.handleEvent(Event.SelectionCodexSelect())
            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
            Navigation.findNavController(view).navigate(
                R.id.action_selectionFragment_to_codexSelectFragment,
                argBundle
            )
        }
    }
}