package fpoly.kot1041.asm.profile

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpoly.kot1041.asm.address.Address
import fpoly.kot1041.asm.order.order.OrderState
import fpoly.kot1041.asm.paymentMethod.PaymentMethod
import fpoly.kot1041.asm.product.Product
import fpoly.kot1041.asm.services.RetrofitInstance
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

class UserViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val userService = RetrofitInstance().userService
    private val productService = RetrofitInstance().productService

    private var sharedPreferences: SharedPreferences? = null

    init {
        fetchUsers()
        fetchProducts()
    }

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        loadUserFromPreferences()
    }

    private fun loadUserFromPreferences() {
        val userJson = sharedPreferences?.getString("current_user", null)
        if (userJson != null) {
            try {
                val user = Json.decodeFromString<User>(userJson)
                _currentUser.value = user
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error loading user from preferences: ${e.message}")
            }
        }
    }

    private fun saveUserToPreferences(user: User) {
        try {
            val userJson = Json.encodeToString(User.serializer(), user)
            sharedPreferences?.edit()?.apply {
                putString("current_user", userJson)
                apply()
            }
        } catch (e: Exception) {
            Log.e("UserViewModel", "Error saving user to preferences: ${e.message}")
        }
    }

    fun login(email: String, password: String, context: Context) {
        viewModelScope.launch {
            try {
                val response = userService.getUsers()
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    val user = users.find { it.email == email && it.password == password }
                    if (user != null) {
                        _currentUser.value = user
                        saveUserToPreferences(user)
                        Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi khi đăng nhập", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        sharedPreferences?.edit()?.clear()?.apply()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val response = userService.getUsers()
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    _users.value = users
                    
                    // Cập nhật currentUser nếu có user đang active
                    val activeUser = users.find { it.state == true }
                    if (activeUser != null) {
                        _currentUser.value = activeUser
                        saveUserToPreferences(activeUser)
                    }
                } else {
                    _users.value = emptyList()
                }
            } catch (e: Exception) {
                _users.value = emptyList()
            }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = productService.getProducts()
                if (response.isSuccessful) {
                    _products.value = response.body()
                } else {
                    _products.value = emptyList()
                }
            } catch (e: Exception) {
                _products.value = emptyList()
            }
        }
    }

    fun addUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val newUser = User(
                    name = name,
                    email = email,
                    password = password,
                    phone = "",
                    addresses = emptyList(),
                    paymentMethods = emptyList(),
                    favourites = emptyList()
                )
                val response = userService.addUser(newUser)
                if (response.isSuccessful) {
                    fetchUsers()
                }
            } catch (e: Exception) {
                // Xử lý lỗi nếu cần
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                val response = userService.updateUser(user.id, user)
                if (response.isSuccessful) {
                    fetchUsers()
                }
            } catch (e: Exception) {

            }
        }
    }

    fun updateDefaultAddress(newDefaultAddress: Address, context: Context, currentUser: User) {
        viewModelScope.launch {
            try {
                // Cập nhật tất cả địa chỉ của user
                val updatedAddresses = currentUser.addresses.map { address ->
                    if (address.id == newDefaultAddress.id) {
                        newDefaultAddress
                    } else {
                        address.copy(isDefault = false)
                    }
                }

                // Cập nhật user với danh sách địa chỉ mới
                val updatedUser = currentUser.copy(addresses = updatedAddresses)
                
                // Cập nhật trên server
                val response = userService.updateUser(currentUser.id, updatedUser)
                if (response.isSuccessful) {
                    // Cập nhật trong danh sách users
                    val updatedUsers = _users.value?.map { user ->
                        if (user.id == currentUser.id) updatedUser else user
                    } ?: listOf(updatedUser)
                    _users.value = updatedUsers
                    
                    // Cập nhật currentUser
                    _currentUser.value = updatedUser
                    
                    // Lưu vào SharedPreferences
                    saveUserToPreferences(updatedUser)
                    
                    // Hiển thị thông báo
                    Toast.makeText(context, "Đã cập nhật địa chỉ mặc định", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Có lỗi xảy ra: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateDefaultPayment(newDefaultPayment: PaymentMethod, context: Context, currentUser: User) {
        viewModelScope.launch {
            try {
                // Cập nhật tất cả phương thức thanh toán của user
                val updatedPayments = currentUser.paymentMethods.map { payment ->
                    if (payment.id == newDefaultPayment.id) {
                        newDefaultPayment
                    } else {
                        payment.copy(isDefault = false)
                    }
                }

                // Cập nhật user với danh sách phương thức thanh toán mới
                val updatedUser = currentUser.copy(paymentMethods = updatedPayments)
                
                // Cập nhật trên server
                val response = userService.updateUser(currentUser.id, updatedUser)
                if (response.isSuccessful) {
                    // Cập nhật trong danh sách users
                    val updatedUsers = _users.value?.map { user ->
                        if (user.id == currentUser.id) updatedUser else user
                    } ?: listOf(updatedUser)
                    _users.value = updatedUsers
                    
                    // Cập nhật currentUser
                    _currentUser.value = updatedUser
                    
                    // Lưu vào SharedPreferences
                    saveUserToPreferences(updatedUser)
                    
                    // Hiển thị thông báo
                    Toast.makeText(context, "Đã cập nhật phương thức thanh toán mặc định", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Có lỗi xảy ra: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getProductById(productId: String): Product? {
        return products.value?.find { it.id == productId }
    }

    fun updateOrderState(orderId: String, newState: OrderState) {
        val currentUser = _currentUser.value
        if (currentUser != null) {
            val updatedOrders = currentUser.orders.map { order ->
                if (order.id == orderId) {
                    order.copy(state = newState)
                } else {
                    order
                }
            }
            val updatedUser = currentUser.copy(orders = updatedOrders)
            _currentUser.value = updatedUser
            updateUser(updatedUser)
        }
    }

    fun checkAndUpdateOrderStates() {
        val currentUser = _currentUser.value
        if (currentUser != null) {
            val currentTime = System.currentTimeMillis()
            val updatedOrders = currentUser.orders.map { order ->
                if (order.state == OrderState.PROCESSING) {
                    try {
                        val orderDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            .parse(order.date)
                        val orderTime = orderDate.time
                        val diffInMillis = currentTime - orderTime
                        val diffInHours = diffInMillis / (1000 * 60 * 60)
                        
                        if (diffInHours >= 24) {
                            order.copy(state = OrderState.DELIVERED)
                        } else {
                            order
                        }
                    } catch (e: Exception) {
                        order
                    }
                } else {
                    order
                }
            }
            val updatedUser = currentUser.copy(orders = updatedOrders)
            _currentUser.value = updatedUser
            updateUser(updatedUser)
        }
    }
}