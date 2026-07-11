package com.techegrity.stream_view.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Rounded strategy: 4 / 8 / 16dp for small / medium / large components.
 * Full radius reserved for search fields and pill-shaped controls.
 */
val StreamViewShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

object StreamViewRadii {
    val Sm = 4.dp
    val Md = 8.dp
    val Default = 8.dp
    val Lg = 12.dp
    val Xl = 16.dp
    val Xxl = 24.dp
    val Full = 9999.dp

    val Card = Xl
    val Button = Md
    val Input = Md
    val Pill = Full
    val Thumbnail = Xl
}
