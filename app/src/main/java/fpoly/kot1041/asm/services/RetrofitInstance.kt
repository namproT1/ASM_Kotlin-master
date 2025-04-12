package fpoly.kot1041.asm.services

import fpoly.kot1041.asm.notification.Notification
import fpoly.kot1041.asm.paymentMethod.PaymentMethod
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface NotificationService {
    @GET("notifications")
    suspend fun getNotifications(): retrofit2.Response<List<Notification>>
}

interface PaymentService {
    @GET("payment-methods")
    suspend fun getPaymentMethods(): retrofit2.Response<List<PaymentMethod>>
}

open class RetrofitInstance {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://10.24.32.82:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val productService: ProductService by lazy {
        retrofit.create(ProductService::class.java)
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val notificationService: NotificationService = retrofit.create(NotificationService::class.java)

    val paymentService: PaymentService = retrofit.create(PaymentService::class.java)
}