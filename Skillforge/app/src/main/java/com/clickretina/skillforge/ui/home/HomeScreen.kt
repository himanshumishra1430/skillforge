package com.clickretina.skillforge.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.clickretina.skillforge.data.model.Category
import com.clickretina.skillforge.data.model.Course
import com.clickretina.skillforge.ui.common.ErrorView
import com.clickretina.skillforge.ui.common.LoadingView
import com.clickretina.skillforge.ui.common.NetworkImage
import com.clickretina.skillforge.ui.common.UiState
import com.clickretina.skillforge.ui.theme.CardBorder
import com.clickretina.skillforge.ui.theme.CardWhite
import com.clickretina.skillforge.ui.theme.Cream
import com.clickretina.skillforge.ui.theme.StarGold
import com.clickretina.skillforge.ui.theme.TealLink
import com.clickretina.skillforge.ui.theme.TealStrong
import com.clickretina.skillforge.ui.theme.TextMuted
import com.clickretina.skillforge.ui.theme.TextPrimary
import com.clickretina.skillforge.ui.theme.TextSecondary
import com.clickretina.skillforge.ui.theme.parseHexColor

@Composable
fun HomeScreen(
    onCourseClick: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize().background(Cream)) {
        when (val s = state) {
            is UiState.Loading -> LoadingView()
            is UiState.Error -> ErrorView(message = s.message, onRetry = { viewModel.load(true) })
            is UiState.Success -> HomeContent(data = s.data, onCourseClick = onCourseClick)
        }
    }
}

@Composable
private fun HomeContent(
    data: HomeData,
    onCourseClick: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query, data.courses) {
        if (query.isBlank()) data.courses
        else data.courses.filter { c ->
            c.title.contains(query, true) ||
                c.instructor.name.contains(query, true) ||
                c.tags.any { it.contains(query, true) }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 36.dp)
    ) {
        item { Header(query = query, onQueryChange = { query = it }) }

        item { SectionHeader(title = "Categories", modifier = Modifier.padding(top = 16.dp)) }
        item {
            LazyRow(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 12.dp)
            ) {
                items(data.categories, key = { it.id }) { CategoryCard(it) }
            }
        }

        item { SectionHeader(title = "Popular courses", modifier = Modifier.padding(top = 26.dp)) }

        if (filtered.isEmpty()) {
            item {
                Text(
                    text = "No courses match \"$query\".",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                )
            }
        } else {
            items(filtered, key = { it.id }) { course ->
                CourseRow(
                    course = course,
                    onClick = { onCourseClick(course.id) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }

        item { Spacer(Modifier.navigationBarsPadding()) }
    }
}

@Composable
private fun Header(query: String, onQueryChange: (String) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Cream)
            .statusBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 14.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Welcome back",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted
                )
                Text(
                    "Find your next skill",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.3).sp,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(CardWhite)
                        .border(1.dp, CardBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                NetworkImage(
                    url = "https://i.pravatar.cc/150?img=5",
                    contentDescription = "Profile",
                    modifier = Modifier.size(42.dp).clip(CircleShape)
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        TextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(14.dp)),
            leadingIcon = {
                Icon(Icons.Rounded.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
            },
            placeholder = { Text("Search courses, topics…", color = TextMuted, fontSize = 15.sp) },
            keyboardOptions = KeyboardOptions.Default,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CardWhite,
                unfocusedContainerColor = CardWhite,
                disabledContainerColor = CardWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = TealStrong,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )
    }
}

@Composable
private fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("See all", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TealLink)
    }
}

@Composable
private fun CategoryCard(category: Category) {
    val accent = parseHexColor(category.iconColor)
    Column(
        Modifier
            .width(148.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(CardWhite)
            .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Box(
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(accent.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(18.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(accent)
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            category.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 18.sp,
            color = TextPrimary
        )
        Text(
            "${category.courseCount} courses",
            fontSize = 12.sp,
            color = TextMuted,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}

@Composable
private fun CourseRow(
    course: Course,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CardWhite)
            .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NetworkImage(
            url = course.thumbnailUrl,
            contentDescription = course.title,
            modifier = Modifier
                .width(98.dp)
                .height(74.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CardBorder)
        )
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(
                course.level.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp,
                color = TealStrong
            )
            Text(
                course.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 19.sp,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 3.dp)
            )
            Text(
                course.instructor.name,
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 3.dp)
            )
            Row(
                Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetaChip(icon = { Icon(Icons.Rounded.Star, null, tint = StarGold, modifier = Modifier.size(13.dp)) }, text = course.rating.toString())
                MetaChip(icon = { Icon(Icons.Rounded.Schedule, null, tint = TextMuted, modifier = Modifier.size(13.dp)) }, text = formatHours(course.durationHours))
            }
        }
    }
}

@Composable
private fun MetaChip(icon: @Composable () -> Unit, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        icon()
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
    }
}

internal fun formatHours(hours: Double): String {
    val rounded = if (hours % 1.0 == 0.0) hours.toInt().toString() else hours.toString()
    return "${rounded}h"
}
