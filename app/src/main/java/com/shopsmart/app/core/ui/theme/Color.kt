package com.shopsmart.app.core.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================
//  LIGHT PALETTE  (ShopEasy brand)
// ============================================================
val Primary       = Color(0xFFFF6B35)   // brand orange
val PrimaryDark   = Color(0xFFE55A2B)   // darker orange (pressed state / tertiary)
val Secondary     = Color(0xFFFF8C42)

val Background    = Color(0xFFF8F8F8)   // page background
val Surface       = Color(0xFFFFFFFF)   // cards, top bars, sheets
val DividerColor  = Color(0xFFEEEEEE)

val TextPrimary   = Color(0xFF1A1A1A)   // body text on light
val TextSecondary = Color(0xFF666666)
val TextHint      = Color(0xFF999999)

val Success       = Color(0xFF4CAF50)
val Error         = Color(0xFFE53935)
val Warning       = Color(0xFFFF9800)


// ============================================================
//  DARK PALETTE
// ============================================================
// Slightly brighter orange so it pops on dark backgrounds.
val PrimaryDarkTheme     = Color(0xFFFF8A5C)
val PrimaryDarkDark       = Color(0xFFE56B3A)
val SecondaryDarkTheme    = Color(0xFFFFA36B)

// Near-black surfaces — NOT pure #000000 (that's harsh on OLED).
val BackgroundDark        = Color(0xFF121212)
val SurfaceDark           = Color(0xFF1E1E1E)
val DividerDark           = Color(0xFF2A2A2A)

// Text on dark
val TextPrimaryDark       = Color(0xFFEDEDED)
val TextSecondaryDark     = Color(0xFFAAAAAA)
val TextHintDark          = Color(0xFF777777)

// A slightly lighter red so error text reads on dark
val ErrorDark             = Color(0xFFEF5350)

// White text/icons that sit on the orange button
val OnPrimaryLight        = Color(0xFFFFFFFF)
val OnPrimaryDark         = Color(0xFF1A1A1A)   // dark text on the brighter orange