package com.mnb.rosterapp.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mnb.rosterapp.usecase.Interactors

class StateViewModelFactory(val application: Application, val interactors: Interactors) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // ignore class, currently only one view model is supported
        // seems like the interactors are supposed to be injected but i don't want to deal with that now
        return StateViewModel(application, interactors) as T
    }
}