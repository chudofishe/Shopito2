package com.chudofishe.shopito.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onDeleteList: () -> Unit,
    onCompleteList: () -> Unit,
    onMenuClicked: () -> Unit,
    title: String,
) {
    var isDropDownExpanded by remember {
        mutableStateOf(false)
    }

    // Функция-обертка, которая закрывает меню и вызывает переданный обработчик
    val wrapWithMenuClose: (() -> Unit) -> () -> Unit = { action ->
        {
            isDropDownExpanded = false
            action()
        }
    }

    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = { // Добавляем значок меню для открытия шторки
            IconButton(onClick = onMenuClicked) {
                Icon(
                    imageVector = Icons.Default.Menu, // Используйте подходящую иконку меню
                    contentDescription = "Open drawer"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                isDropDownExpanded = true
            }) {
                Box {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options")
                    DropdownMenu(
                        expanded = isDropDownExpanded,
                        onDismissRequest = {
                            isDropDownExpanded = false
                        }) {
                        DropdownMenuItem(
                            leadingIcon = { Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete list") },
                            text = { Text(text = "Delete list") },
                            onClick = wrapWithMenuClose(onDeleteList)
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(imageVector = Icons.Default.Check, contentDescription = "Complete list") },
                            text = { Text(text = "Complete list") },
                            onClick = wrapWithMenuClose(onCompleteList)
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}