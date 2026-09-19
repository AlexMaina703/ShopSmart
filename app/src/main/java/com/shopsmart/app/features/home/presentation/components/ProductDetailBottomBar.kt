package com.shopsmart.app.features.home.presentation.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProductDetailBottomBar(
    isAddingToCart: Boolean,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit,
) {
    val shape = RoundedCornerShape(10.dp)

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onAddToCart,
                enabled = !isAddingToCart,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = shape,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                if (isAddingToCart) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Add to Cart", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }

            Button(
                onClick = onBuyNow,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Text("Buy Now", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }
}