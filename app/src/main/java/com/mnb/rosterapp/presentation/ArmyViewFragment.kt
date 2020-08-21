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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stateModel.state.observe(this, Observer {
            val inflater = activity?.layoutInflater
            val view = view

            // bail out if inflater/view aren't available (ie: before onCreateView is called)
            if (inflater == null) {
                System.out.println(ORIGIN + " state observer called, but inflater is null")
                return@Observer
            }
            if (view == null) {
                System.out.println(ORIGIN + " state observer called, but view is null")
                return@Observer
            }

            System.out.println(ORIGIN + " state observer called, inflater/view ok")

            val nameView = view.findViewById(R.id.army_name) as TextView
            nameView.setText(it.currentArmy!!.name)
            val unitList = it.currentArmy!!.units.values
            val layout = view.findViewById(R.id.unit_list) as LinearLayout
            if (unitList != null) {
                layout.removeAllViews()
                for (unit in unitList.sortedDescending()) {
                    val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                    binding.setSelectionPoints(unit.getPoints().toString())
                    binding.setSelectionPower(unit.getPoints().toString())
                    binding.setSelectionName(unit.name)
                    val itemView = binding.root
                    itemView.setOnClickListener {
                        stateModel.handleEvent(Event.ArmyViewUnitSelect(unit.name))
                        val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN, Keywords.UNIT_NAME to unit.name)
                        Navigation.findNavController(view).navigate(
                            R.id.action_armyViewFragment_to_unitViewFragment,
                            argBundle
                        )
                    }
                    layout.addView(itemView)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_army_view, container, false)
    }

    override fun onResume() {
        super.onResume()

        System.out.println(ORIGIN + " onResume called, handle event")
        stateModel.handleEvent(Event.RefreshUi())
    }
}
