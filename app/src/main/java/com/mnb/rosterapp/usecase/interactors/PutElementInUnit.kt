package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.ArmyRepository
import com.mnb.rosterapp.data.CodexRepository
import com.mnb.rosterapp.domain.*
import com.mnb.rosterapp.domain.Unit

class PutElementInUnit (private val codexRepository: CodexRepository, private val armyRepository: ArmyRepository) {
    suspend operator fun invoke(codexName: String, codexUnitName: String, armyName: String, armyUnitName: String, elementName: String): Army {
        // add new element from codex or increment as needed
        val army = armyRepository.loadArmy(armyName)
        val armyUnit = army!!.units.get(armyUnitName)
        // names are selected from a list and should always be valid
        val codex = codexRepository.loadCodex(codexName)
        val codexUnit = codex!!.units.get(codexUnitName)
        if (codexUnit!!.models.containsKey(elementName)) {
            var model = armyUnit!!.models.get(elementName)
            if (model == null) {
                val codexModel = codexUnit!!.models.get(elementName)
                // if there is no minimum number, we are at least adding one
                var modelCount = codexModel!!.required
                if (modelCount < 1) {
                    modelCount = 1
                }
                val modelCopy = Model(codexModel)
                modelCopy.count = modelCount
                armyUnit.models.put(modelCopy.name, modelCopy)
            } else {
                if (model.count < model.limit) {
                    model.count++
                }
            }
        } else if (codexUnit!!.weapons.containsKey(elementName)) {
            var weapon = armyUnit!!.weapons.get(elementName)
            if (weapon == null) {
                val codexWeapon = codexUnit!!.weapons.get(elementName)
                // if there is no minimum number, we are at least adding one
                var weaponCount = codexWeapon!!.required
                if (weaponCount < 1) {
                    weaponCount = 1
                }
                val weaponCopy = Weapon(codexWeapon)
                weaponCopy.count = weaponCount
                armyUnit.weapons.put(weaponCopy.name, weaponCopy)
            } else {
                if (weapon.limit > 0) {
                    if (weapon.count < weapon.limit) {
                        weapon.count++
                    }
                } else {
                    if (weapon.count < armyUnit.getModelCount()) {
                        weapon.count++
                    }
                }
            }
        } else if (codexUnit!!.rules.containsKey(elementName)) {
            var rule = armyUnit!!.rules.get(elementName)
            if (rule == null) {
                val codexRule = codexUnit.rules.get(elementName)
                // if there is no minimum number, we are at least adding one
                var ruleCount = codexRule!!.required
                if (ruleCount < 1) {
                    ruleCount = 1
                }
                val ruleCopy = Rule(codexRule)
                ruleCopy.count = ruleCount
                armyUnit.rules.put(ruleCopy.name, ruleCopy)
            } else {
                if (rule.limit > 0) {
                    if (rule.count < rule.limit) {
                        rule.count++
                    }
                } else {
                    if (rule.count < armyUnit.getModelCount()) {
                        rule.count++
                    }
                }
            }
        } else {

            // TODO: streamline this part, ie: none of these have a limit > 1

            // TODO: need to fix keys vs. names here and elsewhere

            // check warlord traits for element name
            val traitsUnit = codex!!.units.get(Unit.TRAITS_KEY)
            if (traitsUnit != null && traitsUnit.rules.containsKey(elementName)) {
                var rule = armyUnit!!.rules.get(elementName)
                if (rule == null) {
                    // can only have 1 of any trait
                    val traitRule = traitsUnit.rules.get(elementName)!!
                    val traitCopy = Rule(traitRule)
                    traitCopy.count = 1
                    armyUnit.rules.put(elementName, traitCopy)
                } else {
                    // can only have 1 of any trait
                }
            }

            // check relics for element name
            val relicsUnit = codex!!.units.get(Unit.RELICS_KEY)
            if (relicsUnit != null && relicsUnit.weapons.containsKey(elementName)) {
                var weapon = armyUnit!!.weapons.get(elementName)
                if (weapon == null) {
                    // can only have 1 of any relic
                    val relicWeapon = relicsUnit.weapons.get(elementName)!!
                    val relicCopy = Weapon(relicWeapon)
                    relicCopy.count = 1
                    armyUnit.weapons.put(elementName, relicCopy)
                } else {
                    // can only have 1 of any relic
                }
            } else if (relicsUnit!!.rules.containsKey(elementName)) {
                var rule = armyUnit!!.rules.get(elementName)
                if (rule == null) {
                    // can only have 1 of any relic
                    val relicRule = relicsUnit.rules.get(elementName)!!
                    val relicCopy = Rule(relicRule)
                    relicCopy.count = 1
                    armyUnit.rules.put(elementName, relicCopy)
                } else {
                    // can only have 1 of any relic
                }
            }

            // check powers for element name
            val powersUnit = codex!!.units.get(Unit.POWERS_KEY)
            if (powersUnit != null && powersUnit.rules.containsKey(elementName)) {
                var rule = armyUnit!!.rules.get(elementName)
                if (rule == null) {
                    // can only have 1 of any trait
                    val powerRule = powersUnit.rules.get(elementName)!!
                    val powerCopy = Rule(powerRule)
                    powerCopy.count = 1
                    armyUnit.rules.put(elementName, powerCopy)
                } else {
                    // can only have 1 of any trait
                }
            }
        }
        return armyRepository.saveArmy(army)
    }
}