package com.mnb.rosterapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.mnb.rosterapp.R
import com.mnb.rosterapp.databinding.ItemSelectionWithInfoBinding
import com.mnb.rosterapp.domain.Unit

class ArmyEditFragment : Fragment() {

    private val stateModel by activityViewModels<StateViewModel>()

    companion object {
        const val ORIGIN = "army_edit_fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_army_edit, container, false)
        stateModel.state.observe(this, Observer {
            if (it.originEvent.equals(Event.ARMY_EDIT_INIT)
                || it.originEvent.equals(Event.ARMY_EDIT_REMOVE_UNIT)
                || it.originEvent.equals(Event.REFRESH_UI)) {
                val nameView = view.findViewById(R.id.army_name) as TextView
                nameView.setText(it.currentArmy!!.name)
                val addButton = view.findViewById(R.id.add_button) as Button
                addButton.setOnClickListener {
                    val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
                    Navigation.findNavController(view).navigate(
                        R.id.action_armyEditFragment_to_armyCodexFragment,
                        argBundle
                    )
                }
                val unitList = it.currentArmy!!.units.values
                if (unitList != null) {
                    val layout = view.findViewById(R.id.unit_list) as LinearLayout
                    layout.removeAllViews()
                    for (unit in unitList.sortedDescending()) {
                        val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                        binding.setSelectionPoints(unit.getPoints().toString())
                        binding.setSelectionPower(unit.getPoints().toString())
                        binding.setSelectionName(unit.name)
                        val clickView = binding.selectionItem
                        clickView.setOnClickListener {
                            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN, Keywords.UNIT_NAME to unit.name)
                            Navigation.findNavController(view).navigate(
                                R.id.action_armyEditFragment_to_unitEditFragment,
                                argBundle
                            )
                        }
                        val cancelView = binding.selectionCancel
                        if (!unit.name.equals(Unit.RULES)) {
                            cancelView.visibility = View.VISIBLE
                            cancelView.setOnClickListener{
                                stateModel.handleEvent(Event.ArmyEditRemoveUnit(unit.name))
                            }
                        }
                        val itemView = binding.root
                        layout.addView(itemView)
                    }
                }
            }
        })
        val origin: String = arguments?.getString(Keywords.ORIGIN, Keywords.NO_ORIGIN) ?: Keywords.NO_ARGUMENTS
        if (origin.equals(CodexSelectFragment.ORIGIN)) {
            val codexName: String = arguments?.getString(Keywords.CODEX_NAME, Keywords.NO_CODEX) ?: Keywords.NO_ARGUMENTS
            if (codexName.equals(Keywords.NO_CODEX)) {
                val armyName: String = arguments?.getString(Keywords.ARMY_NAME, Keywords.NO_ARMY) ?: Keywords.NO_ARGUMENTS
                val codexPart = armyName.split("_").get(0)
                stateModel.handleEvent(Event.ArmyEditInit(codexPart, armyName))
            } else {
                stateModel.handleEvent(Event.ArmyEditInit(codexName, null))
            }
        } else if (origin.equals(UnitSelectFragment.ORIGIN) || origin.equals(UnitEditFragment.ORIGIN)) {
            stateModel.handleEvent(Event.RefreshUi())
        }
        return view
    }
}
