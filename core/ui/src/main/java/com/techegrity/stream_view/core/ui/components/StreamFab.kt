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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techegrity.stream_view.core.ui.theme.StreamViewRadii
import com.techegrity.stream_view.core.ui.theme.StreamViewTheme

@Composable
fun StreamFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        shape = RoundedCornerShape(StreamViewRadii.Xl),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp,
        ),
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "Add stream",
            modifier = Modifier.size(28.dp),
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
