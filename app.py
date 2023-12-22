from flask import Flask, request, jsonify
from transformers import BertTokenizer
from sklearn.preprocessing import LabelEncoder
from tensorflow.keras.models import load_model
import numpy as np
import pandas as pd

app = Flask(__name__)

# Load the dataset
df = pd.read_csv('Data-Pekerjaan.csv')

# Extract 'Deskripsi Diri' and 'Nama Pekerjaan' from the dataframe
deskripsi_diri = df['Deskripsi Diri'].values.astype('str')
bidang_pekerjaan = df['Nama Pekerjaan'].values.astype('str')

# Initialize the tokenizer and the label encoder
tokenizer = BertTokenizer.from_pretrained('cahya/bert-base-indonesian-522M')
label_encoder = LabelEncoder()

# Fit the label encoder
label_encoder.fit(bidang_pekerjaan)

# Load the model
# Define TFBertModel class as custom_objects when loading the model
from transformers import TFBertModel

custom_objects = {'TFBertModel': TFBertModel}
loaded_model = load_model('model.h5', custom_objects=custom_objects)  # Assuming 'model.h5' is in the correct path

@app.route('/', methods=['GET'])
def home():
    return "<h1>Selamat datang di aplikasi prediksi pekerjaan!</h1>"

@app.route('/predict', methods=['POST'])
def predict():
    # Get the JSON data from the request
    data = request.get_json()

    # Extract 'text' from JSON data
    input_text = data['text']

    # Tokenize the input
    tokenized_input = tokenizer(input_text, padding=True, truncation=True, return_tensors="tf")

    # Run the model
    prediction = loaded_model.predict(tokenized_input.input_ids.numpy())

    # Postprocess the prediction
    predicted_label = np.argmax(prediction, axis=-1)
    predicted_job = label_encoder.inverse_transform(predicted_label)

    # Return the prediction as JSON
    return jsonify({'prediction': predicted_job[0]})

if __name__ == "__main__":
  #    app = create_app()
  print(" Starting app...")
  app.run(host="0.0.0.0", port=8080)
