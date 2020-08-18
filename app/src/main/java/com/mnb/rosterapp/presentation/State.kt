package com.mnb.rosterapp.presentation

import com.mnb.rosterapp.domain.Army
import com.mnb.rosterapp.domain.Codex
import com.mnb.rosterapp.domain.Unit

data class State (
    val originEvent: String,
    val armyList: List<String>?,
    val currentArmy: Army?,
    val currentUnit: Unit?,
    val codexList: List<String>?,
    val availableCodex: Codex?,
    val availableUnit: Unit?
) {

}