package com.mnb.rosterapp.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.mnb.rosterapp.R
import com.mnb.rosterapp.databinding.ItemSelectionWithInfoBinding

class ArmyViewFragment : Fragment() {

    private val stateModel by activityViewModels<StateViewModel>()

    companion object {
        const val ORIGIN = "army_view_fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_army_view, container, false)
        stateModel.state.observe(this, Observer {
            if (it.originEvent.equals(Event.ARMY_VIEW_INIT)) {
                val nameView = view.findViewById(R.id.army_name) as TextView
                nameView.setText(it.currentArmy!!.name)
                val unitList = it.currentArmy!!.units.values
                if (unitList != null) {
                    val layout = view.findViewById(R.id.unit_list) as LinearLayout
                    for (unit in unitList.sortedDescending()) {
                        val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                        binding.setSelectionPoints(unit.getPoints().toString())
                        binding.setSelectionPower(unit.getPoints().toString())
                        binding.setSelectionName(unit.name)
                        val clickView = binding.selectionItem
                        clickView.setOnClickListener {
                            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN, Keywords.UNIT_NAME to unit.name)
                            Navigation.findNavController(view).navigate(
                                R.id.action_armyViewFragment_to_unitViewFragment,
                                argBundle
                            )
                        }
                        val itemView = binding.root
                        layout.addView(itemView)
                    }
                }
            }
        })
        val armyName: String = arguments?.getString(Keywords.ARMY_NAME, Keywords.NO_ARMY) ?: Keywords.NO_ARGUMENTS
        stateModel.handleEvent(Event.ArmyViewInit(armyName))
        return view
    }
}
