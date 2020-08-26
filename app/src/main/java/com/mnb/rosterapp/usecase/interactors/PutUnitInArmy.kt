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
        val codexUnit = codex!!.units.get(unitName)!!

        // TODO: move this logic into codex class?
        // fetch weapons from common table
        val weaponUnit = codex!!.units.get(Unit.WEAPONS_KEY)!!
        if (codexUnit.weaponKeys != null && codexUnit.weaponKeys.isNotEmpty()) {
            for (weaponKey in codexUnit.weaponKeys) {
                val weapon = weaponUnit.weapons.get(weaponKey)
                if (weapon != null) {
                    codexUnit.weapons.put(weaponKey, weapon)
                }
            }
        }
        // fetch items from common table
        val itemUnit = codex!!.units.get(Unit.ITEMS_KEY)!!
        if (codexUnit.itemKeys != null && codexUnit.itemKeys.isNotEmpty()) {
            for (itemKey in codexUnit.itemKeys) {
                val item = itemUnit.rules.get(itemKey)
                if (item != null) {
                    codexUnit.rules.put(itemKey, item)
                }
            }
        }

        val newUnit = Unit(unitName + "#" + counter, codexUnit!!.type, false, codexUnit!!.psyker, HashMap<String, Model>(), codexUnit!!.damages, ArrayList<String>(), HashMap<String, Weapon>(), ArrayList<String>(), HashMap<String, Rule>())
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