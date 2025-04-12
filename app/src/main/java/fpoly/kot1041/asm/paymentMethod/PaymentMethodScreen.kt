package fpoly.kot1041.asm.paymentMethod

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import fpoly.kot1041.asm.activity.ButtonSplash
import fpoly.kot1041.asm.activity.HeaderWithBack
import fpoly.kot1041.asm.R
import fpoly.kot1041.asm.profile.UserViewModel
import fpoly.kot1041.asm.services.RetrofitInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val users by userViewModel.users.observeAsState(initial = null)
    val currentUser = users?.find { it.state == true }
    val context = LocalContext.current

    LaunchedEffect(currentUser) {
        // Khi currentUser thay đổi, UI sẽ tự động cập nhật
        currentUser?.paymentMethods?.forEach { payment ->
            println("Payment Method - ID: ${payment.id}, isDefault: ${payment.isDefault}")
        }
    }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Payment Methods", navController = navController)
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                if (currentUser == null) {
                    Text(
                        text = "Vui lòng đăng nhập",
                        modifier = Modifier.padding(16.dp)
                    )
                } else if (currentUser.paymentMethods.isEmpty()) {
                    Text(
                        text = "Bạn chưa có phương thức thanh toán nào",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(currentUser.paymentMethods) { paymentMethod ->
                            CardPaymentMethod(
                                method = paymentMethod,
                                nameUser = currentUser.name,
                                onDefaultChange = { isChecked ->
                                    println("Payment Method ${paymentMethod.id} - isChecked: $isChecked")
                                    if (isChecked) {
                                        // Cập nhật tất cả phương thức thanh toán
                                        val updatedPayments = currentUser.paymentMethods.map { payment ->
                                            if (payment.id == paymentMethod.id) {
                                                println("Setting payment ${payment.id} as default")
                                                payment.copy(isDefault = true)
                                            } else {
                                                println("Setting payment ${payment.id} as not default")
                                                payment.copy(isDefault = false)
                                            }
                                        }
                                        val updatedUser = currentUser.copy(paymentMethods = updatedPayments)
                                        userViewModel.updateDefaultPayment(paymentMethod.copy(isDefault = true), context, updatedUser)
                                    } else {
                                        // Nếu bỏ chọn, đặt tất cả isDefault = false
                                        val updatedPayments = currentUser.paymentMethods.map { payment ->
                                            println("Setting all payments as not default")
                                            payment.copy(isDefault = false)
                                        }
                                        val updatedUser = currentUser.copy(paymentMethods = updatedPayments)
                                        userViewModel.updateDefaultPayment(paymentMethod.copy(isDefault = false), context, updatedUser)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addPaymentMethod") },
                shape = CircleShape,
                containerColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add payment method")
            }
        }
    )
}

@Composable
fun CardPaymentMethod(method: PaymentMethod, nameUser: String, onDefaultChange: (Boolean) -> Unit) {
    Column(modifier = Modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xEB1A1A1A))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RectangleShape)
                        .background(Color.LightGray)
                        .width(31.dp)
                        .height(24.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mastercard2),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = "**** **** **** ${method.cardNumber.takeLast(4)}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Card Holder Name",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xEBC9C9C9)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = method.name,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                    Column {
                        Text(
                            text = "Expiry Date",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xEBC9C9C9)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = method.expiryDate,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = method.isDefault,
                onCheckedChange = onDefaultChange
            )
            Text(
                text = "Đặt làm phương thức thanh toán mặc định",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
