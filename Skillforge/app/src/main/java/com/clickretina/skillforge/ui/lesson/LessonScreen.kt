package com.clickretina.skillforge.ui.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.clickretina.skillforge.data.model.Lesson
import com.clickretina.skillforge.ui.common.ErrorView
import com.clickretina.skillforge.ui.common.LoadingView
import com.clickretina.skillforge.ui.common.NetworkImage
import com.clickretina.skillforge.ui.common.UiState
import com.clickretina.skillforge.ui.theme.Cream
import com.clickretina.skillforge.ui.theme.Teal
import com.clickretina.skillforge.ui.theme.TealStrong
import com.clickretina.skillforge.ui.theme.TextMuted
import com.clickretina.skillforge.ui.theme.TextPrimary
import com.clickretina.skillforge.ui.theme.TextSecondary

@Composable
fun LessonScreen(
    courseId: String,
    lessonId: String,
    onBack: () -> Unit,
    onOpenLesson: (String) -> Unit,
    viewModel: LessonViewModel = viewModel()
) {
    LaunchedEffect(courseId, lessonId) { viewModel.load(courseId, lessonId) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize().background(Cream)) {
        when (val s = state) {
            is UiState.Loading -> LoadingView()
            is UiState.Error -> ErrorView(message = s.message, onRetry = { viewModel.load(courseId, lessonId, true) })
            is UiState.Success -> LessonContent(
                course = s.data.course,
                lesson = s.data.lesson,
                onOpenLesson = onOpenLesson
            )
        }
        BackButton(onBack)
    }
}

@Composable
private fun LessonContent(
    course: Course,
    lesson: Lesson,
    onOpenLesson: (String) -> Unit
) {
    val currentIndex = course.lessons.indexOfFirst { it.id == lesson.id }
    val nextLesson = course.lessons.getOrNull(currentIndex + 1)

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        VideoPlayer(thumbnailUrl = course.thumbnailUrl, durationMinutes = lesson.durationMinutes)

        Column(Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "LESSON ${currentIndex + 1}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.6.sp,
                    color = TealStrong
                )
                if (lesson.isFree) {
                    Box(
                        Modifier.clip(RoundedCornerShape(8.dp)).background(Teal.copy(alpha = 0.16f)).padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("Free", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TealStrong)
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(lesson.title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 27.sp, color = TextPrimary)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Rounded.Schedule, null, tint = TextMuted, modifier = Modifier.size(15.dp))
                Text("${lesson.durationMinutes} min", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                Text("·  ${course.title}", fontSize = 13.sp, color = TextMuted)
            }
            Spacer(Modifier.height(18.dp))
            Text(
                if (lesson.content.isNotBlank()) lesson.content else "No description for this lesson.",
                fontSize = 15.sp,
                lineHeight = 24.sp,
                color = TextSecondary
            )

            if (nextLesson != null) {
                Spacer(Modifier.height(28.dp))
                OutlinedButton(
                    onClick = { onOpenLesson(nextLesson.id) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TealStrong)
                ) {
                    Text("Next · ${nextLesson.title}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.navigationBarsPadding())
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun VideoPlayer(thumbnailUrl: String, durationMinutes: Int) {
    // The API ships placeholder video URLs (example.com), so this is a faithful
    // player surface rather than a real stream: thumbnail + play/pause + scrubber.
    var playing by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(Color(0xFF0D1217))
    ) {
        NetworkImage(
            url = thumbnailUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.35f)))

        Box(
            Modifier
                .align(Alignment.Center)
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.92f))
                .clickable { playing = !playing },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = if (playing) "Pause" else "Play",
                tint = Color(0xFF0D1217),
                modifier = Modifier.size(34.dp)
            )
        }

        // Faux scrubber along the bottom.
        Column(Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
            Box(Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)).background(Color.White.copy(alpha = 0.3f))) {
                Box(Modifier.fillMaxWidth(if (playing) 0.18f else 0.0f).height(3.dp).clip(RoundedCornerShape(2.dp)).background(Teal))
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "0:00 / ${durationMinutes}:00",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium
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
            .background(Color.Black.copy(alpha = 0.35f))
            .clickable(onClick = onBack),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp))
    }
}
