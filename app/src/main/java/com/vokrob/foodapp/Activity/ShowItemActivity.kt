package com.vokrob.foodapp.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.vokrob.foodapp.Helper.ManagmentCart
import com.vokrob.foodapp.Model.FoodModel
import com.vokrob.foodapp.R

class ShowItemActivity : BaseActivity() {
    private lateinit var managmentCart: ManagmentCart
    private lateinit var item: FoodModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        item = (intent.getSerializableExtra("object") as FoodModel)
        managmentCart = ManagmentCart(this)

        setContent {
            ShowDetailScreen(
                food = item,
                onAddToCartClick = {
                    item.numberInCart = 1
                    managmentCart.insertItem(item)
                }
            )
        }
    }

    @Preview
    @Composable
    private fun ShowDetailScreen(
        food: FoodModel = FoodModel(
            title = "Pizza",
            picUrl = "pizza1",
            description = "test",
            price = 9.99,
            categoryId = ""
        ),
        onAddToCartClick: (FoodModel) -> Unit = {}
    ) {
        var numberOrder by remember { mutableStateOf(1) }
        val scroolState = rememberScrollState()

        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(top = 24.dp)
                .background(Color.White)
        ) {
            val (scrollView, addToCartButton) = createRefs()

            Column(
                Modifier
                    .verticalScroll(scroolState)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .constrainAs(scrollView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(addToCartButton.top)
                    }
            ) {
                Text(
                    text = food.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp)
                )

                Text(
                    text = "$${food.price}",
                    fontSize = 24.sp,
                    color = colorResource(R.color.orange),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                AsyncImage(
                    model = (food.picUrl),
                    contentDescription = "Food Image",
                    modifier = Modifier
                        .height(400.dp)
                        .padding(vertical = 16.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.minus),
                        contentDescription = "Decrease",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { if (numberOrder > 1) numberOrder-- }
                    )

                    Text(
                        text = "$numberOrder",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff373b54),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Image(
                        painter = painterResource(R.drawable.plus),
                        contentDescription = "Increase",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { numberOrder++ }
                    )
                }

                Text(
                    text = food.description,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff686767),
                    modifier = Modifier.padding(16.dp)
                )
            }

            Text(
                text = "Add to Cart",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 24.dp
                    )
                    .background(
                        color = Color(0xffff5e00),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .clickable {
                        food.numberInCart = numberOrder
                        onAddToCartClick(food)
                    }
                    .constrainAs(addToCartButton) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp,
                    )
            )
        }
    }
}





















