package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.ArmyRepository

class GetArmyList (private val armyRepository: ArmyRepository) {
    suspend operator fun invoke(): List<String> {
        return armyRepository.listArmies()
    }
}