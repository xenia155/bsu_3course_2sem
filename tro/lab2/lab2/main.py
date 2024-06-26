import numpy as np
import tensorflow as tf
from tensorflow.keras.datasets import mnist
from tensorflow.keras.utils import to_categorical

# Load MNIST data
(train_images, train_labels), (test_images, test_labels) = mnist.load_data()

# Preprocess data
train_images = train_images.reshape((60000, 28 * 28)).astype('float32') / 255
test_images = test_images.reshape((10000, 28 * 28)).astype('float32') / 255
train_labels = to_categorical(train_labels)
test_labels = to_categorical(test_labels)

# Define RBF network class
class RBFNetwork:
    def init(self, N):
        self.N = N
        self.centers = self.initialize_centers()
        self.weights = np.random.rand(N, 10)

    def initialize_centers(self):
        # Randomly select N data points as centers
        indices = np.random.choice(len(train_images), self.N, replace=False)
        return train_images[indices]

    def rbf_activation(self, x):
        # Calculate RBF activation for each center
        distances = np.linalg.norm(x - self.centers, axis=1)
        return np.exp(-distances**2)

    def predict(self, x):
        # Compute RBF activations
        rbf_outputs = self.rbf_activation(x)
        # Compute final output using weights
        return np.dot(rbf_outputs, self.weights)

    def fit(self):
        # Train the RBF network
        for epoch in range(10):
            for i in range(len(train_images)):
                x = train_images[i]
                y = train_labels[i]
                output = self.predict(x)
                error = y - output
                # Update weights
                self.weights += 0.01 * np.outer(self.rbf_activation(x), error)

    def evaluate(self):
        correct = 0
        for i in range(len(test_images)):
            x = test_images[i]
            y = test_labels[i]
            output = self.predict(x)
            if np.argmax(output) == np.argmax(y):
                correct += 1
        return correct / len(test_images)

# Vary N and train models
N_values = [5, 10, 15, 20, 25]
accuracies = []

for N in N_values:
    rbf_network = RBFNetwork(N)
    rbf_network.fit()
    accuracy = rbf_network.evaluate()
    accuracies.append(accuracy)

# Plot accuracy vs. N
import matplotlib.pyplot as plt

plt.plot(N_values, accuracies, marker='o')
plt.xlabel('Number of Neurons (N)')
plt.ylabel('Recognition Accuracy')
plt.title('Accuracy vs. N in RBF Network')
plt.grid(True)
plt.show()