package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.CodexRepository
import com.mnb.rosterapp.domain.Unit

class GetUnitFromCodex (private val codexRepository: CodexRepository) {
    suspend operator fun invoke(name: String, unit: String): Unit {
        // names are selected from a list and should always be valid
        val codex = codexRepository.loadCodex(name)
        val codexUnit = codex!!.units.get(unit)!!

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

        return codexUnit
    }
}