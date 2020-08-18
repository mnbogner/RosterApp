package com.mnb.rosterapp.data

import com.mnb.rosterapp.domain.Army

interface ArmyDataSource {
    suspend fun listArmies(): List<String>
    suspend fun loadArmy(name: String): Army?
    suspend fun newArmy(name: String): Army
    suspend fun saveArmy(army: Army): Army
}