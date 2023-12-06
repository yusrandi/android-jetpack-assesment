package com.use.togiassesment

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.use.togiassesment.ui.theme.TOGIAssesmentTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TOGIAssesmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Text(
                fontSize = 16.sp,
                text = "POKEDEX",
                modifier = modifier
            )

            Spacer(modifier = Modifier.size(16.dp))
            Image(
                painter = painterResource(id = R.drawable.poke),contentDescription = null, modifier = Modifier.size(300.dp))

            Spacer(modifier = Modifier.size(16.dp))

            OutlinedButton(
                onClick = {
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                }) {
                Text(
                    text = "START",

                    )
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        TOGIAssesmentTheme {
            Greeting("Android")
        }
    }
}

