package com.mnb.rosterapp.data

import com.mnb.rosterapp.domain.Army

class ArmyRepository (private val dataSource: ArmyDataSource) {
    suspend fun listArmies(): List<String> {
        return dataSource.listArmies()
    }
    suspend fun loadArmy(name: String): Army? {
        return dataSource.loadArmy(name)
    }
    suspend fun newArmy(name: String): Army {
        return dataSource.newArmy(name)
    }
    suspend fun saveArmy(army: Army): Army {
        dataSource.saveArmy(army)
        return army
    }
}