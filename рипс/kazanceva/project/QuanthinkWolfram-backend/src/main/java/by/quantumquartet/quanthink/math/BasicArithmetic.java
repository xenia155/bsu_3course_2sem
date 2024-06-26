package by.quantumquartet.quanthink.math;

import java.util.Objects;

public class BasicArithmetic {

    private final static String[] order = {"e","(", "^", "*", "+"};



    public static String solveExpression(String expr){
        String tmp = expr;
        if (Objects.equals(tmp, ""))
            return "0";
        for (String s : order) {
            switch (s) {
                case "e":
                    tmp = UtilFunctions.convertConstToValues(tmp);
                    tmp = UtilFunctions.checkFloatPoints(tmp);
                    tmp = UtilFunctions.checkMulBrackets(tmp);
                    break;
                case "(":
                    while (true) {
                        int begin = -1;
                        int end = -1;
                        for (int i = 0; i < tmp.length(); i++) {
                            if (tmp.charAt(i) == '(') {
                                begin = i;
                                continue;
                            }
                            if (tmp.charAt(i) == ')') {
                                end = i;
                                break;
                            }
                        }
                        if ((begin != -1 && end == -1) || (begin == -1 && end != -1))
                            throw new IllegalArgumentException("Numbers of left and right brackets must be equal");
                        if (begin == end)
                            break;
                        else
                            tmp = tmp.substring(0, begin) + solveExpression(tmp.substring(begin + 1, end))
                                    + tmp.substring(end + 1);
                    }
                    break;
                case "^":
                    tmp = solvePow(tmp);
                    break;
                case "*":
                    tmp = solveMulDiv(tmp);
                    break;
                case "+":
                    tmp = solveSumSub(tmp);
                    break;
            }
            tmp = UtilFunctions.reduceSumSub(tmp);
        }
        return UtilFunctions.FloatRemover(tmp);
    }


    private static String solveMulDiv(String expr) {
        String tmp = expr;
        while (true)
        {
            int ind_operation_begin = -1;
            int ind_operation_end = -1;
            int check_mul_del = -1;
            for (int i = 0; i < tmp.length(); i++) {
                if (tmp.charAt(i) == '+' || tmp.charAt(i) == '-' )
                {
                    ind_operation_begin = i;
                    continue;
                }
                if (tmp.charAt(i) == '*' || tmp.charAt(i) == '/'){
                    check_mul_del = i;
                    break;
                }

            }
            if (check_mul_del == -1)
                break;
            else{
                for (int i = check_mul_del + 1; i < tmp.length(); i++) {
                    if(tmp.charAt(i) == '+' || tmp.charAt(i) == '-' || tmp.charAt(i) == '*' || tmp.charAt(i) == '/') {
                        if (i - 1 == check_mul_del  && !(tmp.charAt(i) == '*' || tmp.charAt(i) == '/'))
                            continue;
                        ind_operation_end = i;
                        break;
                    }
                }
                if (ind_operation_end == -1)
                    ind_operation_end = tmp.length();
                if (ind_operation_end == check_mul_del + 1 || ind_operation_begin == check_mul_del - 1)
                    throw new ArithmeticException("Error");
                double left = Double.parseDouble(tmp.substring(ind_operation_begin + 1, check_mul_del));
                double right = Double.parseDouble(tmp.substring(check_mul_del + 1, ind_operation_end));
                if (tmp.charAt(check_mul_del) == '*')
                {

                    if (Math.abs(left * right) - 1 > (double)Integer.MAX_VALUE)
                        throw new StackOverflowError("Stack overflow");
                    tmp = tmp.substring(0,ind_operation_begin + 1) + (left * right)
                            + tmp.substring(ind_operation_end);
                }
                else {
                    if (right == 0)
                        throw new ArithmeticException("Can't divide by zero");
                    if (Math.abs(left / right) - 1 > (double)Integer.MAX_VALUE)
                        throw new StackOverflowError("Stack overflow");
                    tmp = tmp.substring(0, ind_operation_begin + 1) + (left / right)
                                + tmp.substring(ind_operation_end);

                }

            }
        }
        return tmp;
    }

    private static String solvePow(String expr){
        String tmp = expr;
        while (true)
        {
            int ind_operation_begin = -1;
            int ind_operation_end = -1;
            int check_pow = -1;
            for (int i = 0; i < tmp.length(); i++) {
                if (tmp.charAt(i) == '+' || tmp.charAt(i) == '-' || tmp.charAt(i) == '*' || tmp.charAt(i) == '/')
                {
                    ind_operation_begin = i;
                    continue;
                }
                if (tmp.charAt(i) == '^'){
                    check_pow = i;
                    break;
                }

            }
            if (check_pow == -1)
                break;
            else{
                for (int i = check_pow + 1; i < tmp.length(); i++) {
                    if (tmp.charAt(i) == '+' || tmp.charAt(i) == '-' || tmp.charAt(i) == '*' || tmp.charAt(i) == '/' || tmp.charAt(i) == '^')
                    {
                        if((tmp.charAt(i) == '+' || tmp.charAt(i) == '-') && i - 1 == check_pow)
                            continue;
                        ind_operation_end = i;
                        break;
                    }
                }
                if (ind_operation_end == -1)
                    ind_operation_end = tmp.length();
                if (ind_operation_end == check_pow + 1 || ind_operation_begin == check_pow - 1)
                    throw new ArithmeticException("Error");
                double basis = Double.parseDouble(tmp.substring(ind_operation_begin + 1, check_pow));
                double degree = Double.parseDouble(tmp.substring(check_pow + 1, ind_operation_end));
                if (Math.abs(Math.pow(basis, degree)) - 1 > Integer.MAX_VALUE)
                    throw new StackOverflowError("Stack overflow");
                tmp = tmp.substring(0,ind_operation_begin + 1) + Math.pow(basis, degree)
                        + tmp.substring(ind_operation_end);
            }
        }
        return tmp;
    }

    private static String solveSumSub(String expr){
        String tmp = expr;
        while (true)
        {
            int ind_operation_begin = 0;
            int ind_operation_end = -1;
            int check_sum_sub = -1;
            for (int i = 0; i < tmp.length(); i++) {
                if (tmp.charAt(i) == '+' || tmp.charAt(i) == '-' )
                {
                    if (i == 0)
                        continue;
                    check_sum_sub = i;
                    break;
                }

            }
            if (check_sum_sub == -1)
                break;
            else{
                for (int i = check_sum_sub + 1; i < tmp.length(); i++) {
                    if((tmp.charAt(i) == '+' || tmp.charAt(i) == '-')) {
                        if (i - 1 == check_sum_sub)
                            continue;
                        ind_operation_end = i;
                        break;
                    }
                }
                if (ind_operation_end == -1)
                    ind_operation_end = tmp.length();
                double left = Double.parseDouble(tmp.substring(ind_operation_begin, check_sum_sub));
                double right = Double.parseDouble(tmp.substring(check_sum_sub + 1, ind_operation_end));
                if (tmp.charAt(check_sum_sub) == '+')
                {
                    if (Math.abs(left + right) - 1  > Integer.MAX_VALUE)
                        throw new StackOverflowError("Stack overflow");
                    tmp = tmp.substring(0,ind_operation_begin) + (left + right)
                            + tmp.substring(ind_operation_end);
                }
                else {
                    if (Math.abs(left - right) - 1 > Integer.MAX_VALUE)
                        throw new StackOverflowError("Stack overflow");
                    tmp = tmp.substring(0,ind_operation_begin) + (left - right)
                            + tmp.substring(ind_operation_end);
                }

            }
        }
        return tmp;
    }

}
