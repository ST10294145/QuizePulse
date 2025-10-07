Here's an improved version of your README file for **QuizPulse**. I've enhanced the formatting, clarity, and organization to make it more engaging and user-friendly.

---

# 📱 QuizPulse

**QuizPulse** is an engaging quiz application designed to challenge users across various categories while providing a clean and user-friendly experience. Built with Kotlin in Android Studio, the app integrates Firebase Authentication and Google Sign-In (SSO) for seamless account creation and login.

## 🚀 Features

### 🧑‍💻 User Authentication
- **Email and Password Registration:** Users can create an account with their email address and a secure password.
- **Google Sign-In (SSO):** Alternatively, users can register or sign in using their Google account.
- **Firebase Authentication:** All account data is securely managed and stored on Firebase.

### 🧠 Quiz System
- Users can select from a range of quiz categories.
- Each quiz category consists of 3 multiple-choice questions.
- Scores are displayed at the end of the quiz (currently via a Toast message; will later be displayed on a scoreboard).

### ⚙️ Settings
- Users can update their email address.
- Users can change their password securely through Firebase Authentication.

### 🏆 Scoreboard (Future Development - Part 3)
- A Scoreboard screen (`scoreboard.xml`) is included in the project.
- Future updates will implement a persistent scoreboard to display users’ results instead of using Toast messages.

## 🧩 Tech Stack

| Component          | Technology Used                        |
|--------------------|---------------------------------------|
| Frontend           | Kotlin (Android Studio)               |
| Authentication     | Firebase Authentication                |
| Database           | Firebase Realtime Database / Firestore (if integrated later) |
| Cloud Platform     | Google Firebase                       |
| UI Design          | XML Layouts (ConstraintLayout, LinearLayout, etc.) |
| Version Control     | Git / GitHub                         |

## ⚙️ Setup & Installation

### 1️⃣ Prerequisites
- **Android Studio:** Latest version recommended.
- **Google Firebase Account:** Required for authentication and database services.
- **Android SDK:** Ensure it is installed.
- **Git:** For version control (optional but recommended).

### 2️⃣ Clone and Run the Project
```bash
# Clone the repository
git clone https://github.com/ST10294145/QuizPulse.git

# Open the project in Android Studio
# Connect your emulator or Android device
# Click "Run"
```

## 🧠 How It Works

1. **Registration / Login**
   - New users can sign up using Email/Password or Google SSO.
   - Existing users log in using Firebase Authentication.

2. **Quiz Selection**
   - After login, users select their preferred quiz category.

3. **Taking the Quiz**
   - Each category contains 3 multiple-choice questions.
   - After completion, the total score is displayed using a Toast message.

4. **Account Management**
   - Users can go to Settings to change their email or password.

5. **Scoreboard (Future Update)**
   - A scoreboard will store and display the top scores from Firebase.

## 🧪 Future Improvements
- Display user scores on the Scoreboard screen (`scoreboard.xml`).
- Implement Firebase Firestore to store quiz results and leaderboard data.
- Add more quiz categories and difficulty levels.
- Improve UI/UX with animations and Material Design 3.
- Add dark mode support.

## 🤝 Contributors
- **ST10294145** - Saihil Gurupersad
- **ST10311999** - Dinay Ramchander
- **ST10198206** - Nehara Pillay
- **ST10110356** - Varun Perumal

## 🪪 License
This project is licensed under the MIT License — you are free to use, modify, and distribute it under the same terms.

---

Feel free to adjust any sections as needed!
