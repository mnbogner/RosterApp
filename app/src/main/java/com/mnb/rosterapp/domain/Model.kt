package com.mnb.rosterapp.domain

data class Model (
    override val name: String,
    override var count: Int,
    val points: Int,
    val power: Int,
    val move: String,
    val weapon: String,
    val ballistic: String,
    val strength: String,
    val toughness: String,
    val wounds: String,
    val attacks: String,
    val leadership: String,
    val save: String,
    // TODO: add fields to support better rules
    val required: Int,
    val limit: Int,
    val sharedWith: String
) : Element() {
    constructor(model: Model) : this(
        model.name,
        model.count,
        model.points,
        model.power,
        model.move,
        model.weapon,
        model.ballistic,
        model.strength,
        model.toughness,
        model.wounds,
        model.attacks,
        model.leadership,
        model.save,
        model.required,
        model.limit,
        model.sharedWith
    )
}