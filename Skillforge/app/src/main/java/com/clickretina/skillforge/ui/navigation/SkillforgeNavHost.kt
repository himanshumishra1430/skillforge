package com.clickretina.skillforge.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.clickretina.skillforge.ui.detail.CourseDetailScreen
import com.clickretina.skillforge.ui.home.HomeScreen
import com.clickretina.skillforge.ui.lesson.LessonScreen

@Composable
fun SkillforgeNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(280))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(280))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(280))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(280))
        }
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onCourseClick = { courseId ->
                    navController.navigate(Routes.courseDetail(courseId))
                }
            )
        }

        composable(
            route = Routes.COURSE_DETAIL,
            arguments = listOf(navArgument(Routes.ARG_COURSE_ID) { type = NavType.StringType })
        ) { entry ->
            val courseId = entry.arguments?.getString(Routes.ARG_COURSE_ID).orEmpty()
            CourseDetailScreen(
                courseId = courseId,
                onBack = { navController.popBackStack() },
                onLessonClick = { lessonId ->
                    navController.navigate(Routes.lesson(courseId, lessonId))
                }
            )
        }

        composable(
            route = Routes.LESSON,
            arguments = listOf(
                navArgument(Routes.ARG_COURSE_ID) { type = NavType.StringType },
                navArgument(Routes.ARG_LESSON_ID) { type = NavType.StringType }
            )
        ) { entry ->
            val courseId = entry.arguments?.getString(Routes.ARG_COURSE_ID).orEmpty()
            val lessonId = entry.arguments?.getString(Routes.ARG_LESSON_ID).orEmpty()
            LessonScreen(
                courseId = courseId,
                lessonId = lessonId,
                onBack = { navController.popBackStack() },
                onOpenLesson = { nextLessonId ->
                    navController.navigate(Routes.lesson(courseId, nextLessonId)) {
                        popUpTo(Routes.COURSE_DETAIL)
                    }
                }
            )
        }
    }
}
