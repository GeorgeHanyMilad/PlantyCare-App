## 🔄 Data Flow Diagram (DFD)

The **Data Flow Diagram (DFD)** is a graphical representation used to visualize the flow of data within the PlantyCare system. It illustrates how input data moves through processes and is transformed into output results, highlighting the interactions between external entities, system processes, and data stores.

---

### 📌 Importance of the DFD

- **Clarifies System Behavior:** It simplifies complex system functionalities by showing how data is processed and moved between different components.
- **Enhances Understanding:** Makes it easier for both technical and non-technical stakeholders to understand how the system operates.
- **Helps in Identifying Requirements:** Supports early-stage analysis of what inputs, processes, and outputs are required for the system to function effectively.
- **Improves Communication:** Serves as a valuable tool for communicating system logic between developers, designers, and clients.

---

### 🔍 What It Represents

In the context of the PlantyCare App, the DFD demonstrates:

- **External Entities:**
  - Users interacting with the app (e.g., uploading images, entering soil data).
- **Processes:**
  - AI-based modules such as Disease Detection (AgriCure), Crop Recommendation (PlantPick), and Fertilizer Suggestion (FertiGuide).
- **Data Stores:**
  - Firebase Realtime Database for storing user history, predictions, and profile data.
  - Firebase Storage for storing uploaded images.
- **Data Flows:**
  - How data inputs (images, soil parameters) are processed by the AI models and how results are generated and stored.

---

### 🧠 How It Helps in the PlantyCare Project

- Maps the flow of information from the moment a user interacts with the app to the point a prediction is delivered and saved.
- Identifies all key components responsible for handling data (UI, AI Models, Firebase).
- Ensures that data management is efficient and secure across all modules.

---

### 🛠️ Use in Development

The DFD was an essential tool during the design and planning phases of the PlantyCare project, aiding in:

- Structuring the interaction between frontend and backend components.
- Ensuring smooth data transition and integration with Firebase services.
- Building modular and scalable features based on clearly defined data flows.
