from flask import Flask, request, jsonify
import joblib

app = Flask(__name__)

# Load model and encoders
model = joblib.load("model.pkl")
encoders = joblib.load("encoders.pkl")

@app.route("/predict", methods=["POST"])
def predict():
    try:
        data = request.json

        # Extract input
        age = data["age"]
        income = data["income"]
        occupation = data["occupation"]
        category = data["category"]
        state = data["state"]

        # Encode categorical values
        occupation_encoded = encoders["occupation"].transform([occupation])[0]
        category_encoded = encoders["category"].transform([category])[0]
        state_encoded = encoders["state"].transform([state])[0]

        # Create input array
        features = [[age, income, occupation_encoded, category_encoded, state_encoded]]

        # Predict
        prediction = model.predict(features)[0]

        # Decode scheme
        scheme = encoders["scheme"].inverse_transform([prediction])[0]

        return jsonify({
            "scheme": scheme
        })

    except Exception as e:
        return jsonify({
            "error": str(e)
        })

# Run server
if __name__ == "__main__":
    app.run(debug=True, port=5000)