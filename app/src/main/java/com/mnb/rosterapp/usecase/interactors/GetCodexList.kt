package com.mnb.rosterapp.usecase.interactors

import com.mnb.rosterapp.data.CodexRepository

class GetCodexList (private val codexRepository: CodexRepository) {
    suspend operator fun invoke(): List<String> {
        return codexRepository.listCodexes()
    }
}