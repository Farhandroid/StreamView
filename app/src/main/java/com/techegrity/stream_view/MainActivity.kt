package com.techegrity.stream_view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.techegrity.stream_view.core.ui.theme.StreamViewTheme
import com.techegrity.stream_view.feature.list.StreamListRoute
import com.techegrity.stream_view.feature.list.StreamListScreen
import com.techegrity.stream_view.feature.list.StreamListState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreamViewTheme(darkTheme = true) {
                StreamListRoute(
                    onNavigateToPlayer = { streamId ->
                        Toast.makeText(
                            this,
                            getString(R.string.toast_player_coming_next, streamId),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131313)
@Composable
private fun AppPreview() {
    StreamViewTheme(darkTheme = true) {
        StreamListScreen(
            state = StreamListState(),
            onIntent = {},
        )
    }
}
