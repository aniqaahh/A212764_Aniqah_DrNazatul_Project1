package com.example.a212764_aniqah_drnazatul_project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController // PENTING
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a212764_aniqah_drnazatul_project1.data.Trail
import com.example.a212764_aniqah_drnazatul_project1.data.TrailViewModel
import com.example.a212764_aniqah_drnazatul_project1.screens.ActivityScreen
import com.example.a212764_aniqah_drnazatul_project1.screens.DetailScreen
import com.example.a212764_aniqah_drnazatul_project1.screens.NavigationScreen
import com.example.a212764_aniqah_drnazatul_project1.screens.ProfileScreen
import com.example.a212764_aniqah_drnazatul_project1.screens.ReviewScreen
import com.example.a212764_aniqah_drnazatul_project1.screens.SavedScreen
import com.example.a212764_aniqah_drnazatul_project1.screens.WeatherScreen
import com.example.a212764_aniqah_drnazatul_project1.ui.theme.A212764_Aniqah_DrNazatul_Project1Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            A212764_Aniqah_DrNazatul_Project1Theme {
                val viewModel: TrailViewModel = viewModel()
                val navController = rememberNavController()

                // Scaffold diletak di sini (ROOT)
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(navController, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, viewModel: TrailViewModel) {
    NavHost(navController = navController, startDestination = "home") {

        // 1. Home Screen (Explore)
        composable("home") { HomeScreen(navController, viewModel) }

        // 2. Detail Screen (Item Detail)
        composable("details") {
            DetailScreen(
                navController = navController,
                trail = viewModel.selectedTrail.value,
                viewModel = viewModel
            )
        }
        // 3. Saved Screen
        composable("saved") { SavedScreen(navController, viewModel) }
        // 4. Weather screen
        composable("weather") { WeatherScreen() }
        // 5. Activity Screen
        composable("activity") { ActivityScreen(viewModel) }
        // 6. Review Screen
        composable("reviews_write") {
            ReviewScreen(
                navController = navController,
                trail = viewModel.selectedTrail.value,
                viewModel = viewModel,
                isReadOnly = false
            )
        }
        //7. Navigation Screen
        composable("navigation_screen") {
            NavigationScreen(
                navController = navController,
                trail = viewModel.selectedTrail.value
            )
        }
        // 8. Profile Screen (User Profile)
        composable("profile") { ProfileScreen() }
    }
}
@Composable
fun HomeScreen(navController: NavController, viewModel: TrailViewModel) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp) // Padding atas bawah sahaja
    ) {
        // --- HEADER SECTION ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                "TrailFinder",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Explore the beauty of nature",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(20.dp))
            SearchBar(text = searchText, onTextChange = { searchText = it })
            Spacer(modifier = Modifier.height(20.dp))
            CategoryChips()
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- 1. NEARBY SECTION ---
        Text(
            "Nearby Trails",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        val nearbyScrollState = rememberScrollState()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(nearbyScrollState)
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            // KITA TAPIS KAT SINI: Hanya ambil UKM Loop & MPKJ Kajang
            viewModel.allTrails.filter {
                it.title == "UKM Loop" || it.title == "MPKj Sports Complex"
            }.forEach { trail ->
                NearbyCard(trail, navController, viewModel)
                Spacer(modifier = Modifier.width(12.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- 2. RECOMMENDED SECTION (VERTICAL LIST) ---
        Text(
            "Recommended for you",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            viewModel.allTrails
                .filter { it.title.lowercase().contains(searchText.lowercase()) }
                .forEach { trailItem ->
                    TrailFullCard(trail = trailItem, navController = navController, viewModel = viewModel)
                }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// --- CARD BARU UNTUK NEARBY (Kecil sikit & Swipe-able) ---
@Composable
fun NearbyCard(trail: Trail, navController: NavController, viewModel: TrailViewModel) {
    Card(
        modifier = Modifier
            .width(280.dp) // Set lebar tetap supaya nampak kad
            .height(220.dp),
        shape = RoundedCornerShape(20.dp),
        onClick = {
            viewModel.selectedTrail.value = trail
            navController.navigate("details")
        }
    ) {
        Box {
            Image(
                painter = painterResource(id = trail.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Gradient overlay biar text nampak
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 300f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(trail.title, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Text(trail.location, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}


@Composable
fun SearchBar(text: String, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = { Text("Search forest, hills...") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            Button(
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.padding(end = 4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text("Search")
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChips() {
    // Kita kategorikan ikut "Difficulty" dan "Status"
    val categories = listOf(
        Triple("Easy", Icons.Default.SentimentSatisfied, MaterialTheme.colorScheme.tertiary),
        Triple("Moderate", Icons.Default.SentimentNeutral, MaterialTheme.colorScheme.secondary),
        Triple("Hard", Icons.Default.SentimentVeryDissatisfied, MaterialTheme.colorScheme.error),
        Triple("Trending", Icons.Default.Whatshot, MaterialTheme.colorScheme.primary),
        Triple("Top Rated", Icons.Default.Star, MaterialTheme.colorScheme.primary),
        Triple("New", Icons.Default.FiberNew, MaterialTheme.colorScheme.primary)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Tambah sikit padding depan supaya chip tak rapat ke tepi skrin bila skrol
        Spacer(modifier = Modifier.width(4.dp))

        categories.forEach { (label, icon, themeColor) ->
            FilterChip(
                selected = false,
                onClick = { /* Handle Filter */ },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = themeColor // Guna warna dari colorScheme
                    )
                },
                shape = RoundedCornerShape(12.dp),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    enabled = true,
                    selected = false
                ),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    labelColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}
@Composable
fun TrailFullCard(trail: Trail, navController: NavController, viewModel: TrailViewModel) {
    var expanded by remember { mutableStateOf(false) }

    // Guna ni terus, tak payah remember lagi
    val isSaved by trail.isSaved

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Tambah padding sikit bagi nampak cantik
            .animateContentSize(animationSpec = tween(400)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(id = trail.imageRes), // Guna trail.imageRes
                    contentDescription = null,
                    modifier = Modifier.height(180.dp).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                ) {
                    IconButton(onClick = {
                        trail.isSaved.value = !trail.isSaved.value
                    }) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isSaved) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(trail.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(trail.location, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Surface(
                        color = when (trail.level) {
                            "Hard" -> MaterialTheme.colorScheme.error
                            "Moderate" -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.tertiary
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(trail.level, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("⭐ ${trail.rating}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        InfoItem("DISTANCE", trail.length)
                        InfoItem("ELEVATION", trail.elevation)
                        InfoItem("EST. TIME", trail.time)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // UPDATE NI: Bagitahu ViewModel trail mana yang dipilih
                            viewModel.selectedTrail.value = trail
                            navController.navigate("details")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("View Trail Details")
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Gunakan Triple (Label, Icon, Route) supaya kod lebih bersih
    val navItems = listOf(
        Triple("Explore", Icons.Default.Search, "home"),
        Triple("Saved", Icons.Default.Favorite, "saved"),
        Triple("Weather", Icons.Default.Cloud, "weather"),
        Triple("Activity", Icons.Default.List, "activity"),
        Triple("Profile", Icons.Default.Person, "profile")
    )

    NavigationBar(
        // Guna warna dari MaterialTheme secara dinamik
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        navItems.forEach { (label, icon, route) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (currentRoute == route) FontWeight.Bold else FontWeight.Normal
                    )
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                // Semua warna ditarik dari MaterialTheme scheme
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                )
            )
        }
    }
}
