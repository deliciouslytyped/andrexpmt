package com.example.comp.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

//TODO do something so this isnt so limited to the compose code

val LocalVisualLogger = compositionLocalOf<VisualLogger> {
    error("No VisualLogger provided")
}

object VisualLogger {
    val logs = mutableStateListOf<String>()
    var limit = 20

    fun ringPush(msg: String){
        var msg2 = msg
        var patt = Regex("( x([0-9]+))?\$")
        if (logs.lastOrNull()?.replace(patt, "") == msg){
            msg2 = ringPop().replace(patt) {
                " x${if (it.groups[2] != null) Integer.parseInt(it.groups[2]!!.value) + 1 else 2}"
            }
        }
        logs.add(msg2)
        if(logs.count() > limit) {
            logs.removeAt(0)
        }
    }

    //For update forcing
    fun ringPop(): String {
        val el = logs.last()
        logs.remove(el)
        return el
    }
    fun log(message: String) {
        //logs.add(message)
        ringPush(message)
    }
    fun logAppend(message: String) {
        //logs.add(message)
        var origMessage = ""
        if(!logs.isEmpty()) {
            origMessage = ringPop()
        }
        ringPush(origMessage + message) // force an update with pushpop
    }
}

@Composable
fun logEff(s: String, key: Any? = null){
    val logger = LocalVisualLogger.current
    LaunchedEffect(key) {
        logger.log(s)
    }
}

@Composable
fun VLogger(content : @Composable (() -> Unit)){
    val visualLogger = remember { VisualLogger }
    var showLog by remember { mutableStateOf(false) }
    CompositionLocalProvider(LocalVisualLogger provides visualLogger) {
        content()
    }

    //TODO yeah using the draggable is pointless if you turn it off... i probably want o toggle
    //TODO doesnt seem to help with the mouse interception issue after all
    if(showLog) {
        LazyColumn(modifier = Modifier) {
            visualLogger.logs.forEach { log ->
                item { Text(text = log) }
            }
        }
    }

    Box(modifier = Modifier) {
        Button(
            onClick = { showLog = ! showLog },
            modifier = Modifier
                .alpha(0.3f)
                .align(Alignment.TopEnd) //TODO does nothing currently because of parent size
        ) {
            Text("dbg")
        }
    }
}

@Preview
@Composable
fun VLoggerPreview() { //TODO handle lazy column
    VLogger {
        VisualLogger.limit = 5
        (0..20).plus(listOf(20,20,20)).forEach {
            logEff("$it")
        }
    }
}