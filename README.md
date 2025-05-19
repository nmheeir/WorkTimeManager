# WorkTimeTrackerManager App

A Kotlin + Jetpack Compose Android application implementing MVVM architecture to streamline employee attendance, scheduling, and task management.

---

## 🚀 Technology Stack
- **Language:** Kotlin  
- **UI:** Jetpack Compose  
- **Architecture / Design Pattern:** MVVM  
- **Backend Integration:** .NET RESTful API  
- **Authentication & Notifications:** JWT, Firebase Cloud Messaging  

---

## 🔑 Features

### 🧑‍💼 Employee & Team Management
- **Account Management**  
  - Create / Update / Delete employee profiles  
  - Assign employees to teams  
- **Team Management**  
  - Create, edit, delete “Teams”  
  - View team member lists  

### 📅 Scheduling & Attendance
- **Shift Management**  
  - Define shift schedules  
  - Assign shifts to employees  
- **Time-Off Requests**  
  - Submit leave requests  
  - Approve or reject requests (manager)  
- **Attendance History**  
  - View check‑in / check‑out logs  
  - Filter and export activity logs  

### 📊 Reporting & Dashboard
- **Statistics & Reports**  
  - Generate per‑employee attendance summaries  
  - View team‑ and company‑level dashboards  
- **Project & Task Management**  
  - Create and assign project tasks  
  - Track task progress by team member  

### 🔒 Security & Notifications
- **Authentication**  
  - Secure login with JWT tokens  
  - Role‑based access control (Master / Manager / Staff)  
- **Push Notifications**  
  - Real‑time alerts via Firebase Cloud Messaging  
  - Notify employees of shift changes, approvals, and updates  

---

## 📡 Backend Integration

- Consumes a **.NET Core RESTful API** for all data operations  
- Uses JWT for secure authenticated requests  
- Syncs real‑time notifications via Firebase  

---

## 🏗️ Project Structure

## ⚙️ Getting Started

1. **Clone the repo**  
   ```bash
   git clone https://github.com/nmheeir/WorkTimeManager.git
   cd WorkTimeManager

---
## 🤝 Contributing

Feel free to open issues or submit pull requests. Please follow the MVVM structure and write unit tests for new features.
