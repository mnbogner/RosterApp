package com.mnb.rosterapp.usecase

import com.mnb.rosterapp.usecase.interactors.*

data class Interactors(
    val getArmy: GetArmy,
    val getArmyList: GetArmyList,
    val getCodex: GetCodex,
    val getCodexList: GetCodexList,
    val getUnitFromArmy: GetUnitFromArmy,
    val getUnitFromCodex: GetUnitFromCodex,
    val newArmy: NewArmy,
    val putElementInUnit: PutElementInUnit,
    val putUnitInArmy: PutUnitInArmy,
    val removeElementFromUnit: RemoveElementFromUnit,
    val removeUnitFromArmy: RemoveUnitFromArmy
)