package dev.nhonnq.app.ui.widget

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.core.net.toUri
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Modifier

@Composable
fun LinkedUrl(
    url: String,
    color: Color,
    enableOpenInBrowser: Boolean = true
) {
    val context = LocalContext.current

    // Annotated link string
    val annotatedLinkString = buildAnnotatedString {
        val tag = "URL"
        pushStringAnnotation(tag = tag, annotation = url)
        withStyle(
            style = SpanStyle(
                color = color,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(url)
        }
        pop()
    }

    /*
     *  Handle click on the url
     */
    val handleClickHyperlink = Modifier.clickable {
        annotatedLinkString.getStringAnnotations(
            "URL",
            0,
            annotatedLinkString.length
        )
            .firstOrNull()?.let { stringAnnotation ->
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    stringAnnotation.item.toUri()
                )
                context.startActivity(intent)
            }
    }

    // Display the url annotated string
    BasicText(
        text = annotatedLinkString,
        modifier = if (enableOpenInBrowser) handleClickHyperlink else Modifier
    )

}