# 🏗️ System Architecture Diagram – PlantyCare App

The **System Architecture Diagram** provides a high-level overview of the structural design of the PlantyCare application. It illustrates how the different components of the system interact with each other to achieve smooth functionality, scalability, and integration between mobile, AI models, and backend services.

---

## 📌 Importance of the System Architecture Diagram

- Provides a **clear, visual representation** of the system’s overall structure.
- Helps in understanding the **communication flow** between frontend, backend, AI models, and databases.
- Facilitates **team collaboration**, allowing developers, designers, and stakeholders to stay aligned.
- Acts as a reference during **system upgrades, debugging, and deployment**.
- Improves **project documentation** and planning.

---

## 🧩 Key Components in the Architecture

### 1. 📱 **Mobile Application (Frontend)**
- **Platform**: Android (Java)
- Provides user interface for login, disease prediction, crop and fertilizer recommendation, and history/profile access.
- Handles image capture, form inputs, and result display.

### 2. 🤖 **AI Model Layer**
- **Frameworks**: PyTorch (used in disease prediction model), others as needed.
- Responsible for:
  - Disease classification from plant leaf images.
  - Crop prediction based on soil and climate data.
  - Fertilizer recommendation based on crop and soil composition.
- Receives requests from the app, processes data, and returns predictions.

### 3. ☁️ **Firebase Backend Services**
- **Authentication**: User login, signup, and session management.
- **Realtime Database**: Stores prediction history, profile data, and statistics.
- **Storage**: Saves plant images and AI results.

### 4. 🔁 **Communication Layer**
- **REST API / Cloud Functions**:
  - Bridges the mobile frontend with the AI models.
  - Manages data exchange between app and backend systems.

---

## 🔄 Data Flow Overview

1. **User interacts** with the app interface (e.g., uploads image, enters data).
2. App sends request to **AI Model layer** or Firebase through the communication layer.
3. **AI Model** processes the input and returns predictions.
4. Results are displayed to the user and saved in the **Firebase Realtime Database** and **Storage**.
5. History and statistics can later be retrieved by the **Profile** and **History** pages.

---

## ✅ Contribution to the Project

- Ensures a **modular and scalable design** that can accommodate future enhancements.
- Promotes **separation of concerns** between frontend, AI logic, and backend services.
- Facilitates **efficient troubleshooting** by clearly identifying each system layer.
- Strengthens the foundation for **secure, maintainable, and extensible** app development.

---
