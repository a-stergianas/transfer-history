package com.example.transferhistory

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.transferhistory.ui.theme.tm_blue
import com.example.transferhistory.ui.theme.tm_grey
import java.io.IOException


class DisplayTransferHistoryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name: String = intent.getStringExtra("name") as String
        val year: MutableList<String>  = intent.getSerializableExtra("year") as MutableList<String>
        val team: MutableList<String>  = intent.getSerializableExtra("team") as MutableList<String>
        val teamImage: MutableList<String>  = intent.getSerializableExtra("teamImage") as MutableList<String>
        val countryImage: MutableList<String>  = intent.getSerializableExtra("countryImage") as MutableList<String>
        val transferType: MutableList<String>  = intent.getSerializableExtra("transferType") as MutableList<String>

        setContent {
            DisplayTransferHistoryComposable(name,year,team,teamImage,countryImage,transferType)
        }
    }
}

@Composable
fun DisplayTransferHistoryComposable(
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
            .background(tm_blue)
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
                        .height(40.dp)
                        .background(tm_grey)
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
                        Image(painter = rememberAsyncImagePainter(teamImage[i]),
                            contentDescription = team[i],
                            modifier = Modifier
                                .size(35.dp)
                                .padding(0.dp)
                        )

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
                                    .width(45.dp)
                                    .padding(0.dp),
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