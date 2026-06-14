package com.example.a212764_aniqah_drnazatul_project1.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.a212764_aniqah_drnazatul_project1.R

class TrailViewModel : ViewModel() {

    // Simpan list semua trail
    val allTrails = mutableStateListOf<Trail>()

    // Simpan trail yang dipilih
    var selectedTrail = mutableStateOf<Trail?>(null)

    init {
        // Tambah data awal
        val initialData = listOf(
            Trail(
                1, "Gunung Datuk", "Rembau, Negeri Sembilan",
                "Moderate", "4.6", "4.7km", "586m",
                "3.5hr", R.drawable.datuk, mapRes = R.drawable.datuk_map
            ),
            Trail(
                2, "Taman Negeri Rompin", "Kuala Rompin, Pahang",
                "Hard", "4.8", "31.5km", "1,048m",
                "10hr", R.drawable.rompin, mapRes = R.drawable.rompin_map
            ),
            Trail(
                3, "UKM Loop", "Universiti Kebangsaan Malaysia, Bangi",
                "Moderate", "4.5", "2.9km", "115m",
                "1-2hr", R.drawable.ukmloop, mapRes = R.drawable.ukm_map
            ),
            Trail(
                4, "MPKj Sports Complex", "Bandar Baru Bangi, Selangor",
                "Easy", "5.0", "0.5km", "14m",
                "<1hr", R.drawable.mpkj, mapRes = R.drawable.mpkj_map
            )
        )
        allTrails.addAll(initialData)

        // Set trail pertama sebagai default
        selectedTrail.value = allTrails.firstOrNull()
    }

    //untuk save trail
    fun toggleSaveTrail(trail: Trail) {
        trail.isSaved.value = !trail.isSaved.value
    }

    // Fungsi untuk tambah review
    fun addReviewToTrail(trail: Trail, user: String, msg: String, rate: Float) {
        val newReview = Review(user, msg, rate)
        trail.reviews.add(0, newReview)

        // Update purata rating
        if (trail.reviews.isNotEmpty()) {
            val newAvg = trail.reviews.map { it.rating }.average()
            trail.rating = "%.1f".format(newAvg)
        }
    }

    // Statistik untuk Activity Screen
    val totalTrailsCount get() = allTrails.size
    val savedTrailsCount get() = allTrails.count { it.isSaved.value }
}