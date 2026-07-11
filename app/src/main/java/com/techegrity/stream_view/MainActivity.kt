package com.techegrity.stream_view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.techegrity.stream_view.core.ui.theme.StreamViewTheme
import com.techegrity.stream_view.feature.list.StreamListScreen
import com.techegrity.stream_view.feature.list.StreamListState
import com.techegrity.stream_view.nav.StreamViewNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreamViewTheme(darkTheme = true) {
                StreamViewNavHost()
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
