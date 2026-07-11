![StreamView](https://github.com/user-attachments/assets/4cc64552-8e17-42cc-a150-5c5cb32ff768)


<h1 align="center">StreamView</h1>

<p align="center">
  <i>A minimalistic Android app for browsing and watching HLS streams, built with Clean Architecture.</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-100%25-7F52FF?logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white" />
  <img src="https://img.shields.io/badge/Architecture-MVI%20%2B%20Clean-3F51B5" />
  <img src="https://img.shields.io/badge/minSdk-24-informational" />
  <img src="https://img.shields.io/badge/License-MIT-lightgrey" />
</p>

---

## About

StreamView is a small, polished Android demo app that showcases modern architecture (**MVI + Clean Architecture + multi-module**) and video streaming (**Media3 ExoPlayer + HLS**) — the kind of stack used in cloud-camera / video-viewer applications.

It's a *generic* streaming-viewer demo — **not** a clone of any specific product. It has its own name, its own visual identity, and generic public-test stream labels. It was built as a portfolio piece to demonstrate patterns useful for camera-viewer and video-viewer apps, with clean, modern, testable architecture throughout.

## ✨ Features

- **Stream list** — scrollable list of stream cards with live-frame thumbnails (falls back to a static image, then a placeholder), an expandable search bar, and loading / empty / error states.
- **Search** — filters by name, stream URL, and provider, case-insensitive and live as you type.
- **Add a stream** — a FAB opens a dialog to add any custom HTTPS HLS URL; new streams prepend to the list (in-memory).
- **Player** — near-fullscreen Media3 `PlayerView` with fully custom Compose controls: play/pause, next (circular playlist), volume, seek/progress, a LIVE indicator, and fullscreen toggle.
- **Robust states** — loading, buffering, ready, ended, and error-with-retry are modeled explicitly and handled per screen; a failed thumbnail is never shown as a list error.

## 📸 Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/a5ccc9d5-c0f3-4037-a3ba-7f08b00f1d1a" alt="Stream list" width="45%" />
  &nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/1410f423-106a-40cb-bf90-604c1403d081" alt="Player" width="45%" />
</p>

## 🏗️ Architecture

Each feature follows the same layered Clean Architecture, with dependencies flowing inward:

```
UI (Compose)  — screens, state observation, intent dispatch
      ↓ (intents)
ViewModel (MVI)  — holds StateFlow, reduces intents to new state
      ↓
Domain  — use cases (pure Kotlin, no Android)
      ↓
Data  — repository interface (domain) + seed implementation (data)
```

- Domain is pure Kotlin — no Android or framework dependencies.
- ViewModels expose a single `StateFlow<State>` and accept intents via `onIntent`.
- No business logic lives in composables; they only render state and dispatch intents.
- MVI keeps streaming's async state transitions (loading → ready → error → retry) predictable and easy to test.

## 📦 Modules

Seven modules with clear boundaries:

| Module | Responsibility |
|---|---|
| `:app` | Application, `MainActivity`, Hilt `AppModule`, Navigation Compose host |
| `:core:ui` | Material 3 theme, shared composables, `StreamFrameExtractor` |
| `:core:common` | Shared utils (dispatcher providers), no Android deps |
| `:domain` | `Stream` model, `StreamRepository`, use cases (pure Kotlin) |
| `:data` | `SeedStreamRepository`, `PublicStreamCatalog`, `StreamUrls` |
| `:feature:list` | Stream list screen, ViewModel, contract, add-stream dialog |
| `:feature:player` | Player screen, ViewModel, contract, `ExoStreamPlayer`, controls |

```
:app ──► :feature:list, :feature:player, :core:ui, :domain, :data
:feature:list  ──► :domain, :core:ui, :core:common
:feature:player ──► :domain, :core:ui, :core:common
:data  ──► :domain, :core:common
:domain ──► :core:common
:core:ui ──► :core:common
```

`:feature:*` never depends on `:data` directly — `:app` provides it via Hilt. `:domain` never depends on `:data`, `:core:ui`, or Android framework code.

## 🛠 Built With

- [Kotlin](https://kotlinlang.org/) & [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) / Flow
- [Jetpack Compose](https://developer.android.com/jetpack/compose) & [Material 3](https://m3.material.io/)
- [Navigation Compose](https://developer.android.com/guide/navigation)
- MVI per feature + Clean Architecture layering
- Multi-module setup (`app` / `core` / `domain` / `data` / `feature`)
- [Hilt](https://dagger.dev/hilt/) + KSP for dependency injection
- [Media3 ExoPlayer](https://developer.android.com/guide/topics/media/media3) (`media3-exoplayer`, `media3-exoplayer-hls`, `media3-ui`) for HLS playback
- [Coil](https://coil-kt.github.io/coil/) for optional static thumbnail fallbacks

## 🚀 Getting Started

1. Clone this repository.
2. Open the project in the latest stable Android Studio.
3. Run on a device or emulator with **Android 7.0+ (API 24)**.

No API keys or backend setup required — the repository is seed-backed (in-memory), pre-loaded with ~17 free public HLS test streams (Apple, Mux, Akamai, Bitmovin, Unified Streaming, JW Player, NASA TV, and lorem.video), so the app works out of the box.

## 📁 Project Structure

```
com.techegrity.stream_view
├── app/                 → Application, MainActivity, DI, navigation host
├── core/ui/             → Theme, shared composables, thumbnail extraction
├── core/common/         → Dispatcher providers, shared helpers
├── domain/              → Stream model, repository interface, use cases
├── data/                → Seed repository, public stream catalog
├── feature/list/        → Stream list MVI (screen, ViewModel, add dialog)
└── feature/player/      → Player MVI (screen, ViewModel, ExoPlayer wrapper, controls)
```

## 🧭 What I'd Add for Production

This is a deliberately scoped demo. For a production version, next on the list would be:

- Authentication and per-user stream ownership
- A real backend (`StreamRepository` remote implementation via Retrofit) with persisted custom streams
- Adaptive bitrate strategy and buffering tuning for weak networks
- Multi-camera concurrent viewing
- Offline caching for recently viewed streams and posters
- Analytics on playback quality (startup time, rebuffer rate) and crash reporting
- Full unit / integration / UI test coverage
- Accessibility pass and localization

## 📄 License

MIT — feel free to use this as a reference for your own Clean Architecture / Compose / Media3 projects.

## 🙌 About

Brought to you by [@farhandroid](https://github.com/Farhandroid).
