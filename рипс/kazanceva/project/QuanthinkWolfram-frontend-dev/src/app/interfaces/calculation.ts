export interface BasicArithmetic {
  userId: string | null;
  expression: string;
  library: string;
}

export interface Equation {
  userId: string | null;
  equation: string;
  library: string;
}

export interface MatrixSum {
  userId: string | null;
  matrix1: Matrix;
  matrix2: Matrix;
  threadsUsed: number;
  library: string;
}

export interface MatrixSub {
  userId: string | null;
  matrix1: Matrix;
  matrix2: Matrix;
  threadsUsed: number;
  library: string;
}

export interface MatrixMul {
  userId: string | null;
  matrix1: Matrix;
  matrix2: Matrix;
  threadsUsed: number;
  library: string;
}

export interface MatrixReverse {
  userId: string | null;
  matrix: Matrix;
  library: string;
}

export interface MatrixTranspose {
  userId: string | null;
  matrix: Matrix;
  library: string;
}

export interface MatrixSystem {
  userId: string | null;
  matrix1: Matrix;
  matrix2: Matrix;
  threadsUsed: number;
  library: string;
}

export interface MatrixMulByNum {
  userId: string | null;
  matrix: Matrix;
  number: number;
  threadsUsed: number;
  library: string;
}

export interface MatrixDeterminant {
  userId: string | null;
  matrix: Matrix;
  threadsUsed: number;
  library: string;
}

export interface Matrix{
  rows: number;
  cols: number;
  data: number[][];
}