package fpoly.kot1041.asm.address

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fpoly.kot1041.asm.activity.HeaderWithBack
import fpoly.kot1041.asm.profile.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingAddressScreen(navController: NavController, viewModel: UserViewModel) {
    val users by viewModel.users.observeAsState(initial = null)
    val currentUser = users?.find { it.state == true }
    val context = LocalContext.current

    LaunchedEffect(currentUser) {
        // Khi currentUser thay đổi, UI sẽ tự động cập nhật
        currentUser?.addresses?.forEach { address ->
            println("Address - ID: ${address.id}, isDefault: ${address.isDefault}")
        }
    }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Shipping Address", navController = navController)
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
                } else if (currentUser.addresses.isEmpty()) {
                    Text(
                        text = "Bạn chưa có địa chỉ nào",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(currentUser.addresses) { address ->
                            CardShippingAddress(
                                address = address,
                                nameUser = currentUser.name,
                                onDefaultChange = { isChecked ->
                                    println("Address ${address.id} - isChecked: $isChecked")
                                    if (isChecked) {
                                        // Cập nhật tất cả địa chỉ
                                        val updatedAddresses = currentUser.addresses.map { addr ->
                                            if (addr.id == address.id) {
                                                println("Setting address ${addr.id} as default")
                                                addr.copy(isDefault = true)
                                            } else {
                                                println("Setting address ${addr.id} as not default")
                                                addr.copy(isDefault = false)
                                            }
                                        }
                                        val updatedUser = currentUser.copy(addresses = updatedAddresses)
                                        viewModel.updateDefaultAddress(address.copy(isDefault = true), context, updatedUser)
                                    } else {
                                        // Nếu bỏ chọn, đặt tất cả isDefault = false
                                        val updatedAddresses = currentUser.addresses.map { addr ->
                                            println("Setting all addresses as not default")
                                            addr.copy(isDefault = false)
                                        }
                                        val updatedUser = currentUser.copy(addresses = updatedAddresses)
                                        viewModel.updateDefaultAddress(address.copy(isDefault = false), context, updatedUser)
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
                onClick = { navController.navigate("addShippingAddress") },
                shape = CircleShape,
                containerColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add address")
            }
        }
    )
}

@Composable
fun CardShippingAddress(
    address: Address,
    nameUser: String,
    onDefaultChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = address.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Icon(Icons.Default.Edit, contentDescription = "Edit address")
            }
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${address.address}, ${address.ward}, ${address.district}, ${address.city}",
                modifier = Modifier.padding(12.dp),
                fontSize = 15.sp,
                color = Color.Gray
            )
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = address.isDefault,
            onCheckedChange = onDefaultChange
        )
        Text(
            text = "Đặt làm địa chỉ mặc định",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
