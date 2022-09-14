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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

            val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }

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

                            setShowDialog(true)


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

                                if(text.text.drop(30).dropWhile { it != '/' }.take(16) != "/profil/spieler/"){

                                    var elements : Elements = document.getElementsByClass("sb-vereinslink")

                                    val homeTeam = elements[0].text()
                                    val awayTeam = elements[1].text()

                                    var homeTeamPortraitUrlName = elements[0].attr("href").dropLast(15).drop(1)
                                    var counter = 0
                                    for(char in homeTeamPortraitUrlName){
                                        if (char == '/')
                                            break
                                        counter ++
                                    }
                                    homeTeamPortraitUrlName = homeTeamPortraitUrlName.take(counter)

                                    var homeTeamPortraitUrlId = elements[0].attr("href").dropLast(15).drop(1)
                                    counter = 0
                                    for(char in homeTeamPortraitUrlId){
                                        if (char == '/')
                                            counter ++
                                        if(counter < 2)
                                            homeTeamPortraitUrlId = homeTeamPortraitUrlId.drop(1)
                                        else
                                            break
                                    }

                                    try {
                                        document2 = Jsoup.connect("https://www.transfermarkt.com/$homeTeamPortraitUrlName/datenfakten$homeTeamPortraitUrlId").get()
                                    } catch (e: IOException){
                                        Log.i("TEST","Error")
                                    }
                                    var elements2 : Elements = document2.getElementsByClass("vereinsfarbe").select("span")

                                    var homeColor1 = "#40E0D0"
                                    var homeColor2 = "#FFC0CB"
                                    if (elements2.isNotEmpty()){
                                        homeColor1 = elements2[0].attr("style").dropLast(1).drop(17)
                                        homeColor2 = elements2[1].attr("style").dropLast(1).drop(17)
                                    }

                                    var awayTeamPortraitUrlName = elements[1].attr("href").dropLast(15).drop(1)
                                    counter = 0
                                    for(char in awayTeamPortraitUrlName){
                                        if (char == '/')
                                            break
                                        counter ++
                                    }

                                    awayTeamPortraitUrlName = awayTeamPortraitUrlName.take(counter)

                                    var awayTeamPortraitUrlId = elements[1].attr("href").dropLast(15).drop(1)
                                    counter = 0
                                    for(char in awayTeamPortraitUrlId){
                                        if (char == '/')
                                            counter ++
                                        if(counter < 2)
                                            awayTeamPortraitUrlId = awayTeamPortraitUrlId.drop(1)
                                        else
                                            break
                                    }

                                    try {
                                        document2 = Jsoup.connect("https://www.transfermarkt.com/$awayTeamPortraitUrlName/datenfakten$awayTeamPortraitUrlId").get()
                                    } catch (e: IOException){
                                        Log.i("TEST","Error")
                                    }

                                    elements2 = document2.getElementsByClass("vereinsfarbe").select("span")

                                    var awayColor1 = "#40E0D0"
                                    var awayColor2 = "#FFC0CB"
                                    if (elements2.isNotEmpty()){
                                        awayColor1 = elements2[0].attr("style").dropLast(1).drop(17)
                                        awayColor2 = elements2[1].attr("style").dropLast(1).drop(17)
                                    }

                                    if(awayColor1 == "")
                                        awayColor1 = "#FFFFFF"

                                    if(awayColor2 == "")
                                        awayColor2 = "#FFFFFF"

                                    var homeBadge = ""
                                    var awayBadge = ""

                                    elements = document.getElementsByClass("unterueberschrift aufstellung-unterueberschrift-mannschaft")
                                        .select("img")
                                    for (element in elements) {
                                        homeBadge = element.attr("src")
                                    }

                                    if(homeBadge.drop(35).takeWhile { it != '/' } == "wappen"){
                                        homeBadge = homeBadge.take(42) + "big/" + homeBadge.takeLastWhile { it != '/' }
                                    }
                                    else if(homeBadge.drop(35).takeWhile { it != '/' } == "flagge"){
                                        homeBadge = homeBadge.takeLastWhile { it != '/' }.takeWhile { it != '?' }
                                    }

                                    elements = document.getElementsByClass("unterueberschrift aufstellung-unterueberschrift-mannschaft aufstellung-bordertop-small")
                                        .select("img")
                                    for (element in elements) {
                                        awayBadge = element.attr("src")
                                    }

                                    if(awayBadge.drop(35).takeWhile { it != '/' } == "wappen"){
                                        awayBadge = awayBadge.take(42) + "big/" + awayBadge.takeLastWhile { it != '/' }
                                    }
                                    else if(awayBadge.drop(35).takeWhile { it != '/' } == "flagge"){
                                        awayBadge = awayBadge.takeLastWhile { it != '/' }.takeWhile { it != '?' }
                                    }

                                    elements = document.getElementsByClass("sb-endstand")
                                    var score = elements.text().takeWhile { it != ' ' }
                                    score = score.takeWhile { it != ':' } + "-" + score.takeLastWhile { it != ':'}


                                    elements = document.getElementsByClass("sb-halbzeit")
                                    val extraTimeOrPenalties = if(elements[0].text().last() != ')') elements[0].text() else ""


                                    elements = document.getElementsByClass("spielername-profil")
                                    val competition = elements[0].text()

                                    elements = document.getElementsByClass("sb-datum hide-for-small")
                                    var stageAndDate = elements.text()
                                    while (stageAndDate.last()!='|')
                                        stageAndDate = stageAndDate.dropLast(1)
                                    stageAndDate = stageAndDate.dropLast(2)

                                    var stage = stageAndDate.takeWhile { it != '|' }.dropLast(1)
                                    if(stage.takeLast(10) == ". Matchday")
                                        stage = "Matchday " + stage.takeWhile { it != '.' }

                                    var date = stageAndDate.drop(stage.length+3).takeWhile { it != '|' }.dropLastWhile { it == ' ' }.dropWhile { it == ' ' }
                                    date = date.takeWhile { it != ',' } + ". " + date.dropWhile { it != '/' }.drop(1).takeWhile { it != '/' } + "/" + date.dropWhile { it != ' ' }.drop(1).dropLastWhile { it != '/' }.dropLast(1).dropLastWhile { it != '/' }.dropLast(1) + "/" + if(date.takeLast(2).toInt() >= 34) "19" else "20" + date.takeLast(2)

                                    elements = document.getElementsByClass("hide-for-small").select("a")
                                    val stadium = elements[elements.size-3].text()

                                    elements = document.getElementsByClass("aufstellung-spieler-container")
                                    for(element in elements) {
                                        Log.i("TEST",element.text())
                                    }


                                    val intent = Intent(this, DisplayStartingLineupActivity::class.java)

                                    intent.putExtra("homeTeam", homeTeam)
                                    intent.putExtra("awayTeam", awayTeam)
                                    intent.putExtra("homeColor1", homeColor1)
                                    intent.putExtra("homeColor2", homeColor2)
                                    intent.putExtra("awayColor1", awayColor1)
                                    intent.putExtra("awayColor2", awayColor2)
                                    intent.putExtra("homeBadge", homeBadge)
                                    intent.putExtra("awayBadge", awayBadge)
                                    intent.putExtra("score", score)
                                    intent.putExtra("extraTimeOrPenalties", extraTimeOrPenalties)
                                    intent.putExtra("competition", competition)
                                    intent.putExtra("stage", stage)
                                    intent.putExtra("date", date)
                                    intent.putExtra("stadium", stadium)

                                    startActivity(intent)

                                }
                                else{

                                    var name: String
                                    year.clear()
                                    team.clear()
                                    teamImage.clear()
                                    countryImage.clear()
                                    transferType.clear()

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

                                    elements = document.getElementsByClass("tm-player-transfer-history-grid__new-club").select("img")
                                    var imageUrl : String
                                    for(element2 in elements){
                                        imageUrl = element2.attr("data-srcset")
                                            .dropWhile { it.isWhitespace() }
                                            .dropLastWhile { it != ',' }
                                            .dropLast(4)

                                        if(imageUrl.take(42).takeLast(7) == "wappen/")
                                            teamImage.add(imageUrl.take(42) + "normquad/" + imageUrl.takeLastWhile { it != '/' })
                                    }

                                    /*
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
                                                imageUrl = element2.attr("src").dropLast(14)
                                                var counter2 = 0
                                                for (char in imageUrl) {
                                                    if (char == '/')
                                                        counter2++
                                                    if(counter2 <= 5)
                                                        imageUrl = imageUrl.drop(1)
                                                    else
                                                        break
                                                }
                                                teamImage.add("https://tmssl.akamaized.net/images/wappen/big$imageUrl")
                                            }
                                        }
                                        counter ++
                                    }
                                     */

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

                                        teamImage.add(teamImage.get(teamImage.size-2))
                                        /*
                                        try {
                                            document2 = Jsoup.connect(teamImage[teamImage.size-2]).get()
                                        } catch (e: IOException){
                                            Log.i("TEST","Error")
                                        }

                                        val elements2 = document2.getElementsByClass("dataBild")
                                            .select("img")
                                        var imageUrl : String
                                        for(element2 in elements2){
                                            imageUrl = element2.attr("src").dropLast(14)
                                            var counter2 = 0
                                            for (char in imageUrl) {
                                                if (char == '/')
                                                    counter2++
                                                if(counter2 <= 5)
                                                    imageUrl = imageUrl.drop(1)
                                                else
                                                    break
                                            }
                                            teamImage.add("https://tmssl.akamaized.net/images/wappen/big$imageUrl")
                                        }
                                         */
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

                                    val intent = Intent(this, DisplayTransferHistoryActivity::class.java)
                                    intent.putExtra("name", name)
                                    intent.putExtra("year", year as Serializable)
                                    intent.putExtra("team", team as Serializable)
                                    intent.putExtra("teamImage", teamImage as Serializable)
                                    intent.putExtra("countryImage", countryImage as Serializable)
                                    intent.putExtra("transferType", transferType as Serializable)
                                    startActivity(intent)
                                }
                                setShowDialog(false)
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

            LoadingDialog(
                showDialog,
            )
        }
    }
}


@Composable
fun LoadingDialog(
    showDialog: Boolean
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = {
            },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            )
        ) {
            Card(
                elevation = 8.dp,
            ) {
                Text(
                    text = "Loading... Please wait.",
                    fontWeight = FontWeight.Bold,
                    color = Color(
                        android.graphics.Color.parseColor("#133355")),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                )
            }
        }
    }
}
