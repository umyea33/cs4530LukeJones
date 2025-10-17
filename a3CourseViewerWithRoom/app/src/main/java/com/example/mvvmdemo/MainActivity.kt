package com.example.mvvmdemo

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mvvmdemo.ui.theme.ComposeDEMOTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mvvmdemo.room.CourseEntity
import com.example.mvvmdemo.ui.theme.LightRed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

    val dao=(application as TheApp).repository

    val tasksReadOnly : Flow<List<CourseEntity?>> = dao.allCourses
//    var id: Int = 0

    // my methods that will update the model
    fun addCourse (dep: String, num: String, loc: String){
        dao.addCourse(dep, num, loc)
    }

    fun deleteCourse(id: Int) {
        dao.deleteCourse(id)
    }

    fun switchToDetails(id: Int) {
        dao.updateMode(id, 1)
    }

    fun switchToDefault(id: Int) {
        dao.updateMode(id, 0)
    }

    fun switchToEdit(id: Int) {
        dao.updateMode(id, 2)
    }

    fun saveEdits(id: Int, dep: String, num: String, loc: String) {
        dao.updateCourse(id, dep, num, loc)
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

        val classList by myVM.tasksReadOnly.collectAsState(emptyList())
        var depText by remember {mutableStateOf("")}
        var numText by remember {mutableStateOf("")}
        var locText by remember {mutableStateOf("")}

        Row {
            OutlinedTextField(
                value = depText,
                onValueChange = { newText -> depText = newText },
                label = { Text("Dep") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = numText,
                onValueChange = { newText -> numText = newText },
                label = { Text("Number") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = locText,
                onValueChange = { newText -> locText = newText },
                label = { Text("Location") },
                modifier = Modifier.weight(2f)
            )
        }

        Spacer(Modifier.height(10.dp))
        Row {
            Button(onClick = {
                myVM.addCourse(depText, numText, locText)
                depText = ""
                numText = ""
                locText = ""
            }) {
                Text("Add Course")
            }
        }

        Row {
            LazyColumn {
                items(classList) {
                    course -> CourseItem(
                        course = course!!,
                        myVM = myVM
                    )
                }
            }
        }
    }
}

@Composable
fun CourseItem(course: CourseEntity, myVM: MyViewModel) {
    Spacer(Modifier.height(8.dp))
    when(course.mode) {
        0 -> {
            Box(Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .clickable(onClick = { myVM.switchToDetails(course.id) })) {
                Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.fillMaxHeight().weight(1f).padding(horizontal = 6.dp),
                        verticalArrangement = Arrangement.Center) {
                        Row (){
                            Text(course.dep)
                            Text(course.num)
                        }
                    }
                    Column(Modifier.padding(horizontal = 3.dp),
                        verticalArrangement = Arrangement.Center) {
                        Button(onClick = { myVM.switchToEdit(course.id) }) {
                            Text("Edit")
                        }
                    }
                    Column(Modifier.padding(horizontal = 3.dp),
                        verticalArrangement = Arrangement.Center) {
                        Button(onClick = { myVM.deleteCourse(course.id) }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
        1 -> {
            Box(Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .clickable(onClick = { myVM.switchToDefault(course.id) })) {
                Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.fillMaxHeight().weight(1f).padding(horizontal = 6.dp),
                        verticalArrangement = Arrangement.Center) {
                        Row (){
                            Text(course.dep)
                            Text(course.num)
                        }
                    }
                    Column(Modifier.fillMaxHeight().weight(1f).padding(horizontal = 6.dp),
                        verticalArrangement = Arrangement.Center) {
                        Row (){
                            Text(course.loc)
                        }
                    }
                    Column(Modifier.padding(horizontal = 3.dp),
                        verticalArrangement = Arrangement.Center) {
                        Button(onClick = { myVM.switchToEdit(course.id) }) {
                            Text("Edit")
                        }
                    }
                    Column(Modifier.padding(horizontal = 3.dp),
                        verticalArrangement = Arrangement.Center) {
                        Button(onClick = { myVM.deleteCourse(course.id) }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
        2 -> {
            Box(Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .clickable(onClick = { myVM.switchToDefault(course.id) })) {
                Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    var tempDep by remember { mutableStateOf(course.dep) }
                    Column(Modifier.fillMaxHeight().weight(1f).padding(horizontal = 6.dp),
                        verticalArrangement = Arrangement.Center) {
                        TextField(
                            value = tempDep,
                            onValueChange = { tempDep = it },
                            //singleLine = true,
                            textStyle = TextStyle(fontSize = 16.sp),
                            modifier = Modifier.width(70.dp).height(48.dp)
                        )
                    }
                    var tempNum by remember { mutableStateOf(course.num) }
                    Column(Modifier.fillMaxHeight().weight(1f).padding(horizontal = 6.dp),
                        verticalArrangement = Arrangement.Center) {
                        TextField(
                            value = tempNum,
                            onValueChange = { tempNum = it },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 16.sp),
                            modifier = Modifier.width(75.dp).height(48.dp)
                        )
                    }
                    var tempLoc by remember { mutableStateOf(course.loc) }
                    Column(Modifier.fillMaxHeight().weight(1f).padding(horizontal = 6.dp),
                        verticalArrangement = Arrangement.Center) {
                        TextField(
                            value = tempLoc,
                            onValueChange = { tempLoc = it },
                            textStyle = TextStyle(fontSize = 16.sp),
                            modifier = Modifier.width(125.dp).height(48.dp)
                        )
                    }
                    Column(Modifier.padding(horizontal = 3.dp),
                        verticalArrangement = Arrangement.Center) {
                        Button(onClick = { myVM.saveEdits(course.id, tempDep, tempNum, tempLoc) }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}