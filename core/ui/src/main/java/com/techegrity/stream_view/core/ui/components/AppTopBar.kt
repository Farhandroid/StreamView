package com.techegrity.stream_view.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techegrity.stream_view.core.ui.theme.StreamViewRadii
import com.techegrity.stream_view.core.ui.theme.StreamViewSpacing
import com.techegrity.stream_view.core.ui.theme.StreamViewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    searchExpanded: Boolean = false,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSearchToggle: (() -> Unit)? = null,
    onSearchClear: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val mutedStroke = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    TopAppBar(
        modifier = modifier
            .border(width = 1.dp, color = mutedStroke),
        title = {
            if (searchExpanded && onSearchToggle != null) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = StreamViewSpacing.MinTouchTarget),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Search streams",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = onSearchClear) {
                                Icon(
                                    imageVector = Icons.Outlined.Clear,
                                    contentDescription = "Clear search",
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { focusManager.clearFocus() },
                    ),
                    shape = RoundedCornerShape(StreamViewRadii.Pill),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium,
                )
            } else {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        },
        actions = {
            if (onSearchToggle != null) {
                IconButton(onClick = onSearchToggle) {
                    Icon(
                        imageVector = if (searchExpanded) {
                            Icons.Outlined.Clear
                        } else {
                            Icons.Outlined.Search
                        },
                        contentDescription = if (searchExpanded) "Close search" else "Search",
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            // Level 2 surface elevation
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@Preview
@Composable
private fun AppTopBarPreview() {
    StreamViewTheme(darkTheme = true) {
        AppTopBar(
            title = "StreamView",
            onSearchToggle = {},
        )
    }
}

@Preview
@Composable
private fun AppTopBarSearchPreview() {
    StreamViewTheme(darkTheme = true) {
        AppTopBar(
            title = "StreamView",
            searchExpanded = true,
            searchQuery = "Lobby",
            onSearchToggle = {},
            onSearchQueryChange = {},
            onSearchClear = {},
        )
    }
}
