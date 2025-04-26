package dev.nhonnq.app.ui.users

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.nhonnq.app.ui.theme.colors
import dev.nhonnq.app.ui.widget.LinkedUrl
import dev.nhonnq.app.util.preview.PreviewContainer
import kotlinx.coroutines.delay

@Composable
fun UserProfile(
    name: String,
    avatarUrl: String?,
    htmlUrl: String? = null,
    location: String? = null,
    itemVisible: Boolean? = true,
    onUserClick: (loginUserName: String) -> Unit = {},
) {
    var scale by remember { mutableFloatStateOf(0.70f) }
    val context = LocalContext.current

    LaunchedEffect(itemVisible) {
        if (itemVisible == true) {
            delay(100)
            scale = 1f
        } else {
            scale = 0.70f
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(1f).padding(8.dp).clickable {
            onUserClick(name)
        },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.background
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar
            Box(
                modifier = Modifier.width(72.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(avatarUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray) // fallback bg while loading
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User's information
            Column {
                Text(
                    text = name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Divider(
                    Modifier.padding(0.dp, 8.dp)
                )
                htmlUrl?.let {
                    LinkedUrl(
                        url = it,
                        color = Color(0xFF0A66C2),
                        false
                    )
                }
                location?.let { it ->
                    Row(Modifier.padding(0.dp, 8.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "Location",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = it.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Light)
                        )
                    }
                }
            }
        }
    }
}

@Preview("Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UserProfilePreview() {
    PreviewContainer {
        Surface {
            UserProfile(
                name = "jvantuyl",
                avatarUrl = "https://avatars.githubusercontent.com/u/101?v=4",
                htmlUrl = "https://github.com/jvantuyl",
                location = "Viet Nam",
                itemVisible = false
            )
        }
    }
}