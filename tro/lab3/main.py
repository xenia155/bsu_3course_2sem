import numpy as np
from sklearn.datasets import fetch_openml
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
import matplotlib.pyplot as plt

# Load MNIST dataset
print('Loading MNIST Dataset...')
mnist = fetch_openml('mnist_784', version=1)
X, y = mnist.data, mnist.target.astype(int)

# Normalize the features
X = X / 255.0

# Define the range of training dataset proportions
training_proportions = np.arange(0.1, 0.81, 0.1)

# Define the depth of the trees
max_depth = 20  # Modify this value as per your choice

# Initialize lists to store results
accuracies = []

# Iterate over different proportions of the training dataset
for proportion in training_proportions:
    print(f"Training with {int(proportion*100)}% of the data and max_depth={max_depth}...")

    # Split data into training and testing sets
    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size=proportion, random_state=42)

    # Initialize and train the Random Forest classifier
    clf = RandomForestClassifier(n_estimators=100, max_depth=max_depth, random_state=42)
    clf.fit(X_train, y_train)

    # Make predictions
    y_pred = clf.predict(X_test)

    # Calculate accuracy
    accuracy = accuracy_score(y_test, y_pred)
    accuracies.append(accuracy)
    print(f"Accuracy: {accuracy:.4f}")

# Plot the results
plt.plot(training_proportions, accuracies, marker='o')
plt.title(f'Accuracy vs Training Data Size (Max Depth = {max_depth})')
plt.xlabel('Training Data Size')
plt.ylabel('Accuracy')
plt.grid(True)
plt.show()