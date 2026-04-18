import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart

sealed class Screen(
    val route: String,
    val label: String = "",
    val icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("sign_up_screen")

    // Bottom Nav Items - Icons and Labels added here
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)
    object Cart : Screen("cart", "Cart", Icons.Default.ShoppingCart)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}