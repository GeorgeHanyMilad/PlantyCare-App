# 🧩 System Diagrams – PlantyCare App

This section documents the various system diagrams designed for the PlantyCare mobile application. These diagrams help visualize the architecture, behavior, and data flow within the system, serving as essential references for both developers and stakeholders.

---

## 📌 Data Flow Diagram (DFD)

The Data Flow Diagrams illustrate how data moves within the system.

- **Level 0 (Context Diagram):** Shows the overall interaction between the user and the entire system as a single process.
- **Level 1:** Breaks the system into sub-processes such as image upload, AI model processing, database storage, and history retrieval, providing a more detailed view of internal operations.

---

## 🧱 Class Diagram

The Class Diagram outlines the system’s core classes, their attributes, and relationships.

Key classes include:
- `User`
- `PlantImage`
- `PredictionResult`
- `CropRecommendation`
- `FertilizerRecommendation`
- `FirebaseManager`

This diagram supports understanding of object-oriented structure and guides code development and maintenance.

---

## 🔁 Sequence Diagrams

The Sequence Diagrams display the chronological flow of events between users and system components for different use cases.

Examples:
- **AgriCure Flow:** Upload image → AI processes image → Result shown → Result saved to history.
- **PlantPick Flow:** Input soil and environment data → Predict crop → Show recommendation.
- **FertiGuide Flow:** Input crop and soil details → Predict fertilizer → Show suggestion.

These diagrams help ensure clear understanding of how features work at runtime.

---

## 🏛️ System Architecture

The System Architecture diagram provides an overview of the system’s main components and how they interact.

Includes:
- Android mobile application frontend
- Firebase backend services (Authentication, Storage, Realtime Database)
- Integrated AI models (via local or cloud-based inference)
- Flow of data and logic between these layers

This diagram offers a bird’s-eye view of the entire system’s structure and logic.

---

## 🧑‍💻 Use Case Diagram

The Use Case Diagram maps out all possible actions users can perform in the system.

Core use cases:
- Sign up / Log in
- Diagnose plant disease (AgriCure)
- Predict best crop (PlantPick)
- Recommend suitable fertilizer (FertiGuide)
- View history of diagnoses
- Access calendar, glossary, and profile

Actors: Registered users interacting with different system functionalities.

---

## 📖 Use Case Scenarios

Detailed user interaction flows for key features:

### 1. AgriCure Scenario:
The user logs in, uploads or captures an image of a plant leaf. The AI model detects the disease, and the result page shows the diagnosis, treatment steps, and treatment image. This result is saved in the user’s history.

### 2. PlantPick Scenario:
The user enters soil and climate data (N, P, K, temperature, pH, etc.). The system predicts the most suitable crop and shows the name along with an image.

### 3. FertiGuide Scenario:
The user inputs crop type and soil information (pH, N, P, K, rainfall, etc.). The system recommends the best fertilizer, displaying both name and image of the recommended product.

---

## 🗂️ Notes

- These diagrams are a critical part of the system design and documentation process.
- They provide clarity during development, debugging, and future scaling of the system.
