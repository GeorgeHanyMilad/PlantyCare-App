<h1 align="center">
    <img src="https://readme-typing-svg.herokuapp.com/?font=Righteous&size=35&color=00BFFF&center=true&vCenter=true&width=700&height=100&duration=7000&lines=Graduation+Project+-+Fayoum+Center+✌;We+hope+you+find+our+project+valuable+❤;" />
</h1>

![image](https://www.sciencenews.org/wp-content/uploads/2023/04/040823_chatgpt_feat.gif)
<br>

# 🌿 PlantyCare App

---

## 📄 Project Information

- **Project Title**:  
  **AI-Based Solution for Addressing Plant Diseases in Egypt's Fruit and Vegetable Crops**

- **Team Number**: { 0091 }

- **Faculty**:  
  Faculty of Computers and Information – Fayoum Center - EELU

- **Supervised by**:  
  - 👨‍🏫 Dr. Mahmoud Bassiouni *(Supervisor)*  
  - 👩‍💻 Eng. Toka Ashraf *(Assistant Supervisor)*

---

## 👥 Team Members

| Name               | Role         | University ID |
|--------------------|--------------|---------------|
| Kholoud Mohsen     | Team Leader  | 21-00882      |
| Farah Mohamed      | Member       | 21-00686      |
| Bishoy Ezzat       | Member       | 21-00584      |
| George Hany        | Member       | 21-00724      |
| Hager Abdelqader   | Member       | 21-02131      |
| Mariam Hany        | Member       | 21-01011      |
| Eslam Ayman        | Member       | 21-01854      |

---

🎓 **Graduation Project**  
📍 Project Title: **AI-Based Solution for Addressing Plant Diseases in Egypt's Fruit and Vegetable Crops**

---

## 🧠 Overview

**PlantyCare** is a smart mobile application developed in **Java**, aiming to assist Egyptian farmers by diagnosing plant diseases, recommending suitable crops to grow, and suggesting the best fertilizers based on soil and weather data — all powered by AI models.

---

## 📱 App Features

### 🔐 Authentication

- Login and Sign-Up pages.
- Connected to **Firebase** for user data storage and authentication.
- Keeps a record of the user’s activity and predictions.

---

### 🏠 Home Page

The Home Page includes 3 main vertically scrollable options:

#### 1. 🌿 AgriCure

- Allows users to **upload** an image from their device or **capture** a new photo of the plant.
- After submitting the image, the result page displays:
  - Predicted **disease name**
  - The **submitted image**
  - A **brief description** of the disease
  - **Prevention steps** to follow
  - **Treatment image** and description
- All results are saved to **Firebase** and displayed in a **History section** at the top of the Home Page. Clicking any image in history redirects the user to its detailed result page.

#### 2. 🌾 PlantPick

- Predicts the most suitable **crop** based on input values:
  - N, P, K
  - Temperature
  - Humidity
  - pH
  - Rainfall
- Displays the crop name and an image in a popup result window.

#### 3. 💧 FertiGuide

- Suggests the **best fertilizer** based on the following user inputs:
  - Crop type
  - Soil color
  - Nitrogen, Phosphorus, Potassium values
  - pH, Rainfall, Temperature
- The recommended fertilizer and its image are shown in a popup.

---

## 🔄 Bottom Navigation Bar

The app includes a bottom bar with 4 navigation items:

| Section     | Description |
|-------------|-------------|
| 🏠 Home      | Redirects to the main home page |
| 📅 Calendar  | Shows the top 5 crops grown and harvested in each month in Egypt |
| 📘 Dictionary| Contains important agricultural terms used in the app, with simple definitions |
| 👤 Profile   | Displays user's name, email, join date, and usage statistics for each main feature (AgriCure, PlantPick, FertiGuide) |

---

## 🧩 Additional Pages

- ⚙️ Settings  
- ✏️ Edit Profile  
- ℹ️ About Us  
- ❓ FAQs  
- 🔒 Privacy Policy  
- 📞 Contact Us  
- ⭐ Rate Us  
- 📤 Share the App  

---

## 🧠 AI Models Overview

- **AgriCure**: CNN model for plant disease classification using image input.
- **PlantPick**: Crop recommendation model based on soil and environmental factors.
- **FertiGuide**: Fertilizer suggestion model for optimizing plant nutrition and growth.

---

## 🛠️ Tech Stack

- **Java** (Android development)
- **Firebase** (Authentication, Realtime Database, Storage)
- **AI Models** (Trained using deep learning frameworks)
- **Clean & User-Friendly UI/UX** (Modern mobile design practices)

---

## 📌 Summary

**PlantyCare** was developed as part of a graduation project at the Faculty of Computers and Information. The goal is to support Egypt’s agricultural sector by leveraging AI to reduce crop loss, improve decision-making, and increase productivity.

---

> 💬 For further assistance or questions, feel free to reach out to the development team through the app.
