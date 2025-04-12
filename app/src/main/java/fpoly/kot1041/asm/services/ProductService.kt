package fpoly.kot1041.asm.services

import fpoly.kot1041.asm.product.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {
    @GET("/products")
    suspend fun getProducts(): Response<List<Product>>

}