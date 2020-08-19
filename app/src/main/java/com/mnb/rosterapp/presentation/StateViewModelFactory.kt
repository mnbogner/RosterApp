package com.mnb.rosterapp.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mnb.rosterapp.usecase.Interactors

class StateViewModelFactory(val application: Application, val interactors: Interactors) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // ignore class, currently only one view model is supported
        // TODO: implement proper injection of interactors
        return StateViewModel(application, interactors) as T
    }
}