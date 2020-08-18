package com.mnb.rosterapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.mnb.rosterapp.R
import com.mnb.rosterapp.databinding.ItemSelectionBinding

class CodexSelectFragment : Fragment() {

    private val stateModel by activityViewModels<StateViewModel>()

    companion object {
        const val ORIGIN = "codex_select_fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_codex_select, container, false)
        stateModel.state.observe(this, Observer {
            if (it.originEvent.equals(Event.CODEX_SELECT_INIT)) {
                // build codex list
                val codexList = it.codexList
                val codexLayout = view.findViewById(R.id.codex_list) as LinearLayout
                if (codexList != null) {
                    for (codexName in codexList) {
                        val binding = ItemSelectionBinding.inflate(inflater)
                        binding.setSelectionName(codexName)
                        val itemView = binding.root
                        itemView.setOnClickListener {
                            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN, Keywords.CODEX_NAME to codexName)
                            Navigation.findNavController(view).navigate(
                                R.id.action_codexSelectFragment_to_armyEditFragment,
                                argBundle
                            )
                        }
                        codexLayout.addView(itemView)
                    }
                }
                // build army list
                val armyList = it.armyList
                val armyLayout = view.findViewById(R.id.army_list) as LinearLayout
                if (armyList != null) {
                    for (armyName in armyList) {
                        val binding = ItemSelectionBinding.inflate(inflater)
                        binding.setSelectionName(armyName)
                        val clickView = binding.selectionItem
                        clickView.setOnClickListener {
                            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN, Keywords.ARMY_NAME to armyName)
                            Navigation.findNavController(view).navigate(
                                R.id.action_codexSelectFragment_to_armyEditFragment,
                                argBundle
                            )
                        }
                        val itemView = binding.root
                        armyLayout.addView(itemView)
                    }
                }
            }
        })
        stateModel.handleEvent(Event.CodexSelectInit())
        return view
    }
}
