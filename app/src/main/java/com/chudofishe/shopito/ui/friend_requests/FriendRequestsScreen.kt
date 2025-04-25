package com.chudofishe.shopito.ui.friend_requests

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.model.UserData
import com.chudofishe.shopito.ui.composables.FriendRequestListItem
import com.chudofishe.shopito.util.ObserveAsEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendRequestsScreen(
    onNavigateUp: () -> Unit
) {
    val friendRequestsViewModel: FriendRequestsViewModel = koinViewModel()
    val state by friendRequestsViewModel.friendRequests.collectAsState()
    val context = LocalContext.current

    ObserveAsEvents(friendRequestsViewModel.toastChannelFlow) {
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
                    Text(text = "Friend Requests")
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
        }
    ) { padding ->
        FriendRequestsScreenContent(
            modifier = Modifier.fillMaxSize().padding(padding),
            items = state,
            onAccept = {
                friendRequestsViewModel.acceptFriendRequest(it)
            },
            onDecline = {
                friendRequestsViewModel.declineFriendRequest(it)
            }
        )
    }
}

@Composable
fun FriendRequestsScreenContent(
    modifier: Modifier = Modifier,
    items: List<UserData> = emptyList(),
    onAccept: (UserData) -> Unit,
    onDecline: (UserData) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(items = items) {
            FriendRequestListItem(
                data = it,
                onAccept = onAccept,
                onDecline = onDecline
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FriendsScreenPreview(

) {
    FriendRequestsScreenContent(
        modifier = Modifier.fillMaxSize(),
        items = listOf(
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
        ),
        
        onAccept = {}
    ) {

    }
}