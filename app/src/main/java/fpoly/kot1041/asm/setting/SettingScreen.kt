package fpoly.kot1041.asm.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import fpoly.kot1041.asm.R
import fpoly.kot1041.asm.activity.HeaderWithBack


@Composable
fun SettingScreen(navController : NavController) {
    Scaffold(
        topBar = {
            HeaderWithBack(navController = navController, text = "Setting", modifier = Modifier.fillMaxWidth())
        },
        content = { innerPadding ->
            showSettingsScreen(innerPadding)
        }
    )
}

@Composable
fun showSettingsScreen(innerPaddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, innerPaddingValues.calculateTopPadding(), end = 10.dp)
        , verticalArrangement = Arrangement.SpaceAround
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Personal Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600),
                    color = Color("#909090".toColorInt())
                )
                Image(
                    painter = painterResource(id = R.drawable.edit), contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            OutlinedTextField(
                value = "NAM_Admin",
                label = { Text(text = "Name") },
                placeholder = { Text(text = "Name") },
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color("#FFFFFF".toColorInt()),
                    unfocusedContainerColor = Color("#FFFFFF".toColorInt()),
                    disabledContainerColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray,
                ),
            )


            OutlinedTextField(
                value = "namadmin@gmai.com",
                label = { Text(text = "Email") },
                placeholder = { Text(text = "namadmin@gmai.com") },
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color("#FFFFFF".toColorInt()),
                    unfocusedContainerColor = Color("#FFFFFF".toColorInt()),
                    disabledContainerColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray,
                ),
            )
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Password",
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600),
                    color = Color("#909090".toColorInt())
                )
                Image(
                    painter = painterResource(id = R.drawable.edit), contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            OutlinedTextField(
                value = "***************",
                label = { Text(text = "Password") },
                placeholder = { Text(text = "***************") },
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color("#FFFFFF".toColorInt()),
                    unfocusedContainerColor = Color("#FFFFFF".toColorInt()),
                    disabledContainerColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray,
                ),
            )
        }


        Column(modifier = Modifier.fillMaxWidth().height(230.dp),
            verticalArrangement = Arrangement.SpaceAround) {

            var isChecked by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Notifications",
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600),
                    color = Color("#909090".toColorInt())
                )
                Text(text = "")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation = 0.dp, shape =
                        RoundedCornerShape(8.dp), clip = true
                    ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Sales", fontSize = 18.sp, fontWeight = FontWeight(600))
                    Switch(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation = 0.dp, shape =
                        RoundedCornerShape(8.dp), clip = true
                    ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "New arrivals", fontSize = 18.sp, fontWeight = FontWeight(600))
                    Switch(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation = 0.dp, shape =
                        RoundedCornerShape(8.dp), clip = true
                    ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Delivery status changes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600)
                    )
                    Text(text = "")
                }
            }
        }
        Column (modifier = Modifier.fillMaxWidth().height(230.dp),
            verticalArrangement = Arrangement.SpaceAround){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Help Center",
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600),
                    color = Color("#909090".toColorInt())
                )
                Text(text = "")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation = 0.dp, shape =
                        RoundedCornerShape(8.dp), clip = true
                    ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "FAQ", fontSize = 18.sp, fontWeight = FontWeight(600))
                    Image(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation = 0.dp, shape =
                        RoundedCornerShape(8.dp), clip = true
                    ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "FAQ", fontSize = 18.sp, fontWeight = FontWeight(600))
                    Image(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation = 0.dp, shape =
                        RoundedCornerShape(8.dp), clip = true
                    ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "FAQ", fontSize = 18.sp, fontWeight = FontWeight(600))
                    Image(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}