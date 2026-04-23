import React, { useState } from "react";

function App() {
  const [form, setForm] = useState({
    age: "",
    income: "",
    occupation: "Farmer",
    category: "OBC",
    state: "Karnataka"
  });

  const [result, setResult] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const getRecommendation = async () => {
  try {
    const response = await fetch("http://localhost:8080/schemes/recommend", {
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
  }
};

  return (
    <div style={{ padding: "20px" }}>
      <h2>Smart Scheme Recommendation</h2>

      <input name="age" placeholder="Age" onChange={handleChange} /><br />
      <input name="income" placeholder="Income" onChange={handleChange} /><br />

      <select name="occupation" onChange={handleChange}>
        <option>Farmer</option>
        <option>Student</option>
        <option>Business</option>
        <option>Self-employed</option>
      </select><br />

      <select name="category" onChange={handleChange}>
        <option>OBC</option>
        <option>SC</option>
        <option>ST</option>
        <option>General</option>
      </select><br />

      <input name="state" placeholder="State" onChange={handleChange} /><br />

      <button onClick={getRecommendation}>
        Get Recommendation
      </button>

      {result && (
        <div style={{ marginTop: "20px" }}>
          <h3>Recommended Scheme:</h3>
          <p>{result[0]?.name}</p>
          <p>{result[0]?.benefits}</p>
        </div>
      )}
    </div>
  );
}

export default App;