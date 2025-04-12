package fpoly.kot1041.asm.cart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    @SerialName("productId")
    val productId: String = "",
    @SerialName("quantity")
    val quantity: Int = 0
)

@Serializable
data class CartItem(
    @SerialName("productId")
    val productId: String = "",
    @SerialName("quantity")
    val quantity: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("price")
    val price: Double = 0.0,
    @SerialName("image")
    val image: String = ""
)