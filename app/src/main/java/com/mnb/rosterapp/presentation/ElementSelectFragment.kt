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

class ElementSelectFragment : Fragment() {

    private val stateModel by activityViewModels<StateViewModel>()

    companion object {
        const val ORIGIN = "element_select_fragment"
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

            //if (it.originEvent.equals(Event.ELEMENT_SELECT_INIT)) {
                val layout = view.findViewById(R.id.element_list) as LinearLayout
                layout.removeAllViews()
                val currentUnit = it.currentUnit
                val unit = it.availableUnit
                val modelList = unit!!.models.values
                val weaponList = unit!!.weapons.values
                val ruleList = unit!!.rules.values
                if (modelList != null && modelList.isNotEmpty()) {
                    for (model in modelList) {
                        if (currentUnit!!.models.containsKey(model.name)) {
                            // skip existing elements
                            continue
                        }
                        val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                        binding.setSelectionPoints(model.points.toString())
                        binding.setSelectionPower(model.power.toString())
                        binding.setSelectionName(model.name)
                        val clickView = binding.selectionItem
                        clickView.setOnClickListener {
                            stateModel.handleEvent(Event.ElementSelectAddElement(model.name))
                            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
                            Navigation.findNavController(view).navigate(
                                R.id.action_elementSelectFragment_to_unitEditFragment,
                                argBundle
                            )
                        }
                        val itemView = binding.root
                        layout.addView(itemView)
                    }
                }
                if (weaponList != null && weaponList.isNotEmpty()) {
                    for (weapon in weaponList) {
                        if (currentUnit!!.weapons.containsKey(weapon.name)) {
                            // skip existing elements
                            continue
                        }
                        val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                        binding.setSelectionPoints(weapon.points.toString())
                        binding.setSelectionPower(weapon.power.toString())
                        binding.setSelectionName(weapon.name)
                        val clickView = binding.selectionItem
                        clickView.setOnClickListener {
                            stateModel.handleEvent(Event.ElementSelectAddElement(weapon.name))
                            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
                            Navigation.findNavController(view).navigate(
                                R.id.action_elementSelectFragment_to_unitEditFragment,
                                argBundle
                            )
                        }
                        val itemView = binding.root
                        layout.addView(itemView)
                    }
                }
                if (ruleList != null && ruleList.isNotEmpty()) {
                    for (rule in ruleList) {
                        if (currentUnit!!.rules.containsKey(rule.name)) {
                            // skip existing elements
                            continue
                        }
                        val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                        binding.setSelectionPoints(rule.points.toString())
                        binding.setSelectionPower(rule.power.toString())
                        binding.setSelectionName(rule.name)
                        val clickView = binding.selectionItem
                        clickView.setOnClickListener {
                            stateModel.handleEvent(Event.ElementSelectAddElement(rule.name))
                            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
                            Navigation.findNavController(view).navigate(
                                R.id.action_elementSelectFragment_to_unitEditFragment,
                                argBundle
                            )
                        }
                        val itemView = binding.root
                        layout.addView(itemView)
                    }
                }
            //}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_element_select, container, false)
    }

    override fun onResume() {
        super.onResume()

        System.out.println(ORIGIN + " onResume called, handle event")
        stateModel.handleEvent(Event.ElementSelectInit())
    }
}
