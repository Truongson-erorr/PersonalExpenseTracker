# 💰 Personal Expense Tracker

**Personal Expense Tracker** is a comprehensive and modern Android application developed using **Kotlin** and **Jetpack Compose**, aiming to support users in managing their personal finances in an efficient, transparent, and intelligent way.

In the context of increasing living costs and complex financial habits, the application provides a centralized platform that allows users to **record daily income and expenses**, **monitor spending behavior**, and **gain meaningful financial insights** through intuitive visualizations. By organizing transactions into categories and tracking financial activities in real time, users can better understand where their money goes and make more informed decisions.

Beyond basic expense tracking, the application offers **advanced budgeting and savings management features**, enabling users to set monthly spending limits, create personalized savings goals, and track progress visually. When spending exceeds predefined thresholds, the system automatically sends notifications to help users maintain financial discipline.

A notable highlight of the application is the integration of **AI-powered spending prediction**, which analyzes historical transaction data to estimate future expenses. This feature assists users in planning ahead and adjusting their financial strategies proactively.

With a **clean, responsive user interface**, secure data management through **Firebase**, and a scalable **MVVM architecture**, *Personal Expense Tracker* not only enhances user experience but also ensures maintainability and extensibility for future development. The application is designed for individuals who seek better control over their finances and wish to develop healthier financial habits in the long term.

# 🎥 Demo Video
https://drive.google.com/drive/folders/1Sv8bTc8RPHhmkRba83s0J4gA8TyqVWiD?hl=vi


# 🧩 Core Features

### 💸 Transaction Management
- Add, edit, or delete income and expense records  
- Categorize transactions for better insights  
- Voice input support using **Speech Recognizer**

### 💰 Monthly Budget
- Set monthly spending limits  
- Track real-time spending progress with **progress bars**  
- Receive alerts when exceeding limits  

### 📊 Reports & Analytics
- Generate **bar and pie charts** for income vs. expense  
- View total spending by category  
- Compare financial performance across months  

### ✅ Saving Jars
- Create personalized **money jars** for savings goals  
- Contribute periodically and monitor progress  
- Visual tracking of your saving achievements  

### 💵 Loans & Debts
- Manage borrowed or lent money efficiently  
- Mark payments as completed  
- Keep transparent financial records  

### 👤 Personal Profile
- Edit user details and app preferences  
- Sync data securely with **Firebase**  

### 🤖 AI Smart Prediction
- Predict next month’s spending based on historical data  
- Provide intelligent insights for better budgeting decisions  

### 🔔 Notifications
- Real-time alerts for login, budget limits, updates, and reminders  
- Notifications stored and managed through **Firestore**

### 🧭 Support Center
- In-app FAQs and troubleshooting guides  
- Helps users navigate features and resolve issues easily

# 🧰 Technologies Used

| Technology | Description | Purpose in Project |
|-----------|------------|-------------------|
| **Kotlin** | Official programming language for Android development with concise syntax and strong type safety. | Core language used to implement application logic and features. |
| **Jetpack Compose** | Modern declarative UI toolkit for building native Android interfaces. | Used to design all application screens and dynamic UI components. |
| **MVVM Architecture** | Architectural pattern separating Model, View, and ViewModel layers. | Improves code maintainability, scalability, and testability. |
| **Firebase Authentication** | Authentication service supporting secure user login. | Manages user registration, login, and access control. |
| **Firebase Firestore** | NoSQL cloud database with real-time synchronization. | Stores transactions, budgets, savings jars, loans, and notifications. |
| **Firebase Storage** | Cloud storage service for user-generated content. | Stores profile images and other media files securely. |
| **Kotlin Coroutines** | Lightweight framework for asynchronous programming. | Handles background tasks such as database and network operations. |
| **Flow** | Reactive stream API for asynchronous data handling. | Observes real-time data changes and updates UI automatically. |
| **Charts for Jetpack Compose** | Libraries for rendering charts in Compose UI. | Visualizes income, expenses, and spending trends. |
| **ViewModel** | Lifecycle-aware component for managing UI-related data. | Preserves UI state across configuration changes. |
| **LiveData** | Observable data holder class. | Updates UI automatically when data changes. |
| **Speech Recognizer API** | Android API for speech-to-text processing. | Enables voice input for adding transactions. |
| **Material 3 Components** | Google’s latest design system for Android UI. | Ensures modern, consistent, and responsive user interface. |

# 📱 Screenshots 

### 1. Log in and authenticate your account
<div style="display:flex; gap:10px; flex-wrap:wrap;">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153346/z7184192844850_4a12c361575899759337f022df4138b0_ha1qlv.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153346/z7184192852614_e85e74bf2d8de645988c345fb664c09f_hrbyof.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153346/z7184192864124_d2c21ef16ae34e36cfa90431c1aab4b8_kt6s5s.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153346/z7184192884747_6b194cb6f1dd1142191c3bdb9e94ceb8_p4blkq.jpg" width="160"/>
</div>

### 2. Main screen, balance fluctuation
<div style="display:flex; gap:10px; flex-wrap:wrap;">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153347/z7184192884291_b988ea85ec513084c0eb21ccce58ca0b_somezp.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153347/z7184192915512_a3cf26b40e416d7f6816f12ea75465f4_gby47i.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153347/z7184192948890_903d3b274db3a2ba11450645d9fe79bc_jdardr.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153347/z7184192902830_878f21738ac2da99d9b30a4ff50c863c_jcvr8c.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153348/z7184192913486_96292f278d30d6b1b9d2f3224216380a_uaplba.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153347/z7184192949867_4e9c1fc4065cb86656dc185422eed768_suskmu.jpg" width="160"/>
</div>

### 3. Monthly budget management screen
<div style="display:flex; gap:10px; flex-wrap:wrap;">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153347/z7184192949991_4dbf652d4abe46458f01df2973bfaca2_y7pkj5.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153348/z7184192953869_37d83bab33f4dbc877e8f1b8b85d9a41_ic17eo.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153348/z7184193014053_5ade615ea117f87a275cbe7be88ba988_kj1jcv.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153347/z7184193018345_3db84fc3df41b475c192f235f288285e_tcamox.jpg" width="160"/>
</div>

### 4. Notification screen
<div style="display:flex; gap:10px; flex-wrap:wrap;">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153347/z7184193017254_ebcf1dddfba1483db24a76ab4e506287_ykwgvi.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153348/z7184193019618_4f9f00adf3635eda666cd63a525c9a1c_vy1cef.jpg" width="160"/>
</div>

### 5. Spending statistics according to the chart
<div style="display:flex; gap:10px; flex-wrap:wrap;">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153347/z7184193017527_dd8d8c22792eb10028692d94fcead7bd_taappm.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153348/z7184193029404_959fe23e505bbbdb6c67ac12182edea0_mjufis.jpg" width="160"/>
</div>

## 6. Personal profile
<div style="display:flex; gap:10px; flex-wrap:wrap;">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153348/z7184193023448_f418c71c9697c7203514670d6c87f64b_jje95u.jpg" width="160"/>
    <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153349/z7184193034345_1be10b8ae0921ccb6ecd3669822106b5_jm8n1e.jpg" width="160"/>
</div>

### 7. Personal savings jar 
<div style="display:flex; gap:10px; flex-wrap:wrap;">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153349/z7184193090393_76c00739b91bb2f91c06fe7d3d411a21_rn5dlz.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153349/z7184193108655_f724e660c49158e46498247a9ba69570_n6j45e.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153349/z7184193110876_1d954c3c0bff264f190a3f459e4e8ef3_lagkeu.jpg" width="160"/>
    <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153349/z7184193105204_587682b9869ed932592cd1d25f1cdd09_cqzxgv.jpg" width="160"/>
</div>

### 8. Loan/debt management 
<div style="display:flex; gap:10px; flex-wrap:wrap;">

  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153349/z7184193109979_b131548dbd2652cff17fc58645e16361_xtdpw2.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153349/z7184193111133_428c4676e114c74278d571679127774c_cmsso0.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153350/z7184193157634_63501239915d19895603043a2f335b38_qubrv0.jpg" width="160"/>
</div>

### 9. Predict spending with AI 
<div style="display:flex; gap:10px; flex-wrap:wrap;">
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153751/z7184241958754_1b489e2454905291c40160fbaff9ab44_x1fuhw.jpg" width="160"/>
  <img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1762153363/z7184193160353_511e9b3b22133d4e585d953eb082457a_p1ffaw.jpg" width="160"/>
</div>

# 🏗 Project Architecture

```markdown
## 🏗 Project Architecture

The application follows the **MVVM (Model–View–ViewModel)** architecture to ensure scalability, testability, and maintainability.

### Architecture Layers

- **Model**
  - Data classes (Transaction, Budget, Jar, Loan, User)
  - Firebase Firestore collections
  - Repository layer for data handling

- **View**
  - UI built entirely with **Jetpack Compose**
  - Stateless composables observing state from ViewModels
  - Material 3 design principles applied

- **ViewModel**
  - Handles business logic
  - Exposes UI state using **StateFlow / LiveData**
  - Communicates with repositories using **Coroutines**

### Additional Components
- **Repository Pattern** for separating data sources
- **Firebase Authentication** for user management
- **Firestore** for real-time data synchronization
- **AI module** for spending prediction
```

# ⚠️ Limitations

Despite providing core personal finance features, the application still has some limitations:

- AI spending prediction is based on simple historical patterns and may not be fully accurate
- No offline mode; internet connection is required for most features
- Limited customization for charts and reports
- Does not support multi-currency transactions
- No export feature (PDF / Excel) for financial reports
- Currently available only on Android platform

# 🚀 Future Improvements

In future versions, the application can be enhanced with:

- Advanced AI models for more accurate expense forecasting
- Offline mode with local database synchronization
- Multi-currency and exchange rate support
- Export reports to PDF or Excel formats
- Dark mode and UI customization options
- Integration with bank APIs for automatic transaction syncing
- Cross-platform support (iOS / Web)
- Enhanced security with biometric authentication

# 🛠 Development Tools
- Android Studio
- Gradle Build System
- Git & GitHub for version control

