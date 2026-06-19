package com.shopease.app.ui.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    onOrderPlaced: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is CheckoutEffect.NavigateToProductListAfterOrder -> onOrderPlaced()
                is CheckoutEffect.ShowInvalidAddressMessage ->
                    snackbarHostState.showSnackbar("Please enter a complete delivery address")
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Checkout") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Delivery Address", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = state.addressLine,
                onValueChange = { viewModel.setEvent(CheckoutEvent.AddressChanged(it)) },
                placeholder = { Text("House no., street, city, pincode") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                minLines = 2
            )

            Text(
                "Payment Method",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 24.dp)
            )
            PaymentMethod.entries.forEach { method ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = state.selectedPaymentMethod == method,
                            onClick = { viewModel.setEvent(CheckoutEvent.PaymentMethodSelected(method)) }
                        )
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = state.selectedPaymentMethod == method,
                        onClick = { viewModel.setEvent(CheckoutEvent.PaymentMethodSelected(method)) }
                    )
                    Text(method.displayName, modifier = Modifier.padding(start = 8.dp))
                }
            }

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            Text("Order Summary", style = MaterialTheme.typography.titleMedium)
            SummaryRow("Subtotal", state.subtotal)
            SummaryRow(
                "Delivery",
                state.deliveryFee,
                freeLabel = state.deliveryFee == 0.0
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            SummaryRow("Total", state.total, emphasize = true)

            Button(
                onClick = { viewModel.setEvent(CheckoutEvent.PlaceOrderClicked) },
                enabled = state.items.isNotEmpty() && !state.isPlacingOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                if (state.isPlacingOrder) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(if (state.isPlacingOrder) "Placing order…" else "Place Order")
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    amount: Double,
    freeLabel: Boolean = false,
    emphasize: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = if (emphasize) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
        )
        Text(
            text = if (freeLabel) "FREE" else "₹${"%.0f".format(amount)}",
            style = if (emphasize) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            color = if (emphasize) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
