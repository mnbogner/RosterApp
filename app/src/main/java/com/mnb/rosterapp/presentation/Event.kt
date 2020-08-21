package com.mnb.rosterapp.presentation

sealed class Event {

    data class StateInit(val eventName: String = STATE_INIT): Event()
    data class SelectionArmySelect(val eventName: String = SELECTION_ARMY_SELECT): Event()
    data class ArmySelectOpenArmy(val armyName: String, val eventName: String = ARMY_SELECT_OPEN_ARMY): Event()
    data class ArmyViewUnitSelect(val unitName: String, val eventName: String = ARMY_VIEW_UNIT_SELECT): Event()
    data class SelectionCodexSelect(val eventName: String = SELECTION_CODEX_SELECT): Event()
    data class CodexSelectNewArmy(val codexName: String, val eventName: String = CODEX_SELECT_NEW_ARMY): Event()
    data class CodexSelectOpenArmy(val armyName: String, val eventName: String = CODEX_SELECT_OPEN_ARMY): Event()
    data class ArmyEditRemoveUnit(val unitName: String, val eventName: String = ARMY_EDIT_REMOVE_UNIT): Event()
    data class ArmyEditSelectUnit(val unitName: String, val eventName: String = ARMY_EDIT_SELECT_UNIT): Event()
    data class UnitSelectAddUnit(val unitName: String, val eventName: String = UNIT_SELECT_ADD_UNIT): Event()
    data class UnitEditRemoveElement(val elementName: String, val eventName: String = UNIT_EDIT_REMOVE_ELEMENT): Event()
    data class UnitEditAddElement(val elementName: String, val eventName: String = UNIT_EDIT_ADD_ELEMENT): Event()
    data class ElementSelectAddElement(val elementName: String, val eventName: String = ELEMENT_SELECT_ADD_ELEMENT): Event()
    data class RefreshUi(val eventName: String = REFRESH_UI): Event()

    companion object {
        const val STATE_INIT = "state_init"
        const val SELECTION_ARMY_SELECT = "selection_army_select"
        const val ARMY_SELECT_OPEN_ARMY = "army_select_open_army"
        const val ARMY_VIEW_UNIT_SELECT = "army_view_unit_select"
        const val SELECTION_CODEX_SELECT = "selection_codex_select"
        const val CODEX_SELECT_NEW_ARMY = "codex_select_new_army"
        const val CODEX_SELECT_OPEN_ARMY = "codex_select_open_army"
        const val ARMY_EDIT_REMOVE_UNIT = "army_edit_remove_unit"
        const val ARMY_EDIT_SELECT_UNIT = "army_edit_select_unit"
        const val UNIT_SELECT_ADD_UNIT = "unit_select_add_unit"
        const val UNIT_EDIT_REMOVE_ELEMENT = "unit_edit_remove_element"
        const val UNIT_EDIT_ADD_ELEMENT = "unit_edit_add_element"
        const val ELEMENT_SELECT_ADD_ELEMENT = "element_select_add_element"
        const val REFRESH_UI = "refresh_ui"
    }
}