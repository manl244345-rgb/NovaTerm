# NovaTerm

**The most complete Linux workstation and developer environment for Android.**

[![Android CI](https://github.com/manl244345-rgb/NovaTerm/actions/workflows/android.yml/badge.svg)](https://github.com/manl244345-rgb/NovaTerm/actions/workflows/android.yml)

---

## Overview

NovaTerm is a premium Android application that combines everything a developer, Linux enthusiast, student, or system administrator needs — in one seamless experience.

### Features

- 🖥️ **Professional Terminal** — Multi-tab, split view, ANSI colors, full shell environment
- 📝 **Code Editor** — Syntax highlighting for 30+ languages, code folding, minimap
- 📁 **File Manager** — Desktop-class with grid/list views, archive support, bookmarks
- 🌿 **Git Client** — Full Git workflow with visual diff, branch management
- 🔒 **SSH Manager** — Professional SSH/SFTP with key management
- 📦 **Nova Packages** — Integrated package manager for developer tools
- 🌐 **Built-in Browser** — Dev-focused browser with dev tools support
- 📚 **Documentation** — Offline searchable docs for Linux, Git, Bash, and more
- 🗂️ **Workspaces** — Desktop-like sessions that remember your full context
- 🎨 **Themes** — Full customization with dark/light/AMOLED themes
- 📊 **System Monitor** — Live CPU, RAM, storage, and network metrics
- 📓 **Notes** — Markdown notes linked to your projects
- 🔰 **Beginner Mode** — Natural language command parser with explanations

---

## Technology Stack

| Technology | Purpose |
|------------|---------|
| Kotlin | Primary language |
| Jetpack Compose | UI framework |
| Material Design 3 | Design system |
| Hilt | Dependency injection |
| Room | Local database |
| Kotlin Coroutines + Flow | Async operations |
| DataStore | Preferences |
| Coil | Image loading |
| WebKit | Built-in browser |

---

## Requirements

- Android 8.0+ (API 26+)
- Android Studio Hedgehog or later
- JDK 17
- Gradle 8.9

---

## Building

```bash
# Clone the repository
git clone https://github.com/manl244345-rgb/NovaTerm.git
cd NovaTerm

# Build debug APK
./gradlew assembleDebug

# Build release APK (unsigned)
./gradlew assembleRelease

# Run unit tests
./gradlew test
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

---

## Architecture

NovaTerm follows **Clean Architecture** with:

- **UI Layer** — Jetpack Compose screens + ViewModels
- **Domain Layer** — Business logic and models
- **Data Layer** — Room database, DataStore, shell execution

Each module communicates through clean interfaces following SOLID principles.

---

## Project Structure

```
app/
├── src/main/java/com/novaterm/app/
│   ├── ui/
│   │   ├── navigation/     — NavGraph, Screen routes
│   │   ├── screens/        — All Compose screens
│   │   └── theme/          — Colors, Typography, Theme
│   ├── data/
│   │   ├── db/             — Room database, entities, DAOs
│   │   └── preferences/    — DataStore preferences
│   ├── terminal/           — Shell executor, service
│   ├── di/                 — Hilt modules
│   └── domain/             — Business models
```

---

## License

NovaTerm is open source. See [LICENSE](LICENSE) for details.

---

*Built with ❤️ using Kotlin and Jetpack Compose*
