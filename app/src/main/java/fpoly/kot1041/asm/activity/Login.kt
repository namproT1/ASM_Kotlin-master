package fpoly.kot1041.asm.activity

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.gson.Gson
import fpoly.kot1041.asm.ProgressDialog
import fpoly.kot1041.asm.R
import fpoly.kot1041.asm.profile.User
import fpoly.kot1041.asm.profile.UserViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, context: Context, viewModel: UserViewModel){
    val usersState by viewModel.users.observeAsState(emptyList())
    val users by viewModel.users.observeAsState()

    val sharedPref = context.getSharedPreferences("login_prefs",Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    var emailState by remember { mutableStateOf(TextFieldValue("")) }
    var passwordState by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var rememberMe by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showDialogProgress by remember {
        mutableStateOf(false)
    }

    if (showDialog){
        errorMessage?.let {
            DialogNotify(onConfirmation = { showDialog = false },
                dialogTitle ="Thông báo",
                dialogMessage = it
            )
        }
    }

    if (showDialogProgress){
        ProgressDialog()
    }

    LaunchedEffect(Unit){
        val savedEmail = sharedPref.getString("email","")
        val savedPass = sharedPref.getString("pass","")
        val rememberMePrefs = sharedPref.getBoolean("rememberMe",false)
        if (rememberMePrefs){
            emailState = TextFieldValue(savedEmail ?: "")
            passwordState = TextFieldValue(savedPass ?: "")
            rememberMe = rememberMePrefs
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 8.dp)
    ) {
        HeaderLogin()
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(12.dp)
            .shadow(5.dp)
            .background(color = Color.White)
        ) {
            BodyLogin(
                navController,
                emailState = emailState,
                passwordState = passwordState,
                rememberMe = rememberMe,
                onEmailChange = { emailState = it },
                onPasswordChange = { passwordState = it },
                onRememberMeChange = {rememberMe = it},
                onLoginClick = {
                    val email = emailState.text
                    val password = passwordState.text

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                        return@BodyLogin
                    }
                    for (user in usersState) {
                        if (user.email == email && user.password == password) {
                            if (rememberMe) {
                                editor.putString("email", email)
                                editor.putString("pass", password)
                                editor.putBoolean("rememberMe", true)
                                editor.apply()
                            } else {
                                editor.clear()
                                editor.apply()
                            }
                            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                            user.state = true
                            viewModel.updateUser(user)
                            navController.navigate("Home")
                        }
                        else {
                            Toast.makeText(context, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                            return@BodyLogin
                        }
                    }
                },
                errorMessage = errorMessage
            )
        }
    }
}
@Composable
fun HeaderLogin(){
    Box(modifier = Modifier.fillMaxWidth()) {
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
        text = "Hello!",
        fontSize = 25.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 25.dp, top = 30.dp),
        fontFamily = FontFamily.Serif
    )
    Text(
        text = "WELCOME BACK",
        modifier = Modifier.padding(start = 25.dp, top = 10.dp, bottom = 20.dp),
        fontSize = 22.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Serif
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyLogin(
    navController: NavController,
    emailState: TextFieldValue,
    passwordState: TextFieldValue,
    rememberMe : Boolean,
    onEmailChange: (TextFieldValue) -> Unit,
    onPasswordChange: (TextFieldValue) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    errorMessage: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        verticalArrangement = Arrangement.Top,

    ) {

        OutlinedTextField(
            value = emailState,
            onValueChange =  onEmailChange,
            label = { Text("Email", style = MaterialTheme.typography.titleMedium, color = Color.Gray) },
            modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
            textStyle = TextStyle(fontSize = 20.sp),
            singleLine = true,
        )
        OutlinedTextField(
            value = passwordState,
            onValueChange = onPasswordChange,
            label = { Text("Password", style = MaterialTheme.typography.titleMedium, color = Color.Gray) },
            modifier = Modifier.padding(top = 10.dp).fillMaxWidth(),
            textStyle = TextStyle(fontSize = 20.sp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),

        )

        Row() {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { isChecked ->
                    onRememberMeChange(isChecked)

                }
            )
            Text(
                text = "Nhớ mật khẩu",
                modifier = Modifier.align(Alignment.CenterVertically),
                style = MaterialTheme.typography.labelMedium
            )
        }

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(modifier = Modifier.padding(top = 10.dp, bottom = 25.dp),
                text = "Forgot Password",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            )
            Button(
                onClick = onLoginClick,
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
                    text = "Log in",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
            Text(text = "SIGN UP",
                modifier = Modifier
                    .padding(top = 30.dp)
                    .clickable {
                        navController.navigate("register")
                    },
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            )
        }
    }
}

@Composable
fun DialogNotify(
    onConfirmation:() -> Unit,
    dialogTitle: String,
    dialogMessage: String
) {
    Dialog(onDismissRequest = onConfirmation,) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = Modifier
                .width(300.dp)
                .padding(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(dialogTitle, style =
                MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(20.dp))
                Text(dialogMessage, style =
                MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(20.dp))
                ButtonSplash(modifier = Modifier
                    .width(80.dp)
                    .height(40.dp)
                    .align(Alignment.End), text = "OK", onclick = onConfirmation)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldDetail(){
    var key by remember {
        mutableStateOf("")
    }
    Column {
        Text(text = "Email")
        TextField(value = key, onValueChange = {
            key = it
        },
            textStyle = TextStyle(fontWeight = FontWeight.SemiBold),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Red,
                containerColor = Color.Transparent
            ),

        )
    }

}
