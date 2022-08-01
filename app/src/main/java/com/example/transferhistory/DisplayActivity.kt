package com.example.transferhistory

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

class DisplayActivity : ComponentActivity() {

    var isLoading = true
    var year = mutableListOf<String>()
    var team = mutableListOf<String>()
    var teamImage = mutableListOf<String>()
    var countryImage = mutableListOf<String>()
    var transerType = mutableListOf<String>()

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
            ) {
                var imageCounter = 0
                for(i in 0 until year!!.size){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = year!!.get(i),
                            Modifier.width(80.dp)
                        )
                        if(team.get(i) != "Without Club" && team.get(i) != "Career break" && team.get(i) != "Ban" && team.get(i) != "Unknown" && team.get(i) != "Retired"){
                            Image(painter = rememberAsyncImagePainter(teamImage[imageCounter]),
                                contentDescription = team.get(i),
                                modifier = Modifier.size(40.dp))
                            imageCounter += 1
                        }
                        Text(text = team!!.get(i))
                    }
                }
            }
        }
    }
}