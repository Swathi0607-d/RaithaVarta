# 🌾 RaithaVarta — AI Farming Assistant for Karnataka Farmers

> **ರೈಥವಾರ್ತ** — Bilingual (English + ಕನ್ನಡ) AI-powered crop disease diagnosis and farming tips app built for Karnataka farmers.

---

## 📱 Download APK
[![Download APK](https://img.shields.io/badge/Download-APK%20v1.0-green?style=for-the-badge&logo=android)](https://github.com/Swathi0607-d/RaithaVarta/releases/tag/v1.0)

---

## 📸 About the App

RaithaVarta is an Android app designed to help small and marginal farmers in Karnataka with:
- AI-powered crop disease diagnosis by uploading a leaf photo
- Farming tips customised to their crops and district
- Bilingual support in English and Kannada
- Cloud-synced farmer profile via Firebase
- Offline-first design — works without internet too

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔬 AI Leaf Diagnosis | Upload a crop leaf photo → get instant disease detection and treatment advice |
| 🌿 Farming Tips | Crop-specific tips for Paddy, Tomato, Ragi, Coconut, Banana, Sugarcane, Onion |
| 🗣️ Bilingual | Full English + Kannada language support with one-tap switching |
| ☁️ Firebase Sync | User profile and diagnosis history synced to Firebase Firestore |
| 📱 Offline First | Room local database ensures app works without internet |
| 🌦️ Weather Info | District-wise weather badge on home screen |
| 👤 Farmer Profile | Name, district, crop preferences saved locally and on cloud |
| 📊 Diagnosis History | Last 10 diagnoses saved locally and on Firestore |
| 🔔 Notifications | Toggle farming alerts and reminders |
| 📞 KVK Connect | Direct call to nearest Krishi Vigyana Kendra |

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| **Kotlin** | Primary programming language |
| **Android SDK** | Min SDK 24 (Android 7.0+) |
| **MVVM Architecture** | Clean separation of UI and business logic |
| **Firebase Auth** | Anonymous authentication — no login friction for farmers |
| **Firebase Firestore** | Cloud storage for user profile and diagnosis history |
| **Groq Vision AI** | AI-powered leaf disease analysis (LLaMA Vision model) |
| **Room Database** | Local offline-first diagnosis history storage |
| **ViewBinding** | Type-safe view access |
| **Coroutines** | Async operations and background tasks |
| **OkHttp** | Network calls to Groq API |
| **LiveData** | Reactive UI updates |

---

## 🏗️ Project Structure

```
RaithaVarta/
├── app/
│   ├── src/main/java/com/raithavarta/app/
│   │   ├── data/
│   │   │   ├── FirebaseManager.kt       # Firebase Auth + Firestore helper
│   │   │   ├── GroqClient.kt            # Groq AI Vision API client
│   │   │   ├── DataRepository.kt        # Local tips, stories, crop data
│   │   │   ├── LanguageManager.kt       # EN/KN language switching
│   │   │   ├── Models.kt                # Data models
│   │   │   └── db/
│   │   │       ├── AppDatabase.kt       # Room database
│   │   │       ├── DiagnosisDao.kt      # Diagnosis data access
│   │   │       └── DiagnosisEntity.kt   # Diagnosis table entity
│   │   ├── ui/
│   │   │   ├── home/HomeFragment.kt     # Home screen
│   │   │   ├── tips/TipsFragment.kt     # Farming tips screen
│   │   │   ├── diagnose/
│   │   │   │   ├── DiagnoseFragment.kt  # Camera + diagnosis UI
│   │   │   │   └── DiagnoseViewModel.kt # AI call + DB logic
│   │   │   ├── profile/ProfileFragment.kt # Farmer profile + settings
│   │   │   └── adapters/               # RecyclerView adapters
│   │   ├── SplashActivity.kt
│   │   ├── LoginActivity.kt
│   │   ├── MainActivity.kt
│   │   ├── OnboardingActivity.kt
│   │   └── RaithaVartaApp.kt
│   └── src/main/res/                    # Layouts, drawables, strings (EN + KN)
```

---

## 🔥 Firebase Integration

| Service | Usage |
|---|---|
| **Firebase Authentication** | Anonymous sign-in — every farmer gets a unique UID silently |
| **Cloud Firestore** | Stores `users/{uid}` profile and `users/{uid}/diagnoses` history |
| **Firebase Analytics** | App usage insights |

### Firestore Security Rules
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/{document=**} {
      allow read, write: if request.auth != null
                         && request.auth.uid == userId;
    }
  }
}
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android device / emulator with API 24+
- Groq API key (free at [console.groq.com](https://console.groq.com))
- Firebase project with `google-services.json`

### Setup

**1. Clone the repository**
```bash
git clone https://github.com/Swathi0607-d/RaithaVarta.git
cd RaithaVarta
```

**2. Add your API key**

Open `local.properties` and add:
```
GROQ_API_KEY=your_groq_api_key_here
```

**3. Add Firebase config**

Place your `google-services.json` inside `app/` folder.

**4. Run the app**

Open in Android Studio → click ▶ Run

---

## 🌱 Supported Crops

🌾 Paddy &nbsp;|&nbsp; 🍅 Tomato &nbsp;|&nbsp; 🌾 Ragi &nbsp;|&nbsp; 🥥 Coconut &nbsp;|&nbsp; 🍌 Banana &nbsp;|&nbsp; 🎋 Sugarcane &nbsp;|&nbsp; 🧅 Onion

---

## 📋 Requirements

- Android 7.0 (API 24) and above
- Internet connection for AI diagnosis and Firebase sync
- Camera or gallery access for leaf photo upload

---

## 👩‍💻 Developer

**Swathi V**

---

## 📄 License

This project is built for educational purposes as part of VTU Internship 2026.

---

*Built with ❤️ for Karnataka farmers — ಕರ್ನಾಟಕದ ರೈತರಿಗಾಗಿ ನಿರ್ಮಿಸಲಾಗಿದೆ*
