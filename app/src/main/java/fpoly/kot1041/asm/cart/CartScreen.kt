package fpoly.kot1041.asm.cart

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fpoly.kot1041.asm.ProgressDialog
import fpoly.kot1041.asm.activity.ButtonSplash
import fpoly.kot1041.asm.activity.HeaderWithBack
import fpoly.kot1041.asm.product.navigateToProductDetail
import fpoly.kot1041.asm.product.ProductViewModel
import fpoly.kot1041.asm.profile.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    val users = userViewModel.users.observeAsState(initial = null)
    val currentUser = users.value?.find { it.state == true }
    val products = productViewModel.products.observeAsState(initial = null).value
    val context = LocalContext.current

    // Tạo danh sách CartItem từ cart của user và products
    val cartItems = remember(currentUser, products) {
        if (currentUser != null && products != null) {
            currentUser.cart.mapNotNull { cartItem ->
                val product = products.find { it.id == cartItem.productId }
                product?.let {
                    CartItem(
                        productId = cartItem.productId,
                        quantity = cartItem.quantity,
                        name = it.name,
                        price = it.price,
                        image = it.image
                    )
                }
            }
        } else {
            emptyList()
        }
    }

    // Tính tổng giá trị giỏ hàng
    val totalCartPrice = cartItems.sumOf { it.price * it.quantity }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Cart", navController = navController)
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if (cartItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Giỏ hàng trống",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        items(cartItems) { cartItem ->
                            ItemCart(
                                cartItem = cartItem,
                                onQuantityUpdate = { newQuantity ->
                                    currentUser?.let { user ->
                                        val newCart = user.cart.toMutableList()
                                        val index = newCart.indexOfFirst { it.productId == cartItem.productId }
                                        if (index != -1) {
                                            newCart[index] = Cart(cartItem.productId, newQuantity)
                                            userViewModel.updateUser(user.copy(cart = newCart))
                                        }
                                    }
                                },
                                onRemove = {
                                    currentUser?.let { user ->
                                        val newCart = user.cart.filter { it.productId != cartItem.productId }
                                        userViewModel.updateUser(user.copy(cart = newCart))
                                    }
                                },
                                navController = navController
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total: ")
                    Text(text = "$${String.format("%.2f", totalCartPrice)}")
                }
                ButtonSplash(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(8.dp),
                    text = "Thanh toán",
                    onclick = {
                        if (cartItems.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Oops, giỏ hàng của bạn chưa có gì hết...",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            navController.navigate("checkout_screen/${totalCartPrice}")
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun ItemCart(
    cartItem: CartItem,
    onQuantityUpdate: (newQuantity: Int) -> Unit,
    onRemove: () -> Unit,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navigateToProductDetail(cartItem.productId, navController)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(7.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .width(100.dp)
                        .height(100.dp)
                ) {
                    AsyncImage(
                        model = cartItem.image,
                        contentDescription = "img product",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = cartItem.name,
                            style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$${String.format("%.2f", cartItem.price)}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    QuantityCart(
                        modifier = Modifier,
                        quantity = cartItem.quantity,
                        onMinus = { 
                            if (cartItem.quantity > 1) {
                                onQuantityUpdate(cartItem.quantity - 1)
                            }
                        },
                        onPlus = { onQuantityUpdate(cartItem.quantity + 1) }
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.padding(top = 0.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove item",
                        tint = Color.Red
                    )
                }
                Text(
                    text = "$${String.format("%.2f", cartItem.price * cartItem.quantity)}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                )
            }
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun QuantityCart(
    modifier: Modifier,
    quantity: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            onClick = onMinus,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x9ED8D8D8),
                contentColor = Color.Gray
            ),
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier.size(30.dp)
        ) {
            Text("-", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = "$quantity", fontSize = 16.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            onClick = onPlus,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x9ED8D8D8),
                contentColor = Color.Gray
            ),
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier.size(30.dp)
        ) {
            Text("+", fontSize = 20.sp)
        }
    }
}

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen(navController = NavController(LocalContext.current))
}
