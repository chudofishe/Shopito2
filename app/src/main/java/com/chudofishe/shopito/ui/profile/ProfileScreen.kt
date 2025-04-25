package com.chudofishe.shopito.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chudofishe.shopito.R
import com.chudofishe.shopito.model.UserData
import com.chudofishe.shopito.navigation.ProfileNavigationRoute
import org.koin.androidx.compose.koinViewModel

enum class ProfileItem(val iconsRes: Int, val text: String) {
    FRIENDS(R.drawable.icons8_cola_100, "Friends"),
    FRIEND_REQUESTS(R.drawable.icons8_bread_100, "Friend requests"),
    SIGN_OUT(R.drawable.icons8_exterior_100, "Sign out"),
    //
    MESSAGES(R.drawable.icons8_cola_100, "Messages"),
    NOTIFICATIONS(R.drawable.icons8_cola_100, "Notifications"),
    SETTINGS(R.drawable.icons8_cola_100, "Settings"),
    PROFILE(R.drawable.icons8_cola_100, "Profile"),
    LOG_OUT(R.drawable.icons8_cola_100, "Log out"),
    ABOUT(R.drawable.icons8_cola_100, "About"),
    HELP(R.drawable.icons8_cola_100, "Help"),
    PRIVACY(R.drawable.icons8_cola_100, "Privacy"),
    TERMS(R.drawable.icons8_cola_100, "Terms and Conditions"),
    ACCOUNT(R.drawable.icons8_cola_100, "Account"),
    BLOCKED_USERS(R.drawable.icons8_cola_100, "Blocked users"),
    LANGUAGE(R.drawable.icons8_cola_100, "Language"),
    FEEDBACK(R.drawable.icons8_cola_100, "Feedback"),
    APPEARANCE(R.drawable.icons8_cola_100, "Appearance"),
    SECURITY(R.drawable.icons8_cola_100, "Security"),
    ACTIVITY(R.drawable.icons8_cola_100, "Activity"),
    SAVED(R.drawable.icons8_cola_100, "Saved"),
    SUBSCRIPTIONS(R.drawable.icons8_cola_100, "Subscriptions"),
    PREFERENCES(R.drawable.icons8_cola_100, "Preferences")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateUp: () -> Unit,
    onSignOut: () -> Unit,
    onNavigateTo: (ProfileNavigationRoute) -> Unit
) {

    val profileViewModel: ProfileViewModel = koinViewModel()
    val state by profileViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Profile")
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
        ProfileScreenContent(
            modifier = Modifier.padding(padding),
            state = state.userData,
            onListItemsClicked = {
                if (it == ProfileItem.SIGN_OUT) {
                    onSignOut()
                } else {
                    onNavigateTo(when(it) {
                        ProfileItem.FRIENDS -> ProfileNavigationRoute.FriendsRoute
                        ProfileItem.FRIEND_REQUESTS -> ProfileNavigationRoute.FriendRequestsRoute
                        else -> {
                            ProfileNavigationRoute.FriendRequestsRoute
                        }
                    })
                }
            }
        )
    }
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    state: UserData,
    onListItemsClicked: (ProfileItem) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        ProfileCard(
            userData = state
        )
        Spacer(modifier = Modifier.padding(8.dp))
        LazyColumn(
        ) {
            items(ProfileItem.entries.toTypedArray()) {
                ProfileListItem(
                    modifier = Modifier.padding(12.dp),
                    item = it,
                    onClick = onListItemsClicked
                )
            }
        }
    }
}

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    userData: UserData,
) {
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }
    val borderWidth = 4.dp

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = userData.photoUrl,
            contentDescription = "Prifle image",
            modifier = Modifier
                .size(72.dp)
                .border(
                    BorderStroke(borderWidth, rainbowColorsBrush),
                    CircleShape
                )
                .padding(borderWidth)
                .clip(CircleShape)
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = userData.name.toString(),
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = userData.email.toString(),
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ProfileListItem(
    modifier: Modifier = Modifier,
    item: ProfileItem,
    onClick: (ProfileItem) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(true) {
                onClick(item)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = item.iconsRes),
            contentDescription = "Category image"
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = item.text,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ProfileListItemPreview(

) {
    ProfileListItem(
        modifier = Modifier.padding(12.dp),
        item = ProfileItem.FRIENDS,
        onClick = {}
    )
}

@Composable
@Preview(showBackground = true)
fun ProfileCardPreview(

) {
    ProfileCard(
        userData = UserData(
            userId = "1234",
            name = "Vitalii Vinokurov",
            email = "myemael@mail.ru",
            photoUrl = null
        )
    )
}


@Composable
@Preview(showBackground = true)
fun ProfileScreenPreview() {
    ProfileScreenContent(
        state = UserData(
            userId = "1234",
            name = "Vitalii Vinokurov",
            email = "myemael@mail.ru",
            photoUrl = null,
        ),
        onListItemsClicked = {

        }
    )
}