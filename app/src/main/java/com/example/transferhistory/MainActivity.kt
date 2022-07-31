package com.example.transferhistory

import android.content.Intent
import android.os.Bundle
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


class MainActivity : ComponentActivity() {
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
                        val intent = Intent(this, DisplayActivity::class.java)
                        if(!text.toString().isEmpty()){
                            intent.putExtra("url", text.toString())
                            startActivity(intent)
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