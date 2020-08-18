package com.mnb.rosterapp.domain

data class Codex (
    val name: String,
    // use abstract "Units" to hold collections of faction rules, powers, etc.
    val units: MutableMap<String, Unit>
) {

}