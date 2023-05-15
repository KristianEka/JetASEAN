package com.ekachandra.jetasean.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekachandra.jetasean.data.CountryRepository
import com.ekachandra.jetasean.model.Country
import com.ekachandra.jetasean.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: CountryRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<Country>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Country>>>
        get() = _uiState

    fun getAllFavoriteCountries() {
        viewModelScope.launch {
            repository.getAllFavoriteCountries()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }

                .collect { countryFavoriteList ->
                    _uiState.value = UiState.Success(countryFavoriteList)
                }
        }
    }
}