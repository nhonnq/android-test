package dev.nhonnq.app.ui.navigationbar

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dev.nhonnq.app.R
import dev.nhonnq.app.ui.theme.AppColor
import dev.nhonnq.app.ui.widget.TopBar
import dev.nhonnq.app.util.preview.PreviewContainer

@Composable
fun NavigationBarScreen(
    darkMode: Boolean,
    onThemeUpdated: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                stringResource(R.string.app_name),
                darkMode,
                onThemeUpdated = onThemeUpdated
            )

        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize(1f)
                .padding(paddingValues)
        ) {
            content()
        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavigationBarScreenPreview() = PreviewContainer {
    val darkTheme = isSystemInDarkTheme()

    NavigationBarScreen(
        darkMode = darkTheme,
        onThemeUpdated = { },
        content = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(AppColor.GrayB3)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    text = "Page Content"
                )
            }
        }
    )
}