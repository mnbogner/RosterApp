package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.ArmyRepository
import com.mnb.rosterapp.domain.Unit

class GetUnitFromArmy (private val armyRepository: ArmyRepository) {
    suspend operator fun invoke(name: String, unit: String): Unit {
        // names are selected from a list and should always be valid
        val army = armyRepository.loadArmy(name)
        return army!!.units.get(unit)!!
    }
}