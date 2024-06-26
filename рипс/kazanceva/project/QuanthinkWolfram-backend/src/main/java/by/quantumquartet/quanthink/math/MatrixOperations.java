package by.quantumquartet.quanthink.math;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static by.quantumquartet.quanthink.math.UtilFunctions.determinant;
import static by.quantumquartet.quanthink.math.UtilFunctions.generateSubArray;

public class MatrixOperations {

    public static Matrix MatrixSum(Matrix m1, Matrix m2, int threads){
        int[] size1 = m1.getSize();
        int[] size2 = m2.getSize();
        for (int i = 0; i < size2.length; i++)
            if (size1[i] != size2[i])
                throw new IllegalArgumentException("Invalid matrixes sizes");
        if (threads > 1){
            ExecutorService executorService = Executors.newFixedThreadPool(threads);
            List<ComputingThreads.SumRowTask> tasks = new ArrayList<>(size1[0]);

            for (int i = 0; i < size1[0]; i ++) {
                ComputingThreads.SumRowTask task = new ComputingThreads.SumRowTask(m1.getRow(i),
                        m2.getRow(i), i, size1[1]);
                tasks.add(task);
                executorService.execute(task);
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            double[][] result = new double[size1[0]][];
            for (ComputingThreads.SumRowTask task: tasks) {
                result[task.id] = task.Result;
            }
            return  new Matrix(size1[0], size1[1], result);
        }
        else {
            double[][] new_data = new double[size1[0]][size1[1]];
            for (int i = 0; i < size2[0]; i++)
                for (int j = 0; j < size2[1]; j++)
                    new_data[i][j] = m1.getElememnt(i,j) + m2.getElememnt(i, j);
            return new Matrix(m1.getSize()[0], m1.getSize()[1], new_data);
        }
    }

    public static Matrix MatrixSub(Matrix m1, Matrix m2, int threads){
        int[] size1 = m1.getSize();
        int[] size2 = m2.getSize();
        for (int i = 0; i < size2.length; i++)
            if (size1[i] != size2[i])
                throw new IllegalArgumentException("Invalid matrixes sizes");
        if (threads > 1){
            ExecutorService executorService = Executors.newFixedThreadPool(threads);
            List<ComputingThreads.SubRowTask> tasks = new ArrayList<>(size1[0]);

            for (int i = 0; i < size1[0]; i ++) {
                ComputingThreads.SubRowTask task = new ComputingThreads.SubRowTask(m1.getRow(i),
                        m2.getRow(i), i, size1[1]);
                tasks.add(task);
                executorService.execute(task);
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            double[][] result = new double[size1[0]][];
            for (ComputingThreads.SubRowTask task: tasks) {
                result[task.id] = task.Result;
            }
            return  new Matrix(size1[0], size1[1], result);
        }
        else {
            double[][] res_data = new double[size1[0]][size1[1]];
            for (int i = 0; i < size2[0]; i++)
                for (int j = 0; j < size2[1]; j++)
                    res_data[i][j] = m1.getElememnt(i,j) - m2.getElememnt(i, j);
            return new Matrix(m1.getSize()[0], m1.getSize()[1], res_data);
        }
    }

    public static Matrix MatrixMul(Matrix m1, Matrix m2, int threads){
        int[] size1 = m1.getSize();
        int[] size2 = m2.getSize();
        if (size1[1] != size2[0])
            throw new IllegalArgumentException("Invalid matrixes sizes");
        if (threads > 1){
            ExecutorService executorService = Executors.newFixedThreadPool(threads);
            List<ComputingThreads.MulRowOnColumnTask> tasks = new ArrayList<>(size1[0] * size2[1]);

            for (int i = 0; i < size1[0]; i ++)
                for (int j = 0; j < size2[1]; j ++)
                {
                ComputingThreads.MulRowOnColumnTask task = new ComputingThreads.MulRowOnColumnTask(m1.getRow(i),
                        m2.getColumn(j), i, j,  size1[1]);
                tasks.add(task);
                executorService.execute(task);
                }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            double[][] result = new double[size1[0]][size2[1]];
            for (ComputingThreads.MulRowOnColumnTask task: tasks) {
                result[task.row][task.column] = task.Result;
            }
            return  new Matrix(size1[0], size1[1], result);
        }
        else {
            double[][] res_data = new double[size1[0]][size1[1]];
            for (int i = 0; i < size1[0]; i++){
                double[] row = m1.getRow(i);
                for (int j = 0; j < size2[1]; j++) {
                    double [] column = m2.getColumn(j);
                    double sum = 0;
                    for (int q = 0; q < column.length; q++) {
                        sum += row[q] * column[q];
                    }
                    res_data[i][j] = sum;
                }
            }

            return new Matrix(size1[0], size1[1], res_data);
        }
    }

    public static Matrix MatrixMul(Matrix m, double num, int threads){
        if (threads > 1){
            int[] size = m.getSize();
            ExecutorService executorService = Executors.newFixedThreadPool(threads);
            List<ComputingThreads.MulRowOnNumTask> tasks = new ArrayList<>(size[0]);

            for (int i = 0; i < size[0]; i ++) {
                ComputingThreads.MulRowOnNumTask task = new ComputingThreads.MulRowOnNumTask(m.getRow(i),
                        num, i, size[1]);
                tasks.add(task);
                executorService.execute(task);
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            double[][] result = new double[size[0]][];
            for (ComputingThreads.MulRowOnNumTask task: tasks) {
                result[task.id] = task.Result;
            }
            return  new Matrix(size[0], size[1], result);
        }
        else {
            double[][] res_data = new double[m.getSize()[0]][m.getSize()[1]];
            for (int i = 0; i < m.getSize()[0]; i++)
                for (int j = 0; j < m.getSize()[1]; j++)
                    res_data[i][j] = m.getElememnt(i,j) * num;
            return new Matrix(m.getSize()[0], m.getSize()[1], res_data);
        }
    }

    public static Matrix GetTransposeMatrix(Matrix m){
        int[] size = m.getSize();
        double[][] res_data = new double[size[1]][size[0]];
        for (int i = 0 ; i < size[1]; i++){
            res_data[i] = m.getColumn(i);
        }
        return new Matrix(size[1], size[0], res_data);
    }

    public static Matrix GetReverseMatrix(Matrix m){
        int[] size = m.getSize();
        if (size[0] != size[1])
            throw new IllegalArgumentException("Matrix must be square!");
        double tmp;
        double[][] A = m.getData();
        double[][] E = new double[size[0]][size[0]];
        for (int i = 0; i < size[0]; i++)
            for (int j = 0; j < size[0]; j++)
            {
                E[i][j] = 0.0;
                if (i == j)
                    E[i][j] = 1.0;
            }

        for (int k = 0; k < size[0]; k++)
        {
            tmp = A[k][k];

            for (int j = 0; j < size[0]; j++)
            {
                A[k][j] /= tmp;
                E[k][j] /= tmp;
            }

            for (int i = k + 1; i < size[0]; i++)
            {
                tmp = A[i][k];

                for (int j = 0; j < size[0]; j++)
                {
                    A[i][j] -= A[k][j] * tmp;
                    E[i][j] -= E[k][j] * tmp;
                }
            }
        }

        for (int k = size[0] - 1; k > 0; k--)
        {
            for (int i = k - 1; i >= 0; i--)
            {
                tmp = A[i][k];

                for (int j = 0; j < size[0]; j++)
                {
                    A[i][j] -= A[k][j] * tmp;
                    E[i][j] -= E[k][j] * tmp;
                }
            }
        }

        for (int i = 0; i < size[0]; i++)
            for (int j = 0; j < size[0]; j++)
                A[i][j] = E[i][j];

        return new Matrix(size[0],size[1], A);
    }

    public static double GetDeterminant(Matrix m, int threads){
        if (m.getSize()[0] != m.getSize()[1]){
            throw new IllegalArgumentException("Matrix must be square!");
        }
        else{
            Double res;
            if (threads == 1)
            {
                res = 0.0;
                for (int i = 0; i < m.getSize()[0]; i++){
                    double[][] subArray = generateSubArray (m.getData(), m.getSize()[0], i);
                    res += Math.pow(-1.0, 1.0 + i + 1.0) * m.getElememnt(0,i)
                            * determinant(subArray, m.getSize()[0] - 1);
                }
            }
            else {
                res = 0.0;
                ExecutorService executorService = Executors.newFixedThreadPool(threads);
                List<ComputingThreads.CountDeterminantTask> tasks = new ArrayList<>(m.getSize()[0]);

                for (int i = 0; i < m.getSize()[0]; i ++) {
                    ComputingThreads.CountDeterminantTask task = new ComputingThreads.CountDeterminantTask(m.getData(),
                            m.getSize()[0], i);
                    tasks.add(task);
                    executorService.execute(task);
                }
                executorService.shutdown();
                try {
                    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (ComputingThreads.CountDeterminantTask task: tasks) {
                    res += task.Result;
                }

            }
            return res;
        }

    }

    public static String SolveSystem(Matrix A, Matrix f, int threads){
        if (A.getSize()[0] != A.getSize()[1])
            throw new IllegalArgumentException("Matrix must be square!");
        if (A.getSize()[0] != f.getSize()[0])
            throw new IllegalArgumentException("Both matrix and vector must have the same numbers of rows!");
        if ( f.getSize()[1] != 1)
            throw new IllegalArgumentException("The second argument isn't vertical vector!");
        Matrix Reversed_M = MatrixOperations.GetReverseMatrix(A);
        Matrix answer = MatrixOperations.MatrixMul(Reversed_M, f, threads);
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < answer.getSize()[0]; i ++)
            str.append(UtilFunctions.FloatRemover(String.valueOf(answer.getElememnt(i, 0))) + " ");
        str.delete(str.length()-1, str.length());
        return str.toString();
    }





}

