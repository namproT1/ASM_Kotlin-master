package fpoly.kot1041.asm.address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fpoly.kot1041.asm.activity.ButtonSplash
import fpoly.kot1041.asm.activity.HeaderWithBack
import fpoly.kot1041.asm.profile.UserViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShippingAddressScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val defaultUserName = "Nguyen Van A"

    var nameState by remember { mutableStateOf(TextFieldValue(defaultUserName)) }
    var phoneState by remember { mutableStateOf(TextFieldValue("")) }
    var addressState by remember { mutableStateOf(TextFieldValue("")) }
    var cityState by remember { mutableStateOf(TextFieldValue("")) }
    var districtState by remember { mutableStateOf(TextFieldValue("")) }
    var wardState by remember { mutableStateOf(TextFieldValue("")) }
    var isDefaultState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Shipping Address", navController = navController)
        },
        bottomBar = {
            ButtonSplash(modifier = Modifier, text = "Save Address") {
                val address = Address(
                    id = UUID.randomUUID().toString(),
                    name = nameState.text,
                    phone = phoneState.text,
                    address = addressState.text,
                    city = cityState.text,
                    district = districtState.text,
                    ward = wardState.text,
                    isDefault = isDefaultState
                )
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AddAddress(
                nameState = nameState,
                phoneState = phoneState,
                addressState = addressState,
                cityState = cityState,
                districtState = districtState,
                wardState = wardState,
                onNameChange = { nameState = it },
                onPhoneChange = { phoneState = it },
                onAddressChange = { addressState = it },
                onCityChange = { cityState = it },
                onDistrictChange = { districtState = it },
                onWardChange = { wardState = it },
                errorMessage = errorMessage
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddress(
    nameState: TextFieldValue,
    phoneState: TextFieldValue,
    addressState: TextFieldValue,
    cityState: TextFieldValue,
    districtState: TextFieldValue,
    wardState: TextFieldValue,
    onNameChange: (TextFieldValue) -> Unit,
    onPhoneChange: (TextFieldValue) -> Unit,
    onAddressChange: (TextFieldValue) -> Unit,
    onCityChange: (TextFieldValue) -> Unit,
    onDistrictChange: (TextFieldValue) -> Unit,
    onWardChange: (TextFieldValue) -> Unit,
    errorMessage: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        OutlinedTextField(
            value = nameState,
            onValueChange = onNameChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            label = {
                Text(text = "Name", style = MaterialTheme.typography.titleMedium)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        OutlinedTextField(
            value = phoneState,
            onValueChange = onPhoneChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            label = {
                Text(text = "Phone", style = MaterialTheme.typography.titleMedium)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        OutlinedTextField(
            value = addressState,
            onValueChange = onAddressChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            label = {
                Text(text = "Address", style = MaterialTheme.typography.titleMedium)
            },
            placeholder = {
                Text(text = "Ex: 19 My Dinh", color = Color.Gray)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        OutlinedTextField(
            value = cityState,
            onValueChange = onCityChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            label = {
                Text(text = "City", style = MaterialTheme.typography.titleMedium)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        OutlinedTextField(
            value = districtState,
            onValueChange = onDistrictChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            label = {
                Text(text = "District", style = MaterialTheme.typography.titleMedium)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        OutlinedTextField(
            value = wardState,
            onValueChange = onWardChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            label = {
                Text(text = "Ward", style = MaterialTheme.typography.titleMedium)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
    }
}
