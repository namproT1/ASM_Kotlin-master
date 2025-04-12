package fpoly.kot1041.asm.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fpoly.kot1041.asm.product.navigateToProductDetail
import fpoly.kot1041.asm.activity.HeaderHome1
import fpoly.kot1041.asm.product.Product
import fpoly.kot1041.asm.product.ProductViewModel
import fpoly.kot1041.asm.profile.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(navController: NavController, viewModel: UserViewModel, productViewModel: ProductViewModel) {
    val productState = productViewModel.products.observeAsState(initial = null)
    val products = productState.value
    val userState = viewModel.users.observeAsState(initial = null)
    val users = userState.value
    val user = users?.find { it.state == true }
    var favorites = emptyList<Favorite>()
    var favoriteProducts = emptyList<Product>()
    // Tạo dữ liệu giả cho danh sách yêu thích
    if (user?.favourites != null) {
        favorites = user.favourites
    }
    for (favorite in favorites) {
        val product = products?.find { it.id == favorite.id }
        if (product != null) {
            favoriteProducts = favoriteProducts + product
        }
    }

    Scaffold(
        topBar = {
            HeaderHome1(modifier = Modifier, text = "Favorites", navController = navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                items(favoriteProducts) { favorite ->
                    ItemFavorite(
                        product = favorite,
                        navController = navController,
                        onDeleteItem = {
                            viewModel.updateUser(
                                user?.copy(
                                    favourites = user.favourites.filter { it.id != favorite.id }
                                )!!
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemFavorite(
    product: Product,
    navController: NavController,
    onDeleteItem: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navigateToProductDetail(product.id, navController)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(7.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .width(100.dp)
                        .height(100.dp)
                ) {
                    AsyncImage(
                        model = product.image,
                        contentDescription = "img product",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column {
                    Text(text = product.name, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "$ ${product.price}",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Bottom),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "remove item favorite",
                    modifier = Modifier.clickable {
                        onDeleteItem()
                    },
                    tint = Color.Red
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
