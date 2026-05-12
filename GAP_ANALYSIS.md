# Namma-Mela: PRD vs Implementation Gap Analysis

## Original PRD requirements vs the PWA you had before

| PRD Item | PWA Status | Notes |
|---|---|---|
| Jetpack Compose UI | NOT MET | PWA was HTML/JS |
| Room Database | NOT MET | PWA used IndexedDB |
| MVVM architecture | NOT MET | PWA used global functions + DOM state |
| Glide image loading | NOT MET | PWA used `<img>` tags |
| Tonight's Play screen | MET | |
| Manager edit show info | MET | |
| Cast list | MET (static photos) | |
| Manager edit cast | PARTIAL | Names only — photos hardcoded |
| Seat map | MET | |
| Real-time seat updates | LOCAL ONLY | Not cross-device |
| Reserve seats persistent | MET (IndexedDB) | |
| Fan Wall comments | MET (random names) | No real identity |
| GenAI poster | TEXT ONLY | Calls Gemini for text. User pastes own API key. |
| Theatrical UI | MET | |
| No payment (per PRD) | MET | PRD explicitly excludes payment |
| No auth (per PRD) | MET | PRD explicitly excludes auth |

## After this scaffold (Android Studio rebuild)

| PRD Item | Android Status | Notes |
|---|---|---|
| Jetpack Compose UI | MET | All 5 screens |
| Room Database | MET | 4 entities, 4 DAOs |
| MVVM | MET | `AppViewModel` + repositories |
| Glide image loading | SUBSTITUTED | Coil (Compose-idiomatic, free, same purpose). Swap to Glide if your evaluator insists. |
| Tonight's Play | MET | `PlayScreen` |
| Cast list | MET | `CastScreen` |
| Seat map | MET | `SeatMapScreen` with Premium/Standard/Economy |
| Reserve seats persistent | MET | Room |
| Fan Wall | MET (with real names) | User sets display name once |
| Manager edit show | MET | PIN-gated |
| GenAI poster | DEFERRED | Dependency included; screen not built. Add later in 1 file. |

## Additions you requested (beyond PRD)

| Feature | Implementation |
|---|---|
| PIN-protected manager (local RBAC) | `SettingsRepository.verifyPin` — SHA-256 hashed in DataStore. Default PIN `1234` on first launch, then user-set. |
| Real comments with user identity | User enters display name once on first comment. Stored in DataStore. All future comments use it. |
| Payment integration | Razorpay test mode. User pastes their own `rzp_test_…` key in Manager Mode. On `onPaymentSuccess`, seats are reserved. No real money. |

## What is STILL not built (honest list)

1. **GenAI poster screen.** The Gemini dependency is in `build.gradle.kts` but no screen calls it yet. The PWA had this. To replicate: add a `PosterAIScreen` that calls `GenerativeModel("gemini-1.5-flash", apiKey = ...).generateContent(prompt)` and renders the response. ~80 lines.
2. **Share intent.** PWA had Web Share API. Android needs `Intent.ACTION_SEND`. Small addition.
3. **Splash screen.** Compose can use the SplashScreen API (androidx.core:core-splashscreen). Not critical.
4. **Cast photos editable.** Currently drawable resources. Needs a photo picker (`ActivityResultContracts.PickVisualMedia`) + storing the URI in Room.
5. **Multi-device sync.** Out of scope. Would need Firebase/Supabase.
6. **Notifications.** No FCM. Out of scope for offline-first single-venue.
7. **Tests.** No unit/UI tests. Add if your evaluator scores them.
8. **Launcher icon densities.** All five mipmap folders contain the same 192×192 PNG as a placeholder. Use Image Asset Studio to generate proper density-specific icons.

## Will the scope explode?

Verifiable answer: with what's scaffolded here, no — the additions are bounded.
The three things you asked for (RBAC, named comments, payment) added roughly:

- ~50 lines (SettingsRepository PIN logic)
- ~30 lines (name dialog in FanWall)
- ~60 lines (RazorpayHandler + integration in MainActivity + Manager key field)

That is well under 200 lines of extra code over the strict PRD scope, and it
keeps the app single-device, single-venue, single-show — which matches the
PRD's "Out of Scope" boundary on everything else (multi-venue, multi-show,
auth, streaming).

The trap would be adding **multi-device sync** or **production payment with
KYC** — both are 10× the work and not appropriate for an internship.

## How this compares to BookMyShow

BookMyShow is a multi-tenant, multi-city, partner-API-driven booking
platform with payment, refunds, anti-fraud, recommendations, multi-language,
and a content side (reviews, news). Replicating it is a years-long, multi-
team effort, not an internship project.

What you can credibly say in your internship report: this app implements the
**same core user journey** as BookMyShow (browse show → pick seats → pay →
get ticket) within a **single-venue, single-show, offline-first** scope
suitable for rural Karnataka drama troupes. That is what the PRD asks for,
and it is what this scaffold delivers.
