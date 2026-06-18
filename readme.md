# ShopEase

ShopEase is a modern Android e-commerce application built with Kotlin and Jetpack Compose, following the Model-View-Intent (MVI) architecture. The project demonstrates a scalable Android codebase with unidirectional state management, offline persistence using Room and DataStore, and a modular architecture designed for maintainability. :contentReference[oaicite:0]{index=0}

## Features

- Product catalog with category filtering
- Product search with debounced queries
- Product detail page
- Shopping cart with offline persistence
- Wishlist management
- Checkout flow
- User authentication state persistence
- Material 3 UI
- Shared element transitions
- Compose animations
- Responsive layouts
- Modular MVI architecture

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin |
| UI Toolkit | Jetpack Compose |
| Architecture | MVI (Model-View-Intent) |
| State Management | StateFlow, Kotlin Coroutines |
| Database | Room |
| Preferences | DataStore |
| Navigation | Navigation Compose |
| Design | Material 3 |

## Project Structure

```text
app/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── datastore/
│   │   └── entity/
│   └── repository/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
├── di/
├── ui/
│   ├── base/
│   ├── components/
│   ├── navigation/
│   ├── productlist/
│   ├── productdetail/
│   ├── categorybrowse/
│   ├── search/
│   ├── wishlist/
│   ├── cart/
│   ├── checkout/
│   ├── profile/
│   └── theme/
└── MainActivity.kt
```

The project is organized using a layered architecture with separate data, domain, and presentation layers, along with reusable UI components and manual dependency injection. :contentReference[oaicite:1]{index=1}

## Architecture

The application follows the MVI architecture with a single source of truth for UI state.

```text
User Action
      │
      ▼
    Intent
      │
      ▼
  ViewModel
      │
      ▼
 Repository
      │
      ▼
 Room / DataStore / Assets
      │
      ▼
   StateFlow
      │
      ▼
Jetpack Compose UI
```

## Screens

- Home
- Categories
- Product Details
- Search
- Wishlist
- Cart
- Checkout
- Profile

## Getting Started

```bash
git clone https://github.com/your-username/ShopEase.git
```

Open the project in Android Studio, sync Gradle, and run it on an Android device or emulator.

