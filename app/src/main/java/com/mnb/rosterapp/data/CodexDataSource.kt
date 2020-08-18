package com.mnb.rosterapp.data

import com.mnb.rosterapp.domain.Codex

interface CodexDataSource {
    suspend fun listCodexes(): List<String>
    suspend fun loadCodex(name: String): Codex?
}