package by.quantumquartet.quanthink.math;

public class UtilFunctions {

    private static final double DELTA = 1e-8;
    private static final char PI = 'π';
    private static final char EXP = 'e';

    private UtilFunctions(){}

    public static Boolean CheckDouble(double value){
        return Math.abs(Math.round(value) - value) > UtilFunctions.GetDelta();
    }

    public static String FloatRemover(String value){
        if (CheckDouble(Double.parseDouble(value)))
            return String.valueOf(Double.parseDouble(value));
        else
            return String.valueOf(Math.round(Double.parseDouble(value)));
    }

    public static double GetDelta(){
        return DELTA;
    }

    public static String checkMulBrackets(String expr){
        String tmp = expr;
        int previous = -1;
        while (true)
        {
            int index = tmp.indexOf('(', previous + 1);
            if (index == -1)
                break;
            if (index != 0 && Character.isDigit(tmp.charAt(index-1)))
                tmp = tmp.substring(0, index) + "*" + tmp.substring(index);
            previous = index + 1;
        }
        previous = -1;
        while (true)
        {
            int index = tmp.indexOf(')', previous + 1);
            if (index == -1)
                break;
            if (index != tmp.length() - 1 && (Character.isDigit(tmp.charAt(index+1)) || tmp.charAt(index+1) == '('))
                tmp = tmp.substring(0, index + 1) + "*" + tmp.substring(index + 1);
            previous = index + 2;
        }
        return tmp;
    }

    public static String convertConstToValues(String expr){
        String tmp = expr;
        while (true)
        {
            int index = tmp.indexOf(PI);
            if (index == -1)
                break;
            tmp = tmp.substring(0, index) + Math.PI + tmp.substring(index + 1);
        }
        while (true)
        {
            int index = tmp.indexOf(EXP);
            if (index == -1)
                break;
            tmp = tmp.substring(0, index) + Math.E + tmp.substring(index + 1);
        }
        return tmp;
    }
    public static String addOneBeforeX(String expr){
        String tmp = expr;
        int previous = -1;
        while (true) {
            int index = tmp.indexOf("x",previous + 1);
            if (index == -1)
                break;
            if (index == 0 || tmp.charAt(index - 1) == '-' || tmp.charAt(index - 1) == '+')
                tmp = tmp.substring(0, index) + "1" + tmp.substring(index);
            previous = index;
        }
        return tmp;
    }
    public static String reduceSumSub(String expr){
        String tmp = expr;
        while (true) {
            int ind = tmp.indexOf("--");
            if (ind == -1)
                break;
            tmp = tmp.substring(0, ind) + "+" + tmp.substring(ind + 2);
        }
        while (true) {
            int ind = tmp.indexOf("++");
            if (ind == -1)
                break;
            tmp = tmp.substring(0, ind) + "+" + tmp.substring(ind + 2);
        }
        while (true) {
            int ind = tmp.indexOf("-+");
            if (ind == -1)
                break;
            tmp = tmp.substring(0, ind) + "-" + tmp.substring(ind + 2);
        }
        while (true) {
            int ind = tmp.indexOf("+-");
            if (ind == -1)
                break;
            tmp = tmp.substring(0, ind) + "-" + tmp.substring(ind + 2);
        }
        if(tmp.charAt(0) == '+')
            tmp = tmp.substring(1);
        return tmp;
    }

    public static String checkFloatPoints(String expr){
        String tmp = expr;
        int previous = -1;
        while (true)
        {
            int index = tmp.indexOf('.',previous + 1);
            if (index == -1)
                break;
            if (index == tmp.length() - 1 || !Character.isDigit(tmp.charAt(index + 1)))
                tmp = tmp.substring(0, index + 1) + '0' + tmp.substring(index + 1);
            previous = index + 1;
        }
        previous = -1;
        while (true)
        {
            int index = tmp.indexOf('.',previous + 1);
            if (index == -1)
                break;
            if (index == 0)
                tmp = '0' + tmp.substring(index);
            else if (!Character.isDigit(tmp.charAt(index - 1)))
                tmp = tmp.substring(0, index) + '0' + tmp.substring(index);
            previous = index + 1;
        }
        previous = tmp.indexOf('.');
        while (true)
        {
            int index = tmp.indexOf('.',previous + 1);
            if (index == -1)
                break;
            boolean check = false;
            for (int i = previous + 1; i < index; i++){
                if (!Character.isDigit(tmp.charAt(i)))
                {
                    check = true;
                    break;
                }
            }
            if (!check)
                throw new IllegalArgumentException("Error");
            previous = index;
        }
        return tmp;
    }

    public static String SimplifyMul(String expr){
        String tmp = expr;
        int previous = -1;
        while (true)
        {
            int index = tmp.indexOf('x',previous + 1);
            if (index == -1)
                break;
            if (index != 0 && tmp.charAt(index - 1) == '*')
                tmp = tmp.substring(0, index - 1) + tmp.substring(index);
            else if (index != tmp.length() - 1 && tmp.charAt(index + 1) == '*')
            {
                int end = tmp.length();
                for (int i = index + 2; i < tmp.length(); i++)
                    if (!Character.isDigit(tmp.charAt(i)))
                    {
                        end = i;
                        break;
                    }
                if (index > 0)
                    tmp = tmp.substring(0, index - 1) + tmp.substring(index+2, end) + tmp.substring(end);
                else
                    tmp = tmp.substring(index+2, end) + tmp.substring(index, index+1) + tmp.substring(end);
            }

            previous = index + 1;
        }
        return tmp;
    }

    public static void swap(double a, double b){
        double tmp = a;
        a = b;
        b = tmp;
    }

    public static double determinant(double A[][], int n){
        double res;

        if (n == 1)
            res = A[0][0];
        else if (n == 2)
            res = A[0][0]*A[1][1] - A[1][0]*A[0][1];
        else{
            res = 0;
            for (int i = 0; i < n; i++){
                double[][] subArray = generateSubArray (A, n, i);
                res += Math.pow(-1.0, 1.0 + i + 1.0) * A[0][i] * determinant(subArray, n - 1);
            }
        }
        return res;
    }

    public static double[][] generateSubArray(double A[][], int n, int j1){
        double[][] m = new double[n-1][];
        for (int k=0; k < (n - 1); k++)
            m[k] = new double[n-1];

        for (int i = 1; i < n; i++){
            int j2 = 0;
            for (int j = 0; j < n; j++){
                if(j == j1)
                    continue;
                m[i-1][j2] = A[i][j];
                j2++;
            }
        }
        return m;
    }

}
