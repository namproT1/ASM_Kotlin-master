package fpoly.kot1041.asm.address

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val id: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("phone")
    val phone: String = "",
    @SerialName("address")
    val address: String = "",
    @SerialName("city")
    val city: String = "",
    @SerialName("district")
    val district: String = "",
    @SerialName("ward")
    val ward: String = "",
    @SerialName("isDefault")
    var isDefault: Boolean = false
)