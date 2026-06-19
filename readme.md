# ShopEase 🛒

An Android e-commerce app I built to properly learn (and then actually demonstrate) Jetpack Compose, MVI architecture, and the whole offline-first Android stack — Room, DataStore, Coroutines/StateFlow — end to end, instead of just following a tutorial and copy-pasting the boilerplate.

I built this mainly for my resume/portfolio and for interview prep, so I've tried to keep every decision something I can actually explain out loud, not just something that "worked." That's also why there are a couple of honest notes below about what I skipped or simplified — I'd rather be upfront about that than have it come up awkwardly in an interview.

---

## Why I built this

I'd already built a Todo app and a Campus Notifications app with basic MVVM, and I wanted my next project to go a level deeper — specifically:
- Actually use MVI (unidirectional data flow) instead of the more common MVVM-with-LiveData pattern, since I kept seeing it mentioned in Android job postings and wanted hands-on experience, not just theory.
- Get comfortable with Room migrations on a real schema change instead of just `fallbackToDestructiveMigration()` (which I used, tbh, in an earlier project and always felt like I was cheating).
- Try Shared Element Transitions in Compose, which only recently became stable (`SharedTransitionLayout`) and needed me to bump to Kotlin 2.0 + the new Compose Compiler Gradle plugin — that alone taught me more about how the Compose compiler actually works than I expected going in.

There's no real backend here — everything is a local JSON "catalog" in `assets/`. That was a deliberate choice so I could focus fully on the Android side (architecture, persistence, UI) instead of also debugging a Node/Express backend, given my timeline. If I extend this later, swapping `AssetDataSource` for a Retrofit-based one shouldn't touch anything above the repository layer, which was kind of the point of layering it this way.

---

## Screens / Features

- **Home (Product List)** — grid of products, category filter chips, add-to-cart straight from the card
- **Search** — debounced (350ms) so it's not hammering the "backend" (aka re-filtering a list) on every keystroke
- **Product Detail** — quantity stepper, wishlist heart, and the image does a proper shared-element morph from wherever you tapped it (grid or search results)
- **Wishlist** — Room-backed, so it survives app restarts
- **Cart** — live quantity editing, subtotal calculation, free delivery over ₹999
- **Checkout** — address form + payment method (COD/UPI/Card — no real gateway, see below), order summary, simulated order placement
- **Profile** — local sign-in/sign-out, session persisted via DataStore

---

## Architecture

I went with **MVI** (Model-View-Intent) instead of plain MVVM. The rough shape, per feature:

```
UI (Composable) --Event--> ViewModel --reduces to--> State (StateFlow) --> UI recomposes
                               |
                               +--Effect--> UI (one-shot: navigation, snackbar)
```

Every feature has three things: a `State` data class, a sealed `Event` interface, and a sealed `Effect` interface. All ViewModels extend a shared `BaseViewModel<State, Event, Effect>` (in `ui/base/`) so I'm not rewriting the same StateFlow/Channel plumbing seven times. The `Effect` channel specifically is for stuff that should only happen *once* — like "navigate to detail" or "show this snackbar" — as opposed to `State`, which is just a value the UI keeps rendering. Mixing those two up was actually a mistake I made in my first draft (I had navigation as part of State and it kept re-firing on rotation), so the Effect channel is there on purpose now.

**Why not Hilt?** Honestly — with only 4 repositories, I felt like I'd be introducing DI-framework "magic" I couldn't fully explain if asked, versus a plain `AppContainer` class with a bunch of `by lazy` properties, which does the same job (singleton instances, created once) but I can point to every line and say what it does. I'd probably reach for Hilt if this app had 15+ dependencies or multiple build variants, but for this scope it felt like overkill.

**Data flow:** local JSON assets → `AssetDataSource` → `ProductRepositoryImpl` → `ProductRepository` interface → ViewModel. Same shape for Room (Cart/Wishlist) and DataStore (login), just swap the data source.

---

## Stack

- Kotlin 2.0.21 + Jetpack Compose (BOM 2024.12.01)
- MVI with a shared `BaseViewModel`
- Room — offline cart + wishlist persistence, with an actual `Migration(1, 2)` (not destructive) when I added the wishlist table
- DataStore (Preferences) — login/session state
- Coroutines + StateFlow throughout
- Material 3, including a proper light/dark color scheme (dynamic color available but off by default so the brand colors stay consistent)
- kotlinx.serialization for parsing the local JSON catalog
- Coil for image loading
- Manual DI (`AppContainer`, ServiceLocator-style) — no Hilt/Koin

---

## Things I'd flag if you're reviewing this / asked about it in an interview

I'd rather list these than have them "discovered":

- **No real backend or payment gateway.** The "catalog" is static JSON in `assets/`, and checkout has a `delay(900ms)` to simulate order processing before clearing the cart, instead of an instant fake success. If I added a backend, I'd point to the repository interfaces (`ProductRepository`, etc.) as the seam where a Retrofit implementation would slot in without touching the ViewModels.
- **No real auth.** "Signing in" on the Profile screen just generates a local UUID and stores it + the name/email you typed in DataStore. There's no password, no server round-trip. It's there to demonstrate DataStore usage, not to be a real auth system.
- **Shared element transitions are only wired between List/Search → Detail**, not for every possible screen transition — I scoped it to the main "browse → detail" flow since that's the most natural place for it and I didn't want to force it everywhere just to say I used it everywhere.
- **Search doesn't hit an index or debounce server-side** — it's just filtering an in-memory list, so the 350ms debounce is more about demonstrating the pattern than being strictly necessary at this data size. It would matter a lot more with a real backend.
- I bumped Kotlin from 1.9.24 → 2.0.21 mid-project specifically to get `SharedTransitionLayout`, which meant switching from the old `composeOptions { kotlinCompilerExtensionVersion }` config to the newer `org.jetbrains.kotlin.plugin.compose` Gradle plugin. If asked, I can explain why those two don't mix (Compose 1.7.x's compiler features aren't available in the old Kotlin 1.9-based Compose compiler).

---

## Project structure

```
app/src/main/java/com/shopease/app/
├── data/
│   ├── local/            → Room (entities, DAOs, migrations), DataStore, JSON asset parser
│   └── repository/       → repository implementations
├── domain/
│   ├── model/            → plain data classes (Product, CartItem, WishlistItem, User)
│   └── repository/       → repository interfaces
├── di/                   → AppContainer (manual DI)
└── ui/
    ├── base/              → BaseViewModel, MVI marker interfaces, ViewModelFactory
    ├── theme/             → Material 3 color scheme, typography
    ├── navigation/        → NavHost, bottom nav, shared element wrapper
    ├── components/        → shared composables (ProductCard, etc.)
    └── <feature>/          → one package per screen (productlist, productdetail, search, wishlist, cart, checkout, profile), each with a Contract/ViewModel/Screen trio
```

---

## Running it

Open in Android Studio (Ladybug or newer, since it needs to handle Kotlin 2.0 fine), let Gradle sync, run on an emulator or device with API 26+. No API keys, no backend setup needed — it just works out of the box since everything's local.

---

## What I'd add next if I kept going

- Real backend (probably a small Node/Express + Postgres setup, or Firebase to move faster) behind the existing repository interfaces
- Order history (currently an order just clears the cart and shows nothing afterward — no persisted order record)
- Actual auth with a password/OTP flow
- Unit tests for the ViewModels (I focused my time on getting the architecture and features solid first; testing is the obvious next thing)
