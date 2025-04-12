package fpoly.kot1041.asm.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import fpoly.kot1041.asm.address.Address
import fpoly.kot1041.asm.cart.Cart
import fpoly.kot1041.asm.favorite.Favorite
import fpoly.kot1041.asm.notification.Notification
import fpoly.kot1041.asm.order.order.Order
import fpoly.kot1041.asm.paymentMethod.PaymentMethod

@Serializable
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    @SerialName("addresses")
    var addresses: List<Address> = emptyList(),
    @SerialName("paymentMethods")
    var paymentMethods: List<PaymentMethod> = emptyList(),
    @SerialName("favourites")
    var favourites: List<Favorite> = emptyList(),
    @SerialName("cart")
    var cart: List<Cart> = emptyList(),
    @SerialName("notifications")
    var notifications: List<Notification> = emptyList(),
    @SerialName("orders")
    var orders: List<Order> = emptyList(),
    var state: Boolean = false
)

