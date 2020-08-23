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

            val unitList = it.availableCodex!!.units.values
            val layout = view.findViewById(R.id.unit_list) as LinearLayout
            if (unitList != null) {
                layout.removeAllViews()
                for (unit in unitList.sortedDescending()) {
                    if (unit.type.equals(Unit.RULES)
                        || unit.type.equals(Unit.WEAPONS)
                        || unit.type.equals(Unit.ITEMS)) {
                        // abstract collection "units" can't be added
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
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_unit_select, container, false)
    }

    override fun onResume() {
        super.onResume()

        System.out.println(ORIGIN + " onResume called, handle event")
        stateModel.handleEvent(Event.RefreshUi())
    }
}
