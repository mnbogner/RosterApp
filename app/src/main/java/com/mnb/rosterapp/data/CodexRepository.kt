package com.mnb.rosterapp.data

import com.mnb.rosterapp.domain.Codex

class CodexRepository (private val dataSource: CodexDataSource) {
    suspend fun listCodexes(): List<String> {
        return dataSource.listCodexes()
    }
    suspend fun loadCodex(name: String): Codex? {
        return dataSource.loadCodex(name)
    }
}