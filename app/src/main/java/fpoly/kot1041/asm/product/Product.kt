package fpoly.kot1041.asm.product

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: String,
    @SerializedName("name") var name: String,
    @SerializedName("price") var price: Double,
    @SerializedName("stars") var stars: Double,
    @SerializedName("reviews") var reviews: Int,
    @SerializedName("description") var description: String,
    @SerializedName("image") var image: String,
    @SerializedName("category_id") val category_id: String
)

