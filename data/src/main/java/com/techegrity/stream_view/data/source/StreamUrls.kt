package com.techegrity.stream_view.data.source

object StreamUrls {
    const val APPLE_FMP4 =
        "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8"
    const val APPLE_HEVC =
        "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_adv_example_hevc/master.m3u8"
    const val APPLE_BIPBOP_4X3 =
        "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8"
    const val APPLE_DV_ATMOS =
        "https://devstreaming-cdn.apple.com/videos/streaming/examples/adv_dv_atmos/main.m3u8"
    const val MUX_SAMPLE =
        "https://stream.mux.com/v69RSHhFelSm4701snP22dYz2jICy4E4FUyk02rW4gxRM.m3u8"
    const val MUX_BBB = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
    const val MUX_PTS = "https://test-streams.mux.dev/pts_shift/master.m3u8"
    const val MUX_DAI =
        "https://test-streams.mux.dev/dai-discontinuity-deltatre/manifest.m3u8"
    const val AKAMAI_LIVE =
        "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
    const val AKAMAI_EIGHT =
        "https://moctobpltc-i.akamaihd.net/hls/live/571329/eight/playlist.m3u8"
    const val BITMOVIN_SINTEL =
        "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
    const val UNIFIED_TEARS_OF_STEEL =
        "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"
    const val JW_SAMPLE = "https://content.jwplatform.com/manifests/vM7nH0Kl.m3u8"
    const val NASA_TV =
        "https://ntv1.akamaized.net/hls/live/2014075/NASA-NTV1-HLS/master.m3u8"
    const val LOREM_BUNNY = "https://lorem.video/hls/bunny"
    const val LOREM_CAT = "https://lorem.video/hls/cat"
    const val LOREM_CORGI = "https://lorem.video/hls/corgi"

    /** Official NASA image used when the public NASA TV HLS variants are offline. */
    const val NASA_TV_THUMBNAIL =
        "https://images-assets.nasa.gov/image/NHQ201905310019/NHQ201905310019~medium.jpg"

    // Static poster fallbacks shown when live-frame capture fails (dead/slow feeds).
    /** Mux image service thumbnail generated from the sample stream itself. */
    const val MUX_SAMPLE_THUMBNAIL =
        "https://image.mux.com/v69RSHhFelSm4701snP22dYz2jICy4E4FUyk02rW4gxRM/thumbnail.jpg?width=640"
    /** Big Buck Bunny poster (Blender Foundation, via Wikimedia Commons). */
    const val MUX_BBB_THUMBNAIL =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Big_buck_bunny_poster_big.jpg/960px-Big_buck_bunny_poster_big.jpg"
    /** Sintel poster (Blender Foundation, via Wikimedia Commons). */
    const val BITMOVIN_SINTEL_THUMBNAIL =
        "https://upload.wikimedia.org/wikipedia/commons/8/8f/Sintel_poster.jpg"
    /** Tears of Steel still (official Blender Mango project site). */
    const val UNIFIED_TOS_THUMBNAIL =
        "https://mango.blender.org/wp-content/uploads/2013/05/01_thom_celia_bridge.jpg"
    /** JW Player CDN poster generated from the sample media id. */
    const val JW_SAMPLE_THUMBNAIL = "https://content.jwplatform.com/thumbs/vM7nH0Kl-720.jpg"
}
