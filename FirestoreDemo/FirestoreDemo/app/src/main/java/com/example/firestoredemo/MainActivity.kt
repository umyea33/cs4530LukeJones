package com.example.firestoredemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.firestoredemo.ui.theme.SensorListDemoTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SensorListDemoTheme {
               AuthScreen ()
            }
        }
    }
}
@Composable
fun AuthScreen() {
    val auth = Firebase.auth
    val db = Firebase.firestore
    val scope = rememberCoroutineScope()
    var user by remember { mutableStateOf(auth.currentUser) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var dataString by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(Modifier.padding(15.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp),

            )
        if (user == null) {
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = password, onValueChange = { password = it },
                label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = {
                    scope.launch {
                        try {
                            auth.signInWithEmailAndPassword(email, password).await()
                            user = auth.currentUser
                        } catch (e: Exception) {
                            error = e.message
                        }
                    }
                }, modifier = Modifier.width(120.dp)) { Text("Login") }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    scope.launch {
                        try {
                            auth.createUserWithEmailAndPassword(email, password).await()
                            user = auth.currentUser
                        } catch (e: Exception) {
                            error = e.message
                        }
                    }
                }, modifier = Modifier.width(120.dp) ) { Text("Sign Up") }
            }

            error?.let { Text(it, color = Color.Red) }

        // after user logs in
        } else {
            var dataString by remember { mutableStateOf("") }
            var message by remember { mutableStateOf("") }

            LaunchedEffect(user) {
                if (user != null) {
                    try {
                        val snapshot = db.collection("DemoDir").get().await()
                        val doc = snapshot.firstOrNull()
                        dataString = doc?.data?.toString() ?: "No data found"
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error fetching", e)
                    }
                }
            }

            Column {
                Text("Welcome ${user!!.email}")
                Text("Data: $dataString")
                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = {
                    val newData = hashMapOf(
                        "User: " to user!!.email,
                        "Timestamp: " to System.currentTimeMillis(),
                        "Comment: " to "Saving to Firestore!"
                    )
                    db.collection("DemoDir").add(newData)
                        .addOnSuccessListener {
                            message = "Data saved with ID: ${it.id}"
                        }
                        .addOnFailureListener { e ->
                            message = "Error saving data: ${e.message}"
                        }
                }, modifier = Modifier.width(150.dp)) {
                    Text("Add Demo Data")
                }

                if (message.isNotEmpty()) Text(message)
                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = {
                    Firebase.auth.signOut()
                    user = null
                }, modifier = Modifier.width(150.dp)) {
                    Text("Sign out")
                }
            }

        }
    }
}



