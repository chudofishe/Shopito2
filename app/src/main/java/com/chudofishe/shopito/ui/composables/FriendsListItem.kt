package com.chudofishe.shopito.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chudofishe.shopito.model.UserData

@Composable
fun UserDataListItem(
    modifier: Modifier = Modifier,
    data: UserData,
    endContent: @Composable () -> Unit = {},
) {
    val borderWidth = 4.dp


    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = data.photoUrl,
            contentDescription = "Prifle image",
            modifier = Modifier
                .size(72.dp)
                .border(
                    BorderStroke(borderWidth, MaterialTheme.colorScheme.primary),
                    CircleShape
                )
                .padding(borderWidth)
                .clip(CircleShape)
        )
        Text(
            text = data.name.toString(),
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        endContent()
    }
}

@Composable
fun FriendsListItem(
    modifier: Modifier = Modifier,
    data: UserData,
    onRemoveFriend: (UserData) -> Unit
) {
    var isDropDownExpanded by remember {
        mutableStateOf(false)
    }
    
    UserDataListItem(
        modifier = modifier,
        data = data
    ) {
        IconButton(
            onClick = {
                isDropDownExpanded = !isDropDownExpanded
            }
        ) {
            Box {
                Icon(Icons.Default.MoreVert, contentDescription = "Options")
                DropdownMenu(
                    expanded = isDropDownExpanded,
                    onDismissRequest = {
                        isDropDownExpanded = false
                    }) {
                    DropdownMenuItem(
                        text = { Text(text = "Remove") },
                        onClick = {
                            isDropDownExpanded = false
                            onRemoveFriend(data)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendRequestListItem(
    modifier: Modifier = Modifier,
    data: UserData,
    onDecline: (UserData) -> Unit,
    onAccept: (UserData) -> Unit
) {

    val iconSize = 32.dp

    UserDataListItem(
        modifier = modifier,
        data = data
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = {
                    onAccept(data)
                }
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.Check, contentDescription = "Accept",
                    tint = Color.Green)
            }
            IconButton(
                onClick = {
                    onDecline(data)
                }
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.Clear, contentDescription = "Decline",
                    tint = Color.Red)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FriendsListItemPreview(

) {
    FriendsListItem(
        modifier = Modifier.padding(12.dp),
        data = UserData(
            userId = "uid_001",
            email = "alice@example.com",
            photoUrl = "https://example.com/photos/alice.jpg",
            name = "Alice Johnson"
        ),
        onRemoveFriend = {}
    )
}

@Composable
@Preview(showBackground = true)
fun FriendsRequestItemPreview(

) {
    FriendRequestListItem(
        modifier = Modifier.padding(12.dp),
        data = UserData(
            userId = "uid_001",
            email = "alice@example.com",
            photoUrl = "https://example.com/photos/alice.jpg",
            name = "Alice Johnson"
        ),
        onDecline = {},
        onAccept = {},
    )
}