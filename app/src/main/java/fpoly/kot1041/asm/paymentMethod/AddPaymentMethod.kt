package fpoly.kot1041.asm.paymentMethod

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fpoly.kot1041.asm.activity.ButtonSplash
import fpoly.kot1041.asm.activity.HeaderWithBack
import fpoly.kot1041.asm.R
import java.util.UUID
import androidx.lifecycle.viewmodel.compose.viewModel
import fpoly.kot1041.asm.profile.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentMethodScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    var cardNumberState by remember { mutableStateOf(TextFieldValue("")) }
    var nameState by remember { mutableStateOf(TextFieldValue("")) }
    var expiryDateState by remember { mutableStateOf(TextFieldValue("")) }
    var cvvState by remember { mutableStateOf(TextFieldValue("")) }
    var isDefaultState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            HeaderWithBack(modifier = Modifier, text = "Add Payment Method", navController = navController)
        },
        bottomBar = {
            ButtonSplash(modifier = Modifier, text = "Save Payment Method") {
                val paymentMethod = PaymentMethod(
                    id = UUID.randomUUID().toString(),
                    cardNumber = cardNumberState.text,
                    name = nameState.text,
                    expiryDate = expiryDateState.text,
                    cvv = cvvState.text,
                    isDefault = isDefaultState
                )
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AddPaymentMethod(
                cardNumberState = cardNumberState,
                nameState = nameState,
                expiryDateState = expiryDateState,
                cvvState = cvvState,
                onCardNumberChange = { cardNumberState = it },
                onNameChange = { nameState = it },
                onExpiryDateChange = { expiryDateState = it },
                onCvvChange = { cvvState = it },
                errorMessage = errorMessage
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentMethod(
    cardNumberState: TextFieldValue,
    nameState: TextFieldValue,
    expiryDateState: TextFieldValue,
    cvvState: TextFieldValue,
    onCardNumberChange: (TextFieldValue) -> Unit,
    onNameChange: (TextFieldValue) -> Unit,
    onExpiryDateChange: (TextFieldValue) -> Unit,
    onCvvChange: (TextFieldValue) -> Unit,
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
            value = cardNumberState,
            onValueChange = onCardNumberChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            label = {
                Text(text = "Card Number", style = MaterialTheme.typography.titleMedium)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        OutlinedTextField(
            value = nameState,
            onValueChange = onNameChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            label = {
                Text(text = "Card Holder", style = MaterialTheme.typography.titleMedium)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        OutlinedTextField(
            value = expiryDateState,
            onValueChange = onExpiryDateChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            label = {
                Text(text = "Expiry Date", style = MaterialTheme.typography.titleMedium)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
        OutlinedTextField(
            value = cvvState,
            onValueChange = onCvvChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            label = {
                Text(text = "CVV", style = MaterialTheme.typography.titleMedium)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            )
        )
    }
}

@Composable
fun CardPaymentMethodDemo() {
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xEB1A1A1A)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RectangleShape)
                        .background(Color.LightGray)
                        .width(31.dp)
                        .height(24.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mastercard2),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = "**** **** **** 3030",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Card Holder Name",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "XXXX",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                    Column {
                        Text(
                            text = "Expiry Date",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "XX/XX",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
