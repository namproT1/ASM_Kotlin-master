package fpoly.kot1041.asm.activity

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fpoly.kot1041.asm.R
import fpoly.kot1041.asm.profile.UserViewModel
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: UserViewModel) {
    val context = LocalContext.current
    var nameState by remember { mutableStateOf(TextFieldValue("")) }
    var emailState by remember { mutableStateOf(TextFieldValue("")) }
    var passwordState by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPasswordState by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val userState = viewModel.users.observeAsState(initial = emptyList())
    val user = userState.value

    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
    }

    if (showDialog && errorMessage != null) {
        DialogNotify(
            onConfirmation = { showDialog = false },
            dialogTitle = "Thông báo",
            dialogMessage = errorMessage!!
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 8.dp)
    ) {
        HeaderRegis(Modifier.weight(0.5f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(12.dp)
                .shadow(5.dp)
                .background(color = Color.White)
                .weight(4.5f)
        ) {
            BodyRegis(
                navController,
                nameState,
                emailState,
                passwordState,
                confirmPasswordState,
                onNameChange = { nameState = it },
                onEmailChange = { emailState = it },
                onPasswordChange = { passwordState = it },
                onConfirmPasswordChange = { confirmPasswordState = it },
                onSignUpClick = {
                    val name = nameState.text.trim()
                    val email = emailState.text.trim()
                    val password = passwordState.text.trim()
                    val confirmPassword = confirmPasswordState.text.trim()

                    // Validate name
                    if (name.isEmpty()) {
                        errorMessage = "Vui lòng nhập tên"
                        showDialog = true
                        return@BodyRegis
                    }

                    if (name.any { it.isDigit() }) {
                        errorMessage = "Tên không được chứa số"
                        showDialog = true
                        return@BodyRegis
                    }

                    // Validate email
                    if (email.isEmpty()) {
                        errorMessage = "Vui lòng nhập email"
                        showDialog = true
                        return@BodyRegis
                    }

                    val emailPattern = Pattern.compile(
                        "^[A-Za-z0-9+_.-]+@(.+)$"
                    )
                    if (!emailPattern.matcher(email).matches()) {
                        errorMessage = "Email không hợp lệ"
                        showDialog = true
                        return@BodyRegis
                    }

                    // Check if email exists
                    if (user.any { it.email == email }) {
                        errorMessage = "Email đã tồn tại"
                        showDialog = true
                        return@BodyRegis
                    }

                    // Validate password
                    if (password.isEmpty()) {
                        errorMessage = "Vui lòng nhập mật khẩu"
                        showDialog = true
                        return@BodyRegis
                    }

                    if (password.length < 6) {
                        errorMessage = "Mật khẩu phải có ít nhất 6 ký tự"
                        showDialog = true
                        return@BodyRegis
                    }

                    if (password.contains(" ")) {
                        errorMessage = "Mật khẩu không được chứa khoảng trắng"
                        showDialog = true
                        return@BodyRegis
                    }

                    // Validate confirm password
                    if (confirmPassword.isEmpty()) {
                        errorMessage = "Vui lòng nhập lại mật khẩu"
                        showDialog = true
                        return@BodyRegis
                    }

                    if (password != confirmPassword) {
                        Toast.makeText(context, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
                        return@BodyRegis
                    }

                    // Create new user
                    viewModel.addUser(name, email, password)
                    Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                    navController.navigate("login")
                },
                errorMessage = errorMessage
            )
        }
    }
}

@Composable
fun HeaderRegis(modifier: Modifier){
    Box(modifier = modifier.fillMaxWidth()) {
        Divider(
            color = Color.Gray,
            thickness = 2.dp,
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .align(Alignment.Center)
        )

        Image(
            painter = painterResource(id = R.drawable.img_login),
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(8.dp))
                .background(color = Color.White)
                .padding(horizontal = 10.dp)
        )
    }

    Text(
        text = "WELCOME",
        fontSize = 24.sp,
        color = Color.Black,
        modifier = Modifier.padding(start = 25.dp, top = 10.dp, bottom = 20.dp),
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Serif
    )
}

@Composable
fun BodyRegis(
    navController: NavController,
    nameState: TextFieldValue,
    emailState: TextFieldValue,
    passwordState: TextFieldValue,
    confirmPasswordState: TextFieldValue,
    onNameChange: (TextFieldValue) -> Unit,
    onEmailChange: (TextFieldValue) -> Unit,
    onPasswordChange: (TextFieldValue) -> Unit,
    onConfirmPasswordChange: (TextFieldValue) -> Unit,
    onSignUpClick: () -> Unit,
    errorMessage: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp),
        verticalArrangement = Arrangement.Top,

        ) {

        TextFieldItem(value = nameState,
            onValueChange = onNameChange,
            label = { Text(text = "Name",style = MaterialTheme.typography.titleMedium, color = Color.Gray) },
            visualTransformation = VisualTransformation.None)

        TextFieldItem(value = emailState,
            onValueChange = onEmailChange,
            label = { Text(text = "Email",style = MaterialTheme.typography.titleMedium, color = Color.Gray) },
            visualTransformation = VisualTransformation.None)
        
        TextFieldItem(value = passwordState,
            onValueChange = onPasswordChange,
            label = { Text(text = "Password",style = MaterialTheme.typography.titleMedium, color = Color.Gray) },
            visualTransformation = PasswordVisualTransformation())

        TextFieldItem(value = confirmPasswordState,
            onValueChange = onConfirmPasswordChange,
            label = { Text(text = "Confirm Password",style = MaterialTheme.typography.titleMedium, color = Color.Gray)},
            visualTransformation = PasswordVisualTransformation())

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick =onSignUpClick,
                modifier = Modifier
                    .width(285.dp)
                    .height(50.dp)

                ,
                shape = RoundedCornerShape(7.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),

                ) {
                Text(
                    text = "SIGN UP",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
            Row(modifier = Modifier.padding(top = 7.dp)) {
                Text(text = "Already have account? ",
                    color = Color.Gray,
                    fontSize = 14.sp,
                )
                Text(text = " SIGN IN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    }
                )
            }
        }
    }
}

@Composable
fun ErrorDialog(onDismiss: () -> Unit, message: String) {
    AlertDialog(onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "OK")
            }
        },
        text = { Text(text = message)})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldItem(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier.padding(vertical = 10.dp).fillMaxWidth(),
        textStyle = TextStyle(fontSize = 20.sp),
        singleLine = true,
        visualTransformation = visualTransformation,
        )
}

