package com.clickretina.skillforge.ui.navigation

/** Type-safe-ish route helpers for the three screens. */
object Routes {
    const val HOME = "home"

    const val COURSE_DETAIL = "course/{courseId}"
    fun courseDetail(courseId: String) = "course/$courseId"

    const val LESSON = "course/{courseId}/lesson/{lessonId}"
    fun lesson(courseId: String, lessonId: String) = "course/$courseId/lesson/$lessonId"

    const val ARG_COURSE_ID = "courseId"
    const val ARG_LESSON_ID = "lessonId"
}
