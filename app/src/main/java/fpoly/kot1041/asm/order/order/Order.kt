package fpoly.kot1041.asm.order.order

import kotlinx.serialization.Serializable

enum class OrderState {
    PROCESSING,
    DELIVERED,
    CANCELED
}

@Serializable
data class Order(
    val id: String,
    val orderId: String,
    val userId: String,
    val cartId: String,
    val totalMoney: Double,
    val totalQuantity: Int,
    val nameUser: String,
    val addressUser:String,
    val paymentUser: String,
    val date: String,
    val items: List<OrderItem>,
    val state: OrderState = OrderState.PROCESSING
)

@Serializable
data class OrderItem(
    val nameProduct: String,
    val quantity: String,
)
