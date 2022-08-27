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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
        val competition: String = intent.getStringExtra("competition") as String
        val stage: String = intent.getStringExtra("stage") as String
        val date: String = intent.getStringExtra("date") as String
        val stadium: String = intent.getStringExtra("stadium") as String
        var colorToChange = 1

        //val year: MutableList<String>  = intent.getSerializableExtra("year") as MutableList<String>
        //val team: MutableList<String>  = intent.getSerializableExtra("team") as MutableList<String>
        //val teamImage: MutableList<String>  = intent.getSerializableExtra("teamImage") as MutableList<String>
        //val countryImage: MutableList<String>  = intent.getSerializableExtra("countryImage") as MutableList<String>
        //val transferType: MutableList<String>  = intent.getSerializableExtra("transferType") as MutableList<String>

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

                                Image(
                                    painter = rememberAsyncImagePainter(homeBadge),
                                    contentDescription = "home badge",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(5.dp)
                                )

                                Text(
                                    text = homeTeam,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                        .width(100.dp),
                                    textAlign = TextAlign.Start
                                )

                                Text(
                                    text = score,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier
                                        .width(50.dp),
                                    textAlign = TextAlign.Center
                                )

                                Text(text = awayTeam,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                        .width(100.dp),
                                    textAlign = TextAlign.End
                                )

                                Image(
                                    painter = rememberAsyncImagePainter(awayBadge),
                                    contentDescription = "away badge",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(5.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (selectedIndex.value == 0) {
                                Row {
                                    Button(
                                        onClick = {
                                            colorToChange = 1
                                            setShowDialog(true)
                                        },
                                        modifier = Modifier
                                            .size(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color(
                                                android.graphics.Color.parseColor(
                                                    homeColor1
                                                )
                                            )
                                        )
                                    ) {}
                                    Button(
                                        onClick = {
                                            colorToChange = 2
                                            setShowDialog(true)
                                        },
                                        modifier = Modifier
                                            .size(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color(
                                                android.graphics.Color.parseColor(
                                                    homeColor2
                                                )
                                            )
                                        )
                                    ) {}
                                }
                            } else {
                                Row {
                                    Button(
                                        onClick = {
                                            colorToChange = 3
                                            setShowDialog(true)
                                        },
                                        modifier = Modifier
                                            .size(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color(
                                                android.graphics.Color.parseColor(
                                                    awayColor1
                                                )
                                            )
                                        )
                                    ) {}
                                    Button(
                                        onClick = {
                                            colorToChange = 4
                                            setShowDialog(true)
                                        },
                                        modifier = Modifier
                                            .size(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color(
                                                android.graphics.Color.parseColor(
                                                    awayColor2
                                                )
                                            )
                                        )
                                    ) {}
                                }
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
                    when (colorToChange) {
                        1 -> homeColor1 = newColor
                        2 -> homeColor2 = newColor
                        3 -> awayColor1 = newColor
                        4 -> awayColor2 = newColor
                    }
                    setShowDialog(false)
                }
            )
            //DisplayStartingLineupComposable(name,year,team,teamImage,countryImage,transferType)
        }
    }
}

@Composable
fun DisplayStartingLineupComposable(
    name: String,
    year: MutableList<String>,
    team: MutableList<String>,
    teamImage: MutableList<String>,
    countryImage: MutableList<String>,
    transferType: MutableList<String>
){
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor("#133355")))
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var imageCounter = 0
        var rowCounter = 0
        for(i in 0 until year.size){
            if(transferType[i] != "END OF LOAN"){
                rowCounter += 1
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .background(Color(android.graphics.Color.parseColor("#E6E6E6")))
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = year[i],
                        Modifier.width(80.dp),
                        fontSize = 15.sp
                    )
                    if(transferType[i] == "LOAN"){
                        Image(painterResource(id = R.drawable.arrows),
                            contentDescription = "loan arrows",
                            modifier = Modifier.size(40.dp))
                    }

                    if(team[i] != "Without Club" && team[i] != "Career break" && team[i] != "Ban" && team[i] != "Unknown" && team[i] != "Retired"){
                        Image(painter = rememberAsyncImagePainter(teamImage[imageCounter]),
                            contentDescription = team[i],
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp))

                        val context = LocalContext.current
                        val imageBitmap = remember {
                            mutableStateOf<ImageBitmap?>(null)
                        }
                        val bitmap: Bitmap? = context.assetsToBitmap("flags/" + countryImage[imageCounter])
                        bitmap?.apply {
                            imageBitmap.value = this.asImageBitmap()
                        }
                        imageBitmap.value?.apply {
                            Image(
                                bitmap = this,
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(8.dp),
                            )
                        }
                        imageCounter += if(transferType[i] == "LOAN")
                            2
                        else
                            1
                    }
                    Text(text = team[i], fontSize = 15.sp)
                    if(transferType[i] == "LOAN")
                        Text(text = "(loan)",Modifier.padding(horizontal = 4.dp), fontSize = 15.sp)
                }
            }
        }
        Text("$name ($rowCounter)", color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
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
                        placeholder = { Text("e.g. 133355") },
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