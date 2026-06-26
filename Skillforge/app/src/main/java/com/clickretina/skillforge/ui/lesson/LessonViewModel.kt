package com.clickretina.skillforge.ui.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clickretina.skillforge.data.repository.CourseRepository
import com.clickretina.skillforge.data.repository.LessonWithCourse
import com.clickretina.skillforge.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LessonViewModel : ViewModel() {

    private val repository = CourseRepository.instance

    private val _state = MutableStateFlow<UiState<LessonWithCourse>>(UiState.Loading)
    val state: StateFlow<UiState<LessonWithCourse>> = _state.asStateFlow()

    private var loadedKey: String? = null

    fun load(courseId: String, lessonId: String, forceRefresh: Boolean = false) {
        val key = "$courseId/$lessonId"
        if (loadedKey == key && !forceRefresh && _state.value is UiState.Success) return
        loadedKey = key
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = repository.getLesson(courseId, lessonId)
                _state.value = if (result != null) {
                    UiState.Success(result)
                } else {
                    UiState.Error("Lesson not found.")
                }
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Could not load this lesson.")
            }
        }
    }
}
