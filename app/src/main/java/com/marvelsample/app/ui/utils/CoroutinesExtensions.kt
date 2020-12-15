package com.marvelsample.app.ui.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun launchUI(f: suspend (coroutineScope : CoroutineScope) -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        f.invoke(this)
    }
}