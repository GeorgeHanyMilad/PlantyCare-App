# 🤖 AI Models – PlantyCare App

This directory contains all the AI models developed for the **PlantyCare** mobile application, which provides smart agricultural solutions powered by machine learning. The models work together to diagnose plant diseases, recommend suitable crops, and suggest the right fertilizers based on environmental and soil data.

---

## 🌿 AgriCure – Plant Disease Detection Model

- **Goal:** Detect plant diseases from leaf images captured or uploaded by the user.
- **Type:** Image Classification
- **Framework Used:** PyTorch
- **Model Architecture:** Convolutional Neural Network (CNN)
- **Dataset:** PlantVillage dataset and locally collected images (balanced and preprocessed)
- **Key Features:**
  - Takes an image as input and predicts the disease name.
  - Provides a brief description and treatment steps.
  - Result includes the uploaded image and a treatment illustration.
- **Integration:**
  - Output is sent to the mobile app with the disease name, description, prevention steps, and treatment image.
  - Predictions are stored in Firebase with timestamp for history tracking.

---

## 🌾 PlantPick – Crop Recommendation Model

- **Goal:** Suggest the most suitable crop based on soil nutrients and environmental conditions.
- **Type:** Supervised Classification
- **Framework Used:** Scikit-learn
- **Model Used:** Random Forest Classifier
- **Inputs Required:**
  - N, P, K values
  - Temperature
  - Humidity
  - pH
  - Rainfall
- **Output:** Name of the most suitable crop along with its image.
- **Integration:**
  - Deployed through a backend API.
  - Output displayed as a popup in the mobile app.
  - User inputs are taken from a form UI and passed to the model for real-time prediction.

---

## 💧 FertiGuide – Fertilizer Recommendation Model

- **Goal:** Recommend the best fertilizer for a specific crop and soil condition.
- **Type:** Supervised Classification
- **Framework Used:** Scikit-learn
- **Model Used:** Random Forest Classifier
- **Inputs Required:**
  - Crop type
  - Soil color
  - Nitrogen
  - Phosphorus
  - Potassium
  - pH
  - Rainfall
  - Temperature
- **Output:** Name of the recommended fertilizer and an associated image.
- **Integration:**
  - Deployed via API to serve predictions to the frontend.
  - Result is shown in a popup with fertilizer name and image.
  - Enhances user decision-making in real-time for soil nutrition.

---

## 🚀 Deployment & API Integration

- All models are served through a backend REST API using **Flask**.
- Models accept input from the mobile app and return predictions in real-time.
- Firebase is used to store and retrieve prediction histories.
- Dependencies and environment details are listed in `requirements.txt`.

---

> 🎓 These models are part of the graduation project **"AI-Based Solution for Addressing Plant Diseases in Egypt's Fruit and Vegetable Crops"** developed by a team of AI students at the Faculty of Artificial Intelligence, EELU.
