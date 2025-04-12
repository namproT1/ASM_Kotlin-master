package fpoly.kot1041.asm.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import fpoly.kot1041.asm.profile.UserViewModel
import android.widget.Toast
import fpoly.kot1041.asm.cart.Cart
import fpoly.kot1041.asm.favorite.Favorite

@Composable
fun ProductDetailScreen(
    product: Product, 
    navController: NavController,
    viewModel: UserViewModel = viewModel()
) {
    var productQuantity by remember { mutableStateOf(1) }
    val users = viewModel.users.observeAsState(initial = null)
    val currentUser = users.value?.find { it.state == true }
    val context = LocalContext.current
    
    // Sử dụng derivedStateOf để tự động cập nhật trạng thái yêu thích khi danh sách thay đổi
    val isFavorite by remember(currentUser) {
        derivedStateOf {
            currentUser?.favourites?.any { it.id == product.id } ?: false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxWidth()
            ) {
                HeaderProductDetail(product, navController)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                BodyProductDetail(
                    product = product,
                    quantity = productQuantity,
                    onMinus = { if (productQuantity > 1) productQuantity-- },
                    onPlus = { productQuantity++ },
                    navController = navController
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        FooterProductDetail(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            product = product,
            isFavorite = isFavorite,
            quantity = productQuantity,
            navController = navController,
            onFavoriteClick = {
                currentUser?.let { user ->
                    if (isFavorite) {
                        viewModel.updateUser(
                            user.copy(
                                favourites = user.favourites.filter { it.id != product.id }
                            )
                        )
                    } else {
                        viewModel.updateUser(
                            user.copy(
                                favourites = user.favourites + Favorite(id = product.id)
                            )
                        )
                    }
                }
            },
            onAddToCart = {
                currentUser?.let { user ->
                    // Tạo danh sách giỏ hàng mới
                    val newCart = user.cart.toMutableList()
                    val existingItem = newCart.find { it.productId == product.id }
                    
                    if (existingItem != null) {
                        // Cộng thêm số lượng mới vào số lượng hiện có trong giỏ
                        val index = newCart.indexOf(existingItem)
                        newCart[index] = Cart(product.id, existingItem.quantity + productQuantity)
                    } else {
                        // Thêm sản phẩm mới vào giỏ
                        newCart.add(Cart(product.id, productQuantity))
                    }

                    // Cập nhật user với giỏ hàng mới
                    viewModel.updateUser(
                        user.copy(cart = newCart)
                    )
                    Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
        )
    }
}

@Composable
fun HeaderProductDetail(product: Product, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp)
        ) {
            // Hiển thị ảnh sản phẩm
            AsyncImage(
                model = product.image,
                contentDescription = "Product Image",
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(bottomStart = 100.dp)),
                contentScale = ContentScale.FillBounds,
            )
        }
        // Nút Back
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(13.dp),
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .padding(top = 10.dp, start = 15.dp)
                .size(40.dp)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
            )
        }
        // Color Picker giả (chỉ hiển thị giao diện)
        Button(
            onClick = {  },
            modifier = Modifier.align(Alignment.CenterStart),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(12.dp),
        ) {
            Column {
                listOf(Color.Gray, Color.Yellow, Color.Blue).forEach { color ->
                    Box(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .size(20.dp)
                            .background(color, shape = CircleShape)
                            .clickable {  }
                    )
                }
            }
        }
    }
}

@Composable
fun BodyProductDetail(
    product: Product,
    quantity: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    navController: NavController
) {
    Spacer(modifier = Modifier.height(15.dp))
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = product.name,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                text = "$ ${product.price}",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 25.dp).align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            QuantityProduct(
                modifier = Modifier,
                quantity = quantity,
                onMinus = onMinus,
                onPlus = onPlus
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {navController.navigate("reviews")},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
        ) {
            RateProduct(
            stars = product.stars.toString(),
            reviewCount = product.reviews.toString()
        ) }


        Spacer(modifier = Modifier.height(13.dp))
        Text(
            text = product.description,
            lineHeight = 24.sp,
            color = Color.Gray,
            fontSize = 16.sp
        )

    }
}

@Composable
fun FooterProductDetail(
    modifier: Modifier,
    product: Product,
    isFavorite: Boolean,
    quantity: Int,
    navController: NavController,
    onFavoriteClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onFavoriteClick,
            modifier = Modifier
                .fillMaxHeight()
                .size(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFavorite) Color.Red else Color.Gray,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Button(
            onClick = onAddToCart,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(7.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            elevation = ButtonDefaults.buttonElevation(12.dp)
        ) {
            Text(
                text = "Add to cart",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun QuantityProduct(
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

@Composable
fun RateProduct(stars: String, reviewCount: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Default.Star,
            contentDescription = "",
            tint = Color.Yellow,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = stars,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "($reviewCount review)", color = Color.Gray)
    }
}

fun navigateToProductDetail(productId: String, navController: NavController) {
    navController.navigate("productDetail/$productId")
}

