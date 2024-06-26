package by.quantumquartet.quanthink.math;

import java.util.Arrays;
import java.util.Objects;

import static by.quantumquartet.quanthink.math.UtilFunctions.swap;

public class Equations {

    private final static String[] order = {"e", "+-", "*", "1"};

    public static String SolveEquation(String equation){
        String tmp = equation;
        if (Objects.equals(tmp, ""))
            throw new IllegalArgumentException("Nothing to solve");
        for (String s : order) {
            switch (s) {
                case "e":
                    tmp = UtilFunctions.convertConstToValues(tmp);
                    tmp = UtilFunctions.checkFloatPoints(tmp);

                    break;
                case "+-":
                    tmp = UtilFunctions.reduceSumSub(tmp);
                    break;
                case "1":
                    tmp = UtilFunctions.addOneBeforeX(tmp);
                    break;
                case "*":
                    tmp = UtilFunctions.SimplifyMul(tmp);
                    break;
            }
        }
        double[] coeffs = GetEquationCoefficients(tmp);
        StringBuilder roots = new StringBuilder();
        int pow = 0;
        for (int i = 5; i >= 0; i--)
        {
            if(coeffs[i] != 0){
              pow = i;
              break;
            }

        }
        switch (pow) {
            case 0:
                throw new IllegalArgumentException("Not an equation");
            case 1:
                roots = new StringBuilder(String.valueOf(-coeffs[0] / coeffs[1]));
                break;
            case 2:
                roots = new StringBuilder(SolveSquareEquation(coeffs[2], coeffs[1], coeffs[0]));
                break;
            case 3:
                roots = new StringBuilder(SolveCubeEquation(coeffs[3], coeffs[2], coeffs[1], coeffs[0]));
                break;
            case 4:
                roots = new StringBuilder(SolveQuadraticEquation(coeffs[4], coeffs[3], coeffs[2], coeffs[1], coeffs[0]));
                break;
            case 5:
                roots = new StringBuilder(SolvePentaEquation(coeffs[5], coeffs[4], coeffs[3], coeffs[2], coeffs[1], coeffs[0]));
                break;
        }
        String[] rootsArr = roots.toString().split(" ");
        roots = new StringBuilder();
        for (String s : rootsArr) {
            if (!s.contains("±"))
                roots.append(UtilFunctions.FloatRemover(s)).append(" ");
            else
                roots.append(s).append(" ");
        }
        roots.delete(roots.length()-1, roots.length());
        return roots.toString();
    }

    private static double[] GetEquationCoefficients(String equation){
        double[] eqClass  = {0, 0, 0, 0, 0, 0};
        int start = -1;
        char prev_char = ' ';
        for (int i = 0 ; i < equation.length(); i++) {
                if (start == -1 && Character.isDigit(equation.charAt(i))){
                    if (i > 0)
                        prev_char = equation.charAt(i - 1);
                    start = i;
                }
                if (equation.charAt(i) == 'x'){
                    start = -1;
                    prev_char = ' ';
                }
                if (i == equation.length() - 1 && start != -1 && prev_char != '^')
                {
                    eqClass[0] = Double.parseDouble(equation.substring(start));
                    if (equation.charAt(start - 1) == '-')
                        eqClass[0] *= -1;
                }
                if (!Character.isDigit(equation.charAt(i)) && equation.charAt(i) != '.' && start != -1){
                    if (prev_char != '^'){
                        eqClass[0] = Double.parseDouble(equation.substring(start, i));
                        if (start >= 1 && equation.charAt(start - 1) == '-')
                            eqClass[0] *= -1;
                        break;
                    }
                    else {
                        start = -1;
                        prev_char = ' ';
                    }
                }
        }
        int previous = -1;
        while (true)
        {
            double number = 0;
            int index = equation.indexOf("x", previous + 1);
            if (index == -1)
                break;
            for (int i = index - 1; i >= 0; i--)
            {
                if (i == 0)
                    number = Double.parseDouble(equation.substring(i, index));
                if (!Character.isDigit(equation.charAt(i)) && equation.charAt(i) != '.')
                {
                    number = Double.parseDouble(equation.substring(i + 1, index));
                    if (equation.charAt(i) == '-')
                        number *= -1;
                    break;
                }
            }
            if (index == equation.length() - 1)
                eqClass[1] = number;
            else
            for (int i = index + 1; i < equation.length(); i++)
                if (!Character.isDigit(equation.charAt(i)) && equation.charAt(i) != '.') {
                    if (equation.charAt(i) == '^') {
                        for (int j = i + 1; j < equation.length(); j++) {
                            if (j == equation.length() - 1)
                            {
                                if (UtilFunctions.CheckDouble(Double.parseDouble(equation.substring(j))))
                                    throw new IllegalArgumentException("Equation class can't be defined");
                                eqClass[Integer.parseInt(equation.substring(i + 1))] = number;
                            }
                            if (!Character.isDigit(equation.charAt(j)) && equation.charAt(j) != '.') {
                                if (UtilFunctions.CheckDouble(Double.parseDouble(equation.substring(i + 1, j))))
                                    throw new IllegalArgumentException("Equation class can't be defined");
                                eqClass[Integer.parseInt(equation.substring(i + 1, j))] = number;
                                break;
                            }
                        }
                        break;
                    }
                }
            else
                eqClass[1] = number;
            previous = index;
        }
        return eqClass;
    }

    private static String SolveSquareEquation(double a, double b, double c) {
        double D = Math.pow(b,2) - 4.0 * a * c;
        if (D > 0) {
            D = Math.sqrt(D);
            double x1 = (-b + D) / (2.0 * a);
            double x2  = (-b - D) / (2.0 * a);
            if (x1 < x2)
                return x1 + " " + x2;
            else
                return x2 + " " + x1;

        }
        else if (D == 0)
            return String.valueOf(-b / (2.0 * a ));
        else {
            return -b / (2.0 * a) + "±" + Math.sqrt(Math.abs(D)) / (2.0 * a) +"i";
        }
    }


    private static String SolveCubeEquation(double a, double b, double c, double d) {

        double B = b / a;
        double C = c / a;
        double D = d / a;
        double q, r, r2, q3;
        q = (B * B- 3.0 * C) / 9.0;
        r = (B * (2.0 * B * B - 9.0 * C)+27.*D)/54.;
        r2 = r * r; q3 = q * q * q;
        double S = q3 - r2;
        if(S > 0) {
            double t = 1.0 / 3.0 * Math.acos(r / Math.sqrt(q3));
            q = -2.0 * Math.sqrt(q);
            double x1 = q * Math.cos(t) - B / 3.0;
            double x2 = q * Math.cos((t + 2.0 / 3.0 * Math.PI))- B / 3.0;
            double x3 = q * Math.cos((t - 2.0 / 3.0 * Math.PI))- B / 3.0;
            if (x1 > x2 && x2 > x3)
                return x3 + " " + x2 + " " + x1;
            else if(x3 > x2 && x2 > x1)
                return x1 + " " + x2 + " " + x3;
            else if(x1 > x3 && x3 > x2)
                return x2 + " " + x3 + " " + x1;
            else if(x2 > x3 && x3 > x1)
                return x1 + " " + x3 + " " + x2;
            else if(x3 > x1 && x1 > x2)
                return x2 + " " + x1 + " " + x3;
            else
                return x3 + " " + x1 + " " + x2;
        }
        else if(S < 0) {
            if (q > 0)
            {
                double var = Math.abs(r)/Math.sqrt(q3);
                double t = 1.0 / 3.0 * (Math.log(var + Math.sqrt(var * var - 1)));
                double x1 = -2.0 * r / Math.abs(r) * Math.sqrt(q) * Math.cosh(t) - B / 3.0;
                String x23 = (r / Math.abs(r) * Math.sqrt(q) * Math.cosh(t) - B / 3.0) + "±" +
                        (Math.sqrt(3 *Math.abs(q))*Math.sinh(t))  + "i";
                return x1 + " " + x23;
            }
            else if (q < 0) {
                double var = Math.abs(r)/Math.sqrt(Math.abs(q3));
                double t = 1.0 / 3.0 * (Math.log(var + Math.sqrt(var * var + 1)));
                double x1 = -2.0 * r / Math.abs(r) * Math.sqrt(Math.abs(q)) * Math.sinh(t) - B / 3.0;
                String x23 = (r / Math.abs(r) * Math.sqrt(Math.abs(q)) * Math.sinh(t) - B / 3.0) + "±" +
                        (Math.sqrt(3 * Math.abs(q))*Math.cosh(t)) + "i";
                return x1 + " " + x23;
            }
            else {
                double x1 = -Math.cbrt(D - B * B * B / 27.0) - B / 3.0;
                String x23 = ( - (B + x1) / 2.0) + "±" + (0.5 * Math.sqrt(Math.abs((B-3.0 * x1) * (B + x1) - 4 * C)))
                        + "i";
                return x1 + " " + x23;
            }
        }
        else {
            double x1 = -2.0 * r / Math.abs(r) * Math.sqrt(Math.abs(q)) - B / 3.0;
            double x2 = r / Math.abs(r) * Math.sqrt(Math.abs(q)) - B / 3.0;
            if (x1 > x2)
                return x2 + " " + x1;
            else
                return x1 + " " + x2;
        }
    }

    private static String SolveQuadraticEquation(double a, double b, double c, double d, double e) {
        double[] roots = new double[]{0,0,0,0};
        double B = b / a;
        double C = c / a;
        double D = d / a;
        double E = e / a;
        double d1 = E + 0.25 * B * (0.25 * C * B - 3.0 / 64.0 * B * B * B - D);
        double c1 = D + 0.5 * B * (0.25 * B * B - C);
        double b1 = C - 0.375 * B * B;
        int res = SolveP4De(roots, b1, c1, d1);
        if(res == 4) {
            roots[0]-= B/4;
            roots[1]-= B/4;
            roots[2]-= B/4;
            roots[3] -= B/4;
        }
        else if (res == 2) {
            roots[0]-= B/4;
            roots[1]-= B/4;
            roots[2]-= B/4;
        }
        else {
            roots[0]-= B/4;
            roots[2]-= B/4;
        }

        boolean img1 = false, img2 = false;
        // one Newton step for each real root:
        if(res > 0)
        {
            roots[0] = N4Step(roots[0], B,C,D,E);
            roots[1] = N4Step(roots[1], B,C,D,E);
        }
        else
            img1 = true;
        if(res > 2)
        {
            roots[2] = N4Step(roots[2], B,C,D,E);
            roots[3] = N4Step(roots[3], B,C,D,E);
        }
        else
            img2 = true;
        for(int i = 0; i < 4; i ++)
            if (!UtilFunctions.CheckDouble(roots[i]))
                roots[i] = Math.round(roots[i]);
        if (img1 && img2){
            if (roots[0] > roots[2])
                return roots[2] + "±" + Math.abs(roots[3]) + "i " + roots[0] + "±" + Math.abs(roots[1]) + "i";
            else if (roots[0] < roots[2])
                return roots[0] + "±" + Math.abs(roots[1]) + "i " + roots[2] + "±" + Math.abs(roots[3]) + "i";
            else if (roots[1] < roots[3])
                return roots[0] + "±" + Math.abs(roots[1]) + "i " + roots[2] + "±" + Math.abs(roots[3]) + "i";
            else
                return roots[2] + "±" + Math.abs(roots[3]) + "i " + roots[0] + "±" + Math.abs(roots[1]) + "i";
        }
        else if (img1 && !img2){
            if (roots[2] > roots[3])
                return roots[3] + " " + roots[2] + " " + roots[0] + "±" + Math.abs(roots[1]) + "i";
            else
                return roots[2] + " " + roots[3] + " " + roots[0] + "±" + Math.abs(roots[1]) + "i";
        }
        else if (!img1 && img2){
            if (roots[1] > roots[0])
                return roots[0] + " " + roots[1] + " " + roots[2] + "±" + Math.abs(roots[3]) + "i";
            else
                return roots[1] + " " + roots[0] + " " + roots[2] + "±" + Math.abs(roots[3]) + "i";
        }
        else{
           Arrays.sort(roots);
            return roots[0] + " " + roots[1] + " " + roots[2] + " " + roots[3];
        }
    }

    private static int SolveP4De(double[] roots,double b, double c, double d) { // solve equation x^4 + b*x^2 + c*x + d
        if(Math.abs(c) < UtilFunctions.GetDelta() * (Math.abs(b) + Math.abs(d)))
            return SolveP4Bi(roots,b,d);

        String result = SolveCubeEquation(1, 2*b, b*b-4*d, -c*c);
        String[] tmp = result.split(" ");
        int resReal = 0;
        for (int i = 0; i < tmp.length; i ++){
            if (!tmp[i].contains("±"))
            {
                resReal++;
                roots[i] = Double.parseDouble(tmp[i]);
            }
            else{
                String[] imaginary = tmp[i].split("±");
                roots[i] = Double.parseDouble(imaginary[0]);
                roots[i + 1] = Double.parseDouble(imaginary[1].substring(0, imaginary[1].length()-1));
            }
        }
        if( resReal > 1 )
        {
            dblSort3(roots[0], roots[1], roots[2]);
            if(roots[0] > 0)
            {
                double sz1 = Math.sqrt(roots[0]);
                double sz2 = Math.sqrt(roots[1]);
                double sz3 = Math.sqrt(roots[2]);
                if(c>0 )
                {
                    roots[0] = (-sz1 -sz2 -sz3)/2;
                    roots[1] = (-sz1 +sz2 +sz3)/2;
                    roots[2] = (+sz1 -sz2 +sz3)/2;
                    roots[3] = (+sz1 +sz2 -sz3)/2;
                    return 4;
                }
                roots[0] = (-sz1 -sz2 +sz3)/2;
                roots[1] = (-sz1 +sz2 -sz3)/2;
                roots[2] = (+sz1 -sz2 -sz3)/2;
                roots[3] = (+sz1 +sz2 +sz3)/2;
                return 4;
            }
            double sz1 = Math.sqrt(-roots[0]);
            double sz2 = Math.sqrt(-roots[1]);
            double sz3 = Math.sqrt(roots[2]);

            if( c>0 )
            {
                roots[0] = -sz3 / 2.0;
                roots[1] = (sz1 - sz2) / 2.0;
                roots[2] =  sz3 / 2.0;
                roots[3] = (-sz1 - sz2) / 2.0;
                return 0;
            }
            roots[0] =   sz3 / 2.0;
            roots[1] = (-sz1 + sz2)/2.0;
            roots[2] =  -sz3 / 2.0;
            roots[3] = (sz1 + sz2) / 2.0;
            return 0;
        }
        if (roots[0] < 0)
            roots[0] = 0;
        double sz1 = Math.sqrt(roots[0]);
        double szr = 0, szi = 0;
        double[] ArrSZ = CSqrt(roots[1], roots[2]);
        szr = ArrSZ[0];
        szi = ArrSZ[1];
        if(c > 0)
        {
            roots[0] = -sz1 / 2.0 - szr;
            roots[1] = -sz1 / 2.0 + szr;
            roots[2] = sz1 / 2.0;
            roots[3] = szi;
            return 2;
        }
        roots[0] = sz1 / 2.0 - szr;
        roots[1] = sz1 / 2.0 + szr;
        roots[2] = -sz1 / 2.0;
        roots[3] = szi;
        return 2;
    }

    private static int SolveP4Bi(double[] roots, double b, double d) {
        double D = b * b - 4 * d;
        if(D >= 0)
        {
            double sD = Math.sqrt(D);
            double x1 = (-b + sD)/2;
            double x2 = (-b - sD)/2;	// x2 <= x1
            if(x2 >= 0)				// 0 <= x2 <= x1, 4 real roots
            {
                double sx1 = Math.sqrt(x1);
                double sx2 = Math.sqrt(x2);
                roots[0] = -sx1;
                roots[1] =  sx1;
                roots[2] = -sx2;
                roots[3] =  sx2;
                return 4;
            }
            if( x1 < 0 )				// x2 <= x1 < 0, two pair of imaginary roots
            {
                double sx1 = Math.sqrt(-x1);
                double sx2 = Math.sqrt(-x2);
                roots[0] = 0.0;
                roots[1] = sx1;
                roots[2] = 0.0;
                roots[3] = sx2;
                return 0;
            }
            // now x2 < 0 <= x1 , two real roots and one pair of imginary root
            double sx1 = Math.sqrt( x1);
            double sx2 = Math.sqrt(-x2);
            roots[0] = -sx1;
            roots[1] = sx1;
            roots[2] = 0.0;
            roots[3] = sx2;
            return 2;
        } else {  // if( D < 0 ), two pair of compex roots
            double sD2 = 0.5 * Math.sqrt(-D);
            double[] tmp1 = CSqrt(-0.5 * b, sD2);
            roots[0] = tmp1[0];
            roots[1] = tmp1[1];
            double[] tmp2 = CSqrt(-0.5 * b, -sD2);
            roots[2] = tmp2[0];
            roots[3] = tmp2[1];
            return 0;
        }
    }

    private static double N4Step(double x, double a,double b,double c,double d) {
        double fxs= ((4 * x + 3 * a) * x + 2 * b) * x + c;	// f'(x)
        if (fxs == 0)
            return x;
        double fx = (((x + a) * x + b) * x + c) * x + d;	// f(x)
        return x - fx / fxs;
    }

    private static  double[]  CSqrt(double x, double y) { // returns:  a+i*s = sqrt(x+i*y)
        double a = 0, b = 0;
        double r  = Math.sqrt(x * x + y * y);
        if(y == 0) {
            r =  Math.sqrt(r);
            if(x >= 0) {
                a = r;
                b = 0;
            } else {
                a = 0;
                b = r;
            }
        } else {		// y != 0
            a = Math.sqrt(0.5 * (x + r));
            b = 0.5 * y / a;
        }
        return new double[]{a, b};
    }

    static void  dblSort3(double a, double b, double c) { // make: a <= b <= c
        if(a > b)
            swap(a, b);
        if(c < b) {
            swap(b, c);
            if(a > b)
                swap(a,b);
        }
    }

    private static String SolvePentaEquation(double a, double b, double c, double d, double e, double f) {
        double root = SolveP5_1(b / a, c / a, d / a, e / a, f / a);
        double b1 = b / a + root, c1 = c / a + root * b1, d1 = d / a + root * c1, e1 = e / a +root*d1;
        return root + " " + SolveQuadraticEquation(1, b1, c1, d1, e1);
    }

    private static double FunctionF5(double x, double a, double b, double c, double d, double e){
        return ((((x+a)*x+b)*x+c)*x+d)*x+e;
    }

    private static double SolveP5_1(double a,double b,double c,double d,double e)
    {
        int cnt;
        if(Math.abs(e)< UtilFunctions.GetDelta() ) return 0;

        double brd =  Math.abs(a);
        if( Math.abs(b) > brd )
            brd = Math.abs(b);
        if( Math.abs(c) > brd )
            brd = Math.abs(c);
        if( Math.abs(d) > brd )
            brd = Math.abs(d);
        if( Math.abs(e) > brd )
            brd = Math.abs(e);
        brd++;

        double x0, f0;
        double x1, f1;
        double x2, f2, f2s;
        double dx = 0;

        if(e < 0) {
            x0 =   0;
            x1 = brd;
            f0=e;
            f1 = FunctionF5(x1, a, b, c, d, e);
            x2 = 0.01 * brd;
        }
        else{
            x0 = -brd;
            x1 = 0;
            f0 = FunctionF5(x0, a, b, c, d, e);
            f1 = e;
            x2 = -0.01 * brd;
        }

        if(Math.abs(f0) < UtilFunctions.GetDelta())
            return x0;
        if(Math.abs(f1) < UtilFunctions.GetDelta())
            return x1;

        for(cnt=0; cnt<10; cnt++)
        {
            x2 = (x0 + x1) / 2;
            f2 = FunctionF5(x2, a, b, c, d, e);
            if (Math.abs(f2)<UtilFunctions.GetDelta())
                return x2;
            if (f2 > 0) {
                x1 = x2; f1 = f2;
            }
            else {
                x0 = x2;
                f0 = f2;
            }
        }

        do {
            if(cnt++ > 50)
                break;
            if(x2 <= x0 || x2 >= x1)
                x2 = (x0 + x1) / 2.0;
            f2 = FunctionF5(x2, a, b, c, d, e);
            if(Math.abs(f2) < UtilFunctions.GetDelta())
                return x2;
            if(f2>0) {
                x1 = x2;
                f1 = f2;
            }
            else {
                x0 = x2;
                f0 = f2;
            }
            f2s= (((5 * x2 + 4 * a) * x2 + 3 * b)* x2 + 2 * c) * x2 + d;
            if(Math.abs(f2s) < UtilFunctions.GetDelta() ) {
                x2=1e99;
                continue;
            }
            dx = f2/f2s;
            x2 -= dx;
        } while(Math.abs(dx) > UtilFunctions.GetDelta());
        return x2;
    }
}
