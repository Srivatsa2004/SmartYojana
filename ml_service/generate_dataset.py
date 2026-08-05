import pandas as pd
import numpy as np
import random
import requests

# Define domains for synthetic features
occupations = ['Farmer', 'Student', 'Self-employed', 'Unemployed', 'Employee', 'Business', 'Laborer', 'Fisherman']
categories = ['General', 'OBC', 'SC', 'ST']
states = ['Karnataka', 'Maharashtra', 'Delhi', 'Uttar Pradesh', 'Tamil Nadu', 'Kerala', 'Gujarat', 'West Bengal', 'Chhattisgarh', 'Andhra Pradesh']

try:
    response = requests.get('http://localhost:8080/schemes')
    schemes_data = response.json()
    schemes = list(set([s['schemeName'] for s in schemes_data]))
except Exception as e:
    print("Error fetching schemes from API:", e)
    exit(1)

print(f"Found {len(schemes)} unique schemes from backend.")

data = []

# Generate synthetic data based on real backend schemes
for scheme in schemes:
    # 10 samples per scheme for a robust dataset
    for _ in range(10):
        age = random.randint(18, 65)
        income = random.randint(10000, 500000)
        occupation = random.choice(occupations)
        category = random.choice(categories)
        state = random.choice(states)
        
        data.append({
            'age': age,
            'income': income,
            'occupation': occupation,
            'category': category,
            'state': state,
            'scheme': scheme
        })

df_synth = pd.DataFrame(data)

# Save to excel
output_file = 'schemes_dataset.xlsx'
df_synth.to_excel(output_file, index=False)
print(f"Generated {len(df_synth)} samples and saved to {output_file}.")
