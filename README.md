# 🎬 CineStack - Movie Watchlist & Review App

**ICT3214 – Mobile Application Development**  
**Department of Computing - Faculty of Applied Sciences**  
**Rajarata University of Sri Lanka**

A Java-based Android application for managing movie watchlists and reviews with secure user authentication.

---

## 📱 Features

### ✅ Implemented (Phase 1)
- **User Authentication System**
  - Secure registration with SHA-256 password hashing
  - Login with username or email
  - Session management using SharedPreferences
  - "Remember Me" functionality
  - Auto-login for returning users

### 🚧 Coming Soon (Phase 2)
- **Movie Watchlist Management**
  - Browse and search movies
  - Add movies to personal watchlist
  - Mark movies as watched
  
- **Rating & Review System**
  - Rate watched movies (1-5 stars)
  - Write short reviews
  - View personal review history

---

## 🛠️ Technologies

- **Language:** Java
- **UI Framework:** XML Layouts (Material Design)
- **Database:** SQLite with SQLiteOpenHelper
- **Architecture:** Simple MVC pattern
- **Security:** SHA-256 password hashing
- **Session:** SharedPreferences

---

## 📂 Project Structure

```
CineStack/
├── app/src/main/java/com/example/cinestack/
│   ├── DatabaseHelper.java       # SQLite database management
│   ├── SessionManager.java       # Login session handling
│   ├── LoginActivity.java        # User login screen
│   ├── RegisterActivity.java    # User registration screen
│   └── MainActivity.java         # Main application screen
├── app/src/main/res/layout/
│   ├── activity_login.xml        # Login UI
│   ├── activity_register.xml    # Registration UI
│   └── activity_main.xml         # Main screen UI
└── app/src/main/AndroidManifest.xml
```

---

## 🔐 Security Features

- ✅ **SHA-256 Password Hashing** - Passwords never stored in plain text
- ✅ **Input Validation** - Comprehensive client-side validation
- ✅ **SQL Injection Prevention** - Parameterized queries
- ✅ **Unique Constraints** - Prevents duplicate usernames/emails
- ✅ **Session Security** - Secure SharedPreferences implementation

---

## 🗄️ Database Schema

### Users Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | INTEGER | PRIMARY KEY, AUTOINCREMENT |
| username | TEXT | NOT NULL, UNIQUE |
| email | TEXT | NOT NULL, UNIQUE |
| password | TEXT | NOT NULL (SHA-256 hashed) |
| full_name | TEXT | NOT NULL |
| created_at | TEXT | NOT NULL |

---

## 🚀 Installation & Setup

### Prerequisites
- Android Studio (Arctic Fox or later)
- Android SDK (API Level 21+)
- Java Development Kit (JDK 8+)

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/sh13y/CineStack.git
   ```

2. Open project in Android Studio

3. Sync Gradle files

4. Run on emulator or physical device

### Building APK
```bash
./gradlew assembleDebug
```
APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

---

## 👥 Team Contributions

### My Responsibilities ✅
1. ✅ Database schema design
2. ✅ SQLiteOpenHelper implementation
3. ✅ Users table with secure password hashing
4. ✅ Registration system with validation
5. ✅ Login system with SHA-256 verification
6. ✅ Session management (keep user logged in)

### Upcoming Features (Team Members)
- Movie database integration
- Watchlist management UI
- Rating and review functionality
- Search and filter features

---

## 📸 Screenshots

*Coming soon...*

---

## 🎓 Academic Information

- **Module Code:** ICT3214
- **Module Name:** Mobile Application Development
- **Project Type:** Group Project
- **University:** Rajarata University of Sri Lanka
- **Faculty:** Faculty of Applied Sciences
- **Department:** Department of Computing

---

## 📄 License

This project is developed for academic purposes at Department of Computing - Faculty of Applied Sciences - Rajarata University of Sri Lanka.

---

## 🔗 Links

- [Project Repository](https://github.com/sh13y/CineStack)
- [Issue Tracker](https://github.com/sh13y/CineStack/issues)

---

**Last Updated:** February 9, 2026
