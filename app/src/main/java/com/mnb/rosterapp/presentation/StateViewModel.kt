package com.mnb.rosterapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mnb.rosterapp.usecase.Interactors
import kotlinx.coroutines.runBlocking

class StateViewModel(application: Application, val interactors: Interactors) : AndroidViewModel(application) {

    val state = MutableLiveData<State>()
    val emptyState = State(Event.STATE_INIT, null, null, null, null, null, null)

    fun handleEvent(event: Event) {

        // TODO: encoutered an issue where posting a state would trigger an observer but that observer
        // would receive the previously value of that state.  changing the code to set the state fixed
        // that issue, but caused observers in fragments not in the foreground to trigger (i believe
        // this is a main thread/background thread issue?).  might make state another sealed class to
        // replace the debug name if there isn't a better way to fix this

        when(event) {
            is Event.StateInit -> {
                System.out.println("EVENT - " + Event.STATE_INIT)
                state.postValue(emptyState)
            }
            is Event.SelectionArmySelect -> {
                System.out.println("EVENT - " + Event.SELECTION_ARMY_SELECT)
                runBlocking {
                    val oldState = state.value ?: emptyState
                    val armyList = interactors.getArmyList.invoke()
                    state.value =
                        State(
                            Event.SELECTION_ARMY_SELECT,
                            armyList,
                            oldState.currentArmy,
                            oldState.currentUnit,
                            oldState.codexList,
                            oldState.availableCodex,
                            oldState.availableUnit
                        )
                }
            }
            is Event.ArmySelectOpenArmy -> {
                System.out.println("EVENT - " + Event.ARMY_SELECT_OPEN_ARMY)
                runBlocking {
                    val oldState = state.value ?: emptyState
                    val army = interactors.getArmy.invoke(event.armyName)
                    state.value = State(
                        Event.ARMY_SELECT_OPEN_ARMY,
                        oldState.armyList,
                        army,
                        oldState.currentUnit,
                        oldState.codexList,
                        oldState.availableCodex,
                        oldState.availableUnit
                    )
                }
            }
            is Event.ArmyViewUnitSelect -> {
                System.out.println("EVENT - " + Event.ARMY_VIEW_UNIT_SELECT)
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // get army name from state
                    val unit = interactors.getUnitFromArmy.invoke(oldState.currentArmy!!.name, event.unitName)
                    state.value =
                        State(
                            Event.ARMY_VIEW_UNIT_SELECT,
                            oldState.armyList,
                            oldState.currentArmy,
                            unit,
                            oldState.codexList,
                            oldState.availableCodex,
                            oldState.availableUnit
                        )
                }
            }
            is Event.SelectionCodexSelect -> {
                System.out.println("EVENT - " + Event.SELECTION_CODEX_SELECT)
                runBlocking {
                    val oldState = state.value ?: emptyState
                    val armyList = interactors.getArmyList.invoke()
                    val codexList = interactors.getCodexList.invoke()
                    state.value =
                        State(
                            Event.SELECTION_CODEX_SELECT,
                            armyList,
                            oldState.currentArmy,
                            oldState.currentUnit,
                            codexList,
                            oldState.availableCodex,
                            oldState.availableUnit
                        )
                }
            }
            is Event.CodexSelectNewArmy -> {
                System.out.println("EVENT - " + Event.CODEX_SELECT_NEW_ARMY)
                runBlocking {
                    val oldState = state.value ?: emptyState
                    val codex = interactors.getCodex.invoke(event.codexName)
                    val army = interactors.newArmy.invoke(codex.name)
                    state.value = State(
                        Event.CODEX_SELECT_NEW_ARMY,
                        oldState.armyList,
                        army,
                        oldState.currentUnit,
                        oldState.codexList,
                        codex,
                        oldState.availableUnit
                    )
                }
            }
            is Event.CodexSelectOpenArmy -> {
                System.out.println("EVENT - " + Event.CODEX_SELECT_OPEN_ARMY)
                runBlocking {
                    val oldState = state.value ?: emptyState
                    val army = interactors.getArmy.invoke(event.armyName)

                    // split codex name out of army name
                    // TODO: allow arbitrary army names, store codex name in army object
                    val nameParts = event.armyName.split("_")
                    val codex = interactors.getCodex.invoke(nameParts[0])

                    state.value = State(
                        Event.CODEX_SELECT_OPEN_ARMY,
                        oldState.armyList,
                        army,
                        oldState.currentUnit,
                        oldState.codexList,
                        codex,
                        oldState.availableUnit
                    )
                }
            }
            is Event.ArmyEditRemoveUnit -> {
                System.out.println("EVENT - " + Event.ARMY_EDIT_REMOVE_UNIT)
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
            is Event.ArmyEditSelectUnit -> {
                System.out.println("EVENT - " + Event.ARMY_EDIT_SELECT_UNIT)
                runBlocking {
                    val oldState = state.value ?: emptyState
                    // get army name from state
                    val armyUnit = interactors.getUnitFromArmy.invoke(oldState.currentArmy!!.name, event.unitName)
                    // need to package this logic somewhere
                    val baseName = armyUnit.name.split("#").get(0)
                    // get codex name from state
                    val codexUnit = interactors.getUnitFromCodex.invoke(oldState.availableCodex!!.name, baseName)
                    state.value = State(
                        Event.ARMY_EDIT_SELECT_UNIT,
                        oldState.armyList,
                        oldState.currentArmy,
                        armyUnit,
                        oldState.codexList,
                        oldState.availableCodex,
                        codexUnit
                    )
                }
            }
            is Event.UnitSelectAddUnit -> {
                System.out.println("EVENT - " + Event.UNIT_SELECT_ADD_UNIT)
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
            is Event.UnitEditRemoveElement -> {
                System.out.println("EVENT - " + Event.UNIT_EDIT_REMOVE_ELEMENT)
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
            is Event.UnitEditAddElement -> {
                System.out.println("EVENT - " + Event.UNIT_EDIT_ADD_ELEMENT)
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
            is Event.ElementSelectAddElement -> {
                System.out.println("EVENT - " + Event.ELEMENT_SELECT_ADD_ELEMENT)
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
                // a "no-op" event to drive the ui without making changes to data
                System.out.println("EVENT - " + Event.REFRESH_UI)
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