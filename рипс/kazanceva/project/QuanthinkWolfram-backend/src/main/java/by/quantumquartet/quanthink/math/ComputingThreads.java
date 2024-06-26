package by.quantumquartet.quanthink.math;

import static by.quantumquartet.quanthink.math.UtilFunctions.determinant;
import static by.quantumquartet.quanthink.math.UtilFunctions.generateSubArray;

public class ComputingThreads {
    public static class CountDeterminantTask implements Runnable {
        int i;
        double[][] A;
        int N;
        double Result;

        public CountDeterminantTask(double[][] _A, int _N, int _i) {
            this.A = _A;
            this.N = _N;
            this.i = _i;
        }

        @Override
        public void run() {
            double[][] subArray = generateSubArray(this.A, this.N, this.i);
            this.Result = Math.pow(-1.0, 1.0 + this.i + 1.0) * this.A[0][this.i] * determinant(subArray, this.N - 1);
        }
    }

    public static class SumRowTask implements Runnable {
        int id;
        int len;
        double[] A;
        double[] B;
        double Result[];

        public SumRowTask(double[] _A, double[] _B, int _id, int _len) {
            this.A = _A;
            this.B = _B;
            this.id = _id;
            this.len = _len;
            this.Result = new double[this.len];
        }

        @Override
        public void run() {
            for (int i = 0 ; i < this.len; i ++)
                this.Result[i] = this.A[i] + this.B[i];
        }
    }

    public static class SubRowTask implements Runnable {
        int id;
        int len;
        double[] A;
        double[] B;
        double Result[];

        public SubRowTask(double[] _A, double[] _B, int _id, int _len) {
            this.A = _A;
            this.B = _B;
            this.id = _id;
            this.len = _len;
            this.Result = new double[this.len];
        }

        @Override
        public void run() {
            for (int i = 0 ; i < this.len; i ++)
                this.Result[i] = this.A[i] - this.B[i];
        }
    }

    public static class MulRowOnColumnTask implements Runnable {
        int row;
        int column;
        int len;
        double[] A;
        double[] B;
        double Result;

        public MulRowOnColumnTask(double[] _A, double[] _B, int _row,int _column, int _len) {
            this.A = _A;
            this.B = _B;
            this.row = _row;
            this.column = _column;
            this.len = _len;
            this.Result = 0;
        }

        @Override
        public void run() {
            for (int i = 0 ; i < this.len; i ++)
                this.Result += this.A[i] * this.B[i];
        }
    }

    public static class MulRowOnNumTask implements Runnable {
        int id;
        int len;
        double[] A;
        double num;
        double Result[];

        public MulRowOnNumTask(double[] _A, double _num, int _id, int _len) {
            this.A = _A;
            this.num = _num;
            this.id = _id;
            this.len = _len;
            this.Result = new double[this.len];
        }

        @Override
        public void run() {
            for (int i = 0 ; i < this.len; i ++)
                this.Result[i] = this.A[i] * this.num;
        }
    }

}
