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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.use.togiassesment.database.DBHelper
import com.use.togiassesment.service.Ability
import com.use.togiassesment.service.Form
import com.use.togiassesment.service.PokemonDetail
import com.use.togiassesment.service.PokemonListResponse
import com.use.togiassesment.service.PokemonResult
import com.use.togiassesment.ui.theme.TOGIAssesmentTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val receivedData = intent.getStringExtra("name") ?: "No data received"
            val receivedDataUrl = intent.getStringExtra("url") ?: "No data received"
            
            TOGIAssesmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(name = receivedData, url = receivedDataUrl)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, url: String, modifier: Modifier = Modifier) {
//    fetchDataFromApi(url)
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        Text(
            fontSize = 32.sp,
            text = name,
            modifier = modifier
        )
        Spacer(modifier = Modifier.size(16.dp))
        Image(
            painter = painterResource(id = R.drawable.poke),contentDescription = null, modifier = Modifier.size(100.dp))

        Spacer(modifier = Modifier.size(16.dp))

        PokemonDetailScreen(apiUrl = url)


    }
}


@Composable
fun PokemonDetailScreen(apiUrl: String) {
    var pokemonDetail by remember { mutableStateOf<PokemonDetail?>(null) }

    LaunchedEffect(true) {
        val detail = fetchPokemonDetails(apiUrl)
        pokemonDetail = detail
    }

    DisplayPokemonDetail(pokemonDetail)
}


@Composable
fun DisplayPokemonDetail(pokemonDetail: PokemonDetail?) {
    Column {
        when {
            pokemonDetail != null -> {
                Text(text = "Base Experience: ${pokemonDetail.baseExperience}")

                pokemonDetail.abilities.forEach { ability ->
                    Text(text = "Ability: ${ability.ability.name}, Slot: ${ability.slot}, Hidden: ${ability.isHidden}")
                }

                pokemonDetail.forms.forEach { form ->
                    Text(text = "Form Name: ${form.name}")
                }
            }
            pokemonDetail == null -> Text(text = "Fetching Pokemon details...")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TOGIAssesmentTheme {
        Greeting("Android", "URL")
    }
}




private fun getApiData(apiUrl: String): String {
    val url = URL(apiUrl)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    val response = StringBuilder()
    try {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection.disconnect()
    }
    return response.toString()
}
private suspend fun fetchPokemonDetails(apiUrl: String): PokemonDetail {
    val urlString = apiUrl
    return withContext(Dispatchers.IO) {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val response = StringBuilder()

        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()

        val gson = Gson()
        gson.fromJson(response.toString(), PokemonDetail::class.java)
    }
}

private fun parseJson(jsonString: String): PokemonDetail? {
    return try {
        val gson = Gson()
        gson.fromJson(jsonString, PokemonDetail::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
