package by.quantumquartet.quanthink.cmath;

public class NativeMath {
    static {
        System.loadLibrary("nativeMathLib");
    }

    public static native TimeStruct solveExpressionC(String expr);

    public static native TimeStruct solveEquationC(String expr);

    //с++ не поддерживает многие символы ASCII, поэтому нужна заглушка
    public static TimeStruct solveEquationCStub(String expr){
        String tmp = replacePI(expr);
        TimeStruct tmpC = solveEquationC(tmp);
        tmp = replaceHashTag(tmpC.getStringResult());
        return new TimeStruct(tmp, tmpC.getTime());
    }

    //с++ не поддерживает многие символы ASCII, поэтому нужна заглушка
    public static TimeStruct solveExpressionCStub(String expr){
        String tmp = replacePI(expr);
        TimeStruct tmpC = solveExpressionC(tmp);
        return tmpC;
    }

    private static String replacePI(String expr){
        String tmp = expr;
        int previous = -1;
        while(true){
            int index = tmp.indexOf("π", previous + 1);
            if(index == -1)
                break;
            tmp = tmp.substring(0, index) + "p" + tmp.substring(index + 1);
            previous = index;
        }
        return tmp;
    }

    private static String replaceHashTag(String expr){
        String tmp = expr;
        int previous = -1;
        while(true){
            int index = tmp.indexOf("#", previous + 1);
            if(index == -1)
                break;
            tmp = tmp.substring(0, index) + "±" + tmp.substring(index + 1);
            previous = index;
        }
        return tmp;
    }

    public static native TimeStruct MatrixSumC(MatrixC m1, MatrixC m2, int threads);

    public static native TimeStruct MatrixSubC(MatrixC m1, MatrixC m2, int threads);

    public static native TimeStruct MatrixMulC(MatrixC m1, MatrixC m2, int threads);

    public static native TimeStruct MatrixMulC(MatrixC m, double num, int threads);

    public static native TimeStruct GetTransposeMatrixC(MatrixC m);

    public static native TimeStruct GetReverseMatrixC(MatrixC m);

    public static native TimeStruct GetDeterminantC(MatrixC m, int threads);

    public static native TimeStruct SolveSystemC(MatrixC A, MatrixC f, int threads);
}
