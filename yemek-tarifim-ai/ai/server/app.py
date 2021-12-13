from flask import Flask
from flask import request
import pickle
from flask import json
from flask.json import jsonify
import traceback
import pandas as pd
import nltk
from nltk.corpus import stopwords
from sklearn.feature_extraction.text import TfidfVectorizer

app = Flask(__name__, template_folder="templates")


@app.route('/predict', methods=['POST'])
def predict():
    model = pickle.load(open("./data/spam_model.pkl", "rb"))
    tfidf_model = pickle.load(open("./data/tfidf_model.pkl", "rb"))
    if request.method == "POST":
        jsonResult = request.json
        message = jsonResult["message"]

        dataset = {'message': [message]}
        data = pd.DataFrame(dataset)

        data["message"] = data["message"].str.replace(
            r'^.+@[^\.].*\.[a-z]{2,}$', 'emailaddress', regex=True)
        data["message"] = data["message"].str.replace(
            r'^http\://[a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(/\S*)?$', 'webaddress', regex=True)
        data["message"] = data["message"].str.replace(
            r'£|\$', 'money-symbol', regex=True)
        data["message"] = data["message"].str.replace(
            r'^\(?[\d]{3}\)?[\s-]?[\d]{3}[\s-]?[\d]{4}$', 'phone-number', regex=True)
        data["message"] = data["message"].str.replace(
            r'\d+(\.\d+)?', 'number', regex=True)
        data["message"] = data["message"].str.replace(
            r'[^\w\d\s]', ' ', regex=True)
        data["message"] = data["message"].str.replace(r'\s+', ' ', regex=True)
        data["message"] = data["message"].str.replace(
            r'^\s+|\s*?$', ' ', regex=True)
        data["message"] = data["message"].str.lower()

        stop_words = set(stopwords.words('english'))

        data["message"] = data["message"].apply(lambda x: ' '.join(
            term for term in x.split() if term not in stop_words))

        ss = nltk.SnowballStemmer("english")
        data["message"] = data["message"].apply(lambda x: ' '.join(ss.stem(term)
                                                                   for term in x.split()))

        # tfidf_model = TfidfVectorizer()
        tfidf_vec = tfidf_model.transform(data["message"])
        tfidf_data = pd.DataFrame(tfidf_vec.toarray())
        my_prediction = model.predict(tfidf_data)

        spamResult = int(pd.Series(my_prediction).iloc[0])
        isSpam = False if spamResult == 0 else True

        return jsonify({'isSpam': isSpam})


@app.route('/')
def home():
    return "Hello World"


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
