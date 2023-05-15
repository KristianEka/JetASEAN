package com.ekachandra.jetasean.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekachandra.jetasean.data.CountryRepository
import com.ekachandra.jetasean.model.Country
import com.ekachandra.jetasean.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: CountryRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<Country>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Country>>>
        get() = _uiState

    fun getAllCountries() {
        viewModelScope.launch {
            repository.getAllCountries()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }

                .collect { countryList ->
                    _uiState.value = UiState.Success(countryList)
                }
        }
    }

    private val _query = MutableStateFlow("")
    val query: StateFlow<String>
        get() = _query

    fun search(newQuery: String) {
        _query.value = newQuery
        viewModelScope.launch {
            repository.searchCountries(_query.value)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }

                .collect { countryList ->
                    _uiState.value = UiState.Success(countryList)
                }
        }
    }
}