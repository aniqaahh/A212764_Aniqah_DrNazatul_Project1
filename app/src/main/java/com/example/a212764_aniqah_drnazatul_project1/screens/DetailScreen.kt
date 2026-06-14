package com.example.a212764_aniqah_drnazatul_project1.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a212764_aniqah_drnazatul_project1.data.Trail
import com.example.a212764_aniqah_drnazatul_project1.data.TrailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, trail: Trail?, viewModel: TrailViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trail Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (trail != null) {
                        IconButton(onClick = { viewModel.toggleSaveTrail(trail) }) {
                            Icon(
                                imageVector = if (trail.isSaved.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Save",
                                tint = if (trail.isSaved.value) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        },
        // --- 1. BUTANG START (STICKY KAT BAWAH) ---
        bottomBar = {
            if (trail != null) {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 12.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    // Dalam DetailScreen.kt (Cari butang Start Trail Navigation)
                    Button(
                        onClick = {
                            navController.navigate("navigation_screen")
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Start Trail Navigation", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        if (trail != null) {
            // Box ni penting untuk handle innerPadding supaya content tak kena tutup dek bottomBar
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // HIDUPKAN SCROLL
                        .padding(16.dp)
                ) {
                    // --- GAMBAR UTAMA ---
                    Image(
                        painter = painterResource(id = trail.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- TAJUK & RATING ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = trail.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = trail.location,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Text(text = trail.rating, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- INFO CARDS GRID (Difficulty, Distance, etc.) ---
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoCard("Difficulty", trail.level, Icons.Default.Terrain, Modifier.weight(1f))
                        InfoCard("Distance", trail.length, Icons.Default.DirectionsWalk, Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoCard("Elevation", trail.elevation, Icons.Default.ArrowUpward, Modifier.weight(1f))
                        InfoCard("Est. Time", trail.time, Icons.Default.Timer, Modifier.weight(1f))
                    }

                    // --- 2. SECTION ABOUT ---
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        "About this trail",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Explore the breathtaking scenery of ${trail.title}. This trail is highly recommended for those who enjoy ${trail.level.lowercase()} challenges. With an average completion time of ${trail.time}, it's a perfect spot for your weekend adventure.",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // --- 3. SECTION FACILITIES ---
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        "Facilities & Highlights",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        FacilityItem(Icons.Default.LocalParking, "Parking")
                        FacilityItem(Icons.Default.Wc, "Toilet")
                        FacilityItem(Icons.Default.PhotoCamera, "Scenery")
                        FacilityItem(Icons.Default.Waves, "Water")
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // --- 4. SECTION REVIEWS ---
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "User Reviews",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        // Butang "Write" sekarang duduk sebelah tajuk section
                        TextButton(onClick = { navController.navigate("reviews_write") }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Write Review")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tukar daripada viewModel.reviews kepada trail.reviews
                    if (trail.reviews.isEmpty()) {
                        // ... kod No reviews yet awak ...
                    } else {
                        // Ambil reviews dari objek trail itu sendiri
                        // Dalam DetailScreen.kt
                        trail.reviews.forEach { reviewItem ->
                            // reviewItem.user ambil nama, reviewItem.msg ambil komen
                            ReviewItem(
                                author = reviewItem.username,
                                text = reviewItem.comment // atau reviewItem.msg (ikut data class awak)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    // SPACER EXTRA: Supaya content tak kena tutup dengan sticky button
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

// --- COMPOSABLE BARU UNTUK SETIAP REVIEW ---
@Composable
fun ReviewItem(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Hiker Community", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Text("Just now", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = text, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun InfoCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FacilityItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
    }
}