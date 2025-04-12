package fpoly.kot1041.asm.order.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fpoly.kot1041.asm.activity.ButtonSplash
import fpoly.kot1041.asm.activity.HeaderWithBack
import fpoly.kot1041.asm.profile.UserViewModel
import fpoly.kot1041.asm.order.order.Order
import fpoly.kot1041.asm.order.order.OrderState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(navController: NavController, viewModel: UserViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Processing", "Delivered", "Canceled")
    val users by viewModel.users.observeAsState(initial = emptyList())
    val currentUser = users.find { it.state == true }
    val orders = currentUser?.orders ?: emptyList()

    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
        viewModel.checkAndUpdateOrderStates()
    }

    Scaffold(
        topBar = {
            Column {
                HeaderWithBack(modifier = Modifier, text = "My order", navController = navController)
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = selectedTab == index,
                            onClick = { selectedTab = index }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        val filteredOrders = when (selectedTab) {
            0 -> orders.filter { it.state == OrderState.PROCESSING }
            1 -> orders.filter { it.state == OrderState.DELIVERED }
            else -> orders.filter { it.state == OrderState.CANCELED }
        }

        if (filteredOrders.isEmpty()) {
            Text(
                text = "Bạn chưa có đơn hàng nào",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredOrders) { order ->
                    OrderItem(
                        order = order,
                        onCancelClick = {
                            viewModel.updateOrderState(order.id, OrderState.CANCELED)
                        },
                        onOrderClick = {
                            navController.navigate("orderDetail/${order.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderItem(
    order: Order,
    onCancelClick: () -> Unit,
    onOrderClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOrderClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Order #${order.orderId}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = order.date,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            order.items.forEach { item ->
                Text(
                    text = "${item.nameProduct} x${item.quantity}",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total: $${order.totalMoney}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Status: ${order.state.name}",
                        fontSize = 14.sp,
                        color = when (order.state) {
                            OrderState.PROCESSING -> Color.Blue
                            OrderState.DELIVERED -> Color.Green
                            OrderState.CANCELED -> Color.Red
                        }
                    )
                }

                if (order.state == OrderState.PROCESSING) {
                    Button(
                        onClick = onCancelClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cancel Order")
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCategoryBar(
    categories: List<String>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit
) {
    // Dùng Row để đặt các tab cạnh nhau
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        categories.forEachIndexed { index, category ->
            Column(
                modifier = Modifier
                    .clickable { onCategorySelected(index) }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Text hiển thị tên category_id
                Text(
                    text = category,
                    fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedIndex == index) Color.Black else Color.Gray
                )
                // Nếu là tab đang chọn thì vẽ 1 gạch dưới
                if (selectedIndex == index) {
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                            .width(24.dp)
                            .background(Color.Black)
                    )
                }
            }
        }
    }
}
