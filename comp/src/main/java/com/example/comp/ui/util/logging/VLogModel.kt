package com.example.comp.ui.util.logging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import ch.qos.logback.classic.Level
import com.example.comp.util.logging.LogBuffer
import com.example.comp.util.logging.LogEntry
import com.example.comp.util.logging.format
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*

//TODO rename file
//TODO consider using existing Compose logging libraries.
//TODO convert to mvc?
//TODO mvc architecture a mess
//TODO do something so this isnt so limited to the compose code
//TODO code should be pretty strict about MVC, model stuff should be platform independent.

val LocalLogger = compositionLocalOf<LogModel> {
    error("No VisualLogger provided")
}
@Composable
fun LogAsEffect(s: String, key: Any? = null){
    val logger = KotlinLogging.logger {}
    LaunchedEffect(key) {
        logger.info { s }
    }
}

//TODO scroll with two fingers, one finger should fall through to underlying elements
//TODO incorporate the sonnet suggestions about structures for this
@Composable
fun LogViewer(content : @Composable (() -> Unit)){
    val model = remember { LogModel() }
    val controller = remember { LogController(model) }
    var showLog by remember { mutableStateOf(false) }

    DisposableEffect(model) { //TODO do we want to dispose the whole thing? What does this do exactly? Only want to dispose on game close probably.
        controller.startCollecting(CoroutineScope(SupervisorJob() + Dispatchers.Main)) //TODO is this correct at all?
        onDispose {
            controller.stopCollecting()
        }
    }

    CompositionLocalProvider(LocalLogger provides model) {
        content()
    }

    //TODO yeah using the draggable is pointless if you turn it off... i probably want o toggle
    //TODO doesnt seem to help with the mouse interception issue after all
    if(showLog) {
        LazyColumn(modifier = Modifier) {
            model.logs.forEach { logEntry ->
                item { Text(text = logEntry.format()) }
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

class LogModel() { // Do I need to make this "lifecycle aware"?
    private val buffer = LogBuffer.INSTANCE
    private val _logs = mutableStateListOf<LogEntry>()
    val logs: List<LogEntry> = _logs //TODO why can cast to State?

    init {
        _logs.add(LogEntry("test", Level.DEBUG, "test", null, System.currentTimeMillis()))
    }
    fun add(e: LogEntry) {
        buffer.append(e)
    }

    fun clear() {
        _logs.clear()
    }

    suspend fun collect() { //TODO ehh?
        buffer.entries.collect { entry -> _logs.add(entry) }
    }
}

class LogController(private val model: LogModel) { //TODO?
    private var collectionJob: Job? = null

    fun startCollecting(scope: CoroutineScope) {
        collectionJob?.cancel()
        collectionJob = scope.launch { model.collect() }
        }

    fun stopCollecting() {
        collectionJob?.cancel()
        collectionJob = null
        model.clear() //TODO do I even need to explicitly do this?
    }
}