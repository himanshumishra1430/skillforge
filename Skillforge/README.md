# Skillforge — Android take-home

A small 3-screen learning app built with **Kotlin + Jetpack Compose**, powered by
a single nested JSON endpoint. Browse-to-learn flow:

`Home → Course Detail → Lesson player`

The UI recreates the supplied "cream + teal" designs (Plus Jakarta Sans), and all
content/images come from the API at runtime.

---

## How to run

1. Open the project in **Android Studio** (Ladybug / Hedgehog or newer).
2. Let Gradle sync. It uses **JDK 17**, **AGP 8.7.2**, **Kotlin 2.0.21**, **Gradle 8.9**.
   - This zip does not ship the `gradle-wrapper.jar` binary. Android Studio will
     restore the wrapper automatically on first sync. From a terminal you can also
     run `gradle wrapper` once (any Gradle 8.x), then `./gradlew assembleDebug`.
3. Run the **app** configuration on an emulator/device (**minSdk 26**, target 35).
4. An internet connection is required — the app fetches everything from:
   `https://raw.githubusercontent.com/android-assesment/notes/refs/heads/main/data.json`

---

## Architecture

Clean, lightweight **MVVM** with unidirectional data flow. No DI framework — for a
3-screen app a hand-rolled singleton (`NetworkModule`, `CourseRepository.instance`)
is clearer than pulling in Hilt.

```
data/
  model/        Kotlin models mirroring the JSON (every field defaulted = crash-safe)
  remote/       Retrofit SkillforgeApi + NetworkModule (OkHttp + Gson)
  repository/   CourseRepository — fetches once, caches in memory, resolves
                courses/lessons locally (one network call powers all 3 screens)
ui/
  theme/        Color, Type (Plus Jakarta Sans via downloadable Google Fonts), Theme
  common/       UiState<T> (Loading/Error/Success), LoadingView, ErrorView, NetworkImage
  navigation/   Routes + SkillforgeNavHost (type-safe-ish args, slide transitions)
  home/         HomeViewModel + HomeScreen
  detail/       CourseDetailViewModel + CourseDetailScreen
  lesson/       LessonViewModel + LessonScreen
MainActivity    edge-to-edge, sets the Compose theme + NavHost
```

Each screen has its own `ViewModel` exposing a `StateFlow<UiState<…>>`, collected
with `collectAsStateWithLifecycle()`. Every screen handles **loading / error
(with retry) / success** states explicitly.

## Tech stack

- Jetpack Compose (Material 3) + Navigation-Compose
- ViewModel + Kotlin Coroutines / StateFlow
- Retrofit + OkHttp + Gson
- Coil (async image loading)
- Plus Jakarta Sans via downloadable Google Fonts (no font binaries bundled)
- Gradle version catalog (`gradle/libs.versions.toml`)

## Design fidelity

Exact tokens were lifted from the provided designs:

- Background `#FBFAF8`, cards `#FFFFFF` with `#ECEBE6` hairline borders, radius 18dp
- Teal accents `#2DD4BF` / `#14B8A6`, "See all" links `#0FB5A4`, star `#F5A623`
- Category accent colors come straight from the API (`iconColor`)
- Home: greeting + search, horizontally-scrolling category cards, popular-course rows
  (thumbnail, level, title, instructor, rating + duration)
- Course Detail: hero image, level/title/subtitle, rating·duration·learners, tags,
  description, instructor card, lesson list (free badge / lock), sticky "Start learning"
- Lesson: a faithful video-player surface (the API ships placeholder `example.com`
  video URLs, so it's a thumbnail + play/pause + scrubber rather than a live stream),
  lesson title, duration, content, and a "Next lesson" action

## Notes & trade-offs

- **One API call, cached.** The endpoint returns everything nested, so the repository
  fetches once and the Detail/Lesson screens slice the cached data by id — no redundant
  network calls.
- **Search** on Home filters the popular list locally (title / instructor / tag) — a
  small, sensible bit of life beyond the static mockup.
- **Lesson video** is intentionally a placeholder player because the data has dummy
  video URLs; swapping in ExoPlayer/Media3 against a real URL would be a drop-in.
- Light theme only — the brief specifies a fixed cream + teal look.

## AI-tools workflow

Built with AI assistance (the brief explicitly tests this): the AI scaffolded the
Gradle/module setup, generated models from the JSON shape, and produced the Compose
screens against the extracted design tokens. The structure above — MVVM boundaries,
single-source-of-truth repository, explicit UiState, version catalog — was the
intended design, with the generated code reviewed and tidied to match it.
