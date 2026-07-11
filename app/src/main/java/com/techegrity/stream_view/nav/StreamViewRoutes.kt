package com.techegrity.stream_view.nav

object StreamViewRoutes {
    const val LIST = "list"
    const val PLAYER = "player/{streamId}"

    fun player(streamId: String): String = "player/$streamId"
}
