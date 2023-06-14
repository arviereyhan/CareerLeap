import requests
import pickle
import re
import spacy
import pandas as pd
from spacy.matcher import Matcher
from tika import parser
import nltk
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import WordNetLemmatizer
from flask import Flask, request, jsonify
import tensorflow as tf
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
import numpy as np
import json
from pdfminer.high_level import extract_text


def clean_data(data):
        data = data.lower()
        # URLs
        data = re.sub('http\S+\s*', ' ', data)
        # RT and cc
        data = re.sub('RT|cc', ' ', data)
        # hashtags
        data = re.sub('#\S+', '', data)
        # mentions
        data = re.sub('@\S+', ' ', data)
        # punctuations and non-ASCII char
        data = re.sub('[^a-zA-Z]', ' ', data)
        # extra whitespace
        data = re.sub('\s+', ' ', data)
        return data

def tokenize1(text):
    words = re.split("\W+", text)
    return words

def remove_stopwords(text):
  stopword = nltk.corpus.stopwords.words('english')
  list_stopword = ['skills', 'education', 'windows', 'set', 'may', 'january', 'february',
               'march', 'april', 'june', 'july', 'august', 'september', 'october', 'november',
               'december', 'ymcaust', 'fariabad', 'b', 'e', 'uit', 'us', 'other', 'others',
                 'personal', 'languages', 'su', 'o', 'project', 'exprience', 'company', 'month', 'team', 'detail', 'description', 'system', 'application', 'technology', 'year', 'le', 'ltd', 'university',
                           'j', 'ge', 'like', 'also', 'timely', 'per', 'new', 'daily', 'etc', 'size', 'level', 'experience', 'mumbai', 'ee', 'pre', 'text', 'co', 'till',
                           'mi', 'essfully', 'es', 'uat', 'discus', 'v', 'ci', 'no', 'bachelor', 'k', 'v', 'o', 'sla', 'po', 'hmi', 'dubai', 'h', 'pl', 'nashik', 'capgemini', 'aug',
                           'jan', 'feb', 'whenever', 'n', 'xen', 'l', 'u']
                           
  stopword.extend(list_stopword)
  text = [word for word in text if word not in stopword]
  return text

def tokenize2(text):
    stop_words = set(stopwords.words('english'))
    words = word_tokenize(text)
    words = [word.lower() for word in words if word.lower() not in stop_words and word.isalpha()]
    return words

def lemmatize_word(word):
    lemmatizer = WordNetLemmatizer()
    return [lemmatizer.lemmatize(w) for w in word]


def concat(lst):
    sentence = ' '.join(lst)
    return sentence
