package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.CodexRepository
import com.mnb.rosterapp.domain.Codex

class GetCodex (private val codexRepository: CodexRepository) {
    suspend operator fun invoke(name: String): Codex {
        // names are selected from a list and should always be valid
        return codexRepository.loadCodex(name)!!
    }
}