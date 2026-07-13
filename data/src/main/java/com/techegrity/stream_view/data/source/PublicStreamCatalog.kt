package com.techegrity.stream_view.data.source

import com.techegrity.stream_view.domain.model.Stream

/**
 * Seeded catalog from docs/StreamView_Project_Plan.md §7 (unique HTTPS feeds).
 * Demo order: Mux BBB → PTS → DAI → Sample, then Apple, then the rest.
 */
object PublicStreamCatalog {

    val streams: List<Stream> = listOf(
        // --- Mux (demo lead-ins) ---
        Stream(
            id = StreamCatalogConstants.ID_MUX_BBB,
            name = StreamCatalogConstants.NAME_MUX_BBB,
            thumbnailUrl = StreamUrls.MUX_BBB_THUMBNAIL,
            streamUrl = StreamUrls.MUX_BBB,
            provider = StreamCatalogConstants.PROVIDER_MUX,
            metadata = StreamCatalogConstants.META_VOD_ABR_1080,
        ),
        Stream(
            id = StreamCatalogConstants.ID_MUX_PTS,
            name = StreamCatalogConstants.NAME_MUX_PTS,
            thumbnailUrl = "",
            streamUrl = StreamUrls.MUX_PTS,
            provider = StreamCatalogConstants.PROVIDER_MUX,
            metadata = StreamCatalogConstants.META_VOD_TIMING,
        ),
        Stream(
            id = StreamCatalogConstants.ID_MUX_DAI,
            name = StreamCatalogConstants.NAME_MUX_DAI,
            thumbnailUrl = "",
            streamUrl = StreamUrls.MUX_DAI,
            provider = StreamCatalogConstants.PROVIDER_MUX,
            metadata = StreamCatalogConstants.META_VOD_ADS,
        ),
        Stream(
            id = StreamCatalogConstants.ID_MUX_SAMPLE,
            name = StreamCatalogConstants.NAME_MUX_SAMPLE,
            thumbnailUrl = StreamUrls.MUX_SAMPLE_THUMBNAIL,
            streamUrl = StreamUrls.MUX_SAMPLE,
            provider = StreamCatalogConstants.PROVIDER_MUX,
            metadata = StreamCatalogConstants.META_VOD_HLS,
        ),
        // --- Apple ---
        Stream(
            id = StreamCatalogConstants.ID_APPLE_FMP4,
            name = StreamCatalogConstants.NAME_APPLE_FMP4,
            thumbnailUrl = "",
            streamUrl = StreamUrls.APPLE_FMP4,
            provider = StreamCatalogConstants.PROVIDER_APPLE,
            metadata = StreamCatalogConstants.META_VOD_ABR_FMP4,
        ),
        Stream(
            id = StreamCatalogConstants.ID_APPLE_HEVC,
            name = StreamCatalogConstants.NAME_APPLE_HEVC,
            thumbnailUrl = "",
            streamUrl = StreamUrls.APPLE_HEVC,
            provider = StreamCatalogConstants.PROVIDER_APPLE,
            metadata = StreamCatalogConstants.META_VOD_HEVC,
        ),
        Stream(
            id = StreamCatalogConstants.ID_APPLE_BIPBOP_4X3,
            name = StreamCatalogConstants.NAME_APPLE_BIPBOP_4X3,
            thumbnailUrl = "",
            streamUrl = StreamUrls.APPLE_BIPBOP_4X3,
            provider = StreamCatalogConstants.PROVIDER_APPLE,
            metadata = StreamCatalogConstants.META_VOD_ABR,
        ),
        Stream(
            id = StreamCatalogConstants.ID_APPLE_DV_ATMOS,
            name = StreamCatalogConstants.NAME_APPLE_DV_ATMOS,
            thumbnailUrl = "",
            streamUrl = StreamUrls.APPLE_DV_ATMOS,
            provider = StreamCatalogConstants.PROVIDER_APPLE,
            metadata = StreamCatalogConstants.META_VOD_DV_ATMOS,
        ),
        // --- Rest ---
        Stream(
            id = StreamCatalogConstants.ID_AKAMAI_LIVE,
            name = StreamCatalogConstants.NAME_AKAMAI_LIVE,
            thumbnailUrl = "",
            streamUrl = StreamUrls.AKAMAI_LIVE,
            provider = StreamCatalogConstants.PROVIDER_AKAMAI,
            metadata = StreamCatalogConstants.META_LIVE_ROLLING,
        ),
        Stream(
            id = StreamCatalogConstants.ID_AKAMAI_EIGHT,
            name = StreamCatalogConstants.NAME_AKAMAI_EIGHT,
            thumbnailUrl = "",
            streamUrl = StreamUrls.AKAMAI_EIGHT,
            provider = StreamCatalogConstants.PROVIDER_AKAMAI,
            metadata = StreamCatalogConstants.META_LIVE_HLS,
        ),
        Stream(
            id = StreamCatalogConstants.ID_BITMOVIN_SINTEL,
            name = StreamCatalogConstants.NAME_BITMOVIN_SINTEL,
            thumbnailUrl = StreamUrls.BITMOVIN_SINTEL_THUMBNAIL,
            streamUrl = StreamUrls.BITMOVIN_SINTEL,
            provider = StreamCatalogConstants.PROVIDER_BITMOVIN,
            metadata = StreamCatalogConstants.META_VOD_ABR,
        ),
        Stream(
            id = StreamCatalogConstants.ID_UNIFIED_TOS,
            name = StreamCatalogConstants.NAME_UNIFIED_TOS,
            thumbnailUrl = StreamUrls.UNIFIED_TOS_THUMBNAIL,
            streamUrl = StreamUrls.UNIFIED_TEARS_OF_STEEL,
            provider = StreamCatalogConstants.PROVIDER_UNIFIED,
            metadata = StreamCatalogConstants.META_VOD_HLS,
        ),
        Stream(
            id = StreamCatalogConstants.ID_JW_SAMPLE,
            name = StreamCatalogConstants.NAME_JW_SAMPLE,
            thumbnailUrl = StreamUrls.JW_SAMPLE_THUMBNAIL,
            streamUrl = StreamUrls.JW_SAMPLE,
            provider = StreamCatalogConstants.PROVIDER_JW,
            metadata = StreamCatalogConstants.META_VOD_HLS,
        ),
        Stream(
            id = StreamCatalogConstants.ID_NASA_TV,
            name = StreamCatalogConstants.NAME_NASA_TV,
            thumbnailUrl = StreamUrls.NASA_TV_THUMBNAIL,
            streamUrl = StreamUrls.NASA_TV,
            provider = StreamCatalogConstants.PROVIDER_NASA,
            metadata = StreamCatalogConstants.META_LIVE_PUBLIC,
        ),
        Stream(
            id = StreamCatalogConstants.ID_LOREM_BUNNY,
            name = StreamCatalogConstants.NAME_LOREM_BUNNY,
            thumbnailUrl = "",
            streamUrl = StreamUrls.LOREM_BUNNY,
            provider = StreamCatalogConstants.PROVIDER_LOREM,
            metadata = StreamCatalogConstants.META_PLACEHOLDER,
        ),
        Stream(
            id = StreamCatalogConstants.ID_LOREM_CAT,
            name = StreamCatalogConstants.NAME_LOREM_CAT,
            thumbnailUrl = "",
            streamUrl = StreamUrls.LOREM_CAT,
            provider = StreamCatalogConstants.PROVIDER_LOREM,
            metadata = StreamCatalogConstants.META_PLACEHOLDER,
        ),
        Stream(
            id = StreamCatalogConstants.ID_LOREM_CORGI,
            name = StreamCatalogConstants.NAME_LOREM_CORGI,
            thumbnailUrl = "",
            streamUrl = StreamUrls.LOREM_CORGI,
            provider = StreamCatalogConstants.PROVIDER_LOREM,
            metadata = StreamCatalogConstants.META_PLACEHOLDER,
        ),
    )
}
