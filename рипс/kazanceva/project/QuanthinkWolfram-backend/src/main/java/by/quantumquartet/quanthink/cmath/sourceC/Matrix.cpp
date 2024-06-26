#include "../includeC/by_quantumquartet_quanthink_cmath_NativeMath_Matrix.h"
#include "../includeC/by_quantumquartet_quanthink_cmath_NativeMath_UtilFunctions.h"

Matrix::Matrix(int rows, int cols, double** data) {
        setRows(rows);
        setCols(cols);
        setData(data);
}

void Matrix::setRows(int rows) {
    this->rows = rows;
}

int* Matrix::getSize() {
    return new int[2]{ this->rows, this->cols };
}

void Matrix::setCols(int cols) {
    this->cols = cols;
}

void Matrix::setData(double** data) {
    this->data = data;
}

double** Matrix::getData() {
    return this->data;
}

double Matrix::getElement(int row, int column){
    return this->data[row][column];
}

void Matrix::setElement(double value, int row, int column){
    this->data[row][column] = value;
}

double* Matrix::getRow(int i){
    if (i < 0 || i >= this->rows)
        throw new ThrownJavaException("No such row. Index is out of bounds|IndexOutOfBoundsException");
    return this->data[i];
}

double* Matrix::getColumn(int j){
    if (j < 0 || j >= this->cols)
        throw new ThrownJavaException("No such column. Index is out of bounds|IndexOutOfBoundsException");
    double* column = new double[this->rows];
    for (int i = 0; i < this->rows; i++)
        column[i] = this->data[i][j];
    return column;
}