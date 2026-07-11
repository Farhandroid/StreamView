package com.techegrity.stream_view.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.techegrity.stream_view.core.ui.theme.StreamViewSpacing

@Composable
fun AddStreamDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, streamUrl: String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var streamUrl by rememberSaveable { mutableStateOf("") }
    var showValidationError by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.add_stream_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(StreamViewSpacing.Md),
                modifier = Modifier.padding(top = StreamViewSpacing.Xs),
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        showValidationError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(stringResource(R.string.add_stream_name_label)) },
                )
                OutlinedTextField(
                    value = streamUrl,
                    onValueChange = {
                        streamUrl = it
                        showValidationError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(stringResource(R.string.add_stream_url_label)) },
                    placeholder = { Text(stringResource(R.string.add_stream_url_hint)) },
                )
                if (showValidationError) {
                    Text(
                        text = stringResource(R.string.add_stream_validation_error),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val trimmedName = name.trim()
                    val trimmedUrl = streamUrl.trim()
                    if (trimmedName.isBlank() || trimmedUrl.isBlank()) {
                        showValidationError = true
                    } else {
                        onConfirm(trimmedName, trimmedUrl)
                    }
                },
            ) {
                Text(stringResource(R.string.add_stream_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.add_stream_cancel))
            }
        },
    )
}
