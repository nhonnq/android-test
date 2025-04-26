package dev.nhonnq.app.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * BaseViewModel
 *
 * A base class for ViewModels, providing a convenient way to launch coroutines within the ViewModel's scope.
 *
 * This class extends Android's ViewModel class and offers a helper function [launch] for executing
 * suspend functions in a coroutine on the [viewModelScope]. This helps manage the coroutines'
 * lifecycle automatically, canceling them when the ViewModel is cleared.
 */
open class BaseViewModel : ViewModel() {
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job = viewModelScope.launch(block = block)
}
