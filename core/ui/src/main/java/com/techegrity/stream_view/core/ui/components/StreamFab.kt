package com.techegrity.stream_view.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techegrity.stream_view.core.ui.R
import com.techegrity.stream_view.core.ui.theme.StreamViewRadii
import com.techegrity.stream_view.core.ui.theme.StreamViewTheme
import com.techegrity.stream_view.core.ui.theme.UiConstants

@Composable
fun StreamFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(UiConstants.FAB_SIZE_DP.dp),
        shape = RoundedCornerShape(StreamViewRadii.Xl),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = UiConstants.FAB_ELEVATION_DP.dp,
            pressedElevation = UiConstants.FAB_PRESSED_ELEVATION_DP.dp,
        ),
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = stringResource(R.string.cd_add_stream),
            modifier = Modifier.size(UiConstants.FAB_ICON_SIZE_DP.dp),
        )
    }
}

@Preview
@Composable
private fun StreamFabPreview() {
    StreamViewTheme(darkTheme = true) {
        StreamFab(onClick = {})
    }
}
