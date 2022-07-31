package com.example.transferhistory

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import java.io.IOException
import java.util.concurrent.Executors
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class DisplayActivity : ComponentActivity() {

    var year = mutableListOf<String>()
    var team = mutableListOf<String>()
    var teamImage = mutableListOf<String>()
    var countryImage = mutableListOf<String>()
    var transerType = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("url")

        Log.i("TAG", "Loading...")

        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            var document = Document(null)
            var document2 = Document(null)
            var flag : Boolean

            try {
                document = Jsoup.connect(url!!).get()
            } catch (e: IOException){
                Log.i("TAG","Error")
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
                    Log.i("TAG","Error")
                }

                var elements2 = document2.getElementsByClass("dataBild").select("img")
                var imageUrl : String
                for(element in elements2){
                    imageUrl = element.attr("src")
                    if(imageUrl!="")
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
            Log.i("TAG", year.toString())
            Log.i("TAG", team.toString())
            Log.i("TAG", teamImage.toString())
            Log.i("TAG", countryImage.toString())
            Log.i("TAG", transerType.toString())
        }
    }
}
