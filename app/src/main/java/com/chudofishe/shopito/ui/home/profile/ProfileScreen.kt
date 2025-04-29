package com.chudofishe.shopito.ui.home.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
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
    RECENT(R.drawable.icons8_task_96, "Recent lists")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateUp: () -> Unit,
    onSignOut: () -> Unit,
    onSignIn: () -> Unit,
    onNavigateTo: (ProfileNavigationRoute?) -> Unit
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
            state = state,
            onSignIn = onSignIn,
            onListItemsClicked = {
                if (it == ProfileItem.SIGN_OUT) {
                    onSignOut()
                } else {
                    onNavigateTo(when(it) {
                        ProfileItem.FRIENDS -> ProfileNavigationRoute.FriendsRoute
                        ProfileItem.FRIEND_REQUESTS -> ProfileNavigationRoute.FriendRequestsRoute
                        ProfileItem.RECENT -> ProfileNavigationRoute.RecentListsRoute
                        else -> null
                    })
                }
            }
        )
    }
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    state: ProfileScreenState,
    onSignIn: () -> Unit = {},
    onListItemsClicked: (ProfileItem) -> Unit
) {
    val settingsItems = remember(state.isAuthenticated) {
        if (state.isAuthenticated) {
            ProfileItem.entries.toTypedArray()
        } else {
            arrayOf(
                ProfileItem.RECENT
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (state.isAuthenticated) {
            ProfileCard(
                userData = state.userData
            )
        } else {
            UnauthenticatedProfileCard(
                onSignInClick = onSignIn
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(settingsItems) {
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

    Card(

    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = userData.photoUrl,
                contentDescription = "Profile image",
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
}

@Composable
fun UnauthenticatedProfileCard(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit
) {
    val borderWidth = 4.dp
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

    Card(
        modifier = modifier.clickable {
            onSignInClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка профиля с заглушкой
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .border(
                        BorderStroke(borderWidth, rainbowColorsBrush),
                        CircleShape
                    )
                    .padding(borderWidth)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile icon",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "Log in to use all features",
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.padding(end = 12.dp),
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Sign in"
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
    Card {
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
fun UnAuthCardPreview(

) {
    UnauthenticatedProfileCard {
        
    }
}


@Composable
@Preview(showBackground = true)
fun ProfileScreenPreview() {
    ProfileScreenContent(
        state = ProfileScreenState(
            UserData(
            userId = "1234",
            name = "Vitalii Vinokurov",
            email = "myemael@mail.ru",
            photoUrl = null,
                ),

            isAuthenticated = true),
        onListItemsClicked = {

        }
    )
}