package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.ArmyRepository
import com.mnb.rosterapp.domain.Army

class GetArmy (private val armyRepository: ArmyRepository) {
    suspend operator fun invoke(name: String): Army {
        // names are selected from a list and should always be valid
        return armyRepository.loadArmy(name)!!
    }
}