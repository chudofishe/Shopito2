package com.chudofishe.shopito.ui.friends

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.model.UserData
import com.chudofishe.shopito.ui.composables.FriendsListItem
import com.chudofishe.shopito.util.ObserveAsEvents
import com.chudofishe.shopito.util.isScrollingUp
import com.chudofishe.shopito.util.isValidEmail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(
    onNavigateUp: () -> Unit
) {
    val friendsViewModel: FriendsViewModel = koinViewModel()
    val state by friendsViewModel.friends.collectAsState()
    val context = LocalContext.current

    var showAddFriendDialog by remember {
        mutableStateOf(false)
    }

    val listState = rememberLazyListState()

    ObserveAsEvents(friendsViewModel.toastChannelFlow) {
        Toast.makeText(
            context,
            it,
            Toast.LENGTH_LONG
        ).show()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Friends")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = listState.isScrollingUp().value,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth } // выезд справа
                ) + fadeIn(),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth } // уезжает вправо
                ) + fadeOut()
            )  {
                FloatingActionButton(
                    onClick = {
                        showAddFriendDialog = true
                    }
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Add friend")
                }
            }
        }
    ) { padding ->
        FriendsScreenContent(
            modifier = Modifier.padding(padding),
            items = state,
            listState = listState,
            onRemoveFriend = {
                friendsViewModel.removeFriend(it)
            }
        )
        if (showAddFriendDialog) {
            Box {
                AddFriendDialog(
                    onDismissRequest = {
                        showAddFriendDialog = false
                    },
                    onConfirmClicked = {
                        showAddFriendDialog = false
                        friendsViewModel.sendFriendRequest(it)
                    }
                )
            }
        }
    }
}

@Composable
fun FriendsScreenContent(
    modifier: Modifier = Modifier,
    items: List<UserData> = emptyList(),
    listState: LazyListState,
    onRemoveFriend: (UserData) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(12.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = items) {
            FriendsListItem(
                data = it,
                onRemoveFriend = onRemoveFriend
            )
        }
    }
}

@Composable
fun AddFriendDialog(
    onDismissRequest: () -> Unit,
    onConfirmClicked: (String) -> Unit
) {
    var emailInput by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {
            emailInput = ""
            emailError = false
            onDismissRequest()
        },
        confirmButton = {
            Button(
                onClick = {
                    if (emailInput.isValidEmail()) {
                        emailError = false
                        onConfirmClicked(emailInput)
                    } else {
                        emailError = true
                    }
                }
            ) {
                Text("Add")
            }
        },
        title = {
            Text(text = "Friend email")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = {
                        emailInput = it
                        if (emailError) {
                            emailError = !emailInput.isValidEmail()
                        }
                    },
                    label = { Text("Email") },
                    isError = emailError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                if (emailError) {
                    Text(
                        text = "Please enter a valid email",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    )
}

val testList = listOf(
    UserData(
        userId = "uid_001",
        email = "alice@example.com",
        photoUrl = "https://example.com/photos/alice.jpg",
        name = "Alice Johnson"
    ),
    UserData(
        userId = "uid_002",
        email = "bob@example.com",
        photoUrl = "https://example.com/photos/bob.jpg",
        name = "Bob Smith"
    ),
    UserData(
        userId = "uid_003",
        email = "charlie@example.com",
        photoUrl = "https://example.com/photos/charlie.jpg",
        name = "Charlie Green"
    ),
    UserData(
        userId = "uid_004",
        email = "diana@example.com",
        photoUrl = "https://example.com/photos/diana.jpg",
        name = "Diana Prince"
    ),
    UserData(
        userId = "uid_005",
        email = "eve@example.com",
        photoUrl = "https://example.com/photos/eve.jpg",
        name = "Eve Adams"
    ),
    UserData(
        userId = "uid_001",
        email = "alice@example.com",
        photoUrl = "https://example.com/photos/alice.jpg",
        name = "Alice Johnson"
    ),
    UserData(
        userId = "uid_002",
        email = "bob@example.com",
        photoUrl = "https://example.com/photos/bob.jpg",
        name = "Bob Smith"
    ),
    UserData(
        userId = "uid_003",
        email = "charlie@example.com",
        photoUrl = "https://example.com/photos/charlie.jpg",
        name = "Charlie Green"
    ),
    UserData(
        userId = "uid_004",
        email = "diana@example.com",
        photoUrl = "https://example.com/photos/diana.jpg",
        name = "Diana Prince"
    ),
    UserData(
        userId = "uid_005",
        email = "eve@example.com",
        photoUrl = "https://example.com/photos/eve.jpg",
        name = "Eve Adams"
    ),
    UserData(
        userId = "uid_001",
        email = "alice@example.com",
        photoUrl = "https://example.com/photos/alice.jpg",
        name = "Alice Johnson"
    ),
    UserData(
        userId = "uid_002",
        email = "bob@example.com",
        photoUrl = "https://example.com/photos/bob.jpg",
        name = "Bob Smith"
    ),
    UserData(
        userId = "uid_003",
        email = "charlie@example.com",
        photoUrl = "https://example.com/photos/charlie.jpg",
        name = "Charlie Green"
    ),
    UserData(
        userId = "uid_004",
        email = "diana@example.com",
        photoUrl = "https://example.com/photos/diana.jpg",
        name = "Diana Prince"
    ),
    UserData(
        userId = "uid_005",
        email = "eve@example.com",
        photoUrl = "https://example.com/photos/eve.jpg",
        name = "Eve Adams"
    )
)

@Composable
@Preview(showBackground = true)
fun FriendsScreenPreview(

) {
    FriendsScreenContent(
        items = testList,
        modifier = Modifier,
        listState = rememberLazyListState(),
        onRemoveFriend = {}
    )
}