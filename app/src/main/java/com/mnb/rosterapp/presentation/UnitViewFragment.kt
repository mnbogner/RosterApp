package com.mnb.rosterapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mnb.rosterapp.R
import com.mnb.rosterapp.databinding.ItemDamageBinding
import com.mnb.rosterapp.databinding.ItemModelBinding
import com.mnb.rosterapp.databinding.ItemRuleBinding
import com.mnb.rosterapp.databinding.ItemWeaponBinding

class UnitViewFragment : Fragment() {

    private val stateModel by activityViewModels<StateViewModel>()

    companion object {
        const val ORIGIN = "unit_view_fragment"
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

            val nameView = view.findViewById(R.id.unit_name) as TextView
            nameView.setText(it.currentUnit!!.name)
            val modelList = it.currentUnit!!.models.values
            val modelLayout = view.findViewById(R.id.model_list) as LinearLayout
            if (modelList != null && modelList.isNotEmpty()) {
                modelLayout.removeAllViews()
                // use item with default values as header row
                val header = ItemModelBinding.inflate(inflater)
                // header.root.setBackgroundResource(R.color.headerAccent)
                header.editUiLeft.visibility = View.GONE
                header.editUiRight.visibility = View.GONE

                modelLayout.addView(header.root)
                for (model in modelList) {
                    val binding = ItemModelBinding.inflate(inflater)
                    var nameString = model.name
                    if (model.count > 1) {
                        nameString = nameString + " x" + model.count
                    }
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
            val damageList = it.currentUnit!!.damages.values
            val damageLayout = view.findViewById(R.id.damage_list) as LinearLayout
            if (damageList != null && damageList.isNotEmpty()) {
                damageLayout.removeAllViews()
                // use item with default values as header row
                val header = ItemDamageBinding.inflate(inflater)
                // header.root.setBackgroundResource(R.color.headerAccent)
                damageLayout.addView(header.root)

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
            val weaponList = it.currentUnit!!.weapons.values
            val weaponLayout = view.findViewById(R.id.weapon_list) as LinearLayout
            if (weaponList != null && weaponList.isNotEmpty()) {
                weaponLayout.removeAllViews()
                // use item with default values as header row
                val header = ItemWeaponBinding.inflate(inflater)
                // header.root.setBackgroundResource(R.color.headerAccent)
                header.editUiLeft.visibility = View.GONE
                header.editUiRight.visibility = View.GONE
                weaponLayout.addView(header.root)

                for (weapon in weaponList) {
                    val binding = ItemWeaponBinding.inflate(inflater)
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
            val ruleList = it.currentUnit!!.rules.values
            val ruleLayout = view.findViewById(R.id.rule_list) as LinearLayout
            if (ruleList != null && ruleList.isNotEmpty()) {
                ruleLayout.removeAllViews()
                for (rule in ruleList) {
                    val binding = ItemRuleBinding.inflate(inflater)
                    var nameString = rule.name
                    if (rule.count > 1) {
                        nameString = nameString + " x" + rule.count
                    }
                    binding.rulePoints = rule.points.toString()
                    binding.rulePower = rule.power.toString()
                    binding.ruleName = nameString
                    binding.ruleDescription = rule.description
                    ruleLayout.addView(binding.root)
                }
            } else {
                ruleLayout.visibility = View.INVISIBLE
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_unit_view, container, false)
    }

    override fun onResume() {
        super.onResume()

        System.out.println(ORIGIN + " onResume called, handle event")
        stateModel.handleEvent(Event.RefreshUi())
    }
}
