package dev.nhonnq.app.ui.users.details

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nhonnq.app.util.preview.PreviewContainer

/**
 * Displays a user's follow information, including an icon, the number of followers (or "100+"), and a display text.
 *
 * @param numberOfFollow The number of followers the user has. If greater than 100, "100+" will be displayed.
 * @param displayText The text to display below the follower count (e.g., "Followers", "Following").
 * @param modifier Modifiers to be applied to the layout.
 *
 * Example Usage:
 * ```
 * UserFollow(numberOfFollow = 50, displayText = "Followers")
 * UserFollow(numberOfFollow = 150, displayText = "Following")
 * ```
 */
@Composable
fun UserFollow(
    numberOfFollow: Int,
    displayText: String,
    modifier: Modifier = Modifier
) {

    fun displayFollow(count: Int): String {
        return when {
            count > 100 -> "100+"
            else -> count.toString()
        }
    }

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.size(30.dp).clip(CircleShape).background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Group,
                contentDescription = "Followers",
                tint = Color.Black,
                modifier = Modifier.size(22.dp),
            )
        }

        Text(
            text = displayFollow(numberOfFollow),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Light)
        )
        Text(
            text = displayText,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Light)
        )
    }
}

@Preview("Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UserFollowPreview() {
    PreviewContainer {
        Surface {
            UserFollow(
                numberOfFollow = 101,
                displayText = "Follower"
            )
        }
    }
}