package com.mnb.rosterapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mnb.rosterapp.R
import com.mnb.rosterapp.data.ArmyRepository
import com.mnb.rosterapp.data.CodexRepository
import com.mnb.rosterapp.framework.ArmyJsonDataSource
import com.mnb.rosterapp.framework.CodexJsonDataSource
import com.mnb.rosterapp.usecase.Interactors
import com.mnb.rosterapp.usecase.interactors.*

class MainActivity : AppCompatActivity() {

    private lateinit var stateModel: StateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // val codexRepository = CodexRepository(CodexMemoryDataSource())
        // val armyRepository = ArmyRepository(ArmyMemoryDataSource())
        val codexRepository = CodexRepository(CodexJsonDataSource())
        val armyRepository = ArmyRepository(ArmyJsonDataSource())
        stateModel = ViewModelProvider( this,
            StateViewModelFactory(application,
                Interactors(
                    GetArmy(armyRepository),
                    GetArmyList(armyRepository),
                    GetCodex(codexRepository),
                    GetCodexList(codexRepository),
                    GetUnitFromArmy(armyRepository),
                    GetUnitFromCodex(codexRepository),
                    NewArmy(codexRepository, armyRepository),
                    PutElementInUnit(codexRepository, armyRepository),
                    PutUnitInArmy(codexRepository, armyRepository),
                    RemoveElementFromUnit(armyRepository),
                    RemoveUnitFromArmy(armyRepository)
                )
            )
        ).get(StateViewModel::class.java)
    }
}