package com.ekachandra.jetasean.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekachandra.jetasean.data.CountryRepository
import com.ekachandra.jetasean.model.Country
import com.ekachandra.jetasean.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: CountryRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Country>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Country>>
        get() = _uiState

    fun getCountryById(countryId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getCountryById(countryId))
        }
    }

    private val _favoriteStatus = MutableStateFlow(false)
    val favoriteStatus: StateFlow<Boolean>
        get() = _favoriteStatus

    fun updateStatus(id: Long) = viewModelScope.launch {
        _favoriteStatus.value = repository.isCountriesFavorite(id).first()
    }

    fun changeFavorite(favorite: Country) {
        viewModelScope.launch {
            if (_favoriteStatus.value) {
                repository.delete(favorite)
            } else {
                repository.insert(favorite)
            }

            _favoriteStatus.value = !_favoriteStatus.value
        }
    }
}