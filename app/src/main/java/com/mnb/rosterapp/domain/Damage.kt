package com.mnb.rosterapp.domain

data class Damage (
    val name: String,  // don't need name?  replace with order?
    val remaining: String,
    val move: String,
    val weapon: String,
    val ballistic: String,
    val strength: String,
    val toughness: String,
    val wounds: String,
    val attacks: String,
    val leadership: String,
    val save: String,
    val details: String  // don't need details?
) {
    constructor(damage: Damage) : this(
        damage.name,
        damage.remaining,
        damage.move,
        damage.weapon,
        damage.ballistic,
        damage.strength,
        damage.toughness,
        damage.wounds,
        damage.attacks,
        damage.leadership,
        damage.save,
        damage.details
    )
}