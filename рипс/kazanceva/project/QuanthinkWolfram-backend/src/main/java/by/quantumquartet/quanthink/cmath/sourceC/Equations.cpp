#define _USE_MATH_DEFINES
#include <string>
#include <exception>
#include <iostream>
#include <stdexcept>
#include <limits>
#include <sstream>
#include <vector>
#include <algorithm>
#include <math.h>
#include <chrono>
#include "../includeC/by_quantumquartet_quanthink_cmath_NativeMath_Equations.h"
#include "../includeC/by_quantumquartet_quanthink_cmath_NativeMath_UtilFunctions.h"

const char order[] = { 'e', '+', '*', '1'};

JNIEXPORT jobject JNICALL Java_by_quantumquartet_quanthink_cmath_NativeMath_solveEquationC
  (JNIEnv *env, jobject, jstring jStr){
      const jclass stringClass = env->GetObjectClass(jStr);
      const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
      const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

      size_t length = (size_t) env->GetArrayLength(stringJbytes);
      jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

      std::string ret = std::string((char *)pBytes, length);
      env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

      env->DeleteLocalRef(stringJbytes);
      env->DeleteLocalRef(stringClass);
      std::string result;
      auto begin = std::chrono::steady_clock::now();
      try{
        result = SolveEquation(ret);
        } catch(ThrownJavaException* e) { //do not let C++ exceptions outside of this function
        std::string info = e->what();
        std::cout << info << std::endl;
        int sep = info.find('|');
        const char* message = info.substr(0,sep).c_str();
        std::string ex_type = "java/lang/" + info.substr(sep+1);
        NewJavaException(env, ex_type.c_str(), message);
      }
        auto end = std::chrono::steady_clock::now();
        auto elapsed_ms = std::chrono::duration_cast<std::chrono::milliseconds>(end - begin);
        long duration = elapsed_ms.count();
        jmethodID cnstrctrTimeC;
        jclass cc = env->FindClass("by/quantumquartet/quanthink/cmath/TimeStruct");
        cnstrctrTimeC = env->GetMethodID(cc, "<init>", "(Ljava/lang/String;J)V");
      return env->NewObject(cc, cnstrctrTimeC, env->NewStringUTF(result.c_str()), (jlong)duration);
  }


std::string SolveEquation(std::string equation) {
    std::string tmp = equation;
    if (tmp == "")
        throw new ThrownJavaException("Nothing to solve|IllegalArgumentException");
    for (int i = 0; i < (sizeof(order) / sizeof(*order)); i++) {
        char item = order[i];
        switch (item) {
            case 'e':
            tmp = UtilFunctions::ConvertConstToValues(tmp);
            tmp = UtilFunctions::CheckFloatPoints(tmp);

            break;
        case '+':
            tmp = UtilFunctions::ReduceSumSub(tmp);
            break;
        case '1':
            tmp = UtilFunctions::AddOneBeforeX(tmp);
            break;
        case '*':
            tmp = UtilFunctions::SimplifyMul(tmp);
            break;
        }
    }
    double* coeffs = { GetEquationCoefficients(tmp) };
    std::string roots = "";
    int pow = 0;
    for (int i = 5; i >= 0; i--)
    {
        if (coeffs[i] != 0) {
            pow = i;
            break;
        }

    }
    switch (pow) {
    case 0:
        throw new ThrownJavaException("Not an equation|IllegalArgumentException");
    case 1:
        roots = std::to_string(-coeffs[0] / coeffs[1]);
        break;
    case 2:
        roots = SolveSquareEquation(coeffs[2], coeffs[1], coeffs[0]);
        break;
    case 3:
        roots = SolveCubeEquation(coeffs[3], coeffs[2], coeffs[1], coeffs[0]);
        break;
    case 4:
        roots = SolveQuadraticEquation(coeffs[4], coeffs[3], coeffs[2], coeffs[1], coeffs[0]);
        break;
    case 5:
        roots = SolvePentaEquation(coeffs[5], coeffs[4], coeffs[3], coeffs[2], coeffs[1], coeffs[0]);
        break;
    }

    std::string token;
    char delimiter = ' ';
    std::stringstream ss(roots);
    std::vector<std::string> rootsVector;
    std::string result = "";
    while (std::getline(ss, token, delimiter)) {
        rootsVector.push_back(token);
    }
    for (std::string s : rootsVector) {
        if (s.find("#") == -1)
            result.append(UtilFunctions::FloatRemover(s)).append(" ");
        else
            result.append(s).append(" ");
    }
    result.pop_back();
    return result;
}

double* GetEquationCoefficients(std::string equation) {
    double* eqClass = new double[6]{ 0, 0, 0, 0, 0, 0 };
    int start = -1;
    char prev_char = ' ';
    for (int i = 0; i < equation.length(); i++) {
        if (start == -1 && std::isdigit(equation[i])) {
            if (i > 0)
                prev_char = equation[i - 1];
            start = i;
        }
        if (equation[i] == 'x') {
            start = -1;
            prev_char = ' ';
        }
        if (i == equation.length() - 1 && start != -1 && prev_char != '^')
        {
            eqClass[0] = std::stod(equation.substr(start));
            if (equation[start - 1] == '-')
                eqClass[0] *= -1;
        }
        if (!std::isdigit(equation[i]) && equation[i] != '.' && start != -1) {
            if (prev_char != '^') {
                eqClass[0] = std::stod(equation.substr(start, i - start));
                if (start >= 1 && equation[start - 1] == '-')
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
        int index = equation.find("x", previous + 1);
        if (index == -1)
            break;
        for (int i = index - 1; i >= 0; i--)
        {
            if (i == 0)
                number = std::stod(equation.substr(i, index - i));
            if (!isdigit(equation[i]) && equation[i] != '.')
            {
                number = std::stod(equation.substr(i + 1, index - (i + 1)));
                if (equation[i] == '-')
                    number *= -1;
                break;
            }
        }
        if (index == equation.length() - 1)
            eqClass[1] = number;
        else
            for (int i = index + 1; i < equation.length(); i++)
                if (!std::isdigit(equation[i]) && equation[i] != '.') {
                    if (equation[i] == '^') {
                        for (int j = i + 1; j < equation.length(); j++) {
                            if (j == equation.length() - 1)
                            {
                                if (UtilFunctions::CheckDouble(std::stod(equation.substr(j))))
                                    throw new ThrownJavaException("Equation class can't be defined|IllegalArgumentException");
                                eqClass[std::stoi(equation.substr(i + 1))] = number;
                            }
                            if (!std::isdigit(equation[j]) && equation[j] != '.') {
                                if (UtilFunctions::CheckDouble(std::stod(equation.substr(i + 1, j - (i + 1)))))
                                    throw new ThrownJavaException("Equation class can't be defined|IllegalArgumentException");
                                eqClass[std::stoi(equation.substr(i + 1, j - (i + 1)))] = number;
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

std::string SolveSquareEquation(double a, double b, double c) {
    double D = pow(b, 2) - 4.0 * a * c;
    if (D > 0) {
        D = sqrt(D);
        double x1 = (-b + D) / (2.0 * a);
        double x2 = (-b - D) / (2.0 * a);
        if (x1 < x2)
            return std::to_string(x1) + " " + std::to_string(x2);
        else
            return std::to_string(x2) + " " + std::to_string(x1);

    }
    else if (D == 0)
        return std::to_string(-b / (2.0 * a));
    else {
        return std::to_string(-b / (2.0 * a)) + "#" + std::to_string(sqrt(abs(D)) / (2.0 * a)) + "i";
    }
}


std::string SolveCubeEquation(double a, double b, double c, double d) {

    double B = b / a;
    double C = c / a;
    double D = d / a;
    double q, r, r2, q3;
    q = (B * B - 3.0 * C) / 9.0;
    r = (B * (2.0 * B * B - 9.0 * C) + 27. * D) / 54.;
    r2 = r * r; q3 = q * q * q;
    double S = q3 - r2;
    if (S > 0) {
        double t = 1.0 / 3.0 * acos(r / sqrt(q3));
        q = -2.0 * sqrt(q);
        double x1 = q * cos(t) - B / 3.0;
        double x2 = q * cos((t + 2.0 / 3.0 * M_PI)) - B / 3.0;
        double x3 = q * cos((t - 2.0 / 3.0 * M_PI)) - B / 3.0;
        if (x1 > x2 && x2 > x3)
            return std::to_string(x3) + " " + std::to_string(x2) + " " + std::to_string(x1);
        else if (x3 > x2 && x2 > x1)
            return std::to_string(x1) + " " + std::to_string(x2) + " " + std::to_string(x3);
        else if (x1 > x3 && x3 > x2)
            return std::to_string(x2) + " " + std::to_string(x3) + " " + std::to_string(x1);
        else if (x2 > x3 && x3 > x1)
            return std::to_string(x1) + " " + std::to_string(x3) + " " + std::to_string(x2);
        else if (x3 > x1 && x1 > x2)
            return std::to_string(x2) + " " + std::to_string(x1) + " " + std::to_string(x3);
        else
            return std::to_string(x3) + " " + std::to_string(x1) + " " + std::to_string(x2);
    }
    else if (S < 0) {
        if (q > 0)
        {
            double var = abs(r) / sqrt(q3);
            double t = 1.0 / 3.0 * (log(var + sqrt(var * var - 1)));
            double x1 = -2.0 * r / abs(r) * sqrt(q) * cosh(t) - B / 3.0;
            std::string x23 = std::to_string((r / abs(r) * sqrt(q) * cosh(t) - B / 3.0)) + "#" +
                std::to_string((sqrt(3 * abs(q)) * sinh(t))) + "i";
            return std::to_string(x1) + " " + x23;
        }
        else if (q < 0) {
            double var = abs(r) / sqrt(abs(q3));
            double t = 1.0 / 3.0 * (log(var + sqrt(var * var + 1)));
            double x1 = -2.0 * r / abs(r) * sqrt(abs(q)) * sinh(t) - B / 3.0;
            std::string x23 = std::to_string((r / abs(r) * sqrt(abs(q)) * sinh(t) - B / 3.0)) + "#" +
                std::to_string((sqrt(3 * abs(q)) * cosh(t))) + "i";
            return std::to_string(x1) + " " + x23;
        }
        else {
            double x1 = -cbrt(D - B * B * B / 27.0) - B / 3.0;
            std::string x23 = std::to_string(-(B + x1) / 2.0) + "#" + std::to_string(0.5 * sqrt(abs((B - 3.0 * x1) * (B + x1) - 4 * C)))
                + "i";
            return std::to_string(x1) + " " + x23;
        }
    }
    else {
        double x1 = -2.0 * r / abs(r) * sqrt(abs(q)) - B / 3.0;
        double x2 = r / abs(r) * sqrt(abs(q)) - B / 3.0;
        if (x1 > x2)
            return std::to_string(x2) + " " + std::to_string(x1);
        else
            return std::to_string(x1) + " " + std::to_string(x2);
    }
}

std::string SolveQuadraticEquation(double a, double b, double c, double d, double e) {
    double roots[] = {0, 0, 0, 0};
    double B = b / a;
    double C = c / a;
    double D = d / a;
    double E = e / a;
    double d1 = E + 0.25 * B * (0.25 * C * B - 3.0 / 64.0 * B * B * B - D);
    double c1 = D + 0.5 * B * (0.25 * B * B - C);
    double b1 = C - 0.375 * B * B;
    int res = SolveP4De(roots, b1, c1, d1);
    if (res == 4) {
        roots[0] -= B / 4;
        roots[1] -= B / 4;
        roots[2] -= B / 4;
        roots[3] -= B / 4;
    }
    else if (res == 2) {
        roots[0] -= B / 4;
        roots[1] -= B / 4;
        roots[2] -= B / 4;
    }
    else {
        roots[0] -= B / 4;
        roots[2] -= B / 4;
    }

    bool img1 = false, img2 = false;
    // one Newton step for each real root:
    if (res > 0)
    {
        roots[0] = N4Step(roots[0], B, C, D, E);
        roots[1] = N4Step(roots[1], B, C, D, E);
    }
    else
        img1 = true;
    if (res > 2)
    {
        roots[2] = N4Step(roots[2], B, C, D, E);
        roots[3] = N4Step(roots[3], B, C, D, E);
    }
    else
        img2 = true;
    for (int i = 0; i < 4; i++)
        if (!UtilFunctions::CheckDouble(roots[i]))
            roots[i] = round(roots[i]);
    if (img1 && img2) {
        if (roots[0] > roots[2])
            return std::to_string(roots[2]) + "#" + std::to_string(abs(roots[3])) + "i " + std::to_string(roots[0]) + "#" + std::to_string(abs(roots[1])) + "i";
        else if (roots[0] < roots[2])
            return std::to_string(roots[0]) + "#" + std::to_string(abs(roots[1])) + "i " + std::to_string(roots[2]) + "#" + std::to_string(abs(roots[3])) + "i";
        else if (roots[1] < roots[3])
            return std::to_string(roots[0]) + "#" + std::to_string(abs(roots[1])) + "i " + std::to_string(roots[2]) + "#" + std::to_string(abs(roots[3])) + "i";
        else
            return std::to_string(roots[2]) + "#" + std::to_string(abs(roots[3])) + "i " + std::to_string(roots[0]) + "#" + std::to_string(abs(roots[1])) + "i";
    }
    else if (img1 && !img2) {
        if (roots[2] > roots[3])
            return std::to_string(roots[3]) + " " + std::to_string(roots[2]) + " " + std::to_string(roots[0]) + "#" + std::to_string(abs(roots[1])) + "i";
        else
            return std::to_string(roots[2]) + " " + std::to_string(roots[3]) + " " + std::to_string(roots[0]) + "#" + std::to_string(abs(roots[1])) + "i";
    }
    else if (!img1 && img2) {
        if (roots[1] > roots[0])
            return std::to_string(roots[0]) + " " + std::to_string(roots[1]) + " " + std::to_string(roots[2]) + "#" + std::to_string(abs(roots[3])) + "i";
        else
            return std::to_string(roots[1]) + " " + std::to_string(roots[0]) + " " + std::to_string(roots[2]) + "#" + std::to_string(abs(roots[3])) + "i";
    }
    else {
        std::vector<double> roots_vec;
        for (int i = 0 ; i < 4; i++)
            roots_vec.push_back(roots[i]);
        std::sort(roots_vec.begin(), roots_vec.end());
        return std::to_string(roots_vec[0]) + " " + std::to_string(roots_vec[1]) + " " + std::to_string(roots_vec[2]) + " " + std::to_string(roots_vec[3]);
    }
}

int SolveP4De(double* roots, double b, double c, double d) { // solve equation x^4 + b*x^2 + c*x + d
    if (abs(c) < UtilFunctions::GetDelta() * (abs(b) + abs(d)))
        return SolveP4Bi(roots, b, d);

    std::string result = SolveCubeEquation(1, 2 * b, b * b - 4 * d, -c * c);
    char delimiter = ' ';
    std::string token;
    std::stringstream ss(result);
    std::vector<std::string> rootsVector;
    while (std::getline(ss, token, delimiter)) {
        rootsVector.push_back(token);
    }
    int resReal = 0;
    int i = 0;
    for (std::string s : rootsVector) {
        if (s.find("#") == -1)
        {
            resReal++;
            roots[i] = std::stod(s);
        }
        else {
            int del = s.find('#');
            std::string imaginary[] = {s.substr(0, del),s.substr(del + 1)};
            roots[i] = std::stod(imaginary[0]);
            roots[i + 1] = std::stod(imaginary[1].substr(0, imaginary[1].length() - 1));
        }
        i++;
    }
    if (resReal > 1)
    {
        dblSort3(roots[0], roots[1], roots[2]);
        if (roots[0] > 0)
        {
            double sz1 = sqrt(roots[0]);
            double sz2 = sqrt(roots[1]);
            double sz3 = sqrt(roots[2]);
            if (c > 0)
            {
                roots[0] = (-sz1 - sz2 - sz3) / 2;
                roots[1] = (-sz1 + sz2 + sz3) / 2;
                roots[2] = (+sz1 - sz2 + sz3) / 2;
                roots[3] = (+sz1 + sz2 - sz3) / 2;
                return 4;
            }
            roots[0] = (-sz1 - sz2 + sz3) / 2;
            roots[1] = (-sz1 + sz2 - sz3) / 2;
            roots[2] = (+sz1 - sz2 - sz3) / 2;
            roots[3] = (+sz1 + sz2 + sz3) / 2;
            return 4;
        }
        double sz1 = sqrt(-roots[0]);
        double sz2 = sqrt(-roots[1]);
        double sz3 = sqrt(roots[2]);

        if (c > 0)
        {
            roots[0] = -sz3 / 2.0;
            roots[1] = (sz1 - sz2) / 2.0;
            roots[2] = sz3 / 2.0;
            roots[3] = (-sz1 - sz2) / 2.0;
            return 0;
        }
        roots[0] = sz3 / 2.0;
        roots[1] = (-sz1 + sz2) / 2.0;
        roots[2] = -sz3 / 2.0;
        roots[3] = (sz1 + sz2) / 2.0;
        return 0;
    }
    if (roots[0] < 0)
        roots[0] = 0;
    double sz1 = sqrt(roots[0]);
    double szr = 0, szi = 0;
    double* ArrSZ = CSqrt(roots[1], roots[2]);
    szr = ArrSZ[0];
    szi = ArrSZ[1];
    if (c > 0)
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

int SolveP4Bi(double* roots, double b, double d) {
    double D = b * b - 4 * d;
    if (D >= 0)
    {
        double sD = sqrt(D);
        double x1 = (-b + sD) / 2;
        double x2 = (-b - sD) / 2;	// x2 <= x1
        if (x2 >= 0)				// 0 <= x2 <= x1, 4 real roots
        {
            double sx1 = sqrt(x1);
            double sx2 = sqrt(x2);
            roots[0] = -sx1;
            roots[1] = sx1;
            roots[2] = -sx2;
            roots[3] = sx2;
            return 4;
        }
        if (x1 < 0)				// x2 <= x1 < 0, two pair of imaginary roots
        {
            double sx1 = sqrt(-x1);
            double sx2 = sqrt(-x2);
            roots[0] = 0.0;
            roots[1] = sx1;
            roots[2] = 0.0;
            roots[3] = sx2;
            return 0;
        }
        // now x2 < 0 <= x1 , two real roots and one pair of imginary root
        double sx1 = sqrt(x1);
        double sx2 = sqrt(-x2);
        roots[0] = -sx1;
        roots[1] = sx1;
        roots[2] = 0.0;
        roots[3] = sx2;
        return 2;
    }
    else {  // if( D < 0 ), two pair of compex roots
        double sD2 = 0.5 * sqrt(-D);
        double* tmp1 = CSqrt(-0.5 * b, sD2);
        roots[0] = tmp1[0];
        roots[1] = tmp1[1];
        double* tmp2 = CSqrt(-0.5 * b, -sD2);
        roots[2] = tmp2[0];
        roots[3] = tmp2[1];
        return 0;
    }
}

double N4Step(double x, double a, double b, double c, double d) {
    double fxs = ((4 * x + 3 * a) * x + 2 * b) * x + c;	// f'(x)
    if (fxs == 0)
        return x;
    double fx = (((x + a) * x + b) * x + c) * x + d;	// f(x)
    return x - fx / fxs;
}

double*  CSqrt(double x, double y) { // returns:  a+i*s = sqrt(x+i*y)
    double a = 0, b = 0;
    double r = sqrt(x * x + y * y);
    if (y == 0) {
        r = sqrt(r);
        if (x >= 0) {
            a = r;
            b = 0;
        }
        else {
            a = 0;
            b = r;
        }
    }
    else {		// y != 0
        a = sqrt(0.5 * (x + r));
        b = 0.5 * y / a;
    }
    return new double[2]{a, b};
}

void  dblSort3(double a, double b, double c) { // make: a <= b <= c
    if (a > b)
        std::swap(a, b);
    if (c < b) {
        std::swap(b, c);
        if (a > b)
            std::swap(a, b);
    }
}

std::string SolvePentaEquation(double a, double b, double c, double d, double e, double f) {
    double root = SolveP5_1(b / a, c / a, d / a, e / a, f / a);
    double b1 = b / a + root, c1 = c / a + root * b1, d1 = d / a + root * c1, e1 = e / a + root * d1;
    return std::to_string(root) + " " + SolveQuadraticEquation(1, b1, c1, d1, e1);
}

double FunctionF5(double x, double a, double b, double c, double d, double e) {
    return ((((x + a) * x + b) * x + c) * x + d) * x + e;
}

double SolveP5_1(double a, double b, double c, double d, double e)
{
    int cnt;
    if (abs(e) < UtilFunctions::GetDelta()) return 0;

    double brd = abs(a);
    if (abs(b) > brd)
        brd = abs(b);
    if (abs(c) > brd)
        brd = abs(c);
    if (abs(d) > brd)
        brd = abs(d);
    if (abs(e) > brd)
        brd = abs(e);
    brd++;

    double x0, f0;
    double x1, f1;
    double x2, f2, f2s;
    double dx = 0;

    if (e < 0) {
        x0 = 0;
        x1 = brd;
        f0 = e;
        f1 = FunctionF5(x1, a, b, c, d, e);
        x2 = 0.01 * brd;
    }
    else {
        x0 = -brd;
        x1 = 0;
        f0 = FunctionF5(x0, a, b, c, d, e);
        f1 = e;
        x2 = -0.01 * brd;
    }

    if (abs(f0) < UtilFunctions::GetDelta())
        return x0;
    if (abs(f1) < UtilFunctions::GetDelta())
        return x1;

    for (cnt = 0; cnt < 10; cnt++)
    {
        x2 = (x0 + x1) / 2;
        f2 = FunctionF5(x2, a, b, c, d, e);
        if (abs(f2) < UtilFunctions::GetDelta())
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
        if (cnt++ > 50)
            break;
        if (x2 <= x0 || x2 >= x1)
            x2 = (x0 + x1) / 2.0;
        f2 = FunctionF5(x2, a, b, c, d, e);
        if (abs(f2) < UtilFunctions::GetDelta())
            return x2;
        if (f2 > 0) {
            x1 = x2;
            f1 = f2;
        }
        else {
            x0 = x2;
            f0 = f2;
        }
        f2s = (((5 * x2 + 4 * a) * x2 + 3 * b) * x2 + 2 * c) * x2 + d;
        if (abs(f2s) < UtilFunctions::GetDelta()) {
            x2 = 1e99;
            continue;
        }
        dx = f2 / f2s;
        x2 -= dx;
    } while (abs(dx) > UtilFunctions::GetDelta());
    return x2;
}