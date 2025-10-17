package com.example.storagedemo

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.storagedemo.ui.theme.ComposeDEMOTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File

val Context.dataStore by preferencesDataStore("settings")

val THEME_KEY = booleanPreferencesKey("dark_theme")
val COUNTER_KEY = intPreferencesKey ("counter")

suspend fun saveTheme(context: Context, isDark: Boolean){
    context.dataStore.edit {
        it[THEME_KEY]=isDark
    }
}

suspend fun incrementCounter(context: Context){
    context.dataStore.edit {
        val currValue = it[COUNTER_KEY]?:0
        it[COUNTER_KEY]= currValue + 1
    }
}

// Writing to internal storage
fun writeToInternal(context: Context, filename: String, content: String){
    context.openFileOutput(filename, Context.MODE_PRIVATE).use{
        it.write(content.toByteArray())
    }
}

// Reading from internal storage
fun readInternal(context: Context, filename: String) : String {
    return context.openFileInput(filename).bufferedReader().readText()
}


// Writing to Cache
fun writeCache (context: Context, filename: String, content:String){
    val file = File(context.cacheDir, filename)
    file.writeText(content)
}

// Writing to external storage (app-specific folder)
fun writeExternal (context: Context, filename: String, content:String){
    val file = File(context.getExternalFilesDir(null), filename)
    file.writeText(content)
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDEMOTheme {
                PersistentScreen(this)
            }
        }
    }
}

@Composable
fun PersistentScreen(context: Context) {
    var input by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val theme by context.dataStore.data.map {
        it[THEME_KEY] ?: false
    }.collectAsState(false)

    val counter by context.dataStore.data.map {
        it[COUNTER_KEY] ?: 0
    }.collectAsState(0)

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Spacer(Modifier.height(40.dp))
        Text("DataStore Preferences", fontSize = 20.sp, fontWeight = FontWeight.Bold, color=Color.Blue)
        Text("Dark theme: $theme")
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                saveTheme(context, !theme)
            }
        }) {
            Text("Change Theme")
        }

        Spacer(Modifier.height(10.dp))

        Text("Counter: $counter")
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                incrementCounter(context)
            }
        }) {
            Text("Increment")
        }

        Spacer(Modifier.height(20.dp))

        Text("Files", fontSize = 20.sp, fontWeight = FontWeight.Bold, color=Color.Blue)
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Content") }
        )

        Spacer(Modifier.height(10.dp))

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                writeToInternal(context, "myfile.txt", input)
                input = ""
            }
        }) {
            Text("Save File (Internal Storage)")
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                content = readInternal(context, "myfile.txt")
            }
        }) {
            Text("Read File (Internal Storage)")
        }
        Text("Content: $content", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(40.dp))

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                writeCache(context, "myfile.txt", input)
                input = ""
                status = "Cache File Written"
            }
        }) {
            Text("Save to Cache")
        }

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                writeExternal(context, "myfile.txt", input)
                input = ""
                status = "Written to app-specific external folder"
            }
        }) {
            Text("Save to app-specific external folder")
        }
        Text(status)
    }

}





