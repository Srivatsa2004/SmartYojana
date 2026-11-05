import os
from flask import Flask, request, jsonify
try:
    from flask_cors import CORS
except ImportError:
    CORS = None
import joblib
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)
if CORS:
    CORS(app)

# Load NLP components
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
vectorizer = None
tfidf_matrix = None
scheme_names = None

try:
    vectorizer = joblib.load(os.path.join(BASE_DIR, "tfidf_vectorizer.pkl"))
    tfidf_matrix = joblib.load(os.path.join(BASE_DIR, "tfidf_matrix.pkl"))
    scheme_names = joblib.load(os.path.join(BASE_DIR, "scheme_names.pkl"))
    print(f"NLP Model loaded successfully with {len(scheme_names)} schemes.")
except Exception as e:
    print("Could not load NLP model. Did you run train_model.py?", e)

@app.route("/", methods=["GET"])
def index():
    return jsonify({
        "service": "SmartYojana ML Service",
        "status": "UP",
        "schemes_loaded": len(scheme_names) if scheme_names is not None else 0
    }), 200

@app.route("/health", methods=["GET"])
def health():
    return jsonify({
        "status": "UP",
        "model_loaded": vectorizer is not None and tfidf_matrix is not None
    }), 200

@app.route("/predict", methods=["POST"])
def predict():
    try:
        data = request.json or {}
        
        # Extract user profile
        age = str(data.get("age", ""))
        income = str(data.get("income", ""))
        occupation = data.get("occupation", "")
        category = data.get("category", "")
        state = data.get("state", "")

        # Form a semantic query from the user's profile
        # We repeat occupation and category to give them higher weight in TF-IDF
        query_text = f"age {age} income {income} {occupation} {occupation} {category} {category} {state}".lower().strip()

        if vectorizer is None or tfidf_matrix is None or not scheme_names:
            return jsonify({"scheme": None, "error": "Model not loaded"}), 500

        # Vectorize query
        query_vec = vectorizer.transform([query_text])

        # Compute cosine similarity between the query and all schemes
        similarities = cosine_similarity(query_vec, tfidf_matrix).flatten()

        # Find the most similar scheme
        best_idx = similarities.argmax()
        best_score = float(similarities[best_idx])

        # Define a relevance threshold.
        THRESHOLD = 0.05

        if best_score < THRESHOLD:
            print(f"Query: '{query_text}'. Best match: '{scheme_names[best_idx]}' with score {best_score}. REJECTED (Below threshold)")
            return jsonify({"scheme": None, "score": best_score})

        predicted_scheme = scheme_names[best_idx]
        print(f"Query: '{query_text}'. Best match: '{predicted_scheme}' with score {best_score}. ACCEPTED")

        return jsonify({
            "scheme": predicted_scheme,
            "score": best_score
        })

    except Exception as e:
        print("Prediction Error:", e)
        return jsonify({"error": str(e)}), 400

if __name__ == "__main__":
    port = int(os.environ.get("PORT", 5001))
    app.run(host="0.0.0.0", port=port, debug=False)
