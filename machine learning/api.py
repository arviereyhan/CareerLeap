from function import *
import os

app = Flask(__name__)
@app.route("/predict", methods=['POST']) # Ini untuk kalian biar bisa nampilin nama pekerjaan
def predict():

    data = request.form
    url = data.get('cv_url')
    submitted_data.append(url)

    api_url = eval(repr(submitted_data[0]))

    # api_url = "https://storage.googleapis.com/career-leap/1686228635872-08-Jun-2023168584839321733191.pdf"
    response = requests.get(api_url)
    with open("./file.pdf", 'wb') as file:
        file.write(response.content)

    file_path = "./file.pdf"
    text = extract_text(file_path)

    text = text.replace("\n"," ")
    text = text.replace("[^a-zA-Z0-9]", " ");
    re.sub('\W+','', text)
    text = text.lower()

    #========================================================================================
    # Praproses Data -> mulai extract cv sampai data preparation (tokenizer, sequence, padded)
    #==========================================================================================
    # #==========================================================================================
    # # Get the keywords
    Keywords = ["education","summary","personal profile","work background","qualifications","experience",
                "achievements","projects","skills","soft skills","hard skills"]
    
    content = {}
    indices = []
    keys = []

    for key in Keywords:
        try:
            content[key] = text[text.index(key) + len(key):]
            indices.append(text.index(key))
            keys.append(key)
        except:
            pass
    # Sorting the index
    zipped_lists = zip(indices, keys)
    sorted_pairs = sorted(zipped_lists)
    
    tuples = zip(*sorted_pairs)
    indices, keys = [ list(tuple) for tuple in  tuples]
    #==========================================================================================
    # Remove redundant parts
    parsed_content = {}
    content = []
    for idx in range(len(indices)):
        if idx != len(indices)-1:
            content.append(text[indices[idx]: indices[idx+1]])
        else:
            content.append(text[indices[idx]: ])

    for i in range(len(indices)):
        parsed_content[keys[i]] = content[i]
    #==========================================================================================
    df = pd.DataFrame({'keys': keys, 'parsed_content': [parsed_content[key] for key in keys]})

    # Extract 'skills' dan 'hard skill' rows

    filtered_df = df[df['keys'].isin(['skills', 'hard skill', 'experience'])]
    filtered_df.to_csv('parsed_content.csv', index=False)

    data = filtered_df
    # #==========================================================================================
    # # Clean Data
    filtered_df['parsed_content'] = filtered_df['parsed_content'].apply(clean_data)
    data = filtered_df

    df = pd.DataFrame(data)

    # Convert df to text using to_string()
    text_output = df.to_string(index=False)

    filtered_df['parsed_content_split'] = df['parsed_content'].apply(lambda x: tokenize1(x))


    filtered_df['parsed_content_split_stopwords'] = filtered_df['parsed_content'].apply(lambda x: tokenize2(x))
    
    lemmatizer = WordNetLemmatizer()
    filtered_df['parsed_content_split_stopwords_lemma'] = filtered_df['parsed_content_split_stopwords'].apply(lambda x: [lemmatizer.lemmatize(word) for word in x])

    filtered_df['join_words'] = filtered_df['parsed_content_split_stopwords_lemma'].apply(concat)
    new_filtered_df = filtered_df[['keys', 'join_words']]

    data = new_filtered_df
    df = pd.DataFrame(data)
    # convert df to text using to_string()
    text_output = data
    text_output = df.to_string(index=False)

    data = new_filtered_df
    text_output = data['join_words'].to_string(index=False)

    #==========================================================================================
    # Data Preparation

    text_output = tokenize1(text_output)
    text_output = remove_stopwords(text_output)
    text_output = lemmatize_word(text_output)
    text_output = concat(text_output)

    with open('tokenizer.pickle', 'rb') as handle:
        my_tokenizer = pickle.load(handle)

    sequence_output = my_tokenizer.texts_to_sequences([text_output])
    padded_output = pad_sequences(sequence_output)

    # Prediksi
    prediction = model.predict(padded_output)
    
    kategori = ['Business Analyst', 'Data Scientist', 'DevOps Engineer', 'Java Developer', 'Operations Manager', 'Web Designer']
    
    prediction_kategori = [kategori[np.argmax(row)] for row in prediction]
    json_hasil = {"hasil_prediksi": prediction_kategori[0],
                  "text_output": text_output}
    
    #hapus data
    submitted_data.pop()
    path = "./file.pdf"
    if os.path.exists(path):
        os.remove(path)
        print("File berhasil dihapus")

    return json.dumps(json_hasil)

if __name__ == "__main__":
    nltk.download('stopwords')
    nltk.download('punkt')
    submitted_data = []
    model_path = "./job_classifier.h5"
    model = tf.keras.models.load_model(model_path)
    app.run(host="0.0.0.0", port="5003")

    
    




