package fpoly.kot1041.asm

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson

import fpoly.kot1041.asm.activity.HomeScreen
import fpoly.kot1041.asm.activity.LoginScreen
import fpoly.kot1041.asm.activity.RegisterScreen
import fpoly.kot1041.asm.activity.SplashScreen
import fpoly.kot1041.asm.address.AddShippingAddressScreen
import fpoly.kot1041.asm.address.ShippingAddressScreen
import fpoly.kot1041.asm.cart.CartScreen
import fpoly.kot1041.asm.order.order.OrderScreen
import fpoly.kot1041.asm.product.ProductDetailScreen
import fpoly.kot1041.asm.profile.ProfileScreen
import fpoly.kot1041.asm.favorite.FavoriteScreen
import fpoly.kot1041.asm.order.checkout.CheckoutScreen
import fpoly.kot1041.asm.order.order.OrderDetailScreen
import fpoly.kot1041.asm.paymentMethod.AddPaymentMethod
import fpoly.kot1041.asm.paymentMethod.PaymentMethodScreen
import fpoly.kot1041.asm.ui.theme.ASMTheme
import fpoly.kot1041.asm.order.OrderSuccessScreen
import kotlinx.coroutines.launch
import fpoly.kot1041.asm.notification.NotifyScreen
import fpoly.kot1041.asm.product.Product
import fpoly.kot1041.asm.product.ProductViewModel
import fpoly.kot1041.asm.profile.UserViewModel
import fpoly.kot1041.asm.setting.SettingScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            ASMTheme{
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xD7FFFFFF))
                ){
                    MainApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val titleTopAppBar = when (currentRoute) {
        BottomNavItem.Home.route -> "Home"
        BottomNavItem.Favorite.route -> "Favorite"
        BottomNavItem.Notify.route -> "Notify"
        BottomNavItem.Profile.route -> "Profile"
        "otherScreen" -> "Other Screen"
        else -> "My App"
    }


    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(currentRoute)) {
                BottomNavigationBar(navController)
            }
        }
    )
    {
            paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavigationGraph(navController, context)
        }
    }
}

sealed class BottomNavItem(val title: String, val icon: Int, val route: String) {
    object Home : BottomNavItem("Home", R.drawable.home, "home")
    object Favorite : BottomNavItem("Favorite", R.drawable.bookmark, "favorite")
    object Notify : BottomNavItem("Notify", R.drawable.notifcation, "notify")
    object Profile : BottomNavItem("Profile", R.drawable.user, "profile")
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorite,
        BottomNavItem.Notify,
        BottomNavItem.Profile
    )
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth(),


        ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                CustomNavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.title,
                            tint = if (selected) Color.Black else Color.Gray,
                            modifier = Modifier.size(35.dp)
                        )
                    },
                    label = {
                    },
                    selected = selected,
                    onClick = {
                        coroutineScope.launch {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()

                )
            }
        }

    }

}

@Composable
fun CustomNavigationBarItem(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        icon()
        label()
    }
}


fun shouldShowTopBar(currentRoute: String?): Boolean {
    return when (currentRoute) {
        BottomNavItem.Home.route,
        BottomNavItem.Favorite.route,
        BottomNavItem.Notify.route,
        "otherScreen" -> true
        else -> false
    }
}

fun shouldShowBottomBar(currentRoute: String?): Boolean {
    return when (currentRoute) {
        BottomNavItem.Home.route,
        BottomNavItem.Favorite.route,
        BottomNavItem.Notify.route,
        BottomNavItem.Profile.route -> true
        else -> false
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, context: Context) {
    NavHost(navController, startDestination = "splash") {
        composable("splash"){ SplashScreen(navController = navController) }
        composable("login"){ LoginScreen(navController, context, viewModel = UserViewModel()) }
        composable("order") { OrderScreen(navController) }
        composable(BottomNavItem.Home.route) {
            HomeScreen(navController)
        }
        composable(BottomNavItem.Favorite.route) {
            FavoriteScreen(navController, viewModel = UserViewModel(), productViewModel = ProductViewModel())
        }
        composable(BottomNavItem.Notify.route) {
            NotifyScreen(navController)
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(navController, viewModel = UserViewModel())
        }
        composable("setting"){ SettingScreen(navController) }

        composable(
            "productDetail/{product}",
            arguments = listOf(navArgument("product") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val productJson = backStackEntry.arguments?.getString("product")
            val product = productJson?.let {
                val decodedJson = URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                Gson().fromJson(decodedJson, Product::class.java)
            }

            product?.let {
                ProductDetailScreen(product = it, navController = navController)
            }
        }
        composable("cart"){ CartScreen(navController = navController)}
        composable("register"){ RegisterScreen(navController, viewModel = UserViewModel()) }
        composable("shippingAddress"){ ShippingAddressScreen(navController, viewModel = UserViewModel()) }
        composable("addShippingAddress"){ AddShippingAddressScreen(navController = navController)}
        composable("addressDetail/{addressId}"){ navBackStackEntry ->

        }
        composable("paymentMethod"){ PaymentMethodScreen(navController = navController) }
        composable("submitSuccess"){ OrderSuccessScreen(navController = navController) }
        composable(
            "checkout_screen/{totalMoney}",
            arguments = listOf(navArgument("totalMoney") { type = NavType.StringType })
        ) { backStackEntry ->
            val totalMoneyString = backStackEntry.arguments?.getString("totalMoney") ?: "0.0"
            val totalMoney = totalMoneyString.toDoubleOrNull() ?: 0.0
            CheckoutScreen(navController = navController, totalMoney = totalMoney)
        }

        composable("orderDetail/{orderId}",
            arguments = listOf(navArgument("orderId"){type = NavType.StringType})
        ){navBackStackEntry ->
            val orderId = navBackStackEntry.arguments?.getString("orderId")
            orderId?.let {
                OrderDetailScreen(orderId = it, navController = navController)
            }

        }


    }
}


@Composable
fun ProgressDialog() {
    Dialog(onDismissRequest = {}) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.wrapContentWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize()
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Loading...")
            }
        }
    }
}
