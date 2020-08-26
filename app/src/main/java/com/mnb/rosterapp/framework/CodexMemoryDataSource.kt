package com.mnb.rosterapp.framework

import com.mnb.rosterapp.data.CodexDataSource
import com.mnb.rosterapp.domain.*
import com.mnb.rosterapp.domain.Unit

class CodexMemoryDataSource : CodexDataSource {

    private val codexMap = HashMap<String, Codex>()
    private var codexCache: Codex? = null

    override suspend fun listCodexes(): List<String> {
        val codex1 = makeCodex("codex one")
        val codex2 = makeCodex("codex two")
        val codex3 = makeCodex("codex three")
        codexMap.put(codex1.name, codex1)
        codexMap.put(codex2.name, codex2)
        codexMap.put(codex3.name, codex3)
        return codexMap.keys.toList()
    }

    override suspend fun loadCodex(codexName: String): Codex? {
        val cachedName = codexCache?.name
        if (cachedName == null || !cachedName.equals(codexName)) {
            codexCache = codexMap.get(codexName)
        }

        // TEMP - BEGIN

        if (codexCache == null) {
            val codex = makeCodex(codexName)
            codexMap.put(codex.name, codex)
            codexCache = codex
        }

        // TEMP - END

        return codexCache
    }

    private fun makeCodex(codexName: String): Codex {

        // create codex in memory for testing

        val codexRules = HashMap<String, Rule>()
        codexRules.put("Faction 1", Rule("Faction 1", 0, 0, 0, "faction", "Faction 1 Bonus", 0, 1, "faction"))
        codexRules.put("Faction 2", Rule("Faction 2", 0, 0, 0, "faction", "Faction 2 Bonus", 0, 1, "faction"))

        val rulesUnit = Unit(
            "Rules",
            "rules",
            false,
            0,
            HashMap<String, Model>(),
            HashMap<Int, Damage>(),
            ArrayList<String>(),
            HashMap<String, Weapon>(),
            ArrayList<String>(),
            codexRules
        )

        val infantryModels = HashMap<String, Model>()
        infantryModels.put("Leader", Model("Leader", 0, 30, 3, "6\"", "4+", "3+", "3", "3", "1", "2", "8", "4+", 1, 1, "leader"))
        infantryModels.put("Regular", Model("Regular", 0, 30, 3, "6\"", "4+", "3+", "3", "3", "1", "1", "7", "4+", 4, 9, "regular"))

        val infantryDamages = HashMap<Int, Damage>()

        val infantryWeapons = HashMap<String, Weapon>()
        infantryWeapons.put("Small Gun", Weapon("Small Gun", 0, 5, 0, "12\"", "RF1", "4", "0", "1", "-1 AP on 6+ hit roll", 0, -1, "normal"))
        infantryWeapons.put("Big Gun", Weapon("Big Gun", 0, 10, 0, "24\"", "A3", "3", "0", "1", "", 0, -1, "special"))

        val infantryRules = HashMap<String, Rule>()
        infantryRules.put("Bionics", Rule("Bionics", 0, 0, 0, "passive", "6++", 1, 1, ""))
        infantryRules.put("Shield", Rule("Shield", 0, 5, 0, "item", "4++", 0, -1, "item"))
        infantryRules.put("Psyker", Rule("Psyker", 0, 0, 0, "psyker", "cast 1/deny 1", 0, 1, "psyker"))
        infantryRules.put("Power", Rule("Power", 0, 0, 0, "power", "warp charge 7", 0, 1, "power"))

        val infantryUnit = Unit(
            "Infantry",
            "troop",
            false,
            0,
            infantryModels,
            infantryDamages,
            ArrayList<String>(),
            infantryWeapons,
            ArrayList<String>(),
            infantryRules
        )

        val vehicleModels = HashMap<String, Model>()
        vehicleModels.put("Vehicle", Model("Vehicle", 0, 100, 10, "*", "5+", "*", "6", "7", "12", "*", "8", "3+", 1, 1, "vehicle"))

        val vehicleDamages = HashMap<Int, Damage>()
        vehicleDamages.put(1, Damage("name", "12 - 7", "12\"", "", "3+", "", "", "", "3", "", "", "???"))
        vehicleDamages.put(2, Damage("name", "6 - 4", "9\"", "", "4+", "", "", "", "D3", "", "", "???"))
        vehicleDamages.put(3, Damage("name", "3 - 1", "6\"", "", "5+", "", "", "", "1", "", "", "???"))

        val vehicleWeapons = HashMap<String, Weapon>()
        vehicleWeapons.put("Bigger Gun", Weapon("Bigger Gun", 0, 10, 0, "12\"", "H1", "5", "-1", "3", "", 0, 1, "main"))
        vehicleWeapons.put("Biggest Gun", Weapon("Biggest Gun", 0, 20, 0, "24\"", "H4", "4", "0", "D6", "foo", 0, 1, "main"))

        val vehicleRules = HashMap<String, Rule>()
        infantryRules.put("Comms", Rule("Comms", 0, 5, 0, "item", "morale", 0, 1, "extra"))
        vehicleRules.put("Engine", Rule("Engine", 0, 0, 0, "ability", "fast", 0, 1, "extra"))

        val vehicleUnit = Unit(
            "Vehicle",
            "heavy support",
            false,
            0,
            vehicleModels,
            vehicleDamages,
            ArrayList<String>(),
            vehicleWeapons,
            ArrayList<String>(),
            vehicleRules
        )

        val codexUnits = HashMap<String, Unit>()
        codexUnits.put(rulesUnit.name, rulesUnit)
        codexUnits.put(infantryUnit.name, infantryUnit)
        codexUnits.put(vehicleUnit.name, vehicleUnit)

        return Codex(codexName, codexUnits)
    }
}