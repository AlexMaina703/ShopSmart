package com.shopsmart.app.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Renders up to [maxItems] specs from an arbitrary key/value map.
 * The server sends specs like { "Display": "6.2\"", "Storage": "256GB", ... }.
 */
@Composable
fun SpecsRow(
    specs: Map<String, String>,
    modifier: Modifier = Modifier,
    maxItems: Int = 4,
) {
    if (specs.isEmpty()) return

    val items = specs.entries.take(maxItems)
    val rounded = RoundedCornerShape(12.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(rounded)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items.forEach { entry ->
            SpecItem(
                icon = iconFor(entry.key),
                value = entry.value,
                label = entry.key,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SpecItem(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

private fun iconFor(key: String): ImageVector {
    val k = key.lowercase()
    return when {
        k.contains("display") || k.contains("screen") || k.contains("size")
            -> Icons.Outlined.PhoneAndroid
        k.contains("storage") || k.contains("memory")
            -> Icons.Outlined.SdStorage
        k.contains("ram")
            -> Icons.Outlined.Memory
        k.contains("battery")
            -> Icons.Outlined.BatteryFull
        k.contains("camera")
            -> Icons.Outlined.PhotoCamera
        k.contains("processor") || k.contains("cpu") || k.contains("chip")
            -> Icons.Outlined.Memory
        else -> Icons.Outlined.Info
    }
}