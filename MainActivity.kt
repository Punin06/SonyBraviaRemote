package com.example.braviaremote

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextInputService
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.braviaremote.ui.theme.BraviaRemoteTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import java.net.HttpURLConnection
import java.net.URL
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.OutputStream
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import io.ktor.client.engine.cio.*
import io.ktor.client.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.ktor.serialization.kotlinx.xml.xml
import nl.adaptivity.xmlutil.serialization.XML
import io.ktor.serialization.kotlinx.xml.*
import nl.adaptivity.xmlutil.*
import nl.adaptivity.xmlutil.serialization.*



class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BraviaRemoteTheme {

                //setting the padding and dimensions of the app

                Column (modifier = Modifier.padding(16.dp).fillMaxHeight().fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {

                    //creating a scope to run functions requiring net connection as in internet

                    val scope = rememberCoroutineScope()

                    //Setting the buttons in row
                    Row {
                        //Creating the power button and calling the power function to send the request to the api to turn the tv on or off
                        Button(onClick = {
                            scope.launch {
                                val temp = Power()
                                Toast.makeText(this@MainActivity, "$temp", Toast.LENGTH_SHORT)
                                    .show()

                            }
                        }
                        ) { Text(text = "POWER", fontWeight = FontWeight.Bold) }

                        Spacer(modifier = Modifier.padding(10.dp))

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row (horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {

                        Button(onClick = {
                            //Creating the volume up or plus button.
                            //calling function to send request to sony api to increase the volume 
                            scope.launch {
                                val temp = plus()
                                Toast.makeText(this@MainActivity, "$temp", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }) { Text(text = "+", fontWeight = FontWeight.Bold, fontSize = 20.sp) }

                        Button(onClick = {
                            //Creating the volume down/minus button.
                            //calling function to send request to sony api to decrease the volume 
                            scope.launch {
                                val temp = minus()
                                Toast.makeText(this@MainActivity, "$temp", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }) { Text(text = "-", fontWeight = FontWeight.Bold, fontSize = 20.sp) }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                        val c = TextF1()

                        Spacer(modifier = Modifier.padding(10.dp))

                        Button(onClick = {
                            //Creating the set volume button.
                            //calling function to send request to sony api to set the volume to user choice
                            scope.launch {
                                val temp = SetVol(c)
                                Toast.makeText(this@MainActivity, " $temp", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }) { Text(text = "SET VOLUME", fontWeight = FontWeight.Bold) }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(onClick = {
                            //Creating the volume mute button.
                            //calling function to send request to sony api to mute the volume 
                            scope.launch {
                                val temp = mute()
                                Toast.makeText(this@MainActivity, "$temp", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }) { Text(text = "MUTE", fontWeight = FontWeight.Bold) }

                    Spacer(modifier = Modifier.height(10.dp))

                        val port = TextF1()

                        Spacer(modifier = Modifier.padding(10.dp))

                        Button(onClick = {
                            //Creating the set hdmi button.
                            //calling function to send request to sony api to set the hdmi to user choice
                            scope.launch {
                                val temp = hdmi(port)
                                Toast.makeText(this@MainActivity, "$temp", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }) { Text("SET HDMI", fontWeight = FontWeight.Bold) }

Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {

    Button(onClick = {
        scope.launch {
            //Creating the rewind/prev button for video control.
            //calling function to send request to sony api to rewind the video 
            val temp = prev()
            Toast.makeText(this@MainActivity, "$temp", Toast.LENGTH_SHORT).show()
        }
    }) { Text("REWIND", fontWeight = FontWeight.Bold) }

    Button(onClick = {
        scope.launch {
            //Creating the play button for video control.
            //calling function to send request to sony api to play the video 
            val temp = play()
            Toast.makeText(this@MainActivity, "$temp", Toast.LENGTH_SHORT).show()
        }
    }) { Text("PLAY/PAUSE", fontWeight = FontWeight.Bold) }

    Button(onClick = {
        scope.launch {
            //Creating the forward/next button for video control.
            //calling function to send request to sony api to fast forward the video 
            val temp = next()
            Toast.makeText(this@MainActivity, "$temp", Toast.LENGTH_SHORT).show()
        }
    }) { Text("FORWARD", fontWeight = FontWeight.Bold) }

}
                    Button(onClick = {
                        scope.launch {
                            //Creating the pause for video control.
                            //calling function to send request to sony api to pause the video 
                            val temp = pause()
                            Toast.makeText(this@MainActivity,"$temp",Toast.LENGTH_SHORT).show()
                        }

                    }) { Text("PAUSE", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

//function to run the pause button
suspend fun pause():Int
{
//creating client 
val client = HttpClient(CIO){
    install(ContentNegotiation)
    {
        xml(format = XML{
            xmlDeclMode = XmlDeclMode.Charset
        })
    }
    //setting the default request such as url, header.
    defaultRequest {
        url("http://192.168.1.1/sony/") 
        // the url uses the ip to go to the api site, the ip is set to a default
        //this needs to be changed to the same ip that the tv is using to use this app
        header("X-Auth-PSK","1234") 
        // this is the authetication key, this is set to 1234 for default purposes
        //it has to be changed to the same key set for the tv
        //soapaction to the required api service
        header("SOAPACTION","\"urn:schemas-sony-com:service:IRCC:1#X_SendIRCC\"")
    }
}
//setting the payload
    val payload = """
        <?xml version="1.0" encoding="UTF-8"?>
        <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"
            s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
            <s:Body>
                <u:X_SendIRCC xmlns:u="urn:schemas-sony-com:service:IRCC:1">
                    <IRCCCode>AAAAAgAAAJcAAAAZAw==</IRCCCode>
                </u:X_SendIRCC>
            </s:Body>
        </s:Envelope>
    """.trimIndent()

    val response = client.post("ircc"){
        contentType(ContentType.Application.Xml)
        setBody(payload)
    }
    //request status code, to verify if everything running perfectly
    val temp = response.status.value
    client.close()
    return temp
}

//function to run the play button
suspend fun play():Int
{
    //Creating client setting url, header, soapaction 
    val client = HttpClient(CIO){
        install(ContentNegotiation)
        {
            xml(format = XML{xmlDeclMode = XmlDeclMode.Charset})

        }

        defaultRequest {
            url("http://192.168.1.1/sony/")
            header("SOAPACTION","\"urn:schemas-sony-com:service:IRCC:1#X_SendIRCC\"")
            header("X-Auth-PSK", "1234")
        }
    }
    //setting payload
    val soapPayload = """
        <?xml version="1.0" encoding="UTF-8"?>
        <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"
            s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
            <s:Body>
                <u:X_SendIRCC xmlns:u="urn:schemas-sony-com:service:IRCC:1">
                    <IRCCCode>AAAAAgAAAJcAAAAaAw==</IRCCCode>
                </u:X_SendIRCC>
            </s:Body>
        </s:Envelope>
    """.trimIndent()

    val response = client.post("ircc")
    {
        contentType(ContentType.Application.Xml)
        setBody(soapPayload)

    }
    //getting response and returning it to verify if it's running
    val stat = response.status.value
    client.close()
    return stat
}

//function to run the rewind button
suspend fun prev():Int{
    //Creating client setting url, header, soapaction 
    val client = HttpClient(CIO)
    {
        defaultRequest {
            url("http://192.168.1.1/sony/ircc")
            header("X-Auth-PSK","1234")
            header("SOAPACTION", "\"urn:schemas-sony-com:service:IRCC:1#X_SendIRCC\"")
        }
    }
//setting payload
    val soapPayload = """
        <?xml version="1.0" encoding="UTF-8"?>
        <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"
            s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
            <s:Body>
                <u:X_SendIRCC xmlns:u="urn:schemas-sony-com:service:IRCC:1">
                    <IRCCCode>AAAAAgAAAJcAAAB5Aw==</IRCCCode>
                </u:X_SendIRCC>
            </s:Body>
        </s:Envelope>
    """.trimIndent()

    //sending request, getting response to verify if it's running
    val response = client.post(""){
        contentType(ContentType.Application.Xml)
        setBody(soapPayload)
    }

    val temp = response.status.value
    client.close()
    return temp
}

//function to run the forward button

suspend fun next():Int{
    //Creating client setting url, header, soapaction, then payload
    val client = HttpClient(CIO)
    {
        defaultRequest {
            url("http://192.168.1.1/sony/ircc")
            header("X-Auth-PSK","1234")
            header("SOAPACTION", "\"urn:schemas-sony-com:service:IRCC:1#X_SendIRCC\"")
        }
    }
    
    val soapPayload = """
        <?xml version="1.0" encoding="UTF-8"?>
        <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"
            s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
            <s:Body>
                <u:X_SendIRCC xmlns:u="urn:schemas-sony-com:service:IRCC:1">
                    <IRCCCode>AAAAAgAAAJcAAAB4Aw==</IRCCCode>
                </u:X_SendIRCC>
            </s:Body>
        </s:Envelope>
    """.trimIndent()

    //sending request and getting status code to verify if it's running
    val response = client.post(""){
        contentType(ContentType.Application.Xml)
        setBody(soapPayload)
    }

    val temp = response.status.value
    client.close()
    return temp
}

////function to run the hdmi button

suspend fun hdmi(port:String):Int
{
    //placeholder to hold the value of hdmi port
    val port = port.filterNot { it.isWhitespace() }
    //Creating client setting url, header, soapaction, then payload
    val client = HttpClient(CIO)
    {
        install(ContentNegotiation){
            json(Json{
                isLenient= true
                ignoreUnknownKeys= true
            })
        }
        defaultRequest {
            url("http://192.168.1.1/sony/")
            header("X-Auth-PSK","1234")
        }
    }
    //sending request, getting status, to verify if running
    val response = client.post("avContent"){
        contentType(ContentType.Application.Json)
        setBody(play("setPlayContent", 8, listOf(Params4("extInput:hdmi?port=$port")), "1.0"))
    }
    val status = response.status.value
    client.close()
    return status
}

//Setting up more dictionaries/list equivalents of kotlin to send as a request to the sony bravia api
//Params4 and Params3 are generic lists.
@Serializable
data class play(val method: String, val id: Int, val params: List<Params4>, val version: String)

@Serializable
data class Params4(val uri:String)

@Serializable
data class mute(val method: String, val id: Int, val params: List<Params3>, val version: String)

@Serializable
data class Params3(val status: Boolean)

//function to run the mute button
suspend fun mute(): Int{
    //Creating client setting url, header, soapaction, then payload
    val client = HttpClient(CIO)
    {
        defaultRequest {
            url("http://192.168.1.1/sony/ircc")
            header("X-Auth-PSK","1234")
            header("SOAPACTION", "\"urn:schemas-sony-com:service:IRCC:1#X_SendIRCC\"")
        }
    }

    val soapPayload = """
        <?xml version="1.0" encoding="UTF-8"?>
        <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"
            s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
            <s:Body>
                <u:X_SendIRCC xmlns:u="urn:schemas-sony-com:service:IRCC:1">
                    <IRCCCode>AAAAAQAAAAEAAAAUAw==</IRCCCode>
                </u:X_SendIRCC>
            </s:Body>
        </s:Envelope>
    """.trimIndent()
//sending request, getting status code to verify if working
    val response = client.post(""){
        contentType(ContentType.Application.Xml)
        setBody(soapPayload)
    }

    val temp = response.status.value
    client.close()
    return temp
}

@Composable
//setting textfield for input of set volume and set hdmi
fun TextF1():String{
    var  text  by remember { mutableStateOf(" ") }
   TextField(value = text, onValueChange = {text = it} , label = {Text("label")}, singleLine = true)
    return text

}

//function to run the set volume button

suspend fun SetVol(vol:String):Int{
    //setting placeholder for volume value
    Log.d("Volume","$vol")
    val vol = vol.filterNot { it.isWhitespace() }

    //Creating client setting url, header, soapaction, then payload

    val client = HttpClient(CIO){
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
            defaultRequest{
                url("http://192.168.1.1/sony/")
                header("X-Auth-PSK", "1234")
        }
    }
    //sending request and getting the status code verified to see if running
    val power = client.post("audio") {
        contentType(ContentType.Application.Json)
        setBody(Request2(method = "setAudioVolume", id = 5, params = listOf(Params2(target = "speaker", volume = vol)), version = "1.0"))}
    val status = power.status.value
    client.close()
    return status
}

//function to run the power button
suspend fun Power():Int {

    val client = HttpClient(CIO){
        //Creating client setting url, header, soapaction, then payload
        install(ContentNegotiation)
        {
            xml(format = XML{xmlDeclMode = XmlDeclMode.Charset})

        }

        defaultRequest {
            url("http://192.168.1.1/sony/")
            header("SOAPACTION","\"urn:schemas-sony-com:service:IRCC:1#X_SendIRCC\"")
            header("X-Auth-PSK", "1234")
        }
    }

    val soapPayload = """
        <?xml version="1.0" encoding="UTF-8"?>
        <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"
            s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
            <s:Body>
                <u:X_SendIRCC xmlns:u="urn:schemas-sony-com:service:IRCC:1">
                    <IRCCCode>AAAAAQAAAAEAAAAVAw==</IRCCCode>
                </u:X_SendIRCC>
            </s:Body>
        </s:Envelope>
    """.trimIndent()
//sending request, getting status code and checking if it's running
    val response = client.post("ircc")
    {
        contentType(ContentType.Application.Xml)
        setBody(soapPayload)

    }

    val status = response.status.value
    client.close()
    return status
}

//function to run the volume up button
suspend fun plus():Int {
    //Creating client setting url, header, soapaction, then payload
    val client = HttpClient(CIO) {
        install(ContentNegotiation){
            json(Json{
                isLenient = true
                ignoreUnknownKeys = true})

        }
        defaultRequest {
            url("http:/192.168.1.1/sony/")
            header("X-Auth-PSK","1234")
        }
    }
//sending request and to get status code to verify if it's working
    val power = client.post("audio") {
        contentType(ContentType.Application.Json)
        setBody(Request2(method = "setAudioVolume", id = 4, params = listOf(Params2(target = "speaker", volume = "+1" )), version = "1.0"))}
    val status = power.status.value
    client.close()
    return status
}

//function to run the volume down button

suspend fun minus():Int{
    //Creating client setting url, header, soapaction, then payload
    val client = HttpClient(CIO){
        install(ContentNegotiation)
        {
            json(Json { isLenient=true
            ignoreUnknownKeys= true})
        }
        defaultRequest {
            url("http://192.168.1.1/sony/")
            header("X-Auth-PSK","1234")
        }

    }

    //sending request, getting status code, verifying it

    val power = client.post("audio"){
        contentType(ContentType.Application.Json)
        setBody(Request2(method = "setAudioVolume", id = 4, params = listOf(Params2(target = "speaker", volume = "-1" )), version = "1.0"))
    }
    val status = power.status.value
    client.close()
    return status
}

//Creating the dictionary equivalent in Kotlin to send as a request to execute the functionalities of the remote such as:
//Volume up, down, set hdmi, mute, power etc.

@Serializable
data class Request2(val method: String, val id:Int,val params:List<Params2>,val version: String)

@Serializable
data class Params2(val target: String, val volume:String)

@Serializable
data class Rm(val id:Int,
              val name:String) 
@Serializable
data class Or(val name:String)

@Serializable
data class Request(val method: String, val id:Int,val params:List<Params>,val version: String)

@Serializable
data class Params(val status: Boolean)

