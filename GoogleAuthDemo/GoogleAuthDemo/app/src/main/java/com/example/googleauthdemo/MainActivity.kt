package com.example.googleauthdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.googleauthdemo.ui.theme.SensorListDemoTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SensorListDemoTheme {
               GoogleAuthScreen ()
            }
        }
    }
}

@Composable
fun GoogleAuthScreen() {
    val auth = Firebase.auth
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var user by remember { mutableStateOf(auth.currentUser) }
    var message by remember { mutableStateOf("") }

    Column(Modifier.padding(15.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp),

        )

        if (user == null) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = {
                    scope.launch {
                        try {
                            auth.signInWithEmailAndPassword(email, password).await()
                            user = auth.currentUser
                            message = "Login successful"
                        } catch (e: Exception) {
                            Log.e("Login", "Failed", e)
                            message = "Login failed: ${e.localizedMessage}"
                        }
                    }
                }, modifier = Modifier.width(120.dp)) {
                    Text("Login")
                }
                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    scope.launch {
                        try {
                            auth.createUserWithEmailAndPassword(email, password).await()
                            user = auth.currentUser
                            message = "Sign-up successful"
                        } catch (e: Exception) {
                            Log.e("SignUp", "Failed", e)
                            message = "Sign-up failed: ${e.localizedMessage}"
                        }
                    }
                }, modifier = Modifier.width(120.dp)) {
                    Text("Sign up")
                }
            }

            Text(message)
        } else {
            Text("Welcome ${user!!.email}")
            Button(onClick = {
                auth.signOut()
                user = null
                message=""
            }) {
                Text("Sign Out")
            }
        }
    }
}


