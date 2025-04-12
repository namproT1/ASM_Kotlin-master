package fpoly.kot1041.asm.favorite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Favorite(
    val id: String = ""
)

data class AddFavoriteRequest(
    val userId: String,
    val productId: String
)

@Serializable
data class DeleteFavorite(
    @SerialName("userId")
    val userId: String = "",
    @SerialName("productId")
    val productId: String = ""
)