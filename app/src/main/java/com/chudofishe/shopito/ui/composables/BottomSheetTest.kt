package com.chudofishe.shopito.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.R
import com.chudofishe.shopito.ui.theme.ShopitoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenWithPersistentBottomSheetPreview() {
    ShopitoTheme {
        // Используем поверхностный (непривязанный к реальным состояниям)
        // вариант для превью
        val sheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberStandardBottomSheetState(
                initialValue = SheetValue.PartiallyExpanded,
                skipHiddenState = true
            )
        )

        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetPeekHeight = 100.dp,
            sheetDragHandle = {
                Surface(
                    modifier =
                        Modifier.padding(vertical = 12.dp),
                    color = Color.Gray,
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Box(Modifier.size(width = 32.dp, height = 4.dp))
                }
            },
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = "Category")
                        }
                        TextField(
                            value = "",
                            onValueChange = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            placeholder = { Text("Add item") },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                            ),
                            singleLine = true
                        )
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                    // Для превью показываем оба состояния
                    // В реальном компоненте используйте AnimatedVisibility с проверкой состояния
                    Column(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = "",
                            onValueChange = { },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Description") },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            ),
                            singleLine = true
                        )

                        // Категории
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            CategoryChip(
                                icon = R.drawable.icons8_babys_room_100,
                                text = "Kids"
                            )
                            CategoryChip(
                                icon = R.drawable.icons8_bread_100,
                                text = "Bakery"
                            )
                            CategoryChip(
                                icon = R.drawable.icons8_cola_100,
                                text = "Drinks"
                            )
                            CategoryChip(
                                icon = R.drawable.icons8_corgi_100,
                                text = "Pets"
                            )
                        }

                        // Кнопка "Add from favorites"
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("ADD FROM FAVORITES")
                        }
                    }
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {

                }
            },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Active list") },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }
                    }
                )
            }
        )
    }
}

// Превью для чипа категории
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CategoryChipPreview() {
    ShopitoTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CategoryChip(
                icon = R.drawable.icons8_babys_room_100,
                text = "Kids"
            )
            CategoryChip(
                icon = R.drawable.icons8_bread_100,
                text = "Bakery"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomSheetOnlyPreview() {
    ShopitoTheme {
        // Обертка для показа только BottomSheet контента
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Верхняя часть, которая всегда видна
                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Add item") },
                    trailingIcon = {
                        FloatingActionButton(
                            onClick = { },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    singleLine = true
                )

                // Расширенная часть (видна при полном раскрытии)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.size(8.dp))

                    TextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Description") },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    // Категории
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CategoryChip(
                            icon = R.drawable.icons8_babys_room_100,
                            text = "Kids"
                        )
                        CategoryChip(
                            icon = R.drawable.icons8_bread_100,
                            text = "Bakery"
                        )
                        CategoryChip(
                            icon = R.drawable.icons8_cola_100,
                            text = "Drinks"
                        )
                        CategoryChip(
                            icon = R.drawable.icons8_corgi_100,
                            text = "Pets"
                        )
                    }

                    // Кнопка "Add from favorites"
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("ADD FROM FAVORITES")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomSheetCollapsedPreview() {
    ShopitoTheme {
        // Обертка для показа только верхней части BottomSheet
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Только верхняя часть, которая всегда видна
                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Add item") },
                    trailingIcon = {
                        FloatingActionButton(
                            onClick = { },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        }
    }
}

// Вспомогательный компонент для превью
@Composable
fun CategoryChip(
    icon: Int,
    text: String,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = text,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}