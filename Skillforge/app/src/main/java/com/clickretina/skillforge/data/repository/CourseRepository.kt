package com.clickretina.skillforge.data.repository

import com.clickretina.skillforge.data.model.Category
import com.clickretina.skillforge.data.model.Course
import com.clickretina.skillforge.data.model.Lesson
import com.clickretina.skillforge.data.remote.NetworkModule
import com.clickretina.skillforge.data.remote.SkillforgeApi

/**
 * Single source of truth for catalog data. The API returns everything in one
 * nested payload, so we fetch once and cache it in memory; Course Detail and
 * Lesson screens then resolve their slice of the data locally (no extra calls).
 */
class CourseRepository(
    private val api: SkillforgeApi = NetworkModule.api
) {
    @Volatile
    private var cache: List<Category>? = null

    suspend fun getCategories(forceRefresh: Boolean = false): List<Category> {
        cache?.let { if (!forceRefresh) return it }
        val categories = api.getCatalog().categories
        cache = categories
        return categories
    }

    /** Flattened list of every course across all categories (used by "Popular"). */
    suspend fun getAllCourses(forceRefresh: Boolean = false): List<Course> =
        getCategories(forceRefresh).flatMap { it.courses }

    suspend fun getCourse(courseId: String): Course? =
        getAllCourses().firstOrNull { it.id == courseId }

    suspend fun getLesson(courseId: String, lessonId: String): LessonWithCourse? {
        val course = getCourse(courseId) ?: return null
        val lesson = course.lessons.firstOrNull { it.id == lessonId } ?: return null
        return LessonWithCourse(course, lesson)
    }

    companion object {
        /** Shared instance so the in-memory cache survives across screens. */
        val instance: CourseRepository by lazy { CourseRepository() }
    }
}

data class LessonWithCourse(
    val course: Course,
    val lesson: Lesson
)
