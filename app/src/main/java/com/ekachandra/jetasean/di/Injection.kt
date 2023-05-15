package com.ekachandra.jetasean.di

import android.content.Context
import com.ekachandra.jetasean.data.CountryRepository
import com.ekachandra.jetasean.data.local.room.FavoriteDatabase

object Injection {
    fun provideRepository(context: Context): CountryRepository {
        return CountryRepository(
            database = FavoriteDatabase.getDatabase(context)
        )
    }
}