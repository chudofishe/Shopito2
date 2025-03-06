package com.chudofishe.shopito.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onDeleteList: () -> Unit,
    onCompleteList: () -> Unit,
    title: String,
) {

    var isDropDownExpanded by remember {
        mutableStateOf(false)
    }

    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
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
                            onClick = onDeleteList)
                        DropdownMenuItem(
                            leadingIcon = { Icon(imageVector = Icons.Default.Check, contentDescription = "Complete list") },
                            text = { Text(text = "Complete list") },
                            onClick = onCompleteList)
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}