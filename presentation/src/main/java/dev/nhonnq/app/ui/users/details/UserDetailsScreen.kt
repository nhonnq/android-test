package dev.nhonnq.app.ui.users.details

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dev.nhonnq.app.R
import dev.nhonnq.app.ui.users.UserProfile
import dev.nhonnq.app.ui.users.details.state.UserDetailsState
import dev.nhonnq.app.ui.users.details.viewmodel.UserDetailsViewModel
import dev.nhonnq.app.ui.widget.LinkedUrl
import dev.nhonnq.app.ui.widget.ShimmerBox
import dev.nhonnq.app.util.preview.PreviewContainer
import dev.nhonnq.data.entities.UserData

@Composable
fun UserDetailsScreen(
    mainNavController: NavHostController,
    viewModel: UserDetailsViewModel,
) {
    val state by viewModel.uiState.collectAsState()
    UserDetailsScreenContainer(state, mainNavController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreenContainer(
    state: UserDetailsState,
    appNavController: NavHostController? = null
) {
    val user = state.userData

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.user_details)) },
                navigationIcon = {
                    IconButton(onClick = { appNavController?.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box {
            Column(
                Modifier
                    .fillMaxSize(1f)
                    .padding(paddingValues)
            ) {
                // Display User Profile information
                UserProfile(
                    name = user?.login ?: "",
                    avatarUrl = user?.avatarUrl,
                    location = user?.location,
                )
                Spacer(modifier = Modifier.width(16.dp))

                // Display follower and following
                Row (
                    Modifier.fillMaxWidth(1f).padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    UserFollow(
                        numberOfFollow = user?.followers ?: 0,
                        displayText = stringResource(R.string.follower),
                        modifier = Modifier.weight(1f).padding(8.dp)
                    )
                    UserFollow(
                        numberOfFollow = user?.following ?: 0,
                        displayText = stringResource(R.string.following),
                        modifier = Modifier.weight(1f).padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Check and show Blog
                user?.blog?.let {
                    Column (Modifier.padding(8.dp)) {
                        Text(stringResource(R.string.blog), fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
                        LinkedUrl(
                            url = it,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Show shimmer while API fetching user details data
        if (state.isLoading == true) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }

    }
}

@Preview("Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UserDetailsScreenPreview() {
    PreviewContainer {
        Surface {
            UserDetailsScreenContainer(
                state = UserDetailsState(
                    userData = UserData(
                        login = "",
                        avatarUrl = "https://avatars.githubusercontent.com/u/101?v=4",
                        htmlUrl = "https://github.com/jvantuyl",
                        location = "Viet Nam",
                        blog = "https://github.com/jvantuyl"
                    )
                )
            )
        }
    }
}