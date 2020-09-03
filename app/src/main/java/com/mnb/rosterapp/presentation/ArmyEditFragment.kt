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

            // TODO: replace with data binding
            val nameView = view.findViewById(R.id.army_name) as TextView
            val addButton = view.findViewById(R.id.add_button) as Button
            if (it.currentArmy == null) {
                // no army to edit (codex probably invalid)
                nameView.setText("")
                addButton.visibility = View.GONE
                return@Observer
            } else {
                nameView.setText(it.currentArmy!!.name)
                addButton.setOnClickListener {
                    val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
                    Navigation.findNavController(view).navigate(
                        R.id.action_armyEditFragment_to_armyCodexFragment,
                        argBundle
                    )
                }
            }

            // build unit list
            val unitList = it.currentArmy!!.units.values
            val layout = view.findViewById(R.id.unit_list) as LinearLayout
            if (unitList != null) {
                layout.removeAllViews()
                for (unit in unitList.sortedDescending()) {
                    // skip abstract collection "units"
                    if (unit.type.equals(Unit.WEAPONS)
                        || unit.type.equals(Unit.ITEMS)
                        || unit.type.equals(Unit.TRAITS)
                        || unit.type.equals(Unit.RELICS)
                        || unit.type.equals(Unit.POWERS)) {
                        continue
                    }
                    val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                    binding.setSelectionPoints(unit.getPoints().toString())
                    binding.setSelectionPower(unit.getPoints().toString())
                    binding.setSelectionName(unit.name)
                    val clickView = binding.selectionItem
                    clickView.setOnClickListener {
                        stateModel.handleEvent(Event.ArmyEditSelectUnit(unit.name))
                        val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN, Keywords.UNIT_NAME to unit.name)
                        Navigation.findNavController(view).navigate(
                            R.id.action_armyEditFragment_to_unitEditFragment,
                            argBundle
                        )
                    }
                    val cancelView = binding.selectionCancel
                    System.out.println("RULES? " + unit.type + " / " + Unit.RULES)
                    if (!unit.type.equals(Unit.RULES)) {
                        cancelView.visibility = View.VISIBLE
                        cancelView.setOnClickListener {
                            stateModel.handleEvent(Event.ArmyEditRemoveUnit(unit.name))
                        }
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
        return inflater.inflate(R.layout.fragment_army_edit, container, false)
    }

    override fun onResume() {
        super.onResume()

        System.out.println(SelectionFragment.ORIGIN + " onResume called, handle event")
        stateModel.handleEvent(Event.RefreshUi())
    }
}