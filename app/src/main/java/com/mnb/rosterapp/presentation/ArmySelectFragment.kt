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

class ArmySelectFragment : Fragment() {

    private val stateModel by activityViewModels<StateViewModel>()

    companion object {
        const val ORIGIN = "army_select_fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_army_select, container, false)
        stateModel.state.observe(this, Observer {
            if (it.originEvent.equals(Event.ARMY_SELECT_INIT)) {
                // build army list
                val armyList = it.armyList
                if (armyList != null) {
                    val layout = view.findViewById(R.id.army_list) as LinearLayout
                    for (armyName in armyList) {
                        val binding = ItemSelectionBinding.inflate(inflater)
                        binding.setSelectionName(armyName)
                        val clickView = binding.selectionItem
                        clickView.setOnClickListener {
                            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN, Keywords.ARMY_NAME to armyName)
                            Navigation.findNavController(view).navigate(
                                R.id.action_armySelectFragment_to_armyViewFragment,
                                argBundle
                            )
                        }
                        val itemView = binding.root
                        layout.addView(itemView)
                    }
                }
            }
        })
        stateModel.handleEvent(Event.ArmySelectInit())
        return view
    }
}
