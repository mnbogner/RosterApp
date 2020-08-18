package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.CodexRepository
import com.mnb.rosterapp.domain.Unit

class GetUnitFromCodex (private val codexRepository: CodexRepository) {
    suspend operator fun invoke(name: String, unit: String): Unit {
        // names are selected from a list and should always be valid
        val codex = codexRepository.loadCodex(name)
        return codex!!.units.get(unit)!!
    }
}