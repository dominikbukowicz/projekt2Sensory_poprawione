package com.example.lab2

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab2.ui.theme.Lab2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2Theme {

                val navController = rememberNavController() //NAWIGACJA
                NavHost(navController = navController, startDestination = "wybierz") {
                    composable(route = "wybierz"){
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally){

                            Text(text = "Wybierz czujnik", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            TextButton(onClick = {
                                    navController.navigate("czujnik_swiatla")
                            }) {
                                Text(text = "Czujnik światła", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            }
                            TextButton(onClick = {
                                navController.navigate("czujnik_odl")
                            }) {
                                Text(text = "Czujnik odległości", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            }
                        }

                    }
                    composable(route = "czujnik_swiatla"){

                        val ctx = LocalContext.current
                        val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
                        val lightSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
                        val sensorStatus = remember {
                            mutableStateOf("")
                        }

                        val lightSensorEventListener = object : SensorEventListener {
                            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                            }
                            override fun onSensorChanged(event: SensorEvent) {
                                if (event.sensor.type == Sensor.TYPE_LIGHT) {
                                    if (event.values[0] > 8000f) {
                                        sensorStatus.value = "Bardzo ciemno"
                                    }
                                    if (event.values[0] < 8000f) {
                                        sensorStatus.value = "Ciemno"
                                    }
                                    if(event.values[0] < 4000f) {
                                        sensorStatus.value = "Jasno"
                                    }
                                }
                            }
                        }
                        DisposableEffect(Unit) {
                            sensorManager.registerListener(
                                lightSensorEventListener,
                                lightSensor,
                                SensorManager.SENSOR_DELAY_NORMAL
                            )
                            onDispose {
                                sensorManager.unregisterListener(lightSensorEventListener)
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .background(
                                    when (sensorStatus.value) {
                                        "Bardzo ciemno" -> Color.DarkGray
                                        "Ciemno" -> Color.Gray
                                        "Jasno" -> Color.White
                                        else -> Color.Cyan
                                    }
                                )
                                .padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Jest: \n" +
                                        "\n" +
                                        "${sensorStatus.value}",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default,
                                fontSize = 40.sp, modifier = Modifier.padding(5.dp)
                            )
                            TextButton(onClick = {
                                navController.navigate("wybierz")
                            }) {
                                Text(text = "Back", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            }

                        }

                    }
                    composable(route = "czujnik_odl"){

                        val ctx = LocalContext.current
                        val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
                        val proximitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
                        val sensorStatus = remember {
                            mutableStateOf("")
                        }
                        val proximitySensorEventListener = object : SensorEventListener {
                            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                            }
                            override fun onSensorChanged(event: SensorEvent) {
                                if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                                    if (event.values[0] == 0f) {
                                        sensorStatus.value = "Blisko"
                                    }
                                    if(event.values[0] < 5f) {
                                        sensorStatus.value = "Daleko"
                                    }
                                    if(event.values[0] > 5f) {
                                        sensorStatus.value = "Bardzo daleko"
                                    }
                                }
                            }
                        }

                        DisposableEffect(Unit) {
                            sensorManager.registerListener(
                                proximitySensorEventListener,
                                proximitySensor,
                                SensorManager.SENSOR_DELAY_NORMAL
                            )
                            onDispose {
                                sensorManager.unregisterListener(proximitySensorEventListener)
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .background(
                                    when (sensorStatus.value) {
                                        "Bardzo daleko" -> Color.DarkGray
                                        "Daleko" -> Color.Gray
                                        "Blisko" -> Color.White
                                        else -> Color.Cyan
                                    }
                                )
                                .padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Obiekt jest: \n\n${sensorStatus.value}",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default,
                                fontSize = 40.sp, modifier = Modifier.padding(5.dp)
                            )
                            TextButton(onClick = {
                                navController.navigate("wybierz")
                            }) {
                                Text(text = "Back", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            }
                        }
                    }

                }
                //CzujnikSwiatla()
                //ProximitySensor()
            }
        }
    }
}