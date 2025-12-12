package com.realestateagency.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realestateagency.app.data.PropertyRepository
import com.realestateagency.app.models.PropertyListing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(private val repository: PropertyRepository = PropertyRepository()) : ViewModel() {

    private val _properties = MutableStateFlow<List<PropertyListing>>(emptyList())
    val properties: StateFlow<List<PropertyListing>> = _properties.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadProperties()
    }

    private fun loadProperties() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAllProperties().collect { newProperties ->
                    _properties.value = newProperties
                    _error.value = null
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Неизвестная ошибка"
                _properties.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retryLoadProperties() {
        loadProperties()
    }

    fun searchProperties(query: String): List<PropertyListing> {
        return _properties.value.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.address.contains(query, ignoreCase = true) ||
                    it.city.contains(query, ignoreCase = true)
        }
    }

    fun getPropertiesByType(type: String): List<PropertyListing> {
        return _properties.value.filter { it.type == type }
    }

    fun getPropertiesByPriceRange(minPrice: Double, maxPrice: Double): List<PropertyListing> {
        return _properties.value.filter { it.price in minPrice..maxPrice }
    }

    // ✅ Очистить ошибку
    fun clearError() {
        _error.value = null
    }
}