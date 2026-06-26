package com.clickretina.skillforge.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.clickretina.skillforge.data.model.Course
import com.clickretina.skillforge.data.model.Instructor
import com.clickretina.skillforge.data.model.Lesson
import com.clickretina.skillforge.ui.common.ErrorView
import com.clickretina.skillforge.ui.common.LoadingView
import com.clickretina.skillforge.ui.common.NetworkImage
import com.clickretina.skillforge.ui.common.UiState
import com.clickretina.skillforge.ui.home.formatHours
import com.clickretina.skillforge.ui.theme.CardBorder
import com.clickretina.skillforge.ui.theme.CardWhite
import com.clickretina.skillforge.ui.theme.Cream
import com.clickretina.skillforge.ui.theme.StarGold
import com.clickretina.skillforge.ui.theme.SurfaceDim
import com.clickretina.skillforge.ui.theme.Teal
import com.clickretina.skillforge.ui.theme.TealStrong
import com.clickretina.skillforge.ui.theme.TextMuted
import com.clickretina.skillforge.ui.theme.TextPrimary
import com.clickretina.skillforge.ui.theme.TextSecondary

@Composable
fun CourseDetailScreen(
    courseId: String,
    onBack: () -> Unit,
    onLessonClick: (String) -> Unit,
    viewModel: CourseDetailViewModel = viewModel()
) {
    LaunchedEffect(courseId) { viewModel.load(courseId) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize().background(Cream)) {
        when (val s = state) {
            is UiState.Loading -> LoadingView()
            is UiState.Error -> ErrorView(message = s.message, onRetry = { viewModel.load(courseId, true) })
            is UiState.Success -> DetailContent(
                course = s.data,
                onBack = onBack,
                onLessonClick = onLessonClick
            )
        }
        BackButton(onBack)
    }
}

@Composable
private fun DetailContent(
    course: Course,
    onBack: () -> Unit,
    onLessonClick: (String) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            item {
                NetworkImage(
                    url = course.thumbnailUrl,
                    contentDescription = course.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(CardBorder)
                )
            }
            item { CourseHeaderBlock(course) }
            item { InstructorCard(course.instructor) }
            item {
                Text(
                    "Lessons",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 6.dp)
                )
            }
            itemsIndexed(course.lessons, key = { _, lesson -> lesson.id }) { index, lesson ->
                LessonRow(
                    index = index + 1,
                    lesson = lesson,
                    onClick = { onLessonClick(lesson.id) }
                )
            }
        }

        StartLearningBar(
            course = course,
            onStart = { course.lessons.firstOrNull()?.let { onLessonClick(it.id) } },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun CourseHeaderBlock(course: Course) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp)) {
        Pill(text = course.level.uppercase(), bg = Teal.copy(alpha = 0.16f), fg = TealStrong)
        Spacer(Modifier.height(12.dp))
        Text(course.title, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 28.sp, color = TextPrimary)
        if (course.subtitle.isNotBlank()) {
            Text(
                course.subtitle,
                fontSize = 14.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Meta(icon = { Icon(Icons.Rounded.Star, null, tint = StarGold, modifier = Modifier.size(15.dp)) }, text = course.rating.toString())
            Meta(icon = { Icon(Icons.Rounded.Schedule, null, tint = TextMuted, modifier = Modifier.size(15.dp)) }, text = formatHours(course.durationHours))
            Meta(icon = null, text = "%,d learners".format(course.studentsEnrolled))
        }
        if (course.tags.isNotEmpty()) {
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                course.tags.forEach { tag ->
                    Pill(text = tag, bg = SurfaceDim, fg = TextSecondary)
                }
            }
        }
        if (course.description.isNotBlank()) {
            Spacer(Modifier.height(16.dp))
            Text(
                course.description,
                fontSize = 15.sp,
                lineHeight = 23.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun InstructorCard(instructor: Instructor) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(CardWhite)
            .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        NetworkImage(
            url = instructor.avatarUrl,
            contentDescription = instructor.name,
            modifier = Modifier.size(52.dp).clip(CircleShape).background(SurfaceDim)
        )
        Column(Modifier.weight(1f)) {
            Text(instructor.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(instructor.title, fontSize = 12.sp, color = TealStrong, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 1.dp))
            if (instructor.bio.isNotBlank()) {
                Text(instructor.bio, fontSize = 13.sp, lineHeight = 19.sp, color = TextMuted, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

@Composable
private fun LessonRow(index: Int, lesson: Lesson, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            Modifier.size(36.dp).clip(CircleShape).background(Teal.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.PlayArrow, contentDescription = null, tint = TealStrong, modifier = Modifier.size(20.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(
                "$index. ${lesson.title}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text("${lesson.durationMinutes} min", fontSize = 12.sp, color = TextMuted, modifier = Modifier.padding(top = 2.dp))
        }
        if (lesson.isFree) {
            Pill(text = "Free", bg = Teal.copy(alpha = 0.16f), fg = TealStrong)
        } else {
            Icon(Icons.Rounded.Lock, contentDescription = "Premium", tint = TextMuted, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun StartLearningBar(course: Course, onStart: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .background(Cream)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .navigationBarsPadding()
    ) {
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealStrong)
        ) {
            Text(
                if (course.lessons.firstOrNull()?.isFree == true) "Start learning · free preview" else "Start learning",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BackButton(onBack: () -> Unit) {
    Box(
        Modifier
            .statusBarsPadding()
            .padding(start = 16.dp, top = 8.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.92f))
            .clickable(onClick = onBack),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = TextPrimary, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun Pill(text: String, bg: Color, fg: Color) {
    Box(
        Modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = fg, letterSpacing = 0.3.sp)
    }
}

@Composable
private fun Meta(icon: (@Composable () -> Unit)?, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        icon?.invoke()
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
    }
}
