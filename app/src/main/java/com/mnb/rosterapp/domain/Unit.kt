package com.mnb.rosterapp.domain

data class Unit (
    val name: String,
    val type: String,  // change to enum?
    var warlord: Boolean,
    val models: MutableMap<String, Model>,
    val damages: MutableMap<Int, Damage>,
    val weaponKeys: MutableList<String>?,
    val weapons: MutableMap<String, Weapon>,
    val itemKeys: MutableList<String>?,
    val rules: MutableMap<String, Rule>
): Comparable<Unit> {
    constructor(unit: Unit) : this(
        unit.name,
        unit.type,
        unit.warlord,
        unit.models,
        unit.damages,
        unit.weaponKeys,
        unit.weapons,
        unit.itemKeys,
        unit.rules
    )

    companion object {
        // abstract "units" to hold common elements
        val RULES_KEY = "Rules"
        val RULES = "rules"
        val WEAPONS_KEY = "Weapons"
        val WEAPONS = "weapons"
        val ITEMS_KEY = "Items"
        val ITEMS = "items"
        val POWERS_KEY = "Powers"
        val POWERS = "powers"
        val TRAITS_KEY = "Traits"
        val TRAITS = "traits"
        val RELICS_KEY = "Relics"
        val RELICS = "relics"
        // unit types
        val HQ = "hq"
        val TROOPS = "troops"
        val ELITE = "elite"
        val FAST = "fast"
        val HEAVY = "heavy"
        val FLIER = "flier"
        val TRANSPORT = "transport"
    }

    override operator fun compareTo(other: Unit): Int {
        if (this.type == other.type) {
            // need to invert to sort within categories
            return other.name.compareTo(this.name)
        } else if (this.type == RULES) {
            return 1
        } else if (other.type == RULES) {
            return -1
        } else if (this.type == HQ) {
            return 1
        } else if (other.type == HQ) {
            return -1
        } else if (this.type == TROOPS) {
            return 1
        } else if (other.type == TROOPS) {
            return -1
        } else if (this.type == ELITE) {
            return 1
        } else if (other.type == ELITE) {
            return -1
        } else if (this.type == FAST) {
            return 1
        } else if (other.type == FAST) {
            return -1
        } else if (this.type == HEAVY) {
            return 1
        } else if (other.type == HEAVY) {
            return -1
        } else if (this.type == FLIER) {
            return 1
        } else if (other.type == FLIER) {
            return -1
        } else if (this.type == TRANSPORT) {
            return 1
        } else if (other.type == TRANSPORT) {
            return -1
        }
        return 0
    }

    fun getPoints(): Int {
        // add up model/weapons/rules points
        var points = 0
        for (model in models.values) {
            if (model.count > 0) {
                points += (model.points * model.count)
            } else if (model.required > 0) {
                points += (model.points * model.required)
            }
        }
        for (weapon in weapons.values) {
            if (weapon.count > 0) {
                points += (weapon.points * weapon.count)
            } else if (weapon.required > 0) {
                points += (weapon.points * weapon.required)
            }
        }
        for (rule in rules.values) {
            if (rule.count > 0) {
                points += (rule.points * rule.count)
            } else if (rule.required > 0) {
                points += (rule.points * rule.required)
            }
        }
        return points
    }

    fun getPower(): Int {
        // add up model/weapons/rules power
        var power = 0
        for (model in models.values) {
            if (model.count > 0) {
                power += (model.power * model.count)
            } else if (model.required > 0) {
                power += (model.power * model.required)
            }
        }
        for (weapon in weapons.values) {
            if (weapon.count > 0) {
                power += (weapon.power * weapon.count)
            } else if (weapon.required > 0) {
                power += (weapon.power * weapon.required)
            }
        }
        for (rule in rules.values) {
            if (rule.count > 0) {
                power += (rule.power * rule.count)
            } else if (rule.required > 0) {
                power += (rule.power * rule.required)
            }
        }
        return power
    }

    fun getModelCount(): Int {
        // add up number of models
        var modelCount = 0
        for (model in models.values) {
            if (model.count > 0) {
                modelCount += model.count
            } else if (model.required > 0) {
                modelCount += model.required
            }
        }
        return modelCount
    }
}