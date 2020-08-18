package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.ArmyRepository
import com.mnb.rosterapp.domain.*

class RemoveElementFromUnit (private val armyRepository: ArmyRepository) {
    suspend operator fun invoke(armyName: String, unitName: String, elementName: String): Army {
        // names are selected from a list and should always be valid
        val army = armyRepository.loadArmy(armyName)
        val unit = army!!.units.get(unitName)
        if (unit!!.models.containsKey(elementName)) {
            val model = unit.models.get(elementName)
            if (model!!.count > model!!.required) {
                model!!.count--
                if (model.count == 0) {
                    unit.models.remove(elementName)
                }
            }
        } else if (unit!!.weapons.containsKey(elementName)) {
            val weapon = unit.weapons.get(elementName)
            if (weapon!!.count > weapon!!.required) {
                weapon!!.count--
                if (weapon.count == 0) {
                    unit.weapons.remove(elementName)
                }
            }
        } else if (unit!!.rules.containsKey(elementName)) {
            val rule = unit.rules.get(elementName)
            if (rule!!.count > rule!!.required) {
                rule!!.count--
                if (rule.count == 0) {
                    unit.rules.remove(elementName)
                }
            }
        } else {
            // no-op
        }
        return armyRepository.saveArmy(army)
    }
}