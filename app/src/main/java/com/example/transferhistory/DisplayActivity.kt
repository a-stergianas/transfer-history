package com.example.transferhistory

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.io.IOException
import java.util.concurrent.Executors
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class DisplayActivity : ComponentActivity() {

    var isLoading = true
    var year = mutableListOf<String>()
    var team = mutableListOf<String>()
    var teamImage = mutableListOf<String>()
    var countryImage = mutableListOf<String>()
    var transerType = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("url")

        setContent {
            CircularIndeterminateProgressBar(isLoading)
        }

        Log.i("TEST", "Loading...")

        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            var document = Document(null)
            var document2 = Document(null)
            var flag : Boolean

            try {
                document = Jsoup.connect(url!!).get()
            } catch (e: IOException){
                Log.i("TEST","Error")
            }

            var elements : Elements = document.getElementsByClass("tm-player-transfer-history-grid__date")
            flag = true
            for(element in elements){
                if (flag){
                    flag = false
                    continue
                }
                year.add(element.text().takeLast(4))
            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__new-club")
            flag = true
            for(element in elements){
                if (flag){
                    flag = false
                    continue
                }
                //if(element.text() != "Without Club" && element.text() != "Career break" && element.text() != "Ban" && element.text() != "Unknown")
                team.add(element.text())
            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__club-link")
            flag = false
            for(element in elements){
                flag = !flag
                if(flag)
                    continue

                try {
                    document2 = Jsoup.connect("https://www.transfermarkt.com${element.attr("href")}").get()
                } catch (e: IOException){
                    Log.i("TEST","Error")
                }

                var elements2 = document2.getElementsByClass("dataBild").select("img")
                var imageUrl : String
                for(element in elements2){
                    imageUrl = element.attr("src")
                    teamImage.add(imageUrl)
                }
            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__new-club").select("img")
            var countryImageUrl : String
            for(element in elements){
                countryImageUrl = element.attr("data-src")
                if(countryImageUrl!="")
                    countryImage.add(countryImageUrl.drop(52).dropLast(14))
            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__fee")
            flag = true
            for(element in elements){
                if (flag){
                    flag = false
                    continue
                }
                if (element.text() == "End of loan")
                    transerType.add("END OF LOAN")
                else if (element.text().contains("loan") or element.text().contains("Loan"))
                    transerType.add("LOAN")
                else
                    transerType.add("TRANSFER")
            }
            transerType.removeLast()

            year.reverse()
            team.reverse()
            teamImage.reverse()
            countryImage.reverse()
            transerType.reverse()
            
            for(i in 0..year.size-2){
                if(year[i] != year[i+1])
                    year[i] = year[i] + "-" + year[i+1]
                //Log.i("TEST", year[i] + "-" + year[i+1])
            }
            if(team.last().equals("Retired"))
                //Log.i("TEST", year.last())
            else{
                //Log.i("TEST", year.last() + "-")
                year[year.size-1] = year.last() + "-"
            }

            Log.i("TEST", year.toString())
            Log.i("TEST", team.toString())
            Log.i("TEST", teamImage.toString())
            Log.i("TEST", countryImage.toString())
            Log.i("TEST", transerType.toString())
            isLoading = false
        }
    }
}
@Composable
fun CircularIndeterminateProgressBar(isDisplayed: Boolean){
    if(isDisplayed){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp),
            horizontalArrangement = Arrangement.Center
        ){
            if(isDisplayed){
                CircularProgressIndicator(
                    color = Color.Red
                )
            }
        }
    }
}