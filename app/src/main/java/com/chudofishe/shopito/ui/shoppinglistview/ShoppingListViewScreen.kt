package com.chudofishe.shopito.ui.shoppinglistview

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.chudofishe.shopito.toDayOfWeekDateTimeString
import com.chudofishe.shopito.ui.composables.ShoppingListScreenContent
import com.chudofishe.shopito.ui.shoppinglist.ShoppingListScreenViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListViewScreen(
    onNavigateUp: () -> Unit
) {
    val viewmodel: ShoppingListScreenViewViewModel = koinViewModel()
    val selectedList by viewmodel.selectedList.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = selectedList.timestamp.toDayOfWeekDateTimeString())
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onNavigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "done"
                        )
                    }
                }
            )
        }
    ) { padding ->
        ShoppingListScreenContent(
            modifier = Modifier.padding(padding),
            list = selectedList,
            showCompleteAnimation = false,
            onCategoryCollapseStateToggled = {
                viewmodel.updateCollapsedCategory(it)
            },
        )
    }
}