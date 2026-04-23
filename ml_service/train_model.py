import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import LabelEncoder
import joblib

# Step 1: Load dataset
df = pd.read_excel("schemes_dataset_50.xlsx")
print("Columns:", df.columns.tolist())

#print("Columns:", df.columns)
print(df.head())

# Step 2: Encode categorical columns
label_encoders = {}

categorical_cols = ['age','occupation', 'category', 'state', 'scheme']

for col in categorical_cols:
    le = LabelEncoder()
    df[col] = le.fit_transform(df[col])
    label_encoders[col] = le

# Step 3: Features (X) and Target (y)
X = df[['age', 'income', 'occupation', 'category', 'state']]
y = df['scheme']

# Step 4: Split data
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

# Step 5: Train Random Forest
model = RandomForestClassifier(n_estimators=100, random_state=42)
model.fit(X_train, y_train)

# Step 6: Accuracy
accuracy = model.score(X_test, y_test)
print("Model Accuracy:", accuracy)

# Step 7: Save model + encoders
joblib.dump(model, "model.pkl")
joblib.dump(label_encoders, "encoders.pkl")

print("Model and encoders saved successfully!")