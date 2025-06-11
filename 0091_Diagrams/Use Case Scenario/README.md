# 📖 Use Case Scenarios – PlantyCare App

The **Use Case Scenarios** provide detailed step-by-step descriptions of how users interact with various features of the **PlantyCare App** in real-life situations. These scenarios are essential for understanding how the system should behave from the user's perspective, and they help in guiding development, testing, and user experience improvements.

---

## 🎯 Purpose of Use Case Scenarios

- Describe **real-world user interactions** with the system.
- Highlight the **expected flow** and outcomes of each feature.
- Identify **edge cases**, **exceptions**, and **alternative flows**.
- Serve as a foundation for **functional testing** and **user validation**.

---

## 🧪 Use Case Scenarios Overview

Below are the main scenarios implemented in the PlantyCare App:

---

### 1. 🔍 Scenario: Diagnose a Plant Disease (AgriCure)

**Actor:** Registered User  
**Precondition:** User is logged in.

**Steps:**
1. User navigates to the Home Page.
2. Taps on the “AgriCure” option.
3. Chooses to either:
   - Upload an existing image from the gallery, or
   - Capture a new photo using the device camera.
4. After uploading, taps the confirmation button.
5. System sends the image to the backend AI model (PyTorch-based).
6. Receives diagnosis: displays disease name, input image, brief description, treatment steps, and visual treatment reference.
7. The result is stored in the user’s history in Firebase.
8. User can revisit past results from the History section.

---

### 2. 🌾 Scenario: Get Crop Recommendation (PlantPick)

**Actor:** Registered User  
**Precondition:** User is logged in.

**Steps:**
1. From the Home Page, user selects the “PlantPick” option.
2. Enters the required environmental values:
   - N, P, K
   - Temperature, Humidity
   - pH, Rainfall
3. Clicks the "Predict" button.
4. AI model returns the most suitable crop.
5. System displays crop name and corresponding image in a popup.

---

### 3. 💧 Scenario: Get Fertilizer Recommendation (FertiGuide)

**Actor:** Registered User  
**Precondition:** User is logged in.

**Steps:**
1. From the Home Page, user selects the “FertiGuide” option.
2. Inputs the following:
   - Crop type, Soil color
   - Nitrogen, Phosphorus, Potassium
   - pH, Temperature, Rainfall
3. Clicks “Predict.”
4. AI model returns a suitable fertilizer.
5. System displays the fertilizer name and an image with usage description.

---

### 4. 🕓 Scenario: Access Diagnosis History

**Actor:** Registered User  
**Precondition:** At least one diagnosis has been made.

**Steps:**
1. From the Home Page, user views the horizontally scrollable history section.
2. Taps any image from the history.
3. System displays the complete diagnosis result corresponding to the selected image.

---

### 5. 👤 Scenario: View Profile Activity

**Actor:** Registered User

**Steps:**
1. User navigates to the Profile Page.
2. Sees profile details including:
   - Username, Email, Join date
   - Number of times each AI model was used (AgriCure, PlantPick, FertiGuide)

---

### 6. 🔐 Scenario: Login & Sign Up

**Actor:** New or Returning User

**Steps:**
- **Sign Up:**
  1. User clicks “Sign Up.”
  2. Enters email, password, and other credentials.
  3. Firebase registers the account.
  4. User is redirected to the Home Page.

- **Login:**
  1. User enters existing credentials.
  2. Firebase authenticates the user.
  3. User accesses the main Home Page.

---

## ✅ Benefits of Use Case Scenarios

- Ensure all **user interactions are well-defined** and testable.
- Improve **collaboration** between developers and designers.
- Provide a **clear reference** for UX and testing teams.
- Help identify **potential flaws or enhancements** in flow.

---
