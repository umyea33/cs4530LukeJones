package com.example.funfacts

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.funfacts.ui.theme.ComposeDEMOTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.funfacts.room.Fact
import kotlinx.coroutines.flow.Flow

object MyViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TheApp
            MyViewModel(app)
        }
    }
}
class MyViewModel(application: Application) : AndroidViewModel(application)
{

    val repo=(application as TheApp).repository

    val factsReadOnly : Flow<List<Fact?>> = repo.allFacts

    // my methods that will update the model
    fun addFact () {
        repo.fetchNewFact()
    }

    fun deleteFact(id: Int) {
        repo.deleteFact(id)
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDEMOTheme {
               val vm: MyViewModel by viewModels { MyViewModelProvider.Factory }
               TodoList (vm)
            }
        }
    }
}

@Composable
fun TodoList(myVM: MyViewModel) {
    Column(Modifier.fillMaxWidth().padding(vertical = 50.dp, horizontal = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        val factList by myVM.factsReadOnly.collectAsState(emptyList())

        Spacer(Modifier.height(10.dp))

        Row {
            Button(onClick = {
                myVM.addFact()
            }) {
                Text("Fetch Fact")
            }
        }

        Spacer(Modifier.height(10.dp))

        Row {
            Text("Fun Facts List",fontSize = 25.sp,  fontWeight = FontWeight.ExtraBold, color = Color.Blue)
        }

        Spacer(Modifier.height(10.dp))

        Row {
            LazyColumn {
                items(factList) {
                    fact -> FactItem(
                        fact = fact!!,
                        myVM = myVM
                    )
                }
            }
        }
    }
}

@Composable
fun FactItem(fact: Fact, myVM: MyViewModel) {
    Spacer(Modifier.height(8.dp))
    Box(Modifier
        .fillMaxWidth()
        .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
        .background(Color.White, shape = RoundedCornerShape(8.dp))) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(
                Modifier.fillMaxHeight().weight(1f).padding(horizontal = 6.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row() {
                    Text(fact.text)
                }
            }
            Column(
                Modifier.padding(horizontal = 3.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { myVM.deleteFact(fact.id) }) {
                    Text("Delete")
                }
            }
        }
    }
}