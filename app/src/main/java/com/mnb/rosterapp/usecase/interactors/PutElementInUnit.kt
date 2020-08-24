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
            // check warlord traits for element name
            val traitsUnit = codex!!.units.get(Unit.TRAITS_KEY)
            if (traitsUnit!!.rules.containsKey(elementName)) {
                var rule = armyUnit!!.rules.get(elementName)
                if (rule == null) {
                    val traitRule = traitsUnit.rules.get(elementName)
                    // if there is no minimum number, we are at least adding one
                    var ruleCount = traitRule!!.required
                    if (ruleCount < 1) {
                        ruleCount = 1
                    }
                    val ruleCopy = Rule(traitRule)
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
            }
        }
        return armyRepository.saveArmy(army)
    }
}