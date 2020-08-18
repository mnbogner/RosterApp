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
import com.mnb.rosterapp.databinding.ItemSelectionWithInfoBinding
import com.mnb.rosterapp.domain.Unit

class UnitSelectFragment : Fragment() {

    private val stateModel by activityViewModels<StateViewModel>()

    companion object {
        const val ORIGIN = "unit_select_fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_unit_select, container, false)
        stateModel.state.observe(this, Observer {
            if (it.originEvent.equals(Event.UNIT_SELECT_INIT)) {
                val codex = it.availableCodex
                val unitList = codex!!.units.values
                if (unitList != null) {
                    val layout = view.findViewById(R.id.unit_list) as LinearLayout
                    for (unit in unitList.sortedDescending()) {
                        if (unit.name.equals(Unit.RULES)) {
                            // abstract rules "unit" is added during army creation and can only be edited
                            continue;
                        }
                        val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                        binding.setSelectionPoints(unit.getPoints().toString())
                        binding.setSelectionPower(unit.getPower().toString())
                        binding.setSelectionName(unit.name)
                        val clickView = binding.selectionItem
                        clickView.setOnClickListener {
                            stateModel.handleEvent(Event.UnitSelectAddUnit(unit.name))
                            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
                            Navigation.findNavController(view).navigate(
                                R.id.action_unitSelectFragment_to_armyEditFragment,
                                argBundle
                            )
                        }
                        val itemView = binding.root
                        layout.addView(itemView)
                    }
                }
            }
        })
        stateModel.handleEvent(Event.UnitSelectInit())
        return view
    }
}