import numpy as np
from sklearn.datasets import fetch_openml
from sklearn.svm import SVC
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
import matplotlib.pyplot as plt


print('Loading MNIST Dataset...')
mnist = fetch_openml('mnist_784', version=1, parser='auto')
X, y = mnist.data, mnist.target.astype(int)

# Normalize the features
X = X / 255.0

# Split data into train and test sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Varying gamma from 0.001 to 0.03
gamma_values = np.linspace(0.001, 0.03, 10)

accuracies = []
for gamma in gamma_values:
    # Create SVM classifier with RBF kernel
    svm_classifier = SVC(kernel='rbf', gamma=gamma, C=10)

    # Train the classifier
    svm_classifier.fit(X_train, y_train)

    # Predict labels for test set
    y_pred = svm_classifier.predict(X_test)

    # Calculate accuracy
    accuracy = accuracy_score(y_test, y_pred)
    accuracies.append(accuracy)

# Plotting the results
plt.plot(gamma_values, accuracies, marker='o')
plt.title('Accuracy vs. Gamma for SVM (RBF Kernel)')
plt.xlabel('Gamma')
plt.ylabel('Accuracy')
plt.grid(True)
plt.show()
