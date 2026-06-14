package com.example.a212764_aniqah_drnazatul_project1.data

@Dao
interface TrailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrail(trail: TrailEntity)

    @Query("SELECT * FROM trails ORDER BY id ASC")
    fun getAllTrails(): Flow<List<TrailEntity>>

    @Delete
    suspend fun deleteTrail(trail: TrailEntity)
}