package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.ArmyRepository
import com.mnb.rosterapp.data.CodexRepository
import com.mnb.rosterapp.domain.*
import com.mnb.rosterapp.domain.Unit

class PutUnitInArmy (private val codexRepository: CodexRepository, private val armyRepository: ArmyRepository) {
    suspend operator fun invoke(codexName: String, armyName: String, unitName: String): Army {
        // build new unit with required contents
        val army = armyRepository.loadArmy(armyName)
        var counter = 1
        // update key to ensure uniqueness
        for (key in army!!.units.keys) {
            if (key.startsWith(unitName)) {
                if (key.endsWith("#" + counter)) {
                    counter++
                } else {
                    // fill in gaps in existing keys
                    break
                }
            }
        }
        // names are selected from a list and should always be valid
        val codex = codexRepository.loadCodex(codexName)
        val codexUnit = codex!!.units.get(unitName)
        val newUnit = Unit(unitName + "#" + counter, codexUnit!!.type, HashMap<String, Model>(), codexUnit!!.damages, HashMap<String, Weapon>(), HashMap<String, Rule>())
        for (model in codexUnit.models.values) {
            if (model.required > 0) {
                val modelCopy = Model(model)
                modelCopy.count = model.required
                newUnit.models.put(modelCopy.name, modelCopy)
            }
        }
        for (weapon in codexUnit.weapons.values) {
            if (weapon.required > 0) {
                val weaponCopy = Weapon(weapon)
                weaponCopy.count = weapon.required
                newUnit.weapons.put(weaponCopy.name, weaponCopy)
            }
        }
        for (rule in codexUnit.rules.values) {
            if (rule.required > 0) {
                val ruleCopy = Rule(rule)
                ruleCopy.count = rule.required
                newUnit.rules.put(ruleCopy.name, ruleCopy)
            }
        }
        army!!.units.put(newUnit.name, newUnit)
        return armyRepository.saveArmy(army)
    }
}