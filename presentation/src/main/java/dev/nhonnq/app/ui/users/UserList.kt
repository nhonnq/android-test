package dev.nhonnq.app.ui.users

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import dev.nhonnq.app.R
import dev.nhonnq.app.util.preview.PreviewContainer
import dev.nhonnq.data.entities.UserData

@Composable
fun UserList(
    users: LazyPagingItems<UserData>,
    isLoading: Boolean,
    onUserClick: (loginUserName: String) -> Unit
) {

    // Check users is empty state
    if (users.itemCount == 0) {
        Column(Modifier.padding(8.dp, 16.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            if (isLoading) {
                // Displayed fetching data...
                Text(stringResource( R.string.fetching_data))
            } else {
                // Displayed empty state
                Icon(
                    imageVector = Icons.Filled.SentimentDissatisfied,
                    contentDescription = "no data",
                    tint = Color.LightGray,
                    modifier = Modifier.size(48.dp),
                )
                Text(stringResource(R.string.no_data_found))
            }

        }
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(users.itemCount) { index ->
            val user = users[index]
            user?.let {
                UserProfile(
                    name = it.login,
                    avatarUrl = it.avatarUrl,
                    htmlUrl = it.htmlUrl,
                    onUserClick = onUserClick
                )
            }
        }
    }
}

@Preview("Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SeparatorAndUserItemPreview() {
    PreviewContainer {
        Surface {
            val users= arrayOf(
                UserData(
                    login = "jvantuyl",
                    avatarUrl = "https://avatars.githubusercontent.com/u/101?v=4",
                    htmlUrl = "https://github.com/jvantuyl"
                ),
                UserData(
                    login = "BrianTheCoder",
                    avatarUrl = "https://avatars.githubusercontent.com/u/102?v=4",
                    htmlUrl = ""
                ),
                UserData(
                    login = "freeformz",
                    avatarUrl = "https://i.stack.imgur.com/lDFzt.jpg",
                    htmlUrl = "https://avatars.githubusercontent.com/u/103?v=4"
                )
            )
            LazyColumn {
                items(users.size) { index ->
                    val user = users[index]
                    UserProfile(
                        name = user.login,
                        avatarUrl = user.avatarUrl,
                        htmlUrl = user.htmlUrl,
                    )
                }
            }
        }
    }
}
