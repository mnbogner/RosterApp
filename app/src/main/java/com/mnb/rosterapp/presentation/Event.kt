package com.mnb.rosterapp.presentation

sealed class Event {

    data class SelectionInit(val eventName: String = SELECTION_INIT): Event()

    data class SelectionArmySelect(val eventName: String = SELECTION_ARMY_SELECT): Event()
    data class SelectionCodexSelect(val eventName: String = SELECTION_CODEX_SELECT): Event()

    data class ArmySelectInit(val eventName: String = ARMY_SELECT_INIT): Event()
    data class ArmyViewInit(val armyName: String, val eventName: String = ARMY_VIEW_INIT): Event()
    data class UnitViewInit(val unitName: String, val eventName: String = UNIT_VIEW_INIT): Event()
    data class CodexSelectInit(val eventName: String = CODEX_SELECT_INIT): Event()

    data class CodexSelectNewArmy(val codexName: String, val eventName: String = CODEX_SELECT_NEW_ARMY): Event()
    data class CodexSelectOpenArmy(val armyName: String, val eventName: String = CODEX_SELECT_OPEN_ARMY): Event()

    data class ArmyEditInit(val codexName: String, val armyName: String?, val eventName: String = ARMY_EDIT_INIT): Event()
    data class ArmyEditAddUnit(val unitName: String, val eventName: String = ARMY_EDIT_ADD_UNIT): Event()
    data class ArmyEditRemoveUnit(val unitName: String, val eventName: String = ARMY_EDIT_REMOVE_UNIT): Event()

    data class ArmyEditLoadUnits(val codexName: String, val eventName: String = ARMY_EDIT_LOAD_UNITS): Event()
    data class ArmyEditSelectUnit(val unitName: String, val eventName: String = ARMY_EDIT_SELECT_UNIT): Event()

    data class UnitSelectInit(val eventName: String = UNIT_SELECT_INIT): Event()
    data class UnitSelectAddUnit(val unitName: String, val eventName: String = UNIT_SELECT_ADD_UNIT): Event()
    data class UnitEditInit(val unitName: String, val eventName: String = UNIT_EDIT_INIT): Event()
    data class UnitEditAddElement(val elementName: String, val eventName: String = UNIT_EDIT_ADD_ELEMENT): Event()
    data class UnitEditRemoveElement(val elementName: String, val eventName: String = UNIT_EDIT_REMOVE_ELEMENT): Event()
    data class ElementSelectInit(val eventName: String = ELEMENT_SELECT_INIT): Event()
    data class ElementSelectAddElement(val elementName: String, val eventName: String = ELEMENT_SELECT_ADD_ELEMENT): Event()
    data class RefreshUi(val eventName: String = ELEMENT_SELECT_INIT): Event()

    companion object {
        const val SELECTION_INIT = "selection_init"

        const val SELECTION_ARMY_SELECT = "selection_army_select"
        const val SELECTION_CODEX_SELECT = "selection_codex_select"

        const val ARMY_SELECT_INIT = "army_select_init"
        const val ARMY_VIEW_INIT = "army_view_init"
        const val UNIT_VIEW_INIT = "unit_view_init"
        const val CODEX_SELECT_INIT = "codex_select_init"

        const val CODEX_SELECT_NEW_ARMY = "codex_select_new_army"
        const val CODEX_SELECT_OPEN_ARMY = "codex_select_open_army"

        const val ARMY_EDIT_INIT = "army_edit_init"
        const val ARMY_EDIT_ADD_UNIT = "army_edit_add_unit"
        const val ARMY_EDIT_REMOVE_UNIT = "army_edit_remove_unit"

        const val ARMY_EDIT_LOAD_UNITS = "army_edit_load_units"
        const val ARMY_EDIT_SELECT_UNIT = "army_edit_select_unit"

        const val UNIT_SELECT_INIT = "unit_select_init"
        const val UNIT_SELECT_ADD_UNIT = "unit_select_add_unit"
        const val UNIT_EDIT_INIT = "unit_edit_init"
        const val UNIT_EDIT_ADD_ELEMENT = "unit_edit_add_element"
        const val UNIT_EDIT_REMOVE_ELEMENT = "unit_edit_remove_element"
        const val ELEMENT_SELECT_INIT = "element_select_init"
        const val ELEMENT_SELECT_ADD_ELEMENT = "element_select_add_element"
        const val REFRESH_UI = "refresh_ui"
    }
}