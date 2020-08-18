package com.mnb.rosterapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.mnb.rosterapp.R
import com.mnb.rosterapp.databinding.*

class UnitEditFragment : Fragment() {

    private val stateModel by activityViewModels<StateViewModel>()

    companion object {
        const val ORIGIN = "unit_edit_fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_unit_edit, container, false)
        stateModel.state.observe(this, Observer {
            if (it.originEvent.equals(Event.UNIT_EDIT_INIT)
                || it.originEvent.equals(Event.UNIT_EDIT_ADD_ELEMENT)
                || it.originEvent.equals(Event.UNIT_EDIT_REMOVE_ELEMENT)
                || it.originEvent.equals(Event.REFRESH_UI)) {
                val nameView = view.findViewById(R.id.unit_name) as TextView
                nameView.setText(it.currentUnit!!.name)
                val addButton = view.findViewById(R.id.unit_button) as Button
                addButton.setOnClickListener {
                    val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN)
                    Navigation.findNavController(view).navigate(
                        R.id.action_unitEditFragment_to_unitCodexFragment,
                        argBundle
                    )
                }
                val modelLayout = view.findViewById(R.id.model_list) as LinearLayout
                modelLayout.removeAllViews()
                val modelList = it.currentUnit!!.models.values
                if (modelList != null && modelList.isNotEmpty()) {
                    // use item with default values as header row
                    var headerView = inflater.inflate(R.layout.item_model, container, false)
                    var editLeft = headerView.findViewById(R.id.edit_ui_left) as LinearLayout
                    editLeft.visibility = View.GONE
                    var editRight = headerView.findViewById(R.id.edit_ui_right) as LinearLayout
                    editRight.visibility = View.GONE
                    modelLayout.addView(headerView)
                    for (model in modelList) {
                        val binding = ItemModelBinding.inflate(inflater)
                        updateEditUi(binding.root, model.name, model.points, model.power, model.count, model.required, model.limit)
                        var nameString = model.name
                        if (model.count > 1) {
                            nameString = nameString + " x" + model.count
                        }
                        binding.modelPoints = (model.points * model.count).toString()
                        binding.modelPower = (model.power * model.count).toString()
                        binding.modelName = nameString
                        binding.modelMove = model.move
                        binding.modelWeapon = model.weapon
                        binding.modelBallistic = model.ballistic
                        binding.modelStrength = model.strength
                        binding.modelToughness = model.toughness
                        binding.modelWounds = model.wounds
                        binding.modelAttacks = model.attacks
                        binding.modelLeadership = model.leadership
                        binding.modelSave = model.save
                        modelLayout.addView(binding.root)
                    }
                } else {
                    modelLayout.visibility = View.INVISIBLE
                }
                val damageLayout = view.findViewById(R.id.damage_list) as LinearLayout
                damageLayout.removeAllViews()
                val damageList = it.currentUnit!!.damages.values
                if (damageList != null && damageList.isNotEmpty()) {
                    // use item with default values as header row
                    val headerView = inflater.inflate(R.layout.item_damage, container, false)
                    damageLayout.addView(headerView)
                    for (damage in damageList) {
                        val binding = ItemDamageBinding.inflate(inflater)
                        binding.damageRemaining = damage.remaining
                        binding.damageMove = damage.move
                        binding.damageWeapon = damage.weapon
                        binding.damageBallistic = damage.ballistic
                        binding.damageStrength = damage.strength
                        binding.damageToughness = damage.toughness
                        binding.damageWounds = damage.wounds
                        binding.damageAttacks = damage.attacks
                        binding.damageLeadership = damage.leadership
                        binding.damageSave = damage.save
                        damageLayout.addView(binding.root)
                    }
                } else {
                    damageLayout.visibility = View.INVISIBLE
                }
                val weaponLayout = view.findViewById(R.id.weapon_list) as LinearLayout
                weaponLayout.removeAllViews()
                val weaponList = it.currentUnit!!.weapons.values
                if (weaponList != null && weaponList.isNotEmpty()) {
                    // use item with default values as header row
                    val headerView = inflater.inflate(R.layout.item_weapon, container, false)
                    val editLeft = headerView.findViewById(R.id.edit_ui_left) as LinearLayout
                    editLeft.visibility = View.GONE
                    val editRight = headerView.findViewById(R.id.edit_ui_right) as LinearLayout
                    editRight.visibility = View.GONE
                    weaponLayout.addView(headerView)
                    for (weapon in weaponList) {
                        val binding = ItemWeaponBinding.inflate(inflater)
                        if (weapon.limit < 1) {
                            updateEditUi(binding.root, weapon.name, weapon.points, weapon.power, weapon.count, weapon.required, it.currentUnit!!.getModelCount())
                        } else {
                            updateEditUi(binding.root, weapon.name, weapon.points, weapon.power, weapon.count, weapon.required, weapon.limit)
                        }
                        // need to handle multiple weapon profiles
                        val names = weapon.name.split("|")
                        var nameString = names[0]
                        if (weapon.count > 1) {
                            nameString = nameString + " x" + weapon.count
                        }
                        val ranges = weapon.range.split("|")
                        val types = weapon.type.split("|")
                        val strengths = weapon.strength.split("|")
                        val penetrations = weapon.penetration.split("|")
                        val damages = weapon.damage.split("|")
                        val descriptions = weapon.description.split("|")
                        binding.weaponPoints = (weapon.points * weapon.count).toString()
                        binding.weaponPower = (weapon.power * weapon.count).toString()
                        binding.weaponName = nameString
                        binding.weaponRange = ranges[0]
                        binding.weaponType = types[0]
                        binding.weaponStrength = strengths[0]
                        binding.weaponPenetration = penetrations[0]
                        binding.weaponDamage = damages[0]
                        binding.weaponDescription = descriptions[0]
                        // handle first alternate profile
                        if (names.size > 1) {
                            binding.weaponAlt1.visibility = View.VISIBLE
                            var nameString = names[1]
                            if (weapon.count > 1) {
                                nameString = nameString + " x" + weapon.count
                            }
                            binding.weaponName2 = nameString
                            binding.weaponRange2 = ranges[1]
                            binding.weaponType2 = types[1]
                            binding.weaponStrength2 = strengths[1]
                            binding.weaponPenetration2 = penetrations[1]
                            binding.weaponDamage2 = damages[1]
                            binding.weaponDescription2 = descriptions[1]
                        }
                        // handle second alternate profile
                        if (names.size > 2) {
                            binding.weaponAlt2.visibility = View.VISIBLE
                            var nameString = names[2]
                            if (weapon.count > 1) {
                                nameString = nameString + " x" + weapon.count
                            }
                            binding.weaponName3 = nameString
                            binding.weaponRange3 = ranges[2]
                            binding.weaponType3 = types[2]
                            binding.weaponStrength3 = strengths[2]
                            binding.weaponPenetration3 = penetrations[2]
                            binding.weaponDamage3 = damages[2]
                            binding.weaponDescription3 = descriptions[2]
                        }
                        weaponLayout.addView(binding.root)
                    }
                } else {
                    weaponLayout.visibility = View.INVISIBLE
                }
                val ruleLayout = view.findViewById(R.id.rule_list) as LinearLayout
                ruleLayout.removeAllViews()
                val ruleList = it.currentUnit!!.rules.values
                if (ruleList != null && ruleList.isNotEmpty()) {
                    for (rule in ruleList) {
                        val binding = ItemRuleBinding.inflate(inflater)
                        if (rule.limit < 1) {
                            updateEditUi(binding.root, rule.name, rule.points, rule.power, rule.count, rule.required, it.currentUnit!!.getModelCount())
                        } else {
                            updateEditUi(binding.root, rule.name, rule.points, rule.power, rule.count, rule.required, rule.limit)
                        }
                        var nameString = rule.name
                        if (rule.count > 1) {
                            nameString = nameString + " x" + rule.count
                        }
                        binding.rulePoints = (rule.points * rule.count).toString()
                        binding.rulePower = (rule.power * rule.count).toString()
                        binding.ruleName = nameString
                        binding.ruleDescription = rule.description
                        ruleLayout.addView(binding.root)
                    }
                } else {
                    ruleLayout.visibility = View.INVISIBLE
                }
            }
        })
        val origin: String = arguments?.getString(Keywords.ORIGIN, Keywords.NO_ORIGIN) ?: Keywords.NO_ARGUMENTS
        if (origin.equals(ArmyEditFragment.ORIGIN)) {
            val unitName: String = arguments?.getString(Keywords.UNIT_NAME, Keywords.NO_UNIT) ?: Keywords.NO_ARGUMENTS
            stateModel.handleEvent(Event.UnitEditInit(unitName))
        } else if (origin.equals(ElementSelectFragment.ORIGIN)) {
            stateModel.handleEvent(Event.RefreshUi())
        }
        return view
    }

    private fun updateEditUi(view: View, elementName: String, points: Int, power: Int, count: Int, required: Int, limit: Int) {
        if (limit == 1) {
            // limit 1, can't add
            val addView = view.findViewById(R.id.edit_ui_add_disable) as ImageView
            addView.visibility = View.VISIBLE
            // limit 1, use cancel not remove
            val removeView = view.findViewById(R.id.edit_ui_remove_disable) as ImageView
            removeView.visibility = View.VISIBLE
            if (required == 1) {
                // 1 required, can't cancel/remove
                val cancelView = view.findViewById(R.id.edit_ui_cancel_disable) as ImageView
                cancelView.visibility = View.VISIBLE
            } else {
                // 0 required, can cancel/remove
                val cancelViewClick = view.findViewById(R.id.edit_ui_cancel) as ImageView
                cancelViewClick.setOnClickListener {
                    stateModel.handleEvent(Event.UnitEditRemoveElement(elementName))
                }
                cancelViewClick.visibility = View.VISIBLE
            }
        } else {
            // limit not 1, use add/remove not cancel
            val cancelView = view.findViewById(R.id.edit_ui_cancel_disable) as ImageView
            cancelView.visibility = View.VISIBLE
            if (count > required) {
                // above required, can remove
                val removeViewClick = view.findViewById(R.id.edit_ui_remove) as ImageView
                removeViewClick.setOnClickListener {
                    stateModel.handleEvent(Event.UnitEditRemoveElement(elementName))
                }
                removeViewClick.visibility = View.VISIBLE
            } else {
                // reached required, can't remove
                val removeView = view.findViewById(R.id.edit_ui_remove_disable) as ImageView
                removeView.visibility = View.VISIBLE
            }
            if (count < limit) {
                // below limit, can add
                val addViewClick = view.findViewById(R.id.edit_ui_add) as ImageView
                addViewClick.setOnClickListener {
                    stateModel.handleEvent(Event.UnitEditAddElement(elementName))
                }
                addViewClick.visibility = View.VISIBLE
            } else {
                // reached limit, can't add
                val addView = view.findViewById(R.id.edit_ui_add_disable) as ImageView
                addView.visibility = View.VISIBLE
            }
        }
    }
}