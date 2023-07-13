# Machine Learning Part - CareerLeap
This project focuses on building a natural language processing (NLP) system using Tensorflow. The goal is create model that can accurately recommend text data into predefined job. Dion and I focus on building Machine Learning Model for this project.

## Problem Understanding

## Dataset
The dataset used for this project is sourced from Kaggle and consists of resumes for various job positions. The original dataset contains 962 entries with information on 25 different job positions. However, for this specific project, we have focused on six specific job positions:
- Business Analyst
- Data Science
- Java Developer
- Operations Manager
- Web Designing
- DevOps Engineer

We have filtered the original dataset and extracted a subset of 292 entries that correspond to these six job positions. This subset will be used for analysis, modeling, and any further tasks related to this project.

https://www.kaggle.com/datasets/gauravduttakiit/resume-dataset

## Metrics
To measure the performance of our NLP classifier, we will be using accuracy as our evaluation metric. Accuracy represents the percentage of correctly classified instances out of the total number of instances.

## Model Architecture
![job_classifier-removebg-preview](https://github.com/arviereyhan/CareerLeap/assets/88980651/7fa2532f-7164-4289-8b4d-a27ec9b3a2f7)

### Component
1. LSTM Layer
The LSTM (Long Short-Term Memory) layer is a type of recurrent neural network layer that excels at capturing dependencies and patterns in sequential data. It is well-suited for tasks involving time series, natural language processing, and other sequential data analysis. The LSTM layer in our model helps the network learn long-term dependencies and maintain memory of past inputs. In our model, we use LSTM layer with 32 filters.

2. Dense Layer
Following the LSTM layer, we have three Dense layers. Dense layers, also known as fully connected layers, are responsible for learning non-linear relationships between the features extracted from the input data. Each Dense layer consists of a set of neurons that receive input from all the neurons in the previous layer.

The number of neurons in each Dense layer determines the dimensionality of the layer's output. In our model, the first Dense layer has 128 neurons, the second Dense layer has 64 neurons, and the third Dense layer has 6 neurons. The third Dense layer is a output layer to classify six jobs.
