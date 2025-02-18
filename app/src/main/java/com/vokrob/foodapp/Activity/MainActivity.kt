package com.vokrob.foodapp.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.vokrob.foodapp.Model.CategoryModel
import com.vokrob.foodapp.Model.FoodModel
import com.vokrob.foodapp.R
import com.vokrob.foodapp.ViewModel.MainViewModel

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(
                onHomeClick = {
                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )
                },
                onFoodClick = { food ->
                    val intent = Intent(
                        this,
                        ShowItemActivity::class.java
                    )

                    intent.putExtra("object", food)
                    startActivity(intent)
                }
            )
        }
    }
}

@Preview

@Composable
fun MainScreen(
    onCartClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onFoodClick: (FoodModel) -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        bottomBar = { MyBottomBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                contentColor = Color.White,
                backgroundColor = colorResource(R.color.orange)
            ) {
                Icon(
                    painter = painterResource(R.drawable.shopping_cart),
                    contentDescription = "add",
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp)
                )
            }
        },
        scaffoldState = scaffoldState,
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        content = { paddingValues ->
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {
                val viewModel = MainViewModel()

                val categories = remember { mutableStateListOf<CategoryModel>() }
                val popular = remember { mutableStateListOf<FoodModel>() }

                var showCategoryLoading by remember { mutableStateOf(true) }
                var showPopularLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    viewModel.loadCategory().observeForever {
                        categories.clear()
                        categories.addAll(it)
                        showCategoryLoading = false
                    }
                }

                LaunchedEffect(Unit) {
                    viewModel.loadPopular().observeForever {
                        popular.clear()
                        popular.addAll(it)
                        showPopularLoading = false
                    }
                }

                NameAndProfile()
                Search()
                Banner()
                CategorySection(categories, showCategoryLoading)
                Spacer(Modifier.height(16.dp))
                PopularSection(onFoodClick, popular, showPopularLoading)
                Spacer(Modifier.height(16.dp))
            }
        }
    )
}

@Composable
fun PopularSection(
    onFoodClick: (FoodModel) -> Unit,
    popular: SnapshotStateList<FoodModel>,
    showPopularLoading: Boolean
) {
    Text(
        text = "Popular Items",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    if (showPopularLoading) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(popular) { food -> FoodItem(food, onFoodClick) }
        }
    }
}

@Composable
fun FoodItem(
    food: FoodModel,
    onFoodClick: (FoodModel) -> Unit
) {
    ConstraintLayout(
        Modifier
            .wrapContentSize()
            .border(
                width = 3.dp,
                color = colorResource(R.color.grey),
                shape = RoundedCornerShape(15.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(16.dp)
    ) {
        val (title, image, fee, dollar, addButton) = createRefs()

        Text(
            text = food.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff373b54),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(
                        anchor = parent.top,
                        margin = 16.dp
                    )
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 4.dp)
        )

        AsyncImage(
            model = (food.picUrl),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = "%.2f".format(food.price),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff373b54),
            modifier = Modifier.constrainAs(fee) {
                top.linkTo(image.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = "$",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xffff3d00),
            modifier = Modifier.constrainAs(dollar) {
                top.linkTo(image.bottom)
                end.linkTo(
                    anchor = fee.start,
                    margin = 3.dp
                )
                bottom.linkTo(fee.bottom)
            }
        )

        Text(
            text = "+ Add",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .background(
                    color = Color(0xffff5e00),
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(
                    horizontal = 10.dp,
                    vertical = 3.dp
                )
                .clickable { onFoodClick(food) }
                .constrainAs(addButton) {
                    top.linkTo(dollar.bottom, 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Composable
fun CategorySection(
    categories: SnapshotStateList<CategoryModel>,
    showCategoryLoading: Boolean
) {
    Text(
        text = "Categories",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    if (showCategoryLoading) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories.size) { index ->
                val category = categories[index]

                ConstraintLayout(
                    Modifier
                        .width(75.dp)
                        .wrapContentHeight()
                        .background(
                            color = Color(0xfffef4e5),
                            shape = RoundedCornerShape(13.dp)
                        )
                ) {
                    val (image, text) = createRefs()

                    AsyncImage(
                        model = (category.picUrl),
                        contentDescription = "CategoryImage",
                        modifier = Modifier
                            .size(35.dp)
                            .constrainAs(image) {
                                top.linkTo(
                                    anchor = parent.top,
                                    margin = 10.dp
                                )
                                start.linkTo(
                                    anchor = parent.start,
                                    margin = 10.dp
                                )
                                end.linkTo(
                                    anchor = parent.end,
                                    margin = 10.dp
                                )
                            }
                    )

                    Text(
                        text = category.title,
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .height(30.dp)
                            .constrainAs(text) {
                                top.linkTo(
                                    anchor = image.bottom,
                                    margin = 8.dp
                                )
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun Banner() {
    ConstraintLayout(
        Modifier
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
            .fillMaxWidth()
            .height(150.dp)
            .background(
                color = Color(0xffffc5ab),
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        val (image, title, date, buttonLayout) = createRefs()

        Image(
            painter = painterResource(R.drawable.image_banner),
            contentDescription = null,
            modifier = Modifier.constrainAs(image) {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            }
        )

        Text(
            text = "Free Delivery",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(image.end)
                top.linkTo(image.top)
            }
        )

        Text(
            text = "Feb 23 – March 8",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.constrainAs(date) {
                start.linkTo(title.start)
                top.linkTo(title.bottom)
                end.linkTo(title.end)
            }
        )

        Text(
            text = "Order Now",
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .constrainAs(buttonLayout) {
                    start.linkTo(image.end)
                    top.linkTo(
                        anchor = date.bottom,
                        margin = 8.dp
                    )
                    end.linkTo(date.end)
                    bottom.linkTo(parent.bottom)
                }
                .background(
                    color = Color(0xffff5e00),
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(8.dp)
        )
    }
}

@Composable
fun Search() {
    var text by rememberSaveable { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = {
            Text(
                text = "Find Your Food",
                fontStyle = FontStyle.Italic,
            )
        },
        leadingIcon = {
            Image(
                painter = painterResource(R.drawable.search),
                contentDescription = null,
                modifier = Modifier.size(23.dp)
            )
        },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = colorResource(R.color.grey),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            textColor = Color(android.graphics.Color.parseColor("#5e5e5e")),
            unfocusedLabelColor = Color(android.graphics.Color.parseColor("#5e5e5e"))
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
            .height(50.dp)
            .background(
                color = colorResource(R.color.grey),
                shape = CircleShape
            )
    )
}

@Composable
fun NameAndProfile() {
    ConstraintLayout(
        Modifier
            .padding(top = 48.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        val (name, order, img) = createRefs()

        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = null,
            modifier = Modifier
                .size(75.dp)
                .constrainAs(img) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .clickable {}
        )

        Text(
            text = "Hi, Danil",
            color = colorResource(R.color.orange),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .constrainAs(name) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        Text(
            text = "Order & Eat",
            color = Color.Black,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .constrainAs(order) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
        )
    }
}

@Composable
fun MyBottomBar() {
    val bottomMenuItemsList = prepareBottomMenu()
    val contextForToast = LocalContext.current.applicationContext
    var selectedItem by remember { mutableStateOf("Home") }

    BottomAppBar(
        cutoutShape = CircleShape,
        backgroundColor = Color(android.graphics.Color.parseColor("#f8f8f8")),
        elevation = 3.dp
    ) {
        bottomMenuItemsList.forEachIndexed { index, bottomMenuItem ->
            if (index == 2) {
                BottomNavigationItem(
                    selected = false,
                    onClick = {},
                    icon = {},
                    enabled = false
                )
            }

            BottomNavigationItem(
                selected = (selectedItem == bottomMenuItem.label),
                onClick = {
                    selectedItem = bottomMenuItem.label
                    Toast.makeText(contextForToast, bottomMenuItem.label, Toast.LENGTH_SHORT).show()
                },
                icon = {
                    Icon(
                        painter = bottomMenuItem.icon,
                        contentDescription = bottomMenuItem.label,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                    )
                },
                label = {
                    Text(
                        text = bottomMenuItem.label,
                        modifier = Modifier.padding(top = 14.dp)
                    )
                },
                alwaysShowLabel = true,
                enabled = true
            )
        }
    }
}

data class BottomMenuItem(
    val label: String,
    val icon: Painter
)

@Composable
fun prepareBottomMenu(): List<BottomMenuItem> {
    val bottomMenuItemList = arrayListOf<BottomMenuItem>()

    bottomMenuItemList.add(
        BottomMenuItem(
            label = "Home",
            icon = painterResource(R.drawable.bottom_btn1)
        )
    )

    bottomMenuItemList.add(
        BottomMenuItem(
            label = "Profile",
            icon = painterResource(R.drawable.bottom_btn2)
        )
    )

    bottomMenuItemList.add(
        BottomMenuItem(
            label = "Support",
            icon = painterResource(R.drawable.bottom_btn3)
        )
    )

    bottomMenuItemList.add(
        BottomMenuItem(
            label = "Settings",
            icon = painterResource(R.drawable.bottom_btn4)
        )
    )

    return bottomMenuItemList
}

























