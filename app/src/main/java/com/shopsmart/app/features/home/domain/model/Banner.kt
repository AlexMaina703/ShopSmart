package com.shopsmart.app.features.home.domain.model

import androidx.annotation.DrawableRes

/**
 * Home screen carousel banner.
 * - `imageRes` is used when banners ship with the app (Option A).
 * - `imageUrl` is used when banners come from the server (Option B).
 *   Whichever one is non-null is rendered; the UI decides.
 */
data class Banner(
    val id: String,
    val title: String,
    val subtitle: String,
    val ctaText: String,
    val targetRoute: String,
    @DrawableRes val imageRes: Int? = null,
    val imageUrl: String? = null,
)