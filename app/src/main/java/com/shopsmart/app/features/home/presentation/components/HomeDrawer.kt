package com.shopsmart.app.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DrawerEntry(
    val label: String,
    val icon: ImageVector,
    val route: String,
)

@Composable
fun HomeDrawer(
    userName: String?,
    userEmail: String?,
    unreadCount: Int,
    onEntryClick: (DrawerEntry) -> Unit,
    onLogout: () -> Unit,
) {
    val entries = listOf(
        DrawerEntry("Home", Icons.Outlined.Home, "home"),
        DrawerEntry("Categories", Icons.Outlined.Storefront, "categories"),
        DrawerEntry("Cart", Icons.Outlined.ShoppingCart, "cart"),
        DrawerEntry("Wishlist", Icons.Outlined.FavoriteBorder, "wishlist"),
        DrawerEntry("Orders", Icons.Outlined.ShoppingBag, "orders"),
        DrawerEntry("Notifications", Icons.Outlined.Notifications, "notifications"),
        DrawerEntry("Profile", Icons.Outlined.Person, "profile"),
        DrawerEntry("Addresses", Icons.Outlined.LocationOn, "addresses"),
        DrawerEntry("Payment Methods", Icons.Outlined.CreditCard, "payment_methods"),
        DrawerEntry("Settings", Icons.Outlined.Settings, "settings"),
    )

    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
    ) {
        // ---------- USER HEADER ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                .padding(20.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = initials(userName),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = userName ?: "Guest",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            userEmail?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // ---------- MENU ----------
        entries.forEach { entry ->
            NavigationDrawerItem(
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(entry.label, modifier = Modifier.weight(1f))
                        if (entry.route == "notifications" && unreadCount > 0) {
                            Badge { Text(unreadCount.toString()) }
                        }
                    }
                },
                icon = { Icon(entry.icon, contentDescription = entry.label) },
                selected = false,
                onClick = { onEntryClick(entry) },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
            )
        }

        Spacer(Modifier.weight(1f))
        HorizontalDivider(Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        // ---------- LOGOUT ----------
        NavigationDrawerItem(
            label = {
                Text(
                    "Logout",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold,
                )
            },
            icon = {
                Icon(
                    Icons.Outlined.Logout,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.error,
                )
            },
            selected = false,
            onClick = onLogout,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
        )

        Spacer(Modifier.height(12.dp))
    }
}

private fun initials(name: String?): String {
    if (name.isNullOrBlank()) return "?"
    return name.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifBlank { "?" }
}