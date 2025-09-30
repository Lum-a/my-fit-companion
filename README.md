# MyFitCompanion 💪

A comprehensive fitness companion Android application built with Jetpack Compose, featuring workout tracking, meal planning, trainer connections, and profile management with photo upload capabilities.

## 📱 Features

### 🔐 Authentication & User Management
- **User Registration & Login** - Secure authentication system with email validation
- **Profile Management** - Complete user profiles with customizable information
- **Photo Upload** - Camera integration with cloud storage for profile pictures
- **Settings Management** - Change email and password functionality

### 🏋️ Fitness & Workout
- **Workout Planning** - Create and manage workout routines
- **Exercise Tracking** - Detailed exercise management with splits
- **Progress Monitoring** - Track fitness goals and body metrics (height, weight, body fat)

### 🍽️ Meals
- **Meal Planning** - Comprehensive meal management system

### 👨‍🏫 Trainer Connection
- **Trainer Profiles** - Connect with fitness trainers
- **Professional Guidance** - Access to trainer expertise and programs

### 🛠️ Admin Panel
- **User Management** - Admin interface for user oversight
- **Content Management** - Manage workouts, meals, and trainer profiles
- **System Administration** - Complete admin control panel

## 🏗️ Technical Architecture

### **Technology Stack**
- **Language:** Kotlin 2.0.21
- **UI Framework:** Jetpack Compose with Material 3
- **Architecture:** MVVM with Clean Architecture
- **Dependency Injection:** Dagger Hilt
- **Navigation:** Navigation Compose with type-safe routing
- **Database:** Room with SQLite
- **Networking:** Retrofit + OkHttp + Moshi
- **Image Loading:** Coil Compose
- **Animations:** Lottie Compose
- **Data Storage:** DataStore Preferences
- **Cloud Storage:** Appwrite SDK

### **Key Libraries & Versions**
```toml
# Core Android
compileSdk = 35
targetSdk = 35
minSdk = 24

# Major Dependencies
Compose BOM = "2024.09.00"
Kotlin = "2.0.21"
Hilt = "2.56.2"
Room = "2.7.2"
Retrofit = "2.11.0"
Navigation Compose = "2.9.3"
```

## 🏛️ Project Structure

```
app/src/main/java/com/example/myfitcompanion/
├── 📱 MainActivity.kt                 # Main activity entry point
├── 🚀 MyFitApplication.kt            # Application class with Hilt
├── 🎨 ui/theme/                      # App theming (black & gold theme)
├── 🧭 navigation/                    # Navigation setup and routing
├── 📺 screen/                        # UI screens and composables
│   ├── splash/                       # Splash screen
│   ├── login/                        # Authentication screens
│   ├── signup/                       # Registration flow
│   ├── home/                         # Main dashboard
│   ├── profile/                      # Profile management & settings
│   ├── workout/                      # Workout and exercise screens
│   ├── meal/                         # Meal planning interface
│   └── trainer/                      # Trainer connection screens
├── 🎛️ admin/                        # Admin panel functionality
├── 🌐 api/                          # API models and services
├── 💾 db/                           # Room database components
├── 🏪 repository/                   # Data layer repositories
├── 💉 di/                           # Dependency injection modules
├── 🧩 components/                   # Reusable UI components
├── 📱 appwrite/                     # Appwrite integration
└── 🔧 utils/                        # Utility classes and helpers
```

## 🚀 Getting Started

### **Prerequisites**
- Android Studio Hedgehog | 2023.1.1 or newer
- JDK 11 or higher
- Android SDK API level 24+ (Android 7.0)
- Git

### **Installation**

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd MyFitCompanion
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the project folder and open it

3. **Configure Backend**
   - The app connects to: `https://my-fit-companion-production.up.railway.app/api/`
   - Backend configuration is set in `build.gradle.kts`

4. **Setup Cloud Storage (Optional)**
   - Configure Appwrite for image uploads
   - Add your Appwrite configuration

5. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   # or use Android Studio's Run button
   ```

## 🎨 Design System

### **Color Scheme**
- **Primary Theme:** Black & Gold
- **Background:** Pure black for dark theme
- **Accent:** Gold (#FFD700) for buttons and highlights
- **Text:** White for primary text, gray for secondary
- **Error States:** Red for validation errors

### **UI Components**
- **Material 3 Design System**
- **Custom themed buttons and text fields**
- **Consistent spacing and typography**
- **Responsive layouts for various screen sizes**

## 📊 Core Features Breakdown

### **Authentication Flow**
- Email validation with regex patterns
- Password strength validation (8+ characters, uppercase, lowercase, number)
- Secure login/logout functionality
- Registration with comprehensive user data collection

### **Profile Management**
- **Personal Information:** Name, email, physical metrics
- **Body Metrics:** Height, weight, body fat percentage
- **Fitness Goals:** Customizable goal setting
- **Photo Management:** Camera capture with cloud upload
- **Settings:** Email and password change functionality

### **Workout System**
- **Workout Creation:** Build custom workout routines
- **Exercise Library:** Comprehensive exercise database
- **Split Training:** Organize workouts into splits
- **Progress Tracking:** Monitor workout completion and progress

### **Admin Panel Features**
- **User Management:** View and manage all users
- **Content Administration:** Manage workouts and meals
- **Trainer Management:** Oversee trainer profiles
- **System Analytics:** Monitor app usage and performance

## 🔧 Development Guidelines

### **Code Structure**
- **MVVM Architecture:** Clear separation of concerns
- **Repository Pattern:** Centralized data management
- **Dependency Injection:** Hilt for clean dependency management
- **Type-Safe Navigation:** Kotlin serialization for navigation args

### **Data Flow**
```
UI (Compose) ↔ ViewModel ↔ Repository ↔ [API/Database]
```

### **Error Handling**
- **ResultWrapper:** Standardized result handling
- **User-friendly error messages**
- **Loading states and progress indicators**
- **Offline capability with Room database**

## 🌐 API Integration

### **Backend Services**
- **Authentication:** User login/registration
- **Profile Management:** User data CRUD operations
- **Workout Data:** Exercise and routine management
- **Meal Planning:** Nutrition data services
- **File Upload:** Profile picture handling

### **Network Configuration**
- **Base URL:** Production backend on Railway
- **HTTP Client:** OkHttp with logging interceptor
- **JSON Parsing:** Moshi with Kotlin codegen
- **Error Handling:** Comprehensive error response handling

## 📱 Permissions & Hardware

### **Required Permissions**
- `INTERNET` - API communication
- `CAMERA` - Profile photo capture
- `READ_EXTERNAL_STORAGE` - Image selection

### **Hardware Features**
- **Camera** - Optional for profile photos
- **Network** - Required for full functionality

## 🔒 Security Features

- **Input Validation:** Email and password validation
- **Secure Storage:** DataStore for preferences
- **File Provider:** Secure file sharing for camera
- **Authentication Tokens:** Secure API communication

## 🎯 Future Enhancements

- [ ] Social features and friend connections
- [ ] Advanced workout analytics
- [ ] Nutrition barcode scanning
- [ ] Wearable device integration
- [ ] Offline mode improvements
- [ ] Push notifications
- [ ] Achievement and reward system

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 Support

For support and questions:
- Create an issue in the repository
- Check existing documentation
- Review the code comments for implementation details

---

**Built with ❤️ using Jetpack Compose and modern Android development practices**
