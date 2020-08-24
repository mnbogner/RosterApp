package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.ArmyRepository
import com.mnb.rosterapp.data.CodexRepository
import com.mnb.rosterapp.domain.*
import com.mnb.rosterapp.domain.Unit

class ToggleWarlordUnit (private val codexRepository: CodexRepository, private val armyRepository: ArmyRepository) {
    suspend operator fun invoke(codexName: String, codexUnitName: String, armyName: String, armyUnitName: String): Army {
        // get specified unit (name is selected from a list and should exist
        val army = armyRepository.loadArmy(armyName)
        val armyUnit = army!!.units.get(armyUnitName)!!
        armyUnit.warlord = !armyUnit.warlord
        if (armyUnit.warlord) {
            // no-op, but additional elements will be visible based on flag
        } else {
            // remove any selected warlord traits
            val codex = codexRepository.loadCodex(codexName)
            val traitsUnit = codex!!.units.get(Unit.TRAITS_KEY)!!
            var removeKey: String? = null
            for (ruleKey in armyUnit.rules.keys) {
                if (traitsUnit.rules.containsKey(ruleKey)) {
                    removeKey = ruleKey
                    break
                }
            }
            if (removeKey != null) {
                armyUnit.rules.remove(removeKey)
            }
        }
        return armyRepository.saveArmy(army)
    }
}