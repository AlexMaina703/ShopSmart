package com.shopsmart.app.features.auth.presentation.authScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shopsmart.app.features.auth.presentation.viewmodel.AuthViewModel
import com.shopsmart.app.navigation.NavRoutes
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private data class OnboardPage(val emoji: String, val title: String, val desc: String)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = koinViewModel()
) {
    val pages = listOf(
        OnboardPage("🛍️", "Discover Products", "Browse thousands of products across every category."),
        OnboardPage("🚚", "Fast Delivery", "Get your orders delivered to your doorstep in no time."),
        OnboardPage("💳", "Secure Payments", "Pay safely with multiple trusted payment methods.")
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    fun finishOnboarding() {
        viewModel.completeOnboarding()
        navController.navigate(NavRoutes.LOGIN) {
            popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(4f)
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(pages[page].emoji, fontSize = 80.sp)
                Spacer(Modifier.height(24.dp))
                Text(
                    pages[page].title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    pages[page].desc,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        // Page indicators
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pages.size) { i ->
                val isActive = pagerState.currentPage == i
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isActive) 10.dp else 8.dp)
                        .background(
                            color = if (isActive)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                )
            }
        }

        val isLast = pagerState.currentPage == pages.size - 1
        Button(
            onClick = {
                if (isLast) finishOnboarding()
                else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = if (isLast) "Get Started" else "Next",
                fontWeight = FontWeight.SemiBold
            )
        }

        TextButton(onClick = { finishOnboarding() }) {
            Text("Skip")
        }
    }
}