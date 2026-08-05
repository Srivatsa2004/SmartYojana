import requests
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import joblib

def main():
    print("Fetching schemes from backend for NLP training...")
    try:
        response = requests.get('http://localhost:8080/schemes')
        schemes_data = response.json()
    except Exception as e:
        print("Error fetching schemes:", e)
        return

    if not schemes_data:
        print("No schemes found in DB.")
        return

    print(f"Found {len(schemes_data)} schemes. Building corpus...")

    corpus = []
    scheme_names = []

    for s in schemes_data:
        # Combine text fields to form a rich semantic document
        name = s.get('schemeName', '')
        desc = s.get('description', '')
        benefit = s.get('benefit', '')
        eligibility = s.get('eligibility', '')
        category = s.get('category', '')
        
        # Give extra weight to category and eligibility by repeating them
        document = f"{name} {desc} {benefit} {eligibility} {eligibility} {category} {category}"
        corpus.append(document.lower())
        scheme_names.append(name)

    print("Training TF-IDF Vectorizer...")
    vectorizer = TfidfVectorizer(stop_words='english', max_features=5000)
    tfidf_matrix = vectorizer.fit_transform(corpus)

    # Save the NLP model components
    import os
    BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    joblib.dump(vectorizer, os.path.join(BASE_DIR, 'tfidf_vectorizer.pkl'))
    joblib.dump(tfidf_matrix, os.path.join(BASE_DIR, 'tfidf_matrix.pkl'))
    joblib.dump(scheme_names, os.path.join(BASE_DIR, 'scheme_names.pkl'))

    print("NLP Model trained and saved successfully!")

if __name__ == "__main__":
    main()