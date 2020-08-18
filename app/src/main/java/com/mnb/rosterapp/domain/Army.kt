package com.mnb.rosterapp.domain

data class Army (
    val name: String,
    // use abstract "Units" to hold collections of faction rules, powers, etc.
    val units: MutableMap<String, Unit>
) {
    fun getPoints(): Int {
        // add up unit points
        var points = 0
        for (unit in units.values) {
            points += unit.getPoints()
        }
        return points
    }
    fun getPower(): Int {
        // add up unit power
        var power = 0
        for (unit in units.values) {
            power += unit.getPower()
        }
        return power
    }
}