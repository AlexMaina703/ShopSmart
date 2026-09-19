package com.shopsmart.app.features.home.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuantitySelector(
    quantity: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
    min: Int = 1,
    max: Int = Int.MAX_VALUE,
) {
    val shape = RoundedCornerShape(8.dp)

    Row(
        modifier = modifier
            .clip(shape)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clickable(enabled = quantity > min) { onDecrement() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Decrease")
        }

        Text(
            text = quantity.toString(),
            modifier = Modifier
                .widthIn(min = 32.dp)
                .padding(horizontal = 4.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )

        Box(
            modifier = Modifier
                .size(36.dp)
                .clickable(enabled = quantity < max) { onIncrement() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.Add, contentDescription = "Increase")
        }
    }
}