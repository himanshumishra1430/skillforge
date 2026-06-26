package com.clickretina.skillforge.data.model

/**
 * Models mirror the JSON returned by the single Skillforge endpoint:
 * categories -> courses -> lessons (with a nested instructor on each course).
 *
 * Every field has a default so that a partial/garbled payload can never crash
 * Gson deserialization — missing fields simply fall back to sensible empties.
 */
data class SkillforgeResponse(
    val meta: Meta = Meta(),
    val categories: List<Category> = emptyList()
)

data class Meta(
    val app: String = "",
    val version: String = "",
    val generatedAt: String = ""
)

data class Category(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val iconColor: String = "#2DD4BF",
    val courseCount: Int = 0,
    val courses: List<Course> = emptyList()
)

data class Course(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val thumbnailUrl: String = "",
    val level: String = "",
    val durationHours: Double = 0.0,
    val rating: Double = 0.0,
    val studentsEnrolled: Int = 0,
    val language: String = "",
    val lastUpdated: String = "",
    val tags: List<String> = emptyList(),
    val instructor: Instructor = Instructor(),
    val description: String = "",
    val lessons: List<Lesson> = emptyList()
)

data class Instructor(
    val id: String = "",
    val name: String = "",
    val title: String = "",
    val avatarUrl: String = "",
    val bio: String = ""
)

data class Lesson(
    val id: String = "",
    val title: String = "",
    val durationMinutes: Int = 0,
    val isFree: Boolean = false,
    val videoUrl: String = "",
    val content: String = ""
)
