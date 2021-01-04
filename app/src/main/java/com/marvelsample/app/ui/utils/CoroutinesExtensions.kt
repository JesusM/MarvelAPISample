package com.marvelsample.app.ui.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun launchUI(f: suspend (coroutineScope : CoroutineScope) -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        f.invoke(this)
    }
}