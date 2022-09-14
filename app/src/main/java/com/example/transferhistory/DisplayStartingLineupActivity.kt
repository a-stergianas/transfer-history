package com.example.transferhistory

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.example.transferhistory.ui.theme.tm_blue
import com.example.transferhistory.ui.theme.tm_grey
import java.io.IOException

var newColor = "#133355"

class DisplayStartingLineupActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeTeam: String = intent.getStringExtra("homeTeam") as String
        val awayTeam: String = intent.getStringExtra("awayTeam") as String
        var homeColor1: String = intent.getStringExtra("homeColor1") as String
        var homeColor2: String = intent.getStringExtra("homeColor2") as String
        var awayColor1: String = intent.getStringExtra("awayColor1") as String
        var awayColor2: String = intent.getStringExtra("awayColor2") as String
        val homeBadge: String = intent.getStringExtra("homeBadge") as String
        val awayBadge: String = intent.getStringExtra("awayBadge") as String
        val score: String = intent.getStringExtra("score") as String
        val extraTimeOrPenalties: String = intent.getStringExtra("extraTimeOrPenalties") as String
        val competition: String = intent.getStringExtra("competition") as String
        val stage: String = intent.getStringExtra("stage") as String
        val date: String = intent.getStringExtra("date") as String
        val stadium: String = intent.getStringExtra("stadium") as String
        var colorToChange = 1

        setContent {

            val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }
            val scaffoldState = rememberScaffoldState()
            val selectedIndex = remember { mutableStateOf(0) }

            Scaffold(
                scaffoldState = scaffoldState,
                content = {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(tm_blue)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Text(
                                text = "$competition - $stage\n$date | $stadium",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(tm_grey)
                                    .padding(4.dp),
                                textAlign = TextAlign.Center
                            )
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(tm_grey),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {

                                if(homeBadge.take(5) == "https"){
                                    Image(
                                        painter = rememberAsyncImagePainter(homeBadge),
                                        contentDescription = "home badge",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(5.dp)
                                    )
                                }
                                else{
                                    val context = LocalContext.current
                                    val imageBitmap = remember {
                                        mutableStateOf<ImageBitmap?>(null)
                                    }
                                    val bitmap: Bitmap? = context.assetsToBitmap("flags/" + homeBadge)
                                    bitmap?.apply {
                                        imageBitmap.value = this.asImageBitmap()
                                    }
                                    imageBitmap.value?.apply {
                                        Image(
                                            bitmap = this,
                                            contentDescription = "home badge",
                                            modifier = Modifier
                                                .size(50.dp)
                                                .padding(5.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = homeTeam,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                        .width(100.dp),
                                    textAlign = TextAlign.Start
                                )

                                Column {
                                    Text(
                                        text = score,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier
                                            .width(60.dp),
                                        textAlign = TextAlign.Center
                                    )

                                    if(extraTimeOrPenalties != ""){
                                        Text(
                                            text = extraTimeOrPenalties,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier
                                                .width(60.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Text(text = awayTeam,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                        .width(100.dp),
                                    textAlign = TextAlign.End
                                )

                                if(awayBadge.take(5) == "https"){
                                    Image(
                                        painter = rememberAsyncImagePainter(awayBadge),
                                        contentDescription = "away badge",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(5.dp)
                                    )
                                }
                                else{
                                    val context = LocalContext.current
                                    val imageBitmap = remember {
                                        mutableStateOf<ImageBitmap?>(null)
                                    }
                                    val bitmap: Bitmap? = context.assetsToBitmap("flags/" + awayBadge)
                                    bitmap?.apply {
                                        imageBitmap.value = this.asImageBitmap()
                                    }
                                    imageBitmap.value?.apply {
                                        Image(
                                            bitmap = this,
                                            contentDescription = "away badge",
                                            modifier = Modifier
                                                .size(50.dp)
                                                .padding(5.dp)
                                        )
                                    }
                                }
                            }

                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(380.dp)
                                    .background(tm_grey)
                            ) {

                                val canvasWidth = size.width
                                val canvasHeight = size.height

                                drawImage(
                                    image = ImageBitmap.imageResource(
                                        res = resources,
                                        id = R.drawable.halffield
                                    )
                                )

                                drawCircle(
                                    color = Color(
                                        android.graphics.Color.parseColor(
                                            if (selectedIndex.value == 0)
                                                homeColor2
                                            else
                                                awayColor2
                                        )
                                    ),
                                    center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                                    radius = 12.dp.toPx()
                                )

                                drawArc(
                                    color = Color(
                                        android.graphics.Color.parseColor(
                                            if (selectedIndex.value == 0)
                                                homeColor1
                                            else
                                                awayColor1
                                        )
                                    ),
                                    startAngle = 180f,
                                    sweepAngle = 180f,
                                    useCenter = true,
                                    size = Size(24.dp.toPx(), 24.dp.toPx())
                                )

                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row {
                                Button(
                                    onClick = {
                                        colorToChange =
                                            if (selectedIndex.value == 0)
                                                1
                                            else
                                                3
                                        setShowDialog(true)
                                    },
                                    modifier = Modifier
                                        .size(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(
                                            android.graphics.Color.parseColor(
                                                if (selectedIndex.value == 0)
                                                    homeColor1
                                                else
                                                    awayColor1
                                            )
                                        )
                                    )
                                ) {}
                                Button(
                                    onClick = {
                                        colorToChange =
                                            if (selectedIndex.value == 0)
                                                2
                                            else
                                                4
                                        setShowDialog(true)
                                    },
                                    modifier = Modifier
                                        .size(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(
                                            android.graphics.Color.parseColor(
                                                if (selectedIndex.value == 0)
                                                    homeColor2
                                                else
                                                    awayColor2
                                            )
                                        )
                                    )
                                ) {}
                            }
                        }
                    }
                },
                bottomBar = {
                    BottomNavigation(
                        backgroundColor = tm_grey,
                        contentColor = tm_blue
                    ) {

                        BottomNavigationItem(
                            icon = {Icon(painter = rememberAsyncImagePainter(homeBadge),"home badge") },
                            label = { Text(text = homeTeam) },
                            selected = (selectedIndex.value == 0),
                            onClick = {
                                selectedIndex.value = 0
                            })

                        BottomNavigationItem(
                            icon = {Icon(painter = rememberAsyncImagePainter(awayBadge),"away badge") },
                            label = { Text(text = awayTeam) },
                            selected = (selectedIndex.value == 1),
                            onClick = {
                                selectedIndex.value = 1
                            })

                    }
                }
            )

            ChangeColorDialog(
                showDialog,
                setShowDialog,
                onPositiveClick = {
                    if(newColor.length == 7) {
                        when (colorToChange) {
                            1 -> homeColor1 = newColor
                            2 -> homeColor2 = newColor
                            3 -> awayColor1 = newColor
                            4 -> awayColor2 = newColor
                        }
                        setShowDialog(false)
                    }
                }
            )
            //DisplayStartingLineupComposable(name,year,team,teamImage,countryImage,transferType)
        }
    }
}

private fun Context.assetsToBitmap(fileName: String): Bitmap?{
    return try {
        with(assets.open(fileName)){
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) { null }
}


@Composable
fun ChangeColorDialog(
    showDialog: Boolean, setShowDialog: (Boolean) -> Unit,
    onPositiveClick: () -> Unit,
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = {
                setShowDialog(false)
            },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
            )
        ) {
            Card(
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Hex Color (#):",
                        fontWeight = FontWeight.Bold,
                        color = tm_blue,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    var text by remember { mutableStateOf(TextFieldValue("")) }

                    OutlinedTextField(
                        value = text,
                        placeholder = { Text(text = "e.g. 133355") },
                        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            cursorColor = tm_blue,
                            textColor = tm_blue,
                            focusedIndicatorColor = tm_blue,
                            unfocusedIndicatorColor = tm_blue,
                        ),
                        onValueChange = { newText ->
                            text = newText
                        }
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                setShowDialog(false)
                            },
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                        ) {
                            Text(
                                text = "Cancel",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = tm_blue
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(
                            onClick = {
                                newColor = "#${text.text}"
                                onPositiveClick.invoke()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "OK",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = tm_blue
                            )
                        }
                    }
                }
            }
        }
    }
}