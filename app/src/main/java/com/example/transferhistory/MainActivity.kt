package com.example.transferhistory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import java.io.IOException
import java.util.concurrent.Executors
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.Serializable

class MainActivity : ComponentActivity() {

    var year = mutableListOf<String>()
    var team = mutableListOf<String>()
    var teamImage = mutableListOf<String>()
    var countryImage = mutableListOf<String>()
    var transerType = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val constraints = ConstraintSet {
                val logo = createRefFor("logo")
                val textField = createRefFor("textField")
                val btnPasteURL = createRefFor("btnPasteURL")
                val spacer = createRefFor("spacer")
                val btnGO = createRefFor("btnGO")

                constrain(logo) {
                    top.linkTo(parent.top)
                    bottom.linkTo(textField.top)
                    start.linkTo(textField.start)
                    end.linkTo(textField.end)
                }
                constrain(textField) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.percent(0.8f)
                }
                constrain(btnPasteURL) {
                    top.linkTo(textField.bottom)
                    start.linkTo(textField.start)
                    end.linkTo(spacer.start)
                    width = Dimension.fillToConstraints
                }
                constrain(spacer) {
                    top.linkTo(textField.bottom)
                    start.linkTo(btnPasteURL.end)
                    end.linkTo(btnGO.start)
                    width = Dimension.value(8.dp)
                }
                constrain(btnGO) {
                    top.linkTo(textField.bottom)
                    start.linkTo(spacer.end)
                    end.linkTo(textField.end)
                    width = Dimension.fillToConstraints
                }
            }
            ConstraintLayout(
                constraints,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "tm logo",
                    modifier = Modifier
                        .layoutId("logo")
                )
                var text by remember {
                    mutableStateOf(
                        TextFieldValue("")
                    )
                }
                OutlinedTextField(
                    modifier = Modifier
                        .layoutId("textField"),
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    label = { Text(text = "Enter URL") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(
                            android.graphics.Color.parseColor("#133355")
                        ),
                        unfocusedBorderColor = Color(
                            android.graphics.Color.parseColor("#133355")
                        ),
                        focusedLabelColor = Color(
                            android.graphics.Color.parseColor("#133355")
                        ),
                        cursorColor = Color(
                            android.graphics.Color.parseColor("#133355")
                        )
                    )
                )
                val clipboardManager: ClipboardManager = LocalClipboardManager.current
                Button(
                    modifier = Modifier
                        .layoutId("btnPasteURL"),
                    onClick = {
                        clipboardManager.getText()?.text?.let {
                            text = TextFieldValue(it)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            android.graphics.Color.parseColor("#133355")
                        ),
                        contentColor = Color.White
                    )
                ){
                    Text("Paste URL")
                }
                Spacer(
                    modifier = Modifier
                        .layoutId("spacer"),
                )
                Button(
                    modifier = Modifier
                        .layoutId("btnGO"),
                    onClick = {
                        if(text.text.isNotEmpty()){

                            year.clear()
                            team.clear()
                            teamImage.clear()
                            countryImage.clear()
                            transerType.clear()

                            val executor = Executors.newSingleThreadExecutor()
                            executor.execute {
                                Log.i("TEST", "Loading...")
                                var document = Document(null)
                                var document2 = Document(null)
                                var flag : Boolean

                                try {
                                    document = Jsoup.connect(text.text).get()
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

                                    val elements2 = document2.getElementsByClass("dataBild").select("img")
                                    var imageUrl : String
                                    for(element2 in elements2){
                                        imageUrl = element2.attr("src")
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
                                    if(transerType[i] == "END OF LOAN"){
                                        year[i-2] = year[i-2].dropLast(4) + year[i+1]
                                    }
                                    else{
                                        if(year[i] != year[i+1])
                                            year[i] = year[i] + "-" + year[i+1]
                                    }
                                }
                                if(transerType.last() == "END OF LOAN")
                                    year[year.size-3] = year[year.size-3] + "-"
                                else{
                                    if(team.last() != "Retired")
                                        year[year.size-1] = year.last() + "-"
                                }

                                Log.i("TEST", year.toString())
                                Log.i("TEST", team.toString())
                                Log.i("TEST", teamImage.toString())
                                Log.i("TEST", countryImage.toString())
                                Log.i("TEST", transerType.toString())

                                val intent = Intent(this, DisplayActivity::class.java)
                                intent.putExtra("year", year as Serializable)
                                intent.putStringArrayListExtra("team", (ArrayList<String>(team)))
                                intent.putStringArrayListExtra("teamImage", (ArrayList<String>(teamImage)))
                                intent.putStringArrayListExtra("countryImage", (ArrayList<String>(countryImage)))
                                intent.putStringArrayListExtra("transerType", (ArrayList<String>(transerType)))
                                startActivity(intent)
                            }

                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            android.graphics.Color.parseColor("#133355")
                        ),
                        contentColor = Color.White
                    )
                ){
                    Text("GO")
                }
            }
        }
    }
}