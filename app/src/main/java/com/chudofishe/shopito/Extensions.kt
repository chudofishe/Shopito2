package com.chudofishe.shopito

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx. lifecycle. compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun <T> Flow<T>.asStateFlow(scope: CoroutineScope, initialValue: T) = this.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = initialValue
)

@Composable
fun <T> ObserveAsEvents(flow: Flow<T>, onEvent: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}

@Composable
fun <T> ObserveAsEventsWithContext(flow: Flow<T>, onEvent: (Context, T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect {
                    onEvent(context, it)
                }
            }
        }
    }
}

fun LocalDateTime.toDayOfWeekDateTimeString(): String = this.format(DateTimeFormatter.ofPattern("EEEE, d MMMM, HH:mm"))

fun List<ShoppingListItem>.getCompletedCategories(): List<Category> {
    val resList = mutableListOf<Category>()
    val categorised = this.groupBy { it.category }

    categorised.keys.forEach {
        if (categorised[it]!!.all { item -> item.isChecked }) {
            resList.add(it)
        }
    }

    return resList
}

fun LinkedHashMap<Category, List<ShoppingListItem>>.getCompletedCategories(): List<Category> {
    val resList = mutableListOf<Category>()
    this.keys.forEach {
        if (this[it]!!.all { item -> item.isChecked }) {
            resList.add(it)
        }
    }

    return resList
}

fun List<ShoppingListItem>.areAllItemsComplete() = this.all { it.isChecked } && this.isNotEmpty()
