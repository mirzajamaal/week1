package com.example.quickbite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickbite.ui.theme.QuickBiteTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    // Define other text styles as needed
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase

        setContent {
            QuickBiteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    QuickBiteApp()
                }
            }
        }
    }
}

@Composable
fun QuickBiteApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("menu/{restaurantId}") { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId")?.toInt() ?: 0
            MenuScreen(restaurantId, navController)
        }
        composable("cart") {
            CartScreen()
        }
        composable("profile") {
            ProfileScreen()
        }
    }
}

class SplashViewModel : ViewModel() {
    fun startSplashScreen(onTimeout: () -> Unit) {
        viewModelScope.launch {
            delay(2000L)
            onTimeout()
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: SplashViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.startSplashScreen {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "QuickBite",
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Name
        Text(
            text = "QuickBite",
            style = MaterialTheme.typography.h3.copy(
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Login Button
        Button(
            onClick = {
                // Call onLoginSuccess() to navigate to the home screen
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Login", style = MaterialTheme.typography.button)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Text
        TextButton(onClick = {
            // Navigate to sign up screen
            navController.navigate("signup") {
                popUpTo("login") { inclusive = true }
            }
        }) {
            Text("Don't have an account? Sign Up")
        }
    }
}

@Composable
fun SignupScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Name
        Text(
            text = "QuickBite",
            style = MaterialTheme.typography.h3.copy(
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password Input
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Confirm Password Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Signup Button
        Button(
            onClick = {
                // Signup logic here
                navController.navigate("home") {
                    popUpTo("signup") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Sign Up", style = MaterialTheme.typography.button)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back to Login Text
        TextButton(onClick = {
            // Navigate back to the login screen
            navController.navigate("login") {
                popUpTo("signup") { inclusive = true }
            }
        }) {
            Text("Already have an account? Log In")
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val sampleRestaurants = listOf(
        Restaurant(1, "Pizza Place", "123 Main St", "Pizza"),
        Restaurant(2, "Burger Joint", "456 Elm St", "Burgers"),
        Restaurant(3, "Sushi Spot", "789 Oak St", "Sushi")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search for restaurants or dishes") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            }
        )

        // Restaurant Listings
        LazyColumn {
            items(sampleRestaurants) { restaurant ->
                RestaurantItem(restaurant) {
                    navController.navigate("menu/${restaurant.id}")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {
                navController.navigate("cart")
            }) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart Icon")
            }
            IconButton(onClick = {
                navController.navigate("profile")
            }) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Profile Icon")
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurant: Restaurant, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = restaurant.name, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = restaurant.address, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = restaurant.cuisine, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun MenuScreen(restaurantId: Int, navController: NavController) {
    // Placeholder for MenuScreen implementation
    // Fetch menu items based on restaurantId and implement order placement
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Menu for Restaurant $restaurantId", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        // Menu items and order functionality go here
    }
}

@Composable
fun CartScreen() {
    // Placeholder for CartScreen implementation
    // Show cart items and allow order placement
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Your Cart", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        // Cart items and checkout functionality go here
    }
}

@Composable
fun ProfileScreen() {
    // Placeholder for ProfileScreen implementation
    // Show user profile details and allow editing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Your Profile", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        // Profile details and edit functionality go here
    }
}

data class Restaurant(val id: Int, val name: String, val address: String, val cuisine: String)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QuickBiteTheme {
        QuickBiteApp()
    }
}
