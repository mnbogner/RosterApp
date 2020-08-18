package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.ArmyRepository
import com.mnb.rosterapp.domain.Army

class RemoveUnitFromArmy (private val armyRepository: ArmyRepository) {
    suspend operator fun invoke(armyName: String, unitName: String): Army {
        // names are selected from a list and should always be valid
        val army = armyRepository.loadArmy(armyName)
        if (army!!.units.containsKey(unitName)) {
            army.units.remove(unitName)
        }
        return armyRepository.saveArmy(army)
    }
}