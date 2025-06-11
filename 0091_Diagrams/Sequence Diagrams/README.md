# ⏱️ Sequence Diagrams – PlantyCare App

**Sequence Diagrams** are used to represent how different components of the PlantyCare App interact with each other over time to fulfill specific functionalities. These diagrams are essential in visualizing the flow of logic, frontend-backend communication, and user-system interactions.

---

## 📌 Importance of Sequence Diagrams

- Visualize the **time-ordered interactions** between system components (objects, modules, APIs, databases).
- Help developers and designers understand the **workflow** of specific features.
- Simplify **debugging and maintenance** by mapping out communication sequences.
- Serve as a foundation for **implementation, testing, and documentation**.
- Clarify the **role of each module** in user interactions and data flow.

---

## 📊 Sequence Diagrams in PlantyCare App

We have created **six key sequence diagrams** that represent critical user journeys and system interactions within the app:

### 1. 🔐 Login & SignUp Sequence
- Describes how the user interacts with the authentication form.
- Shows the backend validation with **Firebase Authentication**.
- Outlines the flow for successful login, signup, and handling errors.

---

### 2. 🌿 AgriCure Module – 3 Diagrams:
#### a. Image Input (Camera/Gallery)
- Displays the process when the user selects or captures an image of the plant leaf.
- Covers how the image is passed to the detection model.

#### b. Disease Prediction
- Outlines the communication between the app and the **disease detection AI model**.
- Shows how the result (disease name, description, and treatment) is retrieved and displayed.

#### c. Result Storage in History
- Explains how the prediction result and image are saved in **Firebase Realtime Database**.
- Displays how timestamps and diagnosis records are maintained.

---

### 3. 🌾 PlantPick Module
- Details how the user inputs environmental and soil data (N, P, K, temperature, humidity, pH, rainfall).
- Shows the interaction with the **crop recommendation model**.
- Displays how the predicted crop and corresponding image are shown to the user.

---

### 4. 💧 FertiGuide Module
- Maps the flow from user inputs (crop, soil color, NPK, etc.) to the **fertilizer recommendation model**.
- Illustrates how the result and fertilizer image are returned in the app.

---

### 5. 🕘 History Retrieval
- Demonstrates how stored diagnosis records are fetched from Firebase.
- Shows how clicking a history image loads the detailed result (disease info, image, treatment).

---

### 6. 👤 Profile Page Interaction
- Displays how user profile data (name, email, registration date) is fetched.
- Shows how usage statistics (number of times each AI feature is used) are retrieved and displayed.

---

## ✅ Contribution to the Project

- Ensures **clarity** of complex processes.
- Helps in **effective collaboration** between frontend, backend, and AI teams.
- Serves as a reference for **testing and debugging** during development.
- Enhances the **documentation quality** of the overall project.

---
