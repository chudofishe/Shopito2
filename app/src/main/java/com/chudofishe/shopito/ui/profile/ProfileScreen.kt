package com.chudofishe.shopito.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.chudofishe.shopito.model.UserData
import com.chudofishe.shopito.ui.composables.ShoppingListScreenContent
import com.chudofishe.shopito.ui.home.HomeViewModel
import com.chudofishe.shopito.util.toDayOfWeekDateTimeString
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateUp: () -> Unit,
    onSignOut: () -> Unit
) {

    val profileViewModel: ProfileViewModel = koinViewModel()
    val state by profileViewModel.userData.collectAsState()

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
            onSignOutClicked = onSignOut
        )
    }
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    state: UserData,
    onSignOutClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize().padding(12.dp)
    ) {
        ProfileCard(
            userData = state
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSignOutClicked
        ) {
            Text(text = "Sign out")
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
            model = userData.profilePictureUrl,
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
                text = userData.username.toString(),
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
@Preview(showBackground = true)
fun ProfileCardPreview(

) {
    ProfileCard(
        userData = UserData(
            userId = "1234",
            username = "Vitalii Vinokurov",
            email = "myemael@mail.ru",
            profilePictureUrl = null
        )
    )
}


@Composable
@Preview(showBackground = true)
fun ProfileScreenPreview() {
    ProfileScreenContent(
        state = UserData(
            userId = "1234",
            username = "Vitalii Vinokurov",
            email = "myemael@mail.ru",
            profilePictureUrl = null
    ))
}