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

    private var year = mutableListOf<String>()
    private var team = mutableListOf<String>()
    private var teamImage = mutableListOf<String>()
    private var countryImage = mutableListOf<String>()
    private var transferType = mutableListOf<String>()

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

                            var name: String
                            year.clear()
                            team.clear()
                            teamImage.clear()
                            countryImage.clear()
                            transferType.clear()

                            val executor = Executors.newSingleThreadExecutor()
                            executor.execute {
                                var document = Document(null)
                                var document2 = Document(null)
                                var flag : Boolean

                                try {
                                    document = Jsoup.connect(text.text).get()
                                } catch (e: IOException){
                                    Log.i("TEST","Error")
                                }

                                var elements : Elements = document.getElementsByClass("data-header__headline-wrapper")
                                name = elements.text()
                                if(name[0] == '#'){
                                    while(name[0] != ' ')
                                        name = name.drop(1)
                                    name = name.drop(1)
                                }

                                elements = document.getElementsByClass("tm-player-transfer-history-grid__date")
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

                                elements = document.getElementsByClass("tm-player-transfer-history-grid__fee")
                                flag = true
                                for(element in elements){
                                    if (flag){
                                        flag = false
                                        continue
                                    }
                                    if (element.text() == "End of loan")
                                        transferType.add("END OF LOAN")
                                    else if (element.text().contains("loan") or element.text().contains("Loan"))
                                        transferType.add("LOAN")
                                    else
                                        transferType.add("TRANSFER")
                                }
                                transferType.removeLast()

                                elements = document.getElementsByClass("tm-player-transfer-history-grid__club-link")
                                flag = false
                                var counter = 0
                                for(element in elements){
                                    flag = !flag
                                    if(flag)
                                        continue
                                    if(transferType[counter] == "END OF LOAN")
                                        teamImage.add("https://www.transfermarkt.com${element.attr("href")}")
                                    else{
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
                                    counter += 1
                                }

                                elements = document.getElementsByClass("tm-player-transfer-history-grid__new-club").select("img")
                                var countryImageUrl : String
                                for(element in elements){
                                    countryImageUrl = element.attr("data-src")
                                    if(countryImageUrl!="")
                                        countryImage.add(countryImageUrl.drop(52).dropLast(14))
                                }

                                if(transferType.last() == "LOAN"){
                                    year.add("?")                       // ΑΝ ΞΕΡΩ ΧΡΟΝΙΑ ΤΗΝ ΒΑΖΩ ΕΔΩ
                                    team.add(team[team.size-2])
                                    countryImage.add(countryImage[countryImage.size-2])
                                    transferType.add("TRANSFER")

                                    try {
                                        document2 = Jsoup.connect(teamImage[teamImage.size-2]).get()
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

                                year.reverse()
                                team.reverse()
                                teamImage.reverse()
                                countryImage.reverse()
                                transferType.reverse()

                                for(i in 0..year.size-2){
                                    if(transferType[i] == "END OF LOAN"){
                                        var j = 2
                                        while(transferType[i-j] == "END OF LOAN")
                                            j += 2

                                        if(year[i-j].length>4)
                                            year[i-j] = year[i-j].dropLast(4) + year[i+1]
                                        else
                                            year[i-j] = year[i-j] + "-" + year[i+1]
                                    }
                                    else{
                                        if(year[i] != year[i+1])
                                            year[i] = year[i] + "-" + year[i+1]
                                    }
                                }
                                if(transferType.last() == "END OF LOAN"){
                                    if(year[year.size-3].length>4)
                                        year[year.size-3] = year[year.size-3].dropLast(4)
                                    else
                                        year[year.size-3] = year[year.size-3] + "-"
                                }
                                else{
                                    if(team.last() != "Retired")
                                        year[year.size-1] = year.last() + "-"
                                }

                                val intent = Intent(this, DisplayActivity::class.java)
                                intent.putExtra("name", name)
                                intent.putExtra("year", year as Serializable)
                                intent.putExtra("team", team as Serializable)
                                intent.putExtra("teamImage", teamImage as Serializable)
                                intent.putExtra("countryImage", countryImage as Serializable)
                                intent.putExtra("transferType", transferType as Serializable)
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