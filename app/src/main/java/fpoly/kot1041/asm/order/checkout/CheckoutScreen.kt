package fpoly.kot1041.asm.order.checkout

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fpoly.kot1041.asm.R
import fpoly.kot1041.asm.activity.ButtonSplash
import fpoly.kot1041.asm.activity.HeaderWithBack
import fpoly.kot1041.asm.address.Address
import fpoly.kot1041.asm.paymentMethod.PaymentMethod
import fpoly.kot1041.asm.notification.Notification
import fpoly.kot1041.asm.order.order.Order
import fpoly.kot1041.asm.order.order.OrderItem
import fpoly.kot1041.asm.order.order.OrderState
import fpoly.kot1041.asm.profile.User
import fpoly.kot1041.asm.profile.UserViewModel
import kotlin.random.Random
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

private fun generateOrderId(): String {
    val random = Random(System.currentTimeMillis())
    val number = random.nextLong(10000000000L)
    return "SPXVN$number"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    totalMoney: Double
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val users by userViewModel.users.observeAsState(initial = null)
    val currentUser = users?.find { it.state == true }
    val defaultAddress = currentUser?.addresses?.find { it.isDefault }
    val defaultPayment = currentUser?.paymentMethods?.find { it.isDefault }
    val cartTotal = currentUser?.cart?.sumOf { cart ->
        val product = userViewModel.getProductById(cart.productId)
        (product?.price ?: 0.0) * cart.quantity
    } ?: 0.0
    val shippingFee = 5.0
    val total = cartTotal + shippingFee

    Box(modifier = Modifier.background(color = Color.Transparent).padding(8.dp)) {
        Scaffold(
            topBar = {
                HeaderWithBack(modifier = Modifier, text = "Check Out", navController = navController)
            },
            bottomBar = {
                ButtonSplash(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .height(60.dp),
                    text = "XÁC NHẬN ĐƠN HÀNG"
                ) {
                    if (defaultAddress == null) {
                        Toast.makeText(context, "Vui lòng chọn địa chỉ giao hàng", Toast.LENGTH_SHORT).show()
                    } else if (defaultPayment == null) {
                        Toast.makeText(context, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show()
                    } else {
                        showDialog = true
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if (currentUser == null) {
                    Text(
                        text = "Vui lòng đăng nhập để tiếp tục",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ShippingAddressCheckout(
                            defaultAddress = defaultAddress,
                            user = currentUser,
                            navController = navController
                        )
                        PaymentMethodCheckout(
                            defaultPayment = defaultPayment,
                            navController = navController
                        )
                        TotalCheckout(totalAmount = total)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xác nhận đơn hàng") },
            text = { Text("Bạn có chắc chắn muốn đặt đơn hàng này?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        val orderId = generateOrderId()
                        
                        // Tạo đơn hàng mới
                        val newOrder = Order(
                            id = UUID.randomUUID().toString(),
                            orderId = orderId,
                            userId = currentUser?.id ?: "",
                            cartId = "cart_${System.currentTimeMillis()}",
                            totalMoney = total,
                            totalQuantity = currentUser?.cart?.sumOf { it.quantity } ?: 0,
                            nameUser = currentUser?.name ?: "",
                            addressUser = defaultAddress?.address ?: "",
                            paymentUser = defaultPayment?.cardNumber ?: "",
                            date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                            items = currentUser?.cart?.map { cart ->
                                val product = userViewModel.getProductById(cart.productId)
                                OrderItem(
                                    nameProduct = product?.name ?: "",
                                    quantity = cart.quantity.toString()
                                )
                            } ?: emptyList(),
                            state = OrderState.PROCESSING
                        )

                        // Tạo thông báo mới
                        val notification = Notification(
                            id = orderId,
                            title = "Đơn hàng mới",
                            content = "Đơn hàng $orderId đã được đặt thành công. Tổng tiền: $${String.format("%.2f", total)} (đã bao gồm phí ship)",
                            date = System.currentTimeMillis().toString()
                        )
                        
                        currentUser?.let { user ->
                            val updatedUser = user.copy(
                                cart = emptyList(),
                                notifications = user.notifications + notification,
                                orders = user.orders + newOrder
                            )
                            userViewModel.updateUser(updatedUser)
                        }
                        
                        navController.navigate("submitSuccess")
                    }
                ) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun ShippingAddressCheckout(defaultAddress: Address?, user: User, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        TitleItemCheckout(
            text = "Shipping Address",
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            if (defaultAddress == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Vui lòng chọn địa chỉ giao hàng",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = defaultAddress.address,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                    Text(
                        text = defaultAddress.district,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = defaultAddress.city,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentMethodCheckout(defaultPayment: PaymentMethod?, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        TitleItemCheckout(
            text = "Payment"
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            if (defaultPayment == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Vui lòng chọn phương thức thanh toán",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = defaultPayment.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.mastercard2),
                            contentDescription = "Payment method icon",
                            modifier = Modifier.size(45.dp)
                        )
                    }
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "**** **** **** ${defaultPayment.cardNumber.takeLast(4)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Expiry: ${defaultPayment.expiryDate}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun TotalCheckout(totalAmount: Double) {
    val shippingFee = 5.0
    val total = totalAmount + shippingFee
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Order",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "$${String.format("%.2f", totalAmount)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Shipping Fee",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "$${String.format("%.2f", shippingFee)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "$${String.format("%.2f", total)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun TitleItemCheckout(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
    }
}
