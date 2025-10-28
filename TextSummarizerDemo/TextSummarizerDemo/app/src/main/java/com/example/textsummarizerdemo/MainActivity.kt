package com.example.textsummarizerdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.textsummarizerdemo.ui.theme.ComposeDEMOTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.google.ai.client.generativeai.BuildConfig
import com.example.textsummarizerdemo.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MyViewModel : ViewModel()
{
    private val summaryMutable = MutableStateFlow("")
    val summary = summaryMutable.asStateFlow()
    private val model = GenerativeModel(modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY)


    fun summarizeText(input: String) {
        viewModelScope.launch {
            try {
                val response = model.generateContent("Summarize this: $input")
                summaryMutable.value = response.text ?: "No summary"
            } catch (e: Exception) {
                summaryMutable.value = "Error: ${e.message}"
                Log.e("SummarizeError", e.toString())
            }
        }
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDEMOTheme {
               val vm:MyViewModel = viewModel()
               SummarizerUI (vm)
            }
        }
    }
}

@Composable
fun SummarizerUI(myVM: MyViewModel) {

    val summary by myVM.summary.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(Modifier.fillMaxWidth().padding(50.dp).verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Row {
            OutlinedTextField(
                value = inputText,
                onValueChange = { newText -> inputText = newText},
                label = { Text("Text to summarize") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row {
            Button(onClick = {

                myVM.summarizeText(inputText)
                                inputText=""
            }) {
                Text("Summarize")
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Summary", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color.Blue)
        Row{
                Text(summary)
        }
    }
}

