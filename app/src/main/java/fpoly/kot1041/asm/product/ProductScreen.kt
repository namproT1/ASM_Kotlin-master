package fpoly.kot1041.asm.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import fpoly.kot1041.asm.R
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ProductScreen(navController: NavController, viewModel: ProductViewModel) {
    val productState = viewModel.products.observeAsState(initial = emptyList())
    val products = productState.value
    var category_id by remember { mutableStateOf("0") }

    // Filter products based on selected category
    val filteredProducts = remember(products, category_id) {
        if (category_id == "0") {
            products
        } else {
            products.filter { it.category_id == category_id }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Hiển thị menu chip (danh mục)
        ListMenuChip(onCategorySelected = { id -> category_id = id })
        // Hiển thị danh sách sản phẩm ở dạng lưới
        ListProduct(products = filteredProducts, navController = navController)
    }
}

@Composable
fun ListProduct(products: List<Product>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
    ) {
        items(products) { product ->
            ItemProduct(product = product, navController = navController)
        }
    }
}

@Composable
fun ItemProduct(product: Product, navController: NavController) {
    val gson = Gson();
    val productJson = URLEncoder.encode(gson.toJson(product), StandardCharsets.UTF_8.toString())
    // Khi nhấn vào sản phẩm, điều hướng đến màn hình chi tiết sản phẩm
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("productDetail/$productJson") },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
                .width(155.dp)
                .height(200.dp)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = "img product",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = product.name,
            fontSize = 17.sp,
            color = Color.Gray
        )
        Text(
            text = "$ ${product.price}",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemChipFilter(icon: Int, text: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilterChip(
            selected = isSelected,
            onClick = onClick,
            label = {},
            border = null,
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color(0x9ED8D8D8),
                selectedContainerColor = Color.Black,
                selectedLeadingIconColor = Color.White,
                selectedTrailingIconColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "filter icon",
                    modifier = Modifier.size(40.dp)
                )
            },
            modifier = Modifier
                .width(44.dp)
                .height(44.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) Color.Black else Color(0x6D949494)
        )
    }
}

@Composable
fun ListMenuChip(onCategorySelected: (String) -> Unit) {
    // Fake danh sách danh mục hiển thị cho menu chip
    val listMenuChip = listOf(
        Category(icon = R.drawable.stars, text = "Popular", id = "0"),
        Category(icon = R.drawable.chair, text = "Chair", id = "1"),
        Category(icon = R.drawable.table, text = "Table", id = "2"),
        Category(icon = R.drawable.bed, text = "Bed", id = "3"),
    )
    var selectedChipIndex by remember { mutableStateOf(-1) }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(listMenuChip) { item ->
            val index = listMenuChip.indexOf(item)
            ItemChipFilter(
                icon = item.icon,
                text = item.text,
                isSelected = selectedChipIndex == index,
                onClick = {
                    selectedChipIndex = if (selectedChipIndex == index) -1 else index
                    onCategorySelected(item.id)
                }
            )
        }
    }
}

