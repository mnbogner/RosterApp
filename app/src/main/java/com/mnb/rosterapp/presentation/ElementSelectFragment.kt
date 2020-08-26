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
import com.mnb.rosterapp.databinding.ItemSelectionWithInfoBinding
import com.mnb.rosterapp.domain.Unit

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

            val layout = view.findViewById(R.id.element_list) as LinearLayout
            layout.removeAllViews()
            val currentUnit = it.currentUnit
            val unit = it.availableUnit
            val modelList = unit!!.models.values
            val weaponList = unit!!.weapons.values
            val ruleList = unit!!.rules.values
            if (modelList != null && modelList.isNotEmpty()) {

                var headerAdded = false

                for (model in modelList) {
                    if (currentUnit!!.models.containsKey(model.name)) {
                        // skip existing elements
                        continue
                    }

                    if (!headerAdded) {
                        // add dividers (but only if something else is added)
                        val divider = ItemSelectionBinding.inflate(inflater)
                        divider.setSelectionName("Models")
                        divider.selectionItem.background = null
                        layout.addView(divider.root)
                        headerAdded = true
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
                    layout.addView(binding.root)
                }
            }
            if (weaponList != null && weaponList.isNotEmpty()) {

                var headerAdded = false

                for (weapon in weaponList) {
                    if (currentUnit!!.weapons.containsKey(weapon.name)) {
                        // skip existing elements
                        continue
                    }

                    if (!headerAdded) {
                        // add dividers (but only if something else is added)
                        val divider = ItemSelectionBinding.inflate(inflater)
                        divider.setSelectionName("Weapons")
                        divider.selectionItem.background = null
                        layout.addView(divider.root)
                        headerAdded = true
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

                var headerAdded = false

                for (rule in ruleList) {
                    if (currentUnit!!.rules.containsKey(rule.name)) {
                        // skip existing elements
                        continue
                    }

                    if (!headerAdded) {
                        // add dividers (but only if something else is added)
                        val divider = ItemSelectionBinding.inflate(inflater)
                        divider.setSelectionName("Options")
                        divider.selectionItem.background = null
                        layout.addView(divider.root)
                        headerAdded = true
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
            // if warlord, show traits
            if (currentUnit!!.warlord) {
                val traitUnit = it.availableCodex!!.units.get(Unit.TRAITS_KEY)
                if (traitUnit != null && traitUnit.rules != null && traitUnit.rules.isNotEmpty()) {

                    var headerAdded = false

                    for (ruleKey in traitUnit.rules.keys) {
                        if (currentUnit!!.rules.containsKey(ruleKey)) {
                            // skip existing elements
                            continue
                        }

                        if (!headerAdded) {
                            // add dividers (but only if something else is added)
                            val divider = ItemSelectionBinding.inflate(inflater)
                            divider.setSelectionName("Traits")
                            divider.selectionItem.background = null
                            layout.addView(divider.root)
                            headerAdded = true
                        }

                        val rule = traitUnit.rules.get(ruleKey)!!

                        val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                        binding.setSelectionPoints(rule.points.toString())
                        binding.setSelectionPower(rule.power.toString())
                        binding.setSelectionName(ruleKey)
                        val clickView = binding.selectionItem
                        clickView.setOnClickListener {
                            stateModel.handleEvent(Event.ElementSelectAddElement(ruleKey))
                            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
                            Navigation.findNavController(view).navigate(
                                R.id.action_elementSelectFragment_to_unitEditFragment,
                                argBundle
                            )
                        }
                        layout.addView(binding.root)
                    }
                }
            }
            // if hq/elite, show relics
            if (currentUnit!!.type.equals(Unit.HQ) || currentUnit!!.type.equals(Unit.ELITE)) {
                val relicUnit = it.availableCodex!!.units.get(Unit.RELICS_KEY)
                if (relicUnit != null) {

                    var headerAdded = false

                    if (relicUnit.weapons != null && relicUnit.weapons.isNotEmpty()) {

                        // TODO: need to fix keys vs. names here and elsewhere

                        for (weaponKey in relicUnit.weapons.keys) {
                            if (currentUnit!!.weapons.containsKey(weaponKey)) {
                                // skip existing elements
                                continue
                            }

                            if (!headerAdded) {
                                // add dividers (but only if something else is added)
                                val divider = ItemSelectionBinding.inflate(inflater)
                                divider.setSelectionName("Relics")
                                divider.selectionItem.background = null
                                layout.addView(divider.root)
                                headerAdded = true
                            }

                            val weapon = relicUnit.weapons.get(weaponKey)!!

                            val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                            binding.setSelectionPoints(weapon.points.toString())
                            binding.setSelectionPower(weapon.power.toString())
                            binding.setSelectionName(weaponKey)
                            val clickView = binding.selectionItem
                            clickView.setOnClickListener {
                                stateModel.handleEvent(Event.ElementSelectAddElement(weaponKey))
                                val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
                                Navigation.findNavController(view).navigate(
                                    R.id.action_elementSelectFragment_to_unitEditFragment,
                                    argBundle
                                )
                            }
                            layout.addView(binding.root)
                        }
                    }
                    if (relicUnit.rules != null && relicUnit.rules.isNotEmpty()) {
                        for (ruleKey in relicUnit.rules.keys) {
                            if (currentUnit!!.rules.containsKey(ruleKey)) {
                                // skip existing elements
                                continue
                            }

                            if (!headerAdded) {
                                // add dividers (but only if something else is added)
                                val divider = ItemSelectionBinding.inflate(inflater)
                                divider.setSelectionName("Relics")
                                divider.selectionItem.background = null
                                layout.addView(divider.root)
                                headerAdded = true
                            }

                            val rule = relicUnit.rules.get(ruleKey)!!

                            val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                            binding.setSelectionPoints(rule.points.toString())
                            binding.setSelectionPower(rule.power.toString())
                            binding.setSelectionName(ruleKey)
                            val clickView = binding.selectionItem
                            clickView.setOnClickListener {
                                stateModel.handleEvent(Event.ElementSelectAddElement(ruleKey))
                                val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
                                Navigation.findNavController(view).navigate(
                                    R.id.action_elementSelectFragment_to_unitEditFragment,
                                    argBundle
                                )
                            }
                            layout.addView(binding.root)
                        }
                    }
                }
            }
            // if psyker, show powers
            // TODO: move show/count logic into interactors or units or "business rules" class
            if (currentUnit!!.psyker > 0 && currentUnit.getPsykerCount() < currentUnit.psyker) {
                val powerUnit = it.availableCodex!!.units.get(Unit.POWERS_KEY)
                if (powerUnit != null && powerUnit.rules != null && powerUnit.rules.isNotEmpty()) {

                    var headerAdded = false

                    for (ruleKey in powerUnit.rules.keys) {
                        if (currentUnit!!.rules.containsKey(ruleKey)) {
                            // skip existing elements
                            continue
                        }

                        if (!headerAdded) {
                            // add dividers (but only if something else is added)
                            val divider = ItemSelectionBinding.inflate(inflater)
                            divider.setSelectionName("Powers")
                            divider.selectionItem.background = null
                            layout.addView(divider.root)
                            headerAdded = true
                        }

                        val rule = powerUnit.rules.get(ruleKey)!!

                        val binding = ItemSelectionWithInfoBinding.inflate(inflater)
                        binding.setSelectionPoints(rule.points.toString())
                        binding.setSelectionPower(rule.power.toString())
                        binding.setSelectionName(ruleKey)
                        val clickView = binding.selectionItem
                        clickView.setOnClickListener {
                            stateModel.handleEvent(Event.ElementSelectAddElement(ruleKey))
                            val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
                            Navigation.findNavController(view).navigate(
                                R.id.action_elementSelectFragment_to_unitEditFragment,
                                argBundle
                            )
                        }
                        layout.addView(binding.root)
                    }
                }
            }
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
        stateModel.handleEvent(Event.RefreshUi())
    }
}
