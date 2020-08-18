package com.mnb.rosterapp.domain

data class Rule (
    override val name: String,
    override var count: Int,
    val points: Int,
    val power: Int,
    val type: String,
    val description: String,
    // TODO: add fields to support better rules
    val required: Int,
    val limit: Int,
    val sharedWith: String
) : Element() {
    constructor(rule: Rule) : this(
        rule.name,
        rule.count,
        rule.points,
        rule.power,
        rule.type,
        rule.description,
        rule.required,
        rule.limit,
        rule.sharedWith
    )
}