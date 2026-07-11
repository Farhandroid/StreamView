package com.techegrity.stream_view.data.source

object StreamCatalogConstants {
    const val NETWORK_DELAY_MS = 400L
    const val LOOKUP_DELAY_MS = 150L

    const val THUMBNAIL_WIDTH = 800
    const val THUMBNAIL_HEIGHT = 450

    const val ID_APPLE_FMP4 = "apple-fmp4"
    const val ID_APPLE_HEVC = "apple-hevc"
    const val ID_APPLE_BIPBOP_4X3 = "apple-bipbop-4x3"
    const val ID_APPLE_DV_ATMOS = "apple-dv-atmos"
    const val ID_MUX_SAMPLE = "mux-sample"
    const val ID_MUX_BBB = "mux-bbb"
    const val ID_MUX_PTS = "mux-pts"
    const val ID_MUX_DAI = "mux-dai"
    const val ID_AKAMAI_LIVE = "akamai-live"
    const val ID_AKAMAI_EIGHT = "akamai-eight"
    const val ID_BITMOVIN_SINTEL = "bitmovin-sintel"
    const val ID_UNIFIED_TOS = "unified-tos"
    const val ID_JW_SAMPLE = "jw-sample"
    const val ID_NASA_TV = "nasa-tv"
    const val ID_LOREM_BUNNY = "lorem-bunny"
    const val ID_LOREM_CAT = "lorem-cat"
    const val ID_LOREM_CORGI = "lorem-corgi"

    const val PROVIDER_APPLE = "Apple"
    const val PROVIDER_MUX = "Mux"
    const val PROVIDER_AKAMAI = "Akamai"
    const val PROVIDER_BITMOVIN = "Bitmovin"
    const val PROVIDER_UNIFIED = "Unified"
    const val PROVIDER_JW = "JW Player"
    const val PROVIDER_NASA = "NASA"
    const val PROVIDER_LOREM = "lorem.video"
    const val PROVIDER_USER = "Custom"

    const val USER_STREAM_ID_PREFIX = "user-"
    const val VALIDATION_NAME_REQUIRED = "Name is required"
    const val VALIDATION_URL_REQUIRED = "Stream URL is required"

    const val META_USER_ADDED = "User added • HLS"

    const val NAME_APPLE_FMP4 = "Apple fMP4 BipBop"
    const val NAME_APPLE_HEVC = "Apple HEVC BipBop"
    const val NAME_APPLE_BIPBOP_4X3 = "Apple BipBop 4x3"
    const val NAME_APPLE_DV_ATMOS = "Apple Dolby Vision Atmos"
    const val NAME_MUX_SAMPLE = "Mux Sample Stream"
    const val NAME_MUX_BBB = "Mux Big Buck Bunny"
    const val NAME_MUX_PTS = "Mux PTS-Shift Test"
    const val NAME_MUX_DAI = "Mux DAI Discontinuity"
    const val NAME_AKAMAI_LIVE = "Akamai Live Simulator"
    const val NAME_AKAMAI_EIGHT = "Akamai Live Eight"
    const val NAME_BITMOVIN_SINTEL = "Bitmovin Sintel"
    const val NAME_UNIFIED_TOS = "Unified Tears of Steel"
    const val NAME_JW_SAMPLE = "JW Player Sample"
    const val NAME_NASA_TV = "NASA TV"
    const val NAME_LOREM_BUNNY = "Lorem Bunny"
    const val NAME_LOREM_CAT = "Lorem Cat"
    const val NAME_LOREM_CORGI = "Lorem Corgi"

    const val META_VOD_ABR_FMP4 = "VOD • ABR • fMP4"
    const val META_VOD_HEVC = "VOD • HEVC • advanced"
    const val META_VOD_ABR = "VOD • ABR"
    const val META_VOD_DV_ATMOS = "VOD • Dolby Vision • Atmos"
    const val META_VOD_HLS = "VOD • HLS"
    const val META_VOD_ABR_1080 = "VOD • ABR • 1080p+"
    const val META_VOD_TIMING = "VOD • timing edge case"
    const val META_VOD_ADS = "VOD • ad insertion test"
    const val META_LIVE_ROLLING = "Live • rolling window"
    const val META_LIVE_HLS = "Live • HLS"
    const val META_LIVE_PUBLIC = "Live • public feed"
    const val META_PLACEHOLDER = "Placeholder • HLS"

    fun thumbnailUrl(seed: String): String =
        "https://picsum.photos/seed/$seed/$THUMBNAIL_WIDTH/$THUMBNAIL_HEIGHT"
}
