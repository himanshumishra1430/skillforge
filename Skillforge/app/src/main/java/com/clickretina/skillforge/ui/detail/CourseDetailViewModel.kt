package com.clickretina.skillforge.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clickretina.skillforge.data.model.Course
import com.clickretina.skillforge.data.repository.CourseRepository
import com.clickretina.skillforge.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseDetailViewModel : ViewModel() {

    private val repository = CourseRepository.instance

    private val _state = MutableStateFlow<UiState<Course>>(UiState.Loading)
    val state: StateFlow<UiState<Course>> = _state.asStateFlow()

    private var loadedId: String? = null

    fun load(courseId: String, forceRefresh: Boolean = false) {
        if (loadedId == courseId && !forceRefresh && _state.value is UiState.Success) return
        loadedId = courseId
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                val course = repository.getCourse(courseId)
                _state.value = if (course != null) {
                    UiState.Success(course)
                } else {
                    UiState.Error("Course not found.")
                }
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Could not load this course.")
            }
        }
    }
}
