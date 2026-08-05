import React, { useState } from "react";
import "./App.css";

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || "http://localhost:8080";

function App() {
  const [form, setForm] = useState({
    age: "",
    income: "",
    occupation: "Farmer",
    category: "OBC",
    state: "Karnataka"
  });

  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const getRecommendation = async () => {
    setLoading(true);
    setResult(null);
    try {
      const response = await fetch(`${API_BASE_URL}/schemes/recommend`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          age: Number(form.age),
          income: Number(form.income),
          occupation: form.occupation,
          category: form.category,
          state: form.state,
        }),
      });

      const data = await response.json();
      setResult(data);
    } catch (error) {
      console.error("Error:", error);
      setResult([]); // Treat as empty result on error
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app-container">
      <div className="glass-panel">
        <h1 className="title">Smart Scheme Recommendation</h1>
        <p className="subtitle">Discover government schemes tailored to your profile.</p>

        <div className="form-grid">
          <div className="input-group">
            <label>Age</label>
            <input type="number" name="age" placeholder="e.g. 25" onChange={handleChange} />
          </div>

          <div className="input-group">
            <label>Annual Income (₹)</label>
            <input type="number" name="income" placeholder="e.g. 200000" onChange={handleChange} />
          </div>

          <div className="input-group">
            <label>Occupation</label>
            <select name="occupation" onChange={handleChange}>
              <option>Farmer</option>
              <option>Student</option>
              <option>Business</option>
              <option>Self-employed</option>
              <option>Laborer</option>
              <option>Fisherman</option>
              <option>Unemployed</option>
              <option>Employee</option>
            </select>
          </div>

          <div className="input-group">
            <label>Category</label>
            <select name="category" onChange={handleChange}>
              <option>OBC</option>
              <option>SC</option>
              <option>ST</option>
              <option>General</option>
            </select>
          </div>

          <div className="input-group full-width">
            <label>State</label>
            <input type="text" name="state" placeholder="e.g. Karnataka" onChange={handleChange} />
          </div>
        </div>

        <button className="primary-btn" onClick={getRecommendation} disabled={loading}>
          {loading ? <span className="spinner"></span> : "Get Recommendation"}
        </button>

        {result && result.length > 0 && (
          <div className="result-card fade-in">
            <div className="result-header">
              <h3>Recommended Scheme</h3>
            </div>
            <div className="result-body">
              <h4 className="scheme-name">{result[0]?.schemeName}</h4>
              <p className="scheme-desc">{result[0]?.description}</p>
              
              <div className="benefit-section">
                <strong>Key Benefits:</strong>
                <p>{result[0]?.benefit || "Refer to official scheme documentation for specific benefits."}</p>
              </div>

              <div className="eligibility-section">
                <strong>Eligibility Criteria:</strong>
                <p>{result[0]?.eligibility}</p>
              </div>
            </div>
          </div>
        )}

        {result && result.length === 0 && (
          <div className="error-card fade-in">
            <div className="icon">⚠️</div>
            <h4>No matching schemes found</h4>
            <p>We couldn't find a scheme matching your exact profile. Please try adjusting your income or occupation.</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;