# 🌐 PlantyCare App – Frontend & Backend

This directory contains the complete **Frontend** and **Backend** implementation of the **PlantyCare App**, an AI-powered Android application developed to diagnose plant diseases and provide agricultural recommendations tailored to Egypt's fruits and vegetables.

---

## 📲 Frontend (Mobile Application)

- **Language:** Java
- **IDE:** Android Studio
- **Architecture:** XML Layouts + Java Logic
- **Authentication:** Firebase Authentication

### 🏠 Home Page

- Displays **three main features** vertically scrollable:
  - 🌿 **AgriCure** – Detects plant diseases from leaf images.
  - 🌾 **PlantPick** – Recommends crops based on environmental conditions.
  - 💧 **FertiGuide** – Suggests suitable fertilizers based on soil characteristics.

- **Top section:** Shows a scrollable **History** for AgriCure:
  - Each item includes an image, date, and time.
  - Tapping an image opens the detailed result page.

- **Bottom Navigation Bar** with 4 tabs:
  - `Home` – Main feature dashboard.
  - `Calendar` – Month-wise crop planting/harvesting guide.
  - `Dictionary` – Key agricultural terms and definitions.
  - `Profile` – Displays user info and usage stats.

---

### 🌿 AgriCure (Plant Disease Detection)

- **Process:**
  1. User chooses to upload or capture an image.
  2. After image selection, a popup appears with a ✅ button to confirm.
  3. App navigates to result page showing:
     - Predicted disease name.
     - The submitted leaf image.
     - A short disease description.
     - Prevention instructions.
     - Treatment image and details.
  4. History is automatically updated and stored in Firebase Realtime Database.

---

### 🌾 PlantPick (Crop Recommendation)

- **Inputs Required:**
  - N, P, K
  - Temperature, Humidity
  - pH, Rainfall
- **Output:** A popup with:
  - Recommended **crop name**
  - Relevant **crop image**

---

### 💧 FertiGuide (Fertilizer Recommendation)

- **Inputs Required:**
  - Crop type
  - Soil color
  - Nitrogen, Phosphorus, Potassium
  - pH, Rainfall, Temperature
- **Output:** A popup with:
  - Recommended **fertilizer name**
  - Corresponding **fertilizer image**

---

### 📅 Calendar Page

- Month-wise breakdown of:
  - 🌱 Top 5 crops planted in Egypt.
  - 🌾 Top 5 crops harvested in that month.

---

### 📘 Dictionary Page

- A collection of essential terms used in the app with brief explanations.

---

### 👤 Profile Page

- Shows:
  - User’s profile picture, name, email, and join date.
  - Usage count for each main feature (AgriCure, PlantPick, FertiGuide).

---

### ⚙️ Additional Pages

- `Settings`
- `Edit Profile`
- `About Us`
- `FAQs`
- `Privacy Policy`
- `Contact Us`
- `Rate Us`
- `Share the App`

---

## 🔧 Backend (Firebase Integration)

- **Platform:** Firebase (Google Cloud)

### 🧩 Modules Used:

- 🔐 **Authentication:** User sign-up and login.
- ☁️ **Realtime Database:** Stores:
  - Prediction results
  - User history
  - Profile statistics
- 🗃️ **Cloud Storage:** Saves uploaded images used in predictions.

---

### 🔄 Functionality Highlights

- Store and retrieve plant disease results.
- Maintain per-user usage tracking.
- Sync data between app UI and cloud backend in real-time.
- Secure access to sensitive user info via Firebase Auth.

---

## 🧪 Testing and Validation

- Tested using real devices and Android emulators.
- Full error handling for invalid inputs and offline mode.
- Verified AI predictions through connected Python models (served via APIs).
