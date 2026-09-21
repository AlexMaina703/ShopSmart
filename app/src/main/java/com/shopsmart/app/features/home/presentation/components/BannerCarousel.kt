package com.shopsmart.app.features.home.presentation.components


import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shopsmart.app.features.home.domain.model.Banner
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerCarousel(
    banners: List<Banner>,
    onBannerClick: (Banner) -> Unit,
    modifier: Modifier = Modifier,
    autoScrollMillis: Long = 3500L,
) {
    if (banners.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { banners.size })

    // Auto-advance
    LaunchedEffect(banners.size) {
        while (true) {
            delay(autoScrollMillis)
            val next = (pagerState.currentPage + 1) % banners.size
            pagerState.animateScrollToPage(next, animationSpec = tween(600))
        }
    }

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp,
        ) { page ->
            val banner = banners[page]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
            ) {
                // Banner image (fills the card)
                banner.imageRes?.let { res ->
                    Image(
                        painter = painterResource(id = res),
                        contentDescription = banner.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }

//                // You can overlay title + subtitle + CTA on top of the image
//                // if your PNGs are pure photography. If your PNGs already
//                // contain the text, remove this Column.
//                Column(
//                    modifier = Modifier
//                        .align(Alignment.CenterStart)
//                        .padding(start = 20.dp, end = 140.dp),
//                ) {
//                    Text(
//                        banner.title,
//                        fontSize = 18.sp,
//                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.onSurface,
//                    )
//                    Text(
//                        banner.subtitle,
//                        fontSize = 13.sp,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    )
//                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Dots
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(banners.size) { i ->
                val active = pagerState.currentPage == i
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (active) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (active) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        ),
                )
            }
        }
    }
}