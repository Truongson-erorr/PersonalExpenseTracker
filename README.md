# 💰 Personal Expense Tracker

**Personal Expense Tracker** is a modern Android application built with **Kotlin** and **Jetpack Compose**, designed to help users manage, analyze, and improve their personal financial habits effectively.

This app provides an intuitive way to track spending, set budgets, visualize expenses, and even predict future spending using AI — all within a clean and responsive interface.

---

## 🚀 Overview

With **Personal Expense Tracker**, users can:

- ✅ **Add, edit, and delete transactions** (income & expenses)  
- ✅ **Visualize financial data** through interactive **bar and pie charts**  
- ✅ **Create and monitor monthly budgets**, receive alerts when limits are exceeded  
- ✅ **Manage savings goals (Money Jars)** and contribute progressively  
- ✅ **Track loans & debts**, and mark them as paid  
- ✅ **Edit personal information** easily  
- ✅ **AI-powered prediction** of next month’s spending based on transaction history  
- ✅ **Get notifications** for new activities, app updates, and important reminders  
- ✅ **In-app Support Center** for FAQs, guides, and troubleshooting tips  

---

## 🧠 Architecture & Design

The project is built using the **MVVM (Model - ViewModel - View)** architecture to ensure:
- Clear separation of UI, data, and logic layers  
- Reactive and responsive UI with **Jetpack Compose**  
- Scalable and maintainable structure suitable for future updates  

---

## 🧩 Core Features

### 💸 Transaction Management
- Add, edit, or delete income and expense records  
- Categorize transactions for better analysis  
- Voice input support using **Speech Recognizer**

### 💰 Monthly Budget
- Set monthly spending limits  
- Track real-time spending progress with **progress bars**  
- Get alerts when exceeding limits

### 📊 Reports & Analytics
- Generate bar and pie charts for income vs. expense  
- View total spending per category  
- Compare performance over different months

### ✅ Saving Jars
- Create “money jars” for savings goals  
- Contribute periodically  
- Track your saving progress visually  

### 💵 Loans & Debts
- Add new loans or borrowed amounts  
- Confirm when payments are completed  
- Maintain clear financial records

### 👤 Personal Profile
- Update personal information and preferences  
- Data securely synced with Firebase

### 🤖 AI Smart Prediction
- Predict next month’s expected spending based on user habits  
- Provide insights for better budgeting decisions  

### 🔔 Notifications
- Real-time notifications for login, budget limits, new updates, and reminders  
- Stored and managed in Firestore

### 🧭 Support Center
- Access FAQs and troubleshooting tips right inside the app  
- Help users understand app features and resolve issues quickly  

---

## 🧱 Project Structure
com.example.personalexpensetracker/
│
├── model/ # Data models (Transaction, Budget, Notification, etc.)
├── view/ # Jetpack Compose UI Screens & Dialogs
├── viewmodel/ # ViewModels managing UI state & logic
├── repository/ # Firebase interaction and data logic
├── navigation/ # NavHost and route management
└── utils/ # Constants, formatters, and helpers

## 📷 Screenshots
...
