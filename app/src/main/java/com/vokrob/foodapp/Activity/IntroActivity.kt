package com.vokrob.foodapp.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.vokrob.foodapp.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            SystemBarStyle.dark(
                Color.White.toArgb()
            )
        )

        setContent {
            IntroScreenPreview(
                onGetStartedClick = {
                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun IntroScreenPreview(onGetStartedClick: () -> Unit) {
    IntroScreen(onGetStartedClick)
}

@Composable
fun IntroScreen(onGetStartedClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ConstraintLayout(Modifier.background(Color.White)) {
            val (backgroundImg, logoImg, titleTxt, subtitleTxt, buttonBox) = createRefs()

            Image(
                painter = painterResource(R.drawable.background_intro),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(backgroundImg) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .height(700.dp),
                contentScale = ContentScale.FillBounds
            )

            Text(
                text = "Foodator",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff3d33a8),
                modifier = Modifier
                    .padding(top = 120.dp)
                    .constrainAs(titleTxt) {
                        bottom.linkTo(
                            anchor = logoImg.top,
                            margin = 16.dp
                        )
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.run {
                    constrainAs(logoImg) {
                        top.linkTo(backgroundImg.top)
                        bottom.linkTo(backgroundImg.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                        .width(300.dp)
                        .height(300.dp)
                },
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Food Delivery App",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff3d33a8),
                modifier = Modifier
                    .padding(top = 120.dp)
                    .constrainAs(subtitleTxt) {
                        top.linkTo(
                            anchor = logoImg.bottom,
                            margin = 16.dp
                        )
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            GetStartedButton(
                onClick = onGetStartedClick,
                modifier = Modifier
                    .padding(top = 48.dp)
                    .constrainAs(buttonBox) {
                        top.linkTo(backgroundImg.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
fun GetStartedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(colorResource(R.color.orange)),
        shape = RoundedCornerShape(50.dp),
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(50.dp)
    ) {
        Text(
            text = "Get Started",
            fontSize = 22.sp,
            color = Color.White
        )
    }
}



















