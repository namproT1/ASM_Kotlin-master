package fpoly.kot1041.asm.activity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fpoly.kot1041.asm.product.ProductScreen
import fpoly.kot1041.asm.product.ProductViewModel
import fpoly.kot1041.asm.profile.User

@Composable
fun HomeScreen(navController: NavController){
    Scaffold(
        topBar = {
            HeaderHome1(modifier = Modifier, text = "Home", navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column {
                ProductScreen(navController = navController, viewModel = ProductViewModel())
            }
        }
    }
}

@Composable
fun HeaderHome1(modifier: Modifier,text : String, navController: NavController){
    Box(modifier = modifier
        .fillMaxWidth()
        .height(50.dp),
    ) {
        Icon(Icons.Default.Search,
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .padding(start = 10.dp)
                .align(alignment = Alignment.CenterStart)
                .clickable {

                }

        )
        Column(modifier = Modifier.align(alignment = Alignment.Center)
        ) {
            Text(text = text,
                fontSize = 26.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold)
        }
        Icon(Icons.Default.ShoppingCart, contentDescription = "", modifier = Modifier
            .size(40.dp)
            .padding(end = 10.dp)
            .align(alignment = Alignment.CenterEnd)
            .clickable {
                navController.navigate("cart")
            }
        )
    }
}

@Composable
fun HeaderWithBack(modifier: Modifier,text : String, navController: NavController) {
    Box(modifier = modifier
        .fillMaxWidth()
        .height(50.dp),
    ) {
        Icon(Icons.Default.ArrowBack,
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .padding(start = 10.dp)
                .align(alignment = Alignment.CenterStart)
                .clickable {
                    navController.popBackStack()
                }

        )
        Column(modifier = Modifier.align(alignment = Alignment.Center)
        ) {
            Text(text = text,
                fontSize = 26.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHomeScreen(){
    HomeScreen(navController = NavController(LocalContext.current))
}