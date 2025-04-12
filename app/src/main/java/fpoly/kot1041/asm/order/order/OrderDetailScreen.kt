package fpoly.kot1041.asm.order.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fpoly.kot1041.asm.activity.HeaderWithBack
import fpoly.kot1041.asm.profile.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(orderId: String, navController: NavController, viewModel: UserViewModel = viewModel()) {
    val userState = viewModel.users.observeAsState(initial = null)
    val users = userState.value
    val currentUser = users?.find { it.state == true }
    val order = currentUser?.orders?.find { it.id == orderId }

    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
    }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Order Detail", navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (order == null) {
                Text(
                    text = "Không tìm thấy đơn hàng",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Color.Gray
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Order #${order.orderId}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Date: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(text = order.date, style = MaterialTheme.typography.titleMedium)
                            }
                            
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Name: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(text = order.nameUser, style = MaterialTheme.typography.titleMedium)
                            }
                            
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Address: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(text = order.addressUser, style = MaterialTheme.typography.titleMedium)
                            }
                            
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Payment: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(text = order.paymentUser, style = MaterialTheme.typography.titleMedium)
                            }
                            
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Total Quantity: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "${order.totalQuantity}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Total Money: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "$ ${order.totalMoney}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            
                            Text(
                                text = "Products:",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                            
                            order.items.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 12.dp)
                                ) {
                                    Text(
                                        text = "${item.nameProduct} x${item.quantity}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                            
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Status: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text = order.state.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = when (order.state) {
                                            OrderState.PROCESSING -> Color.Blue
                                            OrderState.DELIVERED -> Color.Green
                                            OrderState.CANCELED -> Color.Red
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
