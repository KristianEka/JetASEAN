package com.ekachandra.jetasean.data

import com.ekachandra.jetasean.data.local.room.FavoriteDatabase
import com.ekachandra.jetasean.model.Country
import com.ekachandra.jetasean.model.FakeCountryDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class CountryRepository(private val database: FavoriteDatabase) {

    private val countryList = mutableListOf<Country>()

    init {
        if (countryList.isEmpty()) {
            FakeCountryDataSource.dummyCountries.forEach { country ->
                countryList.add(
                    Country(
                        id = country.id,
                        image = country.image,
                        title = country.title,
                        capital = country.capital,
                        membership = country.membership,
                        contribution = country.contribution,
                    )
                )
            }
        }
    }

    fun getAllCountries(): Flow<List<Country>> {
        return flowOf(countryList)
    }

    fun getCountryById(countryId: Long): Country {
        return countryList.first { country ->
            country.id == countryId
        }
    }

    fun searchCountries(query: String): Flow<List<Country>> {
        return flow {
            val countries = countryList.filter {
                it.title.contains(query, ignoreCase = true)
            }
            emit(countries)
        }
    }

    suspend fun insert(favorite: Country) {
        withContext(Dispatchers.IO) {
            database.favoriteDao().insert(favorite)
        }
    }

    suspend fun delete(favorite: Country) {
        withContext(Dispatchers.IO) {
            database.favoriteDao().delete(favorite)
        }
    }

    fun getAllFavoriteCountries(): Flow<List<Country>> {
        return database.favoriteDao().getAllFavoriteCountries()
    }

    fun isCountriesFavorite(countryId: Long): Flow<Boolean> {
        return database.favoriteDao().isCountriesFavorite(countryId)
    }

}