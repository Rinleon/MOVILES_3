package com.example.cursofirebaselife.presentation.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursofirebaselife.R
import com.example.cursofirebaselife.ui.theme.Black
import com.example.cursofirebaselife.ui.theme.SelectedField
import com.example.cursofirebaselife.ui.theme.UnselectedField
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(auth: FirebaseAuth, navigateToHome: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row() {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_24),
                contentDescription = "",
                tint = White,
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Text("Email", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)

        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = UnselectedField,
                focusedContainerColor = SelectedField
            )
        )

        Spacer(Modifier.height(48.dp))

        Text("Password", color = White, fontWeight = FontWeight.Bold, fontSize = 40.sp)

        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = UnselectedField,
                focusedContainerColor = SelectedField
            )
        )

        Spacer(Modifier.height(48.dp))

        Button(
            onClick = {
                if (!loading) {
                    loading = true

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            loading = false

                            if (task.isSuccessful) {
                                Log.i("aris", "LOGIN OK")
                                navigateToHome()
                            } else {
                                Log.i("aris", "LOGIN KO")
                            }
                        }
                }
            }
        ) {
            Text(text = if (loading) "Loading..." else "Login")
        }
    }
}
