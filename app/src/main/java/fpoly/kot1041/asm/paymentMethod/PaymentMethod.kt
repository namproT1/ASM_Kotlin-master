package fpoly.kot1041.asm.paymentMethod

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethod(
    val id: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("cardNumber")
    val cardNumber: String = "",
    @SerialName("expiryDate")
    val expiryDate: String = "",
    @SerialName("cvv")
    val cvv: String = "",
    @SerialName("isDefault")
    var isDefault: Boolean = false
)