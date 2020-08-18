package com.mnb.rosterapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mnb.rosterapp.usecase.Interactors
import kotlinx.coroutines.runBlocking

class StateViewModel(application: Application, val interactors: Interactors) : AndroidViewModel(application) {

    val state = MutableLiveData<State>()
    val emptyState = State("", null, null, null, null, null, null)

    fun handleEvent(event: Event) {

        // TODO: encoutered an issue where posting a state would trigger an observer but that observer
        // would receive the previously value of that state.  changing the code to set the state fixed
        // that issue, but caused observers in fragments not in the foreground to trigger (i believe
        // this is a main thread/background thread issue?).  might make state another sealed class to
        // replace the debug name if there isn't a better way to fix this

        when(event) {
            is Event.SelectionInit -> {
                System.out.println("FOO - " + "EVENT SELECTION INIT")
                state.postValue(emptyState)
            }
            is Event.ArmySelectInit -> {
                System.out.println("FOO - " + "ARMY SELECTION INIT")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    val armyList = interactors.getArmyList.invoke()
                    state.value =
                        State(
                            Event.ARMY_SELECT_INIT,
                            armyList,
                            oldState.currentArmy,
                            oldState.currentUnit,
                            oldState.codexList,
                            oldState.availableCodex,
                            oldState.availableUnit
                        )
                }
            }
            is Event.ArmyViewInit -> {
                System.out.println("FOO - " + "ARMY VIEW INIT")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    val army = interactors.getArmy.invoke(event.armyName)
                    state.value =
                        State(
                            Event.ARMY_VIEW_INIT,
                            oldState.armyList,
                            army,
                            oldState.currentUnit,
                            oldState.codexList,
                            oldState.availableCodex,
                            oldState.availableUnit
                        )
                }
            }
            is Event.UnitViewInit -> {
                System.out.println("FOO - " + "UNIT VIEW INIT")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // get army name from state
                    val unit = interactors.getUnitFromArmy.invoke(oldState.currentArmy!!.name, event.unitName)
                    state.value =
                        State(
                            Event.UNIT_VIEW_INIT,
                            oldState.armyList,
                            oldState.currentArmy,
                            unit,
                            oldState.codexList,
                            oldState.availableCodex,
                            oldState.availableUnit
                        )
                }
            }
            is Event.CodexSelectInit -> {
                System.out.println("FOO - " + "CODEX SELECTION INIT")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    val codexList = interactors.getCodexList.invoke()
                    // load army list too so existing files can be opened
                    val armyList = interactors.getArmyList.invoke()
                    state.value =
                        State(
                            Event.CODEX_SELECT_INIT,
                            armyList,
                            oldState.currentArmy,
                            oldState.currentUnit,
                            codexList,
                            oldState.availableCodex,
                            oldState.availableUnit
                        )
                }
            }
            is Event.ArmyEditInit -> {
                System.out.println("FOO - " + "ARMY EDIT INIT")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    val codex = interactors.getCodex.invoke(event.codexName)
                    val armyName = event.armyName ?: oldState.currentArmy?.name
                    if (armyName == null) {
                        val army = interactors.newArmy.invoke(codex.name)
                        state.value = State(
                            Event.ARMY_EDIT_INIT,
                            oldState.armyList,
                            army,
                            oldState.currentUnit,
                            oldState.codexList,
                            codex,
                            oldState.availableUnit
                        )
                    } else {
                        val army = interactors.getArmy.invoke(armyName)
                        state.value = State(
                            Event.ARMY_EDIT_INIT,
                            oldState.armyList,
                            army,
                            oldState.currentUnit,
                            oldState.codexList,
                            codex,
                            oldState.availableUnit
                        )
                    }
                }
            }
            is Event.ArmyEditAddUnit -> {
                System.out.println("FOO - " + "ARMY EDIT ADD")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // get codex and army names from state
                    val army = interactors.putUnitInArmy.invoke(oldState.availableCodex!!.name, oldState.currentArmy!!.name, event.unitName)
                    state.value = State(
                        Event.ARMY_EDIT_ADD_UNIT,
                        oldState.armyList,
                        army,
                        oldState.currentUnit,
                        oldState.codexList,
                        oldState.availableCodex,
                        oldState.availableUnit
                    )
                }
            }
            is Event.ArmyEditRemoveUnit -> {
                System.out.println("FOO - " + "ARMY EDIT REMOVE")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // get army name from state
                    val army = interactors.removeUnitFromArmy.invoke(oldState.currentArmy!!.name, event.unitName)
                    state.value = State(
                            Event.ARMY_EDIT_REMOVE_UNIT,
                            oldState.armyList,
                            army,
                            oldState.currentUnit,
                            oldState.codexList,
                            oldState.availableCodex,
                            oldState.availableUnit
                        )
                }
            }
            is Event.UnitSelectInit -> {
                System.out.println("FOO - " + "UNIT SELECT INIT")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // redundant?  we already have the current codex from army edit init
                    val codex = interactors.getCodex.invoke(oldState.availableCodex!!.name)
                    state.value = State(
                            Event.UNIT_SELECT_INIT,
                            oldState.armyList,
                            oldState.currentArmy,
                            oldState.currentUnit,
                            oldState.codexList,
                            codex,
                            oldState.availableUnit
                        )
                }
            }
            is Event.UnitSelectAddUnit -> {
                System.out.println("FOO - " + "UNIT SELECT ADD")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // get codex and army names from state
                    val army = interactors.putUnitInArmy.invoke(oldState.availableCodex!!.name, oldState.currentArmy!!.name, event.unitName)
                    state.value = State(
                        Event.UNIT_SELECT_ADD_UNIT,
                        oldState.armyList,
                        army,
                        oldState.currentUnit,
                        oldState.codexList,
                        oldState.availableCodex,
                        oldState.availableUnit
                    )
                }
            }
            is Event.UnitEditInit -> {
                System.out.println("FOO - " + "UNIT EDIT INIT")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // get army name from state
                    val armyUnit = interactors.getUnitFromArmy.invoke(oldState.currentArmy!!.name, event.unitName)
                    // need to package this logic somewhere
                    val baseName = armyUnit.name.split("#").get(0)
                    // get codex name from state
                    val codexUnit = interactors.getUnitFromCodex.invoke(oldState.availableCodex!!.name, baseName)
                    state.value = State(
                            Event.UNIT_EDIT_INIT,
                            oldState.armyList,
                            oldState.currentArmy,
                            armyUnit,
                            oldState.codexList,
                            oldState.availableCodex,
                            codexUnit
                        )
                }
            }
            is Event.UnitEditAddElement -> {
                System.out.println("FOO - " + "UNIT EDIT INCREMENT")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // need to package this logic somewhere
                    val baseName = oldState.currentUnit!!.name.split("#").get(0)
                    // get codex and army and unit names from state
                    val army = interactors.putElementInUnit.invoke(oldState.availableCodex!!.name, baseName, oldState.currentArmy!!.name, oldState.currentUnit!!.name, event.elementName)
                    val armyUnit = interactors.getUnitFromArmy.invoke(oldState.currentArmy!!.name, oldState.currentUnit!!.name)
                    state.value = State(
                            Event.UNIT_EDIT_ADD_ELEMENT,
                            oldState.armyList,
                            army,
                            armyUnit,
                            oldState.codexList,
                            oldState.availableCodex,
                            oldState.availableUnit
                        )
                }
            }
            is Event.UnitEditRemoveElement -> {
                System.out.println("FOO - " + "UNIT EDIT DECREMENT")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // get codex and army names from state
                    val army = interactors.removeElementFromUnit.invoke(oldState.currentArmy!!.name, oldState.currentUnit!!.name, event.elementName)
                    val armyUnit = interactors.getUnitFromArmy.invoke(oldState.currentArmy!!.name, oldState.currentUnit!!.name)
                    state.value = State(
                            Event.UNIT_EDIT_REMOVE_ELEMENT,
                            oldState.armyList,
                            army,
                            armyUnit,
                            oldState.codexList,
                            oldState.availableCodex,
                            oldState.availableUnit
                        )
                }
            }
            is Event.ElementSelectInit -> {
                System.out.println("FOO - " + "ELEMENT SELECT INIT")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // redundant?  we already have the current codex unit from unit edit init
                    val codexUnit = interactors.getUnitFromCodex.invoke(oldState.availableCodex!!.name, oldState.availableUnit!!.name)
                    state.value = State(
                            Event.ELEMENT_SELECT_INIT,
                            oldState.armyList,
                            oldState.currentArmy,
                            oldState.currentUnit,
                            oldState.codexList,
                            oldState.availableCodex,
                            codexUnit
                        )
                }
            }
            is Event.ElementSelectAddElement -> {
                System.out.println("FOO - " + "ELEMENT SELECT ADD")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // need to package this logic somewhere
                    val baseName = oldState.currentUnit!!.name.split("#").get(0)
                    // get codex and army and unit names from state
                    val army = interactors.putElementInUnit.invoke(oldState.availableCodex!!.name, baseName, oldState.currentArmy!!.name, oldState.currentUnit!!.name, event.elementName)
                    val armyUnit = interactors.getUnitFromArmy.invoke(oldState.currentArmy!!.name, oldState.currentUnit!!.name)
                    state.value = State(
                        Event.ELEMENT_SELECT_ADD_ELEMENT,
                        oldState.armyList,
                        army,
                        armyUnit,
                        oldState.codexList,
                        oldState.availableCodex,
                        oldState.availableUnit
                    )
                }
            }
            is Event.RefreshUi -> {
                // seem to need a "no-op" event to drive the ui without making changes to data
                // ie: because onCreate is triggered even when returning to a fragment with "back"
                System.out.println("FOO - " + "REFRESH UI")
                runBlocking {
                    val oldState = state.value ?: emptyState
                    state.value = State(
                        Event.REFRESH_UI,
                        oldState.armyList,
                        oldState.currentArmy,
                        oldState.currentUnit,
                        oldState.codexList,
                        oldState.availableCodex,
                        oldState.availableUnit
                    )
                }
            }
        }
    }
}