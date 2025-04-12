package fpoly.kot1041.asm.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fpoly.kot1041.asm.R
@Composable
fun SplashScreen(navController: NavController) {

        Image(
            painter = painterResource(id = R.drawable.bgr_splash),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(bottomEnd = 100.dp)),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

            ) {
            Spacer(modifier = Modifier.height(190.dp))
            HeaderSpl()

            BodySpl()
            FooterSpl(navController)
        }


}
@Composable
fun HeaderSpl(){

        Text(
            text = "MAKE YOUR",
            fontSize = 24.sp,
            textAlign = TextAlign.Start,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = "HOME BEAUTIFUL",
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 30.sp,
            textAlign = TextAlign.Start,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Serif
        )

}
@Composable
fun BodySpl(){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
        ){
        Text(
            text = "The best simple place where you discover most wonderful furnitures and make your home beautiful.",
            modifier = Modifier
                .padding(top = 37.dp)
                .width(320.dp)
            ,
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            fontFamily = FontFamily.Serif,
            color = Color.Gray,
            style = TextStyle(
                lineHeight = 30.sp
            )
        )
    }

}


@Composable
fun FooterSpl(navController: NavController){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(160.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ){
        Button(
            onClick = {
                navController.navigate("login")
            },
            modifier = Modifier
                .width(159.dp)
                .height(54.dp),
            shape = RoundedCornerShape(7.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),

            ) {
            Text(
                text = "GET STARTED",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }

}

@Composable
fun ButtonSplash(modifier: Modifier,text : String, onclick: () -> Unit) {
    Button(
        onClick = onclick,
        modifier = modifier.fillMaxWidth()
        ,
        shape = RoundedCornerShape(7.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        ),
        elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}
