# Machine Learning
This project focuses on building a natural language processing (NLP) system using Tensorflow. The goal is create model that can accurately recommend text data into predefined job.

## Problem Understanding

## Dataset
To train and evaluate our NLP classifier, we will be using a dataset sourced from Kaggle. Kaggle provides a vast collection of datasets, and we have selected a suitable dataset that aligns with our problem understanding. The dataset will consist of labeled text samples and their corresponding categories or labels.



## Metrics
To measure the performance of our NLP classifier, we will be using accuracy as our evaluation metric. Accuracy represents the percentage of correctly classified instances out of the total number of instances.

## Model Architecture
![job_classifier-removebg-preview](https://github.com/arviereyhan/CareerLeap/assets/88980651/7fa2532f-7164-4289-8b4d-a27ec9b3a2f7)

### Component
1. LSTM Layer
The LSTM (Long Short-Term Memory) layer is a type of recurrent neural network layer that excels at capturing dependencies and patterns in sequential data. It is well-suited for tasks involving time series, natural language processing, and other sequential data analysis. The LSTM layer in our model helps the network learn long-term dependencies and maintain memory of past inputs. In our model, we use LSTM layer with 32 filters.
. 
2. Dense Layer
Following the LSTM layer, we have three Dense layers. Dense layers, also known as fully connected layers, are responsible for learning non-linear relationships between the features extracted from the input data. Each Dense layer consists of a set of neurons that receive input from all the neurons in the previous layer.

The number of neurons in each Dense layer determines the dimensionality of the layer's output. In our model, the first Dense layer has 128 neurons, the second Dense layer has 64 neurons, and the third Dense layer has 6 neurons. The third Dense layer is a output layer to classify six jobs.
