# SmartYojana – Smart Scheme Recommendation System

## About the Project

SmartYojana is a machine learning-based web application that recommends suitable government schemes based on user details such as age, income, occupation, category, and state. It uses a Random Forest model to predict a suitable scheme and MongoDB to store scheme information.

## Features

- User management with CRUD operations.
- Machine learning-based scheme prediction.
- Personalized recommendations based on user attributes.
- REST API integration between Spring Boot and Python Flask.
- Scheme information stored in MongoDB Atlas.
- React-based user interface.

## Tech Stack

| Component | Technology |
|---|---|
| Frontend | React.js |
| Backend | Java, Spring Boot |
| Machine Learning | Python, Scikit-learn, Random Forest |
| ML API | Flask |
| Database | MongoDB Atlas |
| Deployment | Vercel (Frontend) |

## System Architecture

```text
User
  ↓
React Frontend (Vercel)
  ↓
Spring Boot Backend
  ↓
Python Flask ML API
  ↓
Random Forest Model
  ↓
Predicted Scheme
  ↓
MongoDB Atlas
  ↓
Recommended Scheme Details
```

## How It Works

1. The user enters details such as age, income, occupation, category, and state.
2. The React frontend sends the input to the Spring Boot backend.
3. Spring Boot sends the user attributes to the Flask API.
4. The trained Random Forest model predicts a scheme.
5. Spring Boot retrieves the corresponding scheme details from MongoDB.
6. The recommended scheme is returned to the frontend.

## Machine Learning Model

- **Algorithm:** Random Forest Classifier
- **Input features:** Age, income, occupation, category, and state
- **Target:** Scheme name
- **Training data:** A small synthetic dataset
- **Reported accuracy:** Approximately 40%

The current accuracy is an initial result from a limited synthetic dataset. Model performance may improve with a larger, more representative dataset and validation using suitable evaluation methods. Predictions should not be treated as official eligibility decisions.

## Running the Project Locally

### 1. Clone the repository

```bash
git clone https://github.com/Srivatsa2004/SmartYojana.git
cd SmartYojana
```

### 2. Start the Flask ML service

Activate your Python virtual environment, install the dependencies, and start the API.

```bash
pip install flask flask-cors scikit-learn pandas joblib
python app.py
```

Ensure the trained model and encoder files required by `app.py` are available.

### 3. Start the Spring Boot backend

Using Gradle:

```bash
./gradlew bootRun
```

Configure your MongoDB Atlas connection and ensure the Flask API URL is correct.

### 4. Start the React frontend

Open a separate terminal in the React project directory:

```bash
npm install
npm start
```

The frontend typically runs at `http://localhost:3000`, the backend at `http://localhost:8080`, and the Flask API at `http://localhost:5000`.

Keep all three services running while testing the complete application.

## Future Improvements

- Train and evaluate the model using a larger, reliable dataset.
- Recommend multiple relevant schemes instead of a single prediction.
- Add scheme eligibility validation using official criteria.
- Improve error handling and input validation.
- Deploy the Spring Boot backend and Flask ML service.
- Improve the user interface and recommendation explanations.

## Author

**Srivatsa S.**

GitHub: [Srivatsa2004](https://github.com/Srivatsa2004)

---

*SmartYojana aims to make government scheme discovery easier by combining machine learning with a full-stack web application.*
