# WorkTimeTrackerManager

A native Android management app for the WorkTimeTracker system, enabling managers to oversee employees, projects, schedules, leave requests, and real-time team messaging.

## Features

- **Employee & Project Management**  
  View, add, edit, and deactivate employees and projects in a unified interface.

- **Schedule Creation & Assignment**  
  Create and assign work schedules with calendar-style views and bulk editing.

- **Leave Request Workflows**  
  Review, approve, or reject time-off requests with automated notifications and audit trails.

- **Real-Time Messaging**  
  Instant in-app chat between managers and staff using WebSockets.

## Tech Stack

- **Language:** Kotlin  
- **UI:** Jetpack Compose  
- **Networking:** Retrofit + OkHttp  
- **Real-Time:** Socket.IO Client for Android  
- **Authentication & Database:** Supabase (Auth & PostgreSQL) via Kotlin SDK  
- **Notifications:** Firebase Cloud Messaging  
- **Dependency Injection:** Hilt  
- **Coroutines & Flow:** Asynchronous data handling  
