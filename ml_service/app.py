import os
from flask import Flask, request, jsonify
import joblib
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)

# Load NLP components
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
try:
    vectorizer = joblib.load(os.path.join(BASE_DIR, "tfidf_vectorizer.pkl"))
    tfidf_matrix = joblib.load(os.path.join(BASE_DIR, "tfidf_matrix.pkl"))
    scheme_names = joblib.load(os.path.join(BASE_DIR, "scheme_names.pkl"))
    print("NLP Model loaded successfully.")
except Exception as e:
    print("Could not load NLP model. Did you run train_model.py?", e)

@app.route("/predict", methods=["POST"])
def predict():
    try:
        data = request.json
        
        # Extract user profile
        age = str(data.get("age", ""))
        income = str(data.get("income", ""))
        occupation = data.get("occupation", "")
        category = data.get("category", "")
        state = data.get("state", "")

        # Form a semantic query from the user's profile
        # We repeat occupation and category to give them higher weight in TF-IDF
        query_text = f"age {age} income {income} {occupation} {occupation} {category} {category} {state}".lower()

        # Vectorize query
        query_vec = vectorizer.transform([query_text])

        # Compute cosine similarity between the query and all schemes
        similarities = cosine_similarity(query_vec, tfidf_matrix).flatten()

        # Find the most similar scheme
        best_idx = similarities.argmax()
        best_score = similarities[best_idx]

        # Define a relevance threshold. If the best score is too low, it's just random noise.
        # TF-IDF cosine similarities for short queries usually sit around 0.05 to 0.3.
        # We'll use a conservative threshold of 0.05.
        THRESHOLD = 0.05

        if best_score < THRESHOLD:
            print(f"Query: '{query_text}'. Best match: '{scheme_names[best_idx]}' with score {best_score}. REJECTED (Below threshold)")
            return jsonify({"scheme": None})

        predicted_scheme = scheme_names[best_idx]
        print(f"Query: '{query_text}'. Best match: '{predicted_scheme}' with score {best_score}. ACCEPTED")

        return jsonify({
            "scheme": predicted_scheme
        })

    except Exception as e:
        print("Prediction Error:", e)
        return jsonify({"error": str(e)}), 400

if __name__ == "__main__":
    app.run(debug=True, port=5001)