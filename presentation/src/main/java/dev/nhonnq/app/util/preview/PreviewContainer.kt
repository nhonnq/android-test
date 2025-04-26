package dev.nhonnq.app.util.preview

import androidx.compose.runtime.Composable
import dev.nhonnq.app.ui.theme.AppTheme

@Composable
fun PreviewContainer(
    content: @Composable () -> Unit
) {
    AppTheme {
        content()
    }
}