package com.mnb.rosterapp.framework

import com.mnb.rosterapp.data.ArmyDataSource
import com.mnb.rosterapp.domain.Army
import com.mnb.rosterapp.domain.Unit

class ArmyMemoryDataSource : ArmyDataSource {

    private val armyMap = HashMap<String, Army>()
    private var armyCache: Army? = null

    override suspend fun listArmies(): List<String> {
        return armyMap.keys.toList()
    }

    override suspend fun loadArmy(name: String): Army? {
        val cachedName = armyCache?.name
        if (cachedName == null || !cachedName.equals(name)) {
            armyCache = armyMap.get(name)
        }
        return armyCache
    }

    override suspend fun newArmy(name: String): Army {
        val army = Army(name, HashMap<String, Unit>())
        armyMap.put(army.name, army)
        return army
    }

    override suspend fun saveArmy(army: Army): Army {
        armyMap.put(army.name, army)
        return army
    }
}