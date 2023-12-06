package com.use.togiassesment

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.use.togiassesment.database.DBHelper
import com.use.togiassesment.database.PokemonContract
import com.use.togiassesment.service.PokemonListResponse
import com.use.togiassesment.service.PokemonResult
import com.use.togiassesment.ui.theme.TOGIAssesmentTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

val itemsIndexedList = listOf("Asha Levine",
    "Manon Rollins",
    "Amaya Benson",
    "Phillippa Harrell",
    "Britney Castillo",
    "Ellen Reyes",
    "Kaidan Frame")

val listPokemon = mutableListOf<PokemonResult>()

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        fetchDataFromApi()
        val dbHelper = DBHelper(this)
        val pokemonList = getPokemonData(dbHelper)

        setContent {
            TOGIAssesmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DisplayPokemonList(dbHelper = dbHelper)
                }
            }
        }
    }

    private fun fetchDataFromApi() {
        val url = "https://pokeapi.co/api/v2/pokemon/"

        GlobalScope.launch(Dispatchers.IO) {
            val response = getApiData(url)
            val pokemonList = parseJson(response)

            // Handle the parsed data here (update UI, etc.)
            pokemonList?.results?.forEach { pokemonResult ->
                println("Pokemon Name: ${pokemonResult.name}")
                println("Pokemon URL: ${pokemonResult.url}")
                listPokemon.add(pokemonResult)

                val dbHelper = DBHelper(this@HomeActivity)
                insertPokemonData(dbHelper, pokemonResult.name, pokemonResult.url)

            }
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

    private fun parseJson(jsonString: String): PokemonListResponse? {
        return try {
            val gson = Gson()
            gson.fromJson(jsonString, PokemonListResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun insertPokemonData(dbHelper: DBHelper, name: String, url: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(PokemonContract.PokemonEntry.COLUMN_NAME, name)
            put(PokemonContract.PokemonEntry.COLUMN_URL, url)
        }

        val newRowId = db.insert(PokemonContract.PokemonEntry.TABLE_NAME, null, values)
    }




}

fun getPokemonData(dbHelper: DBHelper): List<PokemonResult> {
    val pokemonList = mutableListOf<PokemonResult>()
    val db = dbHelper.readableDatabase

    val projection = arrayOf(
        PokemonContract.PokemonEntry.COLUMN_NAME,
        PokemonContract.PokemonEntry.COLUMN_URL
    )

    val sortOrder = "${PokemonContract.PokemonEntry._ID} DESC"

    val cursor = db.query(
        PokemonContract.PokemonEntry.TABLE_NAME,
        projection,
        null,
        null,
        null,
        null,
        sortOrder
    )

    with(cursor) {
        while (moveToNext()) {
            val name = getString(getColumnIndexOrThrow(PokemonContract.PokemonEntry.COLUMN_NAME))
            val url = getString(getColumnIndexOrThrow(PokemonContract.PokemonEntry.COLUMN_URL))

            println("Pokemon Name: $name")
            println("Pokemon URL: $url")

            pokemonList.add(PokemonResult(name, url))
        }
    }

    cursor.close()
    return pokemonList
}

@Composable
fun DisplayPokemonList(dbHelper: DBHelper) {
    var pokemonList by remember { mutableStateOf(listOf<PokemonResult>()) }

    LaunchedEffect(true) {
        pokemonList = getPokemonData(dbHelper)
    }

    PokemonList(pokemonList)
}
@Composable
fun PokemonList(pokemonList: List<PokemonResult>) {
    val context = LocalContext.current

    LazyColumn {
        items(pokemonList) { pokemon ->
//            Text(text = pokemon.name)
            GreetingView(name = pokemon.name) {
                Toast.makeText(context, pokemon.name, Toast.LENGTH_SHORT).show()
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("name", pokemon.name)
                intent.putExtra("url", pokemon.url)
                context.startActivity(intent)
            }
        }
    }
}



@Composable
private fun GreetingList(greetings: List<PokemonResult>) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(count = greetings.size) {
            val pokemon = greetings[it]
            GreetingView(name = pokemon.name) {
                Toast.makeText(context, pokemon.name, Toast.LENGTH_SHORT).show()
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("name", pokemon.name)
                context.startActivity(intent)
            }
        }
    }
}

@Composable
private fun GreetingView(name: String, onClick: (msg: String) -> Unit) {
    val msg = "Pokemon $name"

    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick(msg) }
    ) {
        Row(modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()) {
            Text(text = msg)
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun GreetingListPreview() {
    MaterialTheme {
        GreetingList(listPokemon)
    }
}

