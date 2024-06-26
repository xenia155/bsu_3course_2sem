#define _USE_MATH_DEFINES
#include "../includeC/by_quantumquartet_quanthink_cmath_NativeMath_UtilFunctions.h"

bool UtilFunctions::CheckDouble(double value) {
    return abs(round(value) - value) > GetDelta();
}

std::string UtilFunctions::FloatRemover(std::string value) {
    if (CheckDouble(std::stod(value)))
        return std::to_string(std::stod(value));
    else
        return std::to_string((int)round(std::stod(value)));
}

double UtilFunctions::GetDelta() {
    return 1e-8;
}

std::string UtilFunctions::CheckMulBrackets(std::string expr) {
    std::string tmp = expr;
    int previous = -1;
    while (true)
    {
        int index = tmp.find('(', previous + 1);
        if (index == -1)
            break;
        if (index != 0 && std::isdigit(tmp[index - 1]))
            tmp = tmp.substr(0, index) + "*" + tmp.substr(index);
        previous = index + 1;
    }
    previous = -1;
    while (true)
    {
        int index = tmp.find(')', previous + 1);
        if (index == -1)
            break;
        if (index != tmp.length() - 1 && (std::isdigit(tmp[index + 1]) || tmp[index + 1] == '('))
            tmp = tmp.substr(0, index + 1) + "*" + tmp.substr(index + 1);
        previous = index + 2;
    }
    return tmp;
}

std::string UtilFunctions::ConvertConstToValues(std::string expr) {
    std::string tmp = expr;
    while (true)
    {
        int index = tmp.find('p');
        if (index == -1)
            break;
        tmp = tmp.substr(0, index) + std::to_string(M_PI) + tmp.substr(index + 1);
    }
    while (true)
    {
        int index = tmp.find('e');
        if (index == -1)
            break;
        tmp = tmp.substr(0, index) + std::to_string(M_E) + tmp.substr(index + 1);
    }
    return tmp;
}



std::string UtilFunctions::ReduceSumSub(std::string expr) {
    std::string tmp = expr;
    while (true) {
        int ind = tmp.find("--");
        if (ind == -1)
            break;
        tmp = tmp.substr(0, ind) + "+" + tmp.substr(ind + 2);
    }
    while (true) {
        int ind = tmp.find("++");
        if (ind == -1)
            break;
        tmp = tmp.substr(0, ind) + "+" + tmp.substr(ind + 2);
    }
    while (true) {
        int ind = tmp.find("-+");
        if (ind == -1)
            break;
        tmp = tmp.substr(0, ind) + "-" + tmp.substr(ind + 2);
    }
    while (true) {
        int ind = tmp.find("+-");
        if (ind == -1)
            break;
        tmp = tmp.substr(0, ind) + "-" + tmp.substr(ind + 2);
    }
    if (tmp[0] == '+')
        tmp = tmp.substr(1);
    return tmp;
}

std::string UtilFunctions::CheckFloatPoints(std::string expr) {
    std::string tmp = expr;
    int previous = -1;
    while (true)
    {
        int index = tmp.find('.', previous + 1);
        if (index == -1)
            break;
        if (index == tmp.length() - 1 || !std::isdigit(tmp[index + 1]))
            tmp = tmp.substr(0, index + 1) + '0' + tmp.substr(index + 1);
        previous = index + 1;
    }
    previous = -1;
    while (true)
    {
        int index = tmp.find('.', previous + 1);
        if (index == -1)
            break;
        if (index == 0)
            tmp = '0' + tmp.substr(index);
        else if (!std::isdigit(tmp[index - 1]))
            tmp = tmp.substr(0, index) + '0' + tmp.substr(index);
        previous = index + 1;
    }
    previous = tmp.find('.');
    while (true)
    {
        int index = tmp.find('.', previous + 1);
        if (index == -1)
            break;
        bool check = false;
        for (int i = previous + 1; i < index; i++) {
            if (!std::isdigit(tmp[i]))
            {
                check = true;
                break;
            }
        }
        if (!check)
            throw new ThrownJavaException("Error|IllegalArgumentException");
        previous = index;
    }
    return tmp;
}

std::string UtilFunctions::SimplifyMul(std::string expr) {
    std::string tmp = expr;
    int previous = -1;
    while (true)
    {
        int index = tmp.find('x', previous + 1);
        if (index == -1)
            break;
        if (index != 0 && tmp[index - 1] == '*')
            tmp = tmp.substr(0, index - 1) + tmp.substr(index);
        else if (index != tmp.length() - 1 && tmp[index + 1] == '*')
        {
            int end = tmp.length();
            for (int i = index + 2; i < tmp.length(); i++)
                if (!std::isdigit(tmp[i]))
                {
                    end = i;
                    break;
                }
            if (index > 0)
                tmp = tmp.substr(0, index - 1) + tmp.substr(index + 2, end - (index + 2)) + tmp.substr(end);
            else
                tmp = tmp.substr(index + 2, end - (index + 2)) + tmp.substr(index, 1) + tmp.substr(end);
        }

        previous = index + 1;
    }
    return tmp;
}


std::string UtilFunctions::AddOneBeforeX(std::string expr) {
    std::string tmp = expr;
    int previous = -1;
    while (true) {
        int index = tmp.find("x", previous + 1);
        if (index == -1)
            break;
        if (index == 0 || tmp[index - 1] == '-' || tmp[index - 1] == '+')
            tmp = tmp.substr(0, index) + "1" + tmp.substr(index);
        previous = index;
    }
    return tmp;
}

double UtilFunctions::Determinant(double** A, int n) {
    double res;

    if (n == 1)
        res = A[0][0];
    else if (n == 2)
        res = A[0][0] * A[1][1] - A[1][0] * A[0][1];
    else {
        res = 0;
        for (int i = 0; i < n; i++) {
            double** subArray = GenerateSubArray(A, n, i);
            res += pow(-1.0, 1.0 + i + 1.0) * A[0][i] * Determinant(subArray, n - 1);
        }
    }
    return res;
}

double** UtilFunctions::GenerateSubArray(double** A, int n, int j1) {
    double** m = new double*[n - 1];
    for (int k = 0; k < (n - 1); k++)
        m[k] = new double[n - 1];

    for (int i = 1; i < n; i++) {
        int j2 = 0;
        for (int j = 0; j < n; j++) {
            if (j == j1)
                continue;
            m[i - 1][j2] = A[i][j];
            j2++;
        }
    }
    return m;
}