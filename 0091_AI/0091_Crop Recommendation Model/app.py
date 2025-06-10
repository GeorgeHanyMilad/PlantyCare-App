from flask import Flask, request, jsonify
import joblib
import numpy as np

app = Flask(__name__)

# Load the model
MODEL_PATH = "D:\hager\grr\Randomm_forest.joblib"
model = joblib.load(MODEL_PATH)

@app.route('/')
def home():
    return "🚀 Model API is Running! Use /predict endpoint."

@app.route('/predict', methods=['POST', 'OPTIONS'])  # Allow OPTIONS for CORS issues
def predict():
    if request.method == 'OPTIONS':
        return jsonify({"message": "✅ OPTIONS request allowed."})

    try:
        print("🔍 Request Method:", request.method)
        print("🔍 Received Headers:", request.headers)
        print("🔍 Received Data:", request.data)

        data = request.get_json()
        if not data or "features" not in data:
            return jsonify({"error": "Missing 'features' key in request"}), 400
        
        features = np.array(data["features"]).reshape(1, -1)
        prediction = model.predict(features)
        return jsonify({"prediction": prediction.tolist()})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# Run Flask app directly (without ngrok)
if __name__ == "__main__":
    app.run(port=5000)
