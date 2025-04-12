package fpoly.kot1041.asm.services

import fpoly.kot1041.asm.address.Address
import fpoly.kot1041.asm.paymentMethod.PaymentMethod
import fpoly.kot1041.asm.profile.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("/users")
    suspend fun getUsers(): Response<List<User>>

    @POST("/users")
    suspend fun addUser(@Body user: User): Response<User>

    @PUT("/users/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body user: User
    ): Response<User>

    @PUT("/users/{userId}/address")
    suspend fun updateDefaultAddress(
        @Path("userId") userId: String,
        @Body address: Address
    ): Response<User>

    @PUT("/users/{userId}/payment")
    suspend fun updateDefaultPayment(
        @Path("userId") userId: String,
        @Body paymentMethod: PaymentMethod
    ): Response<User>
}