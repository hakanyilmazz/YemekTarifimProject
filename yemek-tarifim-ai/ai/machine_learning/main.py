from sklearn.model_selection import RandomizedSearchCV
from sklearn.metrics import f1_score
import lightgbm as lgb
from sklearn import model_selection
import pickle
from sklearn.feature_extraction.text import TfidfVectorizer
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import plotly_express as px
import wordcloud
import nltk
import warnings
warnings.filterwarnings('ignore')

df = pd.read_csv("./spam.csv", encoding='latin-1')
print(df.head())

df.drop(columns=['Unnamed: 2', 'Unnamed: 3', 'Unnamed: 4'], inplace=True)
df.rename(columns={'v1': 'class_label', 'v2': 'message'}, inplace=True)
df.head()

fig = px.histogram(df, x="class_label", color="class_label",
                   color_discrete_sequence=["#871fff", "#ffa78c"])
fig.show()

fig = px.pie(df.class_label.value_counts(), labels='index', values='class_label',
             color="class_label", color_discrete_sequence=["#871fff", "#ffa78c"])
fig.show()

df['length'] = df['message'].apply(len)
print(df.head())

fig = px.histogram(df, x="length", color="class_label",
                   color_discrete_sequence=["#871fff", "#ffa78c"])
fig.show()

data_ham = df[df['class_label'] == "ham"].copy()
data_spam = df[df['class_label'] == "spam"].copy()


def show_wordcloud(df, title):
    text = ' '.join(df['message'].astype(str).tolist())
    stopwords = set(wordcloud.STOPWORDS)
    fig_wordcloud = wordcloud.WordCloud(stopwords=stopwords, background_color="#ffa78c",
                                        width=3000, height=2000).generate(text)
    plt.figure(figsize=(15, 15), frameon=True)
    plt.imshow(fig_wordcloud)
    plt.axis('off')
    plt.title(title, fontsize=20)
    plt.show()


show_wordcloud(data_spam, "Spam messages")
show_wordcloud(data_ham, "ham messages")

df['class_label'] = df['class_label'].map({'spam': 1, 'ham': 0})

# Replace email address with 'emailaddress'
df['message'] = df['message'].str.replace(
    r'^.+@[^\.].*\.[a-z]{2,}$', 'emailaddress')

# Replace urls with 'webaddress'
df['message'] = df['message'].str.replace(
    r'^http\://[a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(/\S*)?$', 'webaddress')

# Replace money symbol with 'money-symbol'
df['message'] = df['message'].str.replace(r'£|\$', 'money-symbol')

# Replace 10 digit phone number with 'phone-number'
df['message'] = df['message'].str.replace(
    r'^\(?[\d]{3}\)?[\s-]?[\d]{3}[\s-]?[\d]{4}$', 'phone-number')

# Replace normal number with 'number'
df['message'] = df['message'].str.replace(r'\d+(\.\d+)?', 'number')

# remove punctuation
df['message'] = df['message'].str.replace(r'[^\w\d\s]', ' ')

# remove whitespace between terms with single space
df['message'] = df['message'].str.replace(r'\s+', ' ')

# remove leading and trailing whitespace
df['message'] = df['message'].str.replace(r'^\s+|\s*?$', ' ')

# change words to lower case
df['message'] = df['message'].str.lower()

# nltk.download()

stop_words = set(stopwords.words('english'))
df['message'] = df['message'].apply(lambda x: ' '.join(
    term for term in x.split() if term not in stop_words))

ss = nltk.SnowballStemmer("english")
df['message'] = df['message'].apply(
    lambda x: ' '.join(ss.stem(term) for term in x.split()))

sms_df = df['message']

# creating a bag-of-words model
all_words = []
for sms in sms_df:
    words = word_tokenize(sms)
    for w in words:
        all_words.append(w)

all_words = nltk.FreqDist(all_words)

print('Number of words: {}'.format(len(all_words)))

all_words.plot(10, title='Top 10 Most Common Words in Corpus')

tfidf_model = TfidfVectorizer()
tfidf_vec = tfidf_model.fit_transform(sms_df)
# serializing our model to a file called model.pkl
pickle.dump(tfidf_model, open("./tfidf_model.pkl", "wb"))
tfidf_data = pd.DataFrame(tfidf_vec.toarray())
print(tfidf_data.head())

# Separating Columns
df_train = tfidf_data.iloc[:4457]
df_test = tfidf_data.iloc[4457:]

target = df['class_label']
df_train['class_label'] = target

Y = df_train['class_label']
X = df_train.drop('class_label', axis=1)

# splitting training data into train and validation using sklearn
X_train, X_test, y_train, y_test = model_selection.train_test_split(
    X, Y, test_size=.2, random_state=42)


def train_and_test(model, model_name):
    model.fit(X_train, y_train)
    pred = model.predict(X_test)
    print(f'F1 score is: {f1_score(pred, y_test)}')


for depth in [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]:
    lgbmodel = lgb.LGBMClassifier(
        max_depth=depth, n_estimators=200, num_leaves=40)
    print(f"Max Depth {depth}")
    print(" ")
    print(" ")
    train_and_test(lgbmodel, "Light GBM")

lgbmodel_bst = lgb.LGBMClassifier(max_depth=6, n_estimators=200, num_leaves=40)
param_grid = {
    'num_leaves': list(range(8, 92, 4)),
    'min_data_in_leaf': [10, 20, 40, 60, 100],
    'max_depth': [3, 4, 5, 6, 8, 12, 16, -1],
    'learning_rate': [0.1, 0.05, 0.01, 0.005],
    'bagging_freq': [3, 4, 5, 6, 7],
    'bagging_fraction': np.linspace(0.6, 0.95, 10),
    'reg_alpha': np.linspace(0.1, 0.95, 10),
    'reg_lambda': np.linspace(0.1, 0.95, 10),
    "min_split_gain": [0.0, 0.1, 0.01],
    "min_child_weight": [0.001, 0.01, 0.1, 0.001],
    "min_child_samples": [20, 30, 25],
    "subsample": [1.0, 0.5, 0.8],
}
model = RandomizedSearchCV(lgbmodel_bst, param_grid, random_state=1)
search = model.fit(X_train, y_train)
search.best_params_

best_model = lgb.LGBMClassifier(subsample=0.5,
                                reg_lambda=0.47777777777777775,
                                reg_alpha=0.5722222222222222,
                                num_leaves=88,
                                min_split_gain=0.01,
                                min_data_in_leaf=10,
                                min_child_weight=0.01,
                                min_child_samples=30,
                                max_depth=3,
                                learning_rate=0.1,
                                bagging_freq=3,
                                bagging_fraction=0.6,
                                random_state=1)
best_model.fit(X_train, y_train)

prediction = best_model.predict(X_test)
print(f'F1 score is: {f1_score(prediction, y_test)}')

best_model.fit(tfidf_data, target)
pickle.dump(best_model, open("./spam_model.pkl", "wb"))
