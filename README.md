# Pocket
This app is mimics the functionality of the popular
app [Pocket](https://play.google.com/store/apps/details?id=com.ideashower.readitlater.pro&hl=en_IN&gl=US)
which is available to download in the play store. <br>

To try out this app you need to use the latest version of
[Android Studio Arctic Fox](https://developer.android.com/studio/preview).

## Table of contents
1. [Tech Stack](#tech-stack)
2. [Features](#features)
3. [Source code and architecture](#source-code-and-architecture)
4. [Screenshots](#screenshots)

## Tech Stack
- Entirely written in [Kotlin](https://kotlinlang.org/).
- Manual dependency injection.
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for building the UI.
- [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html) for
  threading.
- [Room](https://developer.android.com/training/data-storage/room) for database.
- [Timber](https://github.com/JakeWharton/timber) for logging.
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore?gclid=EAIaIQobChMItJiD6eTG8QIVQTUrCh0OSAGpEAAYASAAEgJ5H_D_BwE&gclsrc=aw.ds)
  for storing user preferences.
- [Glide](https://github.com/bumptech/glide) for image loading.
- [Jsoup](https://jsoup.org/) for parsing the website.
- [Work Manager](https://developer.android.com/topic/libraries/architecture/workmanager?gclid=EAIaIQobChMIwJy33ufG8QIVGcEWBR31Mwa-EAAYASAAEgIF3vD_BwE&gclsrc=aw.ds)
  for background tasks.

## Features
- Save URL's from any web browser using the share menu.
- Dark and light mode support.
- The app's theme matches the current system theme for all devices above API level 29 (Android 10).
- Ability to search through the list of saved urls.
- The user's theme preference will be saved, and it will be applied everytime the app is re-opened (
  API level < 29).

## Source code and architecture
- [Architecture components](https://developer.android.com/topic/libraries/architecture/) such as
  Room,Lifecycle and ViewModels are used.
- [MVVM](https://developer.android.com/jetpack/guide?gclid=EAIaIQobChMI-_GIsejG8QIVzNaWCh0NXQANEAAYASAAEgKZ2fD_BwE&gclsrc=aw.ds)
  architecture is used.
- Source code conforms to
  the [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html).
- Dependency injection is done manually.
- [Material design color system](https://material.io/design/color/the-color-system.html#color-usage-and-palettes)
  specification is used for assigning colors to the UI components.
- Commit messages follow
  the [Angular specification](https://github.com/angular/angular/blob/22b96b9/CONTRIBUTING.md#-commit-message-guidelines)
  for commit messages._(Note:Some early commits do not follow this convention.)_

## Screenshots

### Home Screen

#### Light Mode

![Home Screen - Light Mode - Api 21](screenshots/api29_homeScreen_light_mode.png)

#### Dark Mode

![Home Screen - Dark Mode - Api 21](screenshots/api29_homeScreen_dark_mode.png)

### Top bar (API < 29)

The top bar will display the option to change the theme if, and only if, the device' current android
version doesn't support dark mode.If it supports dark mode, then the theme of the app will match the
current system theme.

#### Dark Mode

![Top bar](screenshots/api26_topbar_dark_mode.png)

#### Light Mode

![Top bar](screenshots/api26_topbar_light_mode.png)







