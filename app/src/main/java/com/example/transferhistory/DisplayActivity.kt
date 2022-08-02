package com.example.transferhistory

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import java.io.IOException

class DisplayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val year: MutableList<String>  = intent.getSerializableExtra("year") as MutableList<String>
        val team: MutableList<String>  = intent.getSerializableExtra("team") as MutableList<String>
        val teamImage: MutableList<String>  = intent.getSerializableExtra("teamImage") as MutableList<String>
        val countryImage: MutableList<String>  = intent.getSerializableExtra("countryImage") as MutableList<String>
        val transerType: MutableList<String>  = intent.getSerializableExtra("transerType") as MutableList<String>

        Log.i("TEST", year.toString())
        Log.i("TEST", team.toString())
        Log.i("TEST", teamImage.toString())
        Log.i("TEST", countryImage.toString())
        Log.i("TEST", transerType.toString())

        setContent {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(android.graphics.Color.parseColor("#133355")))
                    .verticalScroll(scrollState)
            ) {
                var imageCounter = 0
                for(i in 0 until year.size){
                    if(transerType[i] != "END OF LOAN"){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .background(Color.White)
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = year.get(i),
                                Modifier.width(80.dp)
                            )
                            if(transerType.get(i) == "LOAN"){
                                Image(painterResource(id = R.drawable.arrows),
                                    contentDescription = "loan arrows",
                                    modifier = Modifier.size(33.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                            }

                            if(team.get(i) != "Without Club" && team.get(i) != "Career break" && team.get(i) != "Ban" && team.get(i) != "Unknown" && team.get(i) != "Retired"){
                                Image(painter = rememberAsyncImagePainter(teamImage[imageCounter]),
                                    contentDescription = team.get(i),
                                    modifier = Modifier.width(30.dp).height(30.dp))

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
                                            .size(36.dp)
                                            .padding(8.dp),
                                    )
                                }
                                if(transerType[i] == "LOAN")
                                    imageCounter += 2
                                else
                                    imageCounter += 1
                            }
                            Text(text = team.get(i))
                            if(transerType.get(i) == "LOAN")
                                Text(text = "(loan)",Modifier.padding(horizontal = 4.dp))
                        }
                    }
                }
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
}