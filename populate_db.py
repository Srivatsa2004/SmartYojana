import pandas as pd
import requests
import json
import math

csv_path = 'updated_data.csv'
df = pd.read_csv(csv_path)

schemes = []
for index, row in df.iterrows():
    # Only process if scheme_name exists
    if pd.isna(row.get('scheme_name')):
        continue
    
    schemeName = str(row['scheme_name']).strip()
    description = str(row.get('details', '')).strip()
    benefit = str(row.get('benefits', '')).strip()
    eligibility = str(row.get('eligibility', '')).strip()
    category = str(row.get('schemeCategory', 'All')).strip()
    
    # Handle NaN strings
    if description == 'nan': description = ''
    if benefit == 'nan': benefit = ''
    if eligibility == 'nan': eligibility = ''
    if category == 'nan': category = 'All'
    
    scheme = {
        "schemeName": schemeName[:250],  # Limit length just in case
        "description": description[:1000],
        "minAge": 18,
        "maxAge": 100,
        "maxIncome": 9999999.0,
        "occupation": "Any",
        "category": category[:100],
        "state": "All",
        "benefit": benefit[:1000],
        "eligibility": eligibility[:1000]
    }
    schemes.append(scheme)

# Batch insert
batch_size = 200
total = len(schemes)
print(f"Total valid schemes to insert: {total}")

url = "http://localhost:8080/schemes/bulk"
headers = {"Content-Type": "application/json"}

for i in range(0, total, batch_size):
    batch = schemes[i:i+batch_size]
    response = requests.post(url, json=batch, headers=headers)
    if response.status_code == 200:
        print(f"Successfully inserted batch {i//batch_size + 1} ({i} to {i+len(batch)})")
    else:
        print(f"Failed to insert batch {i//batch_size + 1}. Status code: {response.status_code}")
        print(response.text)

print("Database population complete!")
