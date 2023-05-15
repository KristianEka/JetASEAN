package com.ekachandra.jetasean.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ekachandra.jetasean.model.Country
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Country)

    @Delete
    suspend fun delete(favorite: Country)

    @Query("SELECT * FROM Country")
    fun getAllFavoriteCountries(): Flow<List<Country>>

    @Query("SELECT EXISTS(SELECT * FROM Country WHERE id = :id)")
    fun isCountriesFavorite(id: Long): Flow<Boolean>
}