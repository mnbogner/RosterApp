package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.ArmyRepository
import com.mnb.rosterapp.data.CodexRepository
import com.mnb.rosterapp.domain.*
import com.mnb.rosterapp.domain.Unit
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class NewArmy (private val codexRepository: CodexRepository, private val armyRepository: ArmyRepository) {

    // TODO: let user name/rename army?
    private val dateFormatString = "hh-mm-MM-dd"

    suspend operator fun invoke(codexName: String): Army {
        // build unique army name
        val format = SimpleDateFormat(dateFormatString)
        val part = format.format(Date())
        // build new army with required contents
        val army = armyRepository.newArmy(codexName + "_" + part)
        // names are selected from a list and should always be valid
        val codex = codexRepository.loadCodex(codexName)
        val codexUnit = codex!!.units.get("Rules")
        val rulesUnit = Unit(codexUnit!!.name, codexUnit!!.type, HashMap<String, Model>(), HashMap<Int, Damage>(), ArrayList<String>(), HashMap<String, Weapon>(), ArrayList<String>(), HashMap<String, Rule>())
        for (rule in codexUnit.rules.values) {
            if (rule.required > 0) {
                val ruleCopy = Rule(rule)
                rulesUnit.rules.put(ruleCopy.name, ruleCopy)
            }
        }
        army.units.put(rulesUnit.name, rulesUnit)
        return armyRepository.saveArmy(army)
    }
}