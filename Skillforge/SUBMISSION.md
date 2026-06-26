# Submitting Skillforge to Clickretina

Per the assignment page, submission is by email to **hr@clickretina.com**, and must
include a **GitHub repo link + the APK** (+ an optional short screen recording).
Deadline: **within 4 days** of receiving the assignment (ask if you need more time).

---

## Checklist before you send

- [ ] **1. Push the project to GitHub** (repo must contain the `README.md`, which has the
      required "AI-tools workflow" section).
- [ ] **2. Build the debug APK** (see commands below) — lands in
      `app/build/outputs/apk/debug/app-debug.apk`.
- [ ] **3. (Optional, appreciated)** record a 1–2 min screen recording of the app running.
- [ ] **4. Email** the GitHub link + APK (attach it, or a Google Drive link) + recording
      to **hr@clickretina.com**.

### Build the APK
In Android Studio: **Build ▸ Build Bundle(s) / APK(s) ▸ Build APK(s)** (or just Run the
app once). The file appears at `app/build/outputs/apk/debug/app-debug.apk`.

From a terminal (after Gradle wrapper is restored / using a local Gradle 8.x):
```
gradle wrapper          # one-time, if ./gradlew is missing
./gradlew assembleDebug  # APK -> app/build/outputs/apk/debug/
```

### Push to GitHub
```
cd Skillforge
git init
git add .
git commit -m "Skillforge: 3-screen Android app (Kotlin + Compose)"
git branch -M main
git remote add origin https://github.com/<your-username>/skillforge-android.git
git push -u origin main
```

---

## Email draft

Subject: Android take-home — Skillforge (Kotlin + Compose)

Hi team,

Here's my submission for the Skillforge take-home.

- GitHub: https://github.com/<your-username>/skillforge-android
- APK: attached (also at app/build/outputs/apk/debug/app-debug.apk)
- Screen recording: <attached / Google Drive link>   [optional]

Quick notes on the build:
- Kotlin + Jetpack Compose (Material 3), MVVM. All three screens — Home → Course
  Detail → Lesson — built to match the supplied designs, driven entirely by the
  single provided endpoint (fetched once, cached; images load via Coil).
- Each screen exposes a StateFlow<UiState> with explicit loading / error+retry /
  success handling. Design tokens (cream #FBFAF8, teal #2DD4BF/#14B8A6, Plus Jakarta
  Sans) come from the brief; category colors come from the API.
- The Lesson screen is a faithful player surface since the API ships placeholder
  video URLs — wiring in Media3/ExoPlayer against a real URL would be a drop-in.

The repo README covers architecture, the full stack, trade-offs, and how AI was used.
Happy to walk through any of it in the interview.

Best,
Bhanu
