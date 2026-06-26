package com.clickretina.skillforge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clickretina.skillforge.data.model.Category
import com.clickretina.skillforge.data.model.Course
import com.clickretina.skillforge.data.repository.CourseRepository
import com.clickretina.skillforge.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeData(
    val categories: List<Category>,
    val courses: List<Course>
)

class HomeViewModel : ViewModel() {

    private val repository = CourseRepository.instance

    private val _state = MutableStateFlow<UiState<HomeData>>(UiState.Loading)
    val state: StateFlow<UiState<HomeData>> = _state.asStateFlow()

    init {
        load()
    }

    fun load(forceRefresh: Boolean = false) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                val categories = repository.getCategories(forceRefresh)
                val courses = categories.flatMap { it.courses }
                _state.value = UiState.Success(HomeData(categories, courses))
            } catch (e: Exception) {
                _state.value = UiState.Error(
                    e.message ?: "Could not load courses. Check your connection."
                )
            }
        }
    }
}
