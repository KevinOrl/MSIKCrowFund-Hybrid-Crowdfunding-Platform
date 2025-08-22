MSIKCrowFund – Hybrid Crowdfunding Platform

MSIKCrowFund is a hybrid crowdfunding platform developed as part of an academic project focused on planning, managing, and executing software processes using the PMI framework. The system enables users to create, manage, and support donation-based initiatives through both a native Android app and a responsive web interface.

Repository Structure

MSIKCrowFund/
|- android-app/   # Native Android application built with Kotlin
|- web-app/       # Responsive web interface built with React and Tailwind CSS


Project Overview

The platform simulates a digital economy where users can:
- Register and manage profiles using institutional TEC email validation.
- Create and edit crowdfunding projects with goals, deadlines, and multimedia content.
- Browse and filter active projects by category, funding progress, and time remaining.
- Make simulated donations using a virtual wallet with fictional currency.
- Track donation history and receive email notifications for key events.
- Access admin features to monitor users, donations, and system statistics.

Key Features

- User Authentication: Firebase Auth with full profile setup and email validation.
- Project Management: Multimedia support for project creation and editing.
- Donation System: Virtual wallet with balance validation and donation tracking.
- Admin Dashboard: Real-time monitoring of system activity and statistics.
- Email Notifications: Automated alerts for registration, updates, and donations.

Technologies Used

Android App (android-app/)

- Programming Language: Kotlin
- Architecture Pattern: MVVM
- Database: Firebase Realtime Database
- Authentication: Firebase Auth
- Notifications: Firebase + custom email templates
- Development Tools: Android Studio

Web App (web-app/)

- Frontend Framework: React
- Styling: Tailwind CSS
- Hosting: Firebase Hosting
- Authentication: Firebase Auth
- Development Tools: Visual Studio Code

Academic Context

This project was developed during a semester-long course on Software Requirements Engineering at Instituto Tecnológico de Costa Rica. It simulates a real-world crowdfunding platform under educational constraints:
- Use of fictional currency (no real payment gateways).
- Cloud-based data persistence.
- Institutional email validation.
- Weekly milestones and deliverables.
