import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cursofirebaselife.presentation.home.HomeScreen
import com.example.cursofirebaselife.presentation.inicio.InitialScreen
import com.example.cursofirebaselife.presentation.login.LoginScreen
import com.example.cursofirebaselife.presentation.signup.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationWrapper(
    navHostController: NavHostController,
    auth: FirebaseAuth
) {
    NavHost(navController = navHostController, startDestination = "initial") {

        composable("initial") {
            InitialScreen(
                navigateToLogin = {
                    val current = navHostController.currentBackStackEntry?.destination?.route
                    if (current != "logIn") {
                        navHostController.navigate("logIn")
                    }
                },
                navigateToSignUp = {
                    val current = navHostController.currentBackStackEntry?.destination?.route
                    if (current != "signUp") {
                        navHostController.navigate("signUp")
                    }
                }
            )
        }

        composable("logIn") {
            LoginScreen(auth) {
                val current = navHostController.currentBackStackEntry?.destination?.route
                if (current != "home") {
                    navHostController.navigate("home")
                }
            }
        }

        composable("signUp") {
            SignUpScreen(auth)
        }

        composable("home") {
            HomeScreen()
        }
    }
}

