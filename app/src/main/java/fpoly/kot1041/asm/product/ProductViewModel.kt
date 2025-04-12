package fpoly.kot1041.asm.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpoly.kot1041.asm.services.RetrofitInstance
import kotlinx.coroutines.launch

class ProductViewModel: ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance().productService.getProducts()
                if (response.isSuccessful) {
                    _products.postValue(response.body())
                } else {
                    _products.postValue(emptyList())
                }
            } catch (e: Exception) {
                _products.postValue(emptyList())
            }
        }
    }
}