package com.mnb.rosterapp.domain

data class Weapon (
    override val name: String,
    override var count: Int,
    val points: Int,
    val power: Int,
    val range: String,
    val type: String,
    val strength: String,
    val penetration: String,
    val damage: String,
    val description: String,
    // TODO: add fields to support better rules
    val required: Int,
    val limit: Int,
    val sharedWith: String
) : Element() {
    constructor(weapon: Weapon) : this(
        weapon.name,
        weapon.count,
        weapon.points,
        weapon.power,
        weapon.range,
        weapon.type,
        weapon.strength,
        weapon.penetration,
        weapon.damage,
        weapon.description,
        weapon.required,
        weapon.limit,
        weapon.sharedWith
    )
}