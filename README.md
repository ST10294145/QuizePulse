<p align="center">
  <img src="https://raw.githubusercontent.com/VarunPerumal08/ST10110356_Prog5121_Poe-part1/main/AppLogo.jpg" width="320" alt="QuizPulse Logo"/>
</p>


<p align="center"> 
   
# QuizPulse

 </p>


<p align="center">
  <strong>A modern, intelligent, and beautifully designed quiz application built with Kotlin & Firebase.</strong><br/>
  <em>Featuring biometric authentication, multilingual support, dynamic difficulty, leaderboard, notifications, and real-time data updates.</em>
</p>

---

# 📘 Table of Contents
> *Click to jump to any section instantly.*

- [✨ Features](#features)
- [🖼️ Screenshots](#screenshots)
- [🎯 Purpose of the Application](#purpose-of-the-application)
- [🎨 Design Considerations](#design-considerations)
- [🛠️ GitHub & GitHub Actions](#use-of-github--github-actions)
- [🧩 Tech Stack](#tech-stack)
- [⚙️ Setup & Installation](#setup--installation)
- [🧠 How It Works](#how-it-works)
- [🚀 App Release Status](#app-release-status)
- [🌟 Future Improvements](#future-improvements)
- [🤝 Contributors](#contributors)
- [🎥 Video Demonstration](#video-demonstration)

---

<!-- Anchors ensure the ToC links work reliably -->
<a id="features"></a>
# 🚀 Features 

## 🧑‍💻 User Authentication 

> Email & Password registration

> Google Sign-In (SSO)

> Biometric Login (Fingerprint/Face Unlock) 

> Secure session management using Firebase Authentication 

## 🧠 Quiz System 

✔ Multiple quiz categories 

✔ 3 questions per quiz  

✔ Instant scoring + feedback 

✔ Difficulty levels: 

❇️ Easy 

🔴 Hard 

✔ Background music to improve the user experience 

✔ Countdown Timer per question (Improves challenge + prevents guessing) 

## 🏆 Leaderboard & Scoreboard 

✔ Stores all quiz attempts 

✔ Shows highest scores 

✔ Displays top players globally 

✔ Synced in real-time using Firebase Firestore 

## 🌍 Multilingual Support 

QuizPulse now supports: 

🔷 English 

🔷 Afrikaans 

🔷 isiZulu 

🔷 The language can be changed in the settings screen. 

## 🔔 Real-Time Notifications Using Firebase Cloud Messaging (FCM), users receive: 

✔ Quiz reminders 

✔ New category releases 

✔ Special updates 

✔ System alerts 

---

<a id="screenshots"></a>
# 🖼️ Screenshots

### 📌 Home Screen  
<img width="300" height="600" alt="Screenshot_20251117_184357" src="https://github.com/user-attachments/assets/670f2fa9-6c72-4b07-b833-56d79fbb8223" />

### 📌 Sign Up Screen  
<img width="300" height="600" alt="Screenshot_20251117_184415" src="https://github.com/user-attachments/assets/14dfe123-0717-4137-be36-c9e7d7938e61" />

### 📌 Login Screen 
<img width="300" height="600" alt="Screenshot_20251117_184455" src="https://github.com/user-attachments/assets/7640e56e-a3aa-4e86-a7fa-b44ae1ac6514" />

### 📌 Select Quiz Screen  
<img width="300" height="600" alt="Screenshot_20251117_184519" src="https://github.com/user-attachments/assets/4dc4e6f7-23a5-4bf5-b595-8066463a9acb" />

### 📌 Leaderboard  
<img width="300" height="600" alt="Screenshot_20251117_184527" src="https://github.com/user-attachments/assets/4d3d6ec9-d561-4d6c-a9c8-bc5faa70d96f" />

### 📌 Notifications  
<img width="300" height="600"  alt="Notifications" src="https://github.com/user-attachments/assets/146c8646-56da-4375-91e2-e00a63e3e4f4" />

### 📌 Settings Page  
<img width="300" height="600" alt="Screenshot_20251118_143902" src="https://github.com/user-attachments/assets/58c4e10f-48b6-4069-bd5f-8e0a1ed8f12c" />

---

<a id="purpose-of-the-application"></a>
# 📘 Purpose of the Application 

QuizPulse was developed as a modern, interactive quiz platform designed to make learning engaging, accessible, and measurable. The application combines gamification techniques with educational value, giving users a motivating environment to test and expand their knowledge. 

### The purpose of QuizPulse includes: 

- Enhancing learning through gamification: quizzes, scoring, feedback, and leaderboards to make learning fun and competitive. 
- Providing measurable progress: users can track improvements over time through score history, leaderboards, and difficulty settings.
- Supporting multilingual accessibility: English, isiZulu, and Afrikaans support.
- Offering personalized experiences: difficulty levels, notifications, background audio.
- Enabling secure authentication and personalization: Firebase Auth, Google Sign-In, biometric login.

<a id="design-considerations"></a>
## 🧩 Design Considerations

1. User Experience (UX)
   - Clean, minimal layout with intuitive navigation
   - High-contrast color themes for readability
   - Large, easily tappable buttons for accessibility
   - Multi-language support integrated into the UI
   - Background music and sound effects to enhance engagement

2. Performance
   - Kotlin coroutines for non-blocking operations
   - Images and assets cached locally
   - Optimized layouts using ConstraintLayout

3. Security
   - Firebase Authentication
   - Biometric authentication (fingerprint)
   - Encrypted data transmission

4. Scalability
   - Firebase backend scales with user growth
   - Modular Kotlin architecture
   - Separation of concerns (UI, logic, data layers)

---

<a id="use-of-github--github-actions"></a>
# 🛠️ Use of GitHub & GitHub Actions 

GitHub and GitHub Actions played a critical role in development and maintenance of QuizPulse. The project uses GitHub for version control, issue tracking, documentation, and collaborative code reviews.

### Automated Testing Workflow (example)

```yaml
name: Android Unit Tests
on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]
jobs:
  unit-tests:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - uses: actions/setup-java@v3
      with:
        distribution: 'temurin'
        java-version: '17'
    - uses: actions/cache@v3
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
    - run: chmod +x gradlew
    - run: ./gradlew test --stacktrace
```

---

<a id="tech-stack"></a>
# 🧩 Tech Stack 

- Frontend: Kotlin (Android Studio)  
- Authentication: Firebase Authentication  
- Database: Firebase Firestore  
- Notifications: Firebase Cloud Messaging  
- UI Design: XML Layouts  
- Version Control: Git + GitHub  
- CI/CD: GitHub Actions

---

<a id="setup--installation"></a>
# ⚙️ Setup & Installation

1. Install Requirements
   - Android Studio
   - Firebase Project
   - Android SDK
   - google-services.json added to app module

2. Clone Repo
```bash
git clone https://github.com/ST10294145/QuizePulse.git
```

3. Run the App
- Open the project in Android Studio
- Connect a device or start an emulator
- Build and run the project

---

<a id="how-it-works"></a>
# 🧠 How It Works

### 🔐 Authentication
- Register via email or Google SSO 
- Optional: biometric unlock 

### 📚 Quiz Flow
- Select category and difficulty
- Timer starts for each question
- Answer questions and receive instant scoring

### 🏆 Leaderboard
- Scores saved automatically to Firestore
- Global ranking updates live

### 🔔 Notifications
- Reminders and updates pushed via FCM

---

<a id="app-release-status"></a>
## 📦 App Release Status 

The app is fully developed and production-ready but not yet published to the Play Store due to developer account issues. An APK build exists and the app compiles without errors — ready to be published once account access is resolved.

---

<a id="future-improvements"></a>
## 🌟 Future Improvements

- Dark mode  
- More languages  
- Achievement badges  
- Larger question bank  
- Animated transitions

---

<a id="contributors"></a>
## 🤝 Contributors

- ST10294145 — Saihil Gurupersad (Group Leader)
- ST10311999 — Dinay Ramchander  
- ST10198206 — Nehara Pillay  
- ST10110356 — Varun Perumal

---

<a id="video-demonstration"></a>
# 🎥 Video Demonstration

<a href="https://youtube.com/shorts/ujJWChi8kzQ" target="_blank">
  <img src="https://img.shields.io/badge/Watch%20Demo%20Video-FF0000?style=for-the-badge&logo=youtube&logoColor=white&labelColor=FF0000" alt="Watch Demo Video" />
</a>

---

## 🪪 License
MIT License — free to use, modify, distribute.
