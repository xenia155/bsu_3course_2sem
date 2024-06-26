#define _USE_MATH_DEFINES
#include <string>
#include <exception>
#include <iostream>
#include <stdexcept>
#include <limits>
#include <math.h>
#include <chrono>
#include <algorithm>
#include "../includeC/by_quantumquartet_quanthink_cmath_NativeMath_BasicArithmetic.h"
#include "../includeC/by_quantumquartet_quanthink_cmath_NativeMath_UtilFunctions.h"

const char order[] = { 'e','(', '^', '*', '+'};

JNIEXPORT jobject JNICALL Java_by_quantumquartet_quanthink_cmath_NativeMath_solveExpressionC
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
        result = SolveExpression(ret);
        } catch(ThrownJavaException* e) { //do not let C++ exceptions outside of this function
        std::string info = e->what();
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
      jstring s = env->NewStringUTF(result.c_str());
      return env->NewObject(cc, cnstrctrTimeC, s, (jlong)duration);
  }

std::string SolveExpression(std::string expr) {
    std::string tmp = expr;

    if (tmp == "")
        return "0";
    for (int i = 0; i < (sizeof(order) / sizeof(*order)); i++) {
    char item = order[i];
        switch (item) {
        case 'e':
            tmp = UtilFunctions::ConvertConstToValues(tmp);
            tmp = UtilFunctions::CheckFloatPoints(tmp);
            tmp = UtilFunctions::CheckMulBrackets(tmp);
            break;
        case '(':
            while (true) {
                int begin = -1;
                int end = -1;
                for (int i = 0; i < tmp.length(); i++) {
                    if (tmp[i] == '(') {
                        begin = i;
                        continue;
                    }
                    if (tmp[i] == ')') {
                        end = i;
                        break;
                    }
                }
                if ((begin != -1 && end == -1) || (begin == -1 && end != -1))
                    throw new ThrownJavaException("Incorrect brackets count|IllegalArgumentException");
                if (begin == end)
                    break;
                else
                    tmp = tmp.substr(0, begin) + SolveExpression(tmp.substr(begin + 1, end - (begin + 1)))
                    + tmp.substr(end + 1);
            }
            break;
        case '^':
            tmp = SolvePow(tmp);
            break;
        case '*':
            tmp = SolveMulDiv(tmp);
            break;
        case '+':
            tmp = SolveSumSub(tmp);
            break;
        }
        tmp = UtilFunctions::ReduceSumSub(tmp);
    }
    return UtilFunctions::FloatRemover(tmp);
}


std::string SolveMulDiv(std::string expr) {
    std::string tmp = expr;
    while (true)
    {
        int ind_operation_begin = -1;
        int ind_operation_end = -1;
        int check_mul_del = -1;
        for (int i = 0; i < tmp.length(); i++) {
            if (tmp[i] == '+' || tmp[i] == '-')
            {
                ind_operation_begin = i;
                continue;
            }
            if (tmp[i] == '*' || tmp[i] == '/') {
                check_mul_del = i;
                break;
            }

        }
        if (check_mul_del == -1)
            break;
        else {
            for (int i = check_mul_del + 1; i < tmp.length(); i++) {
                if (tmp[i] == '+' || tmp[i] == '-' || tmp[i] == '*' || tmp[i] == '/') {
                    if (i - 1 == check_mul_del && !(tmp[i] == '*' || tmp[i] == '/'))
                        continue;
                    ind_operation_end = i;
                    break;
                }
            }
            if (ind_operation_end == -1)
                ind_operation_end = tmp.length();
            if (ind_operation_end == check_mul_del + 1 || ind_operation_begin == check_mul_del - 1)
                throw new ThrownJavaException("Error|ArithmeticException");
            double left = std::stod(tmp.substr(ind_operation_begin + 1, check_mul_del - (ind_operation_begin + 1)));
            double right = std::stod(tmp.substr(check_mul_del + 1, ind_operation_end - (check_mul_del + 1)));
            if (tmp[check_mul_del] == '*')
            {

                if ( abs(left * right) - 1 > (double)std::numeric_limits<int>::max())
                    throw new ThrownJavaException("Stack overflow|StackOverflowError");
                tmp = tmp.substr(0, ind_operation_begin + 1) + std::to_string(left * right)
                    + tmp.substr(ind_operation_end);
            }
            else {
                if (right == 0)
                    throw new ThrownJavaException("Can't divide by zero|ArithmeticException");
                if (abs(left / right) - 1 > (double)std::numeric_limits<int>::max())
                    throw new ThrownJavaException("Stack overflow|StackOverflowError");
                tmp = tmp.substr(0, ind_operation_begin + 1) + std::to_string(left / right)
                    + tmp.substr(ind_operation_end);

            }

        }
    }
    return tmp;
}

std::string SolvePow(std::string expr) {
    std::string tmp = expr;
    while (true)
    {
        int ind_operation_begin = -1;
        int ind_operation_end = -1;
        int check_pow = -1;
        for (int i = 0; i < tmp.length(); i++) {
            if (tmp[i] == '+' || tmp[i] == '-' || tmp[i] == '*' || tmp[i] == '/')
            {
                ind_operation_begin = i;
                continue;
            }
            if (tmp[i] == '^') {
                check_pow = i;
                break;
            }

        }
        if (check_pow == -1)
            break;
        else {
            for (int i = check_pow + 1; i < tmp.length(); i++) {
                if (tmp[i] == '+' || tmp[i] == '-' || tmp[i] == '*' || tmp[i] == '/' || tmp[i] == '^')
                {
                    if ((tmp[i] == '+' || tmp[i] == '-') && i - 1 == check_pow)
                        continue;
                    ind_operation_end = i;
                    break;
                }
            }
            if (ind_operation_end == -1)
                ind_operation_end = tmp.length();
            if (ind_operation_end == check_pow + 1 || ind_operation_begin == check_pow - 1)
                throw new ThrownJavaException("Error|ArithmeticException");
            double basis = std::stod(tmp.substr(ind_operation_begin + 1, check_pow - (ind_operation_begin + 1)));
            double degree = std::stod(tmp.substr(check_pow + 1, ind_operation_end - (check_pow + 1)));
            if (abs(pow(basis, degree)) - 1 > std::numeric_limits<int>::max())
                throw new ThrownJavaException("Stack overflow|StackOverflowError");
            tmp = tmp.substr(0, ind_operation_begin + 1) + std::to_string(pow(basis, degree))
                + tmp.substr(ind_operation_end);
        }
    }
    return tmp;
}

std::string SolveSumSub(std::string expr) {
    std::string tmp = expr;
    while (true)
    {
        int ind_operation_begin = 0;
        int ind_operation_end = -1;
        int check_sum_sub = -1;
        for (int i = 0; i < tmp.length(); i++) {
            if (tmp[i] == '+' || tmp[i] == '-')
            {
                if (i == 0)
                    continue;
                check_sum_sub = i;
                break;
            }

        }
        if (check_sum_sub == -1)
            break;
        else {
            for (int i = check_sum_sub + 1; i < tmp.length(); i++) {
                if ((tmp[i] == '+' || tmp[i] == '-')) {
                    if (i - 1 == check_sum_sub)
                        continue;
                    ind_operation_end = i;
                    break;
                }
            }
            if (ind_operation_end == -1)
                ind_operation_end = tmp.length();
            double left = std::stod(tmp.substr(ind_operation_begin, check_sum_sub - ind_operation_begin));
            double right = std::stod(tmp.substr(check_sum_sub + 1, ind_operation_end - (check_sum_sub + 1)));
            if (tmp[check_sum_sub] == '+')
            {
                if (abs(left + right) - 1 > std::numeric_limits<int>::max())
                    throw new ThrownJavaException("Stack overflow|StackOverflowError");
                tmp = tmp.substr(0, ind_operation_begin) + std::to_string(left + right)
                    + tmp.substr(ind_operation_end);
            }
            else {
                if (abs(left - right) - 1 > std::numeric_limits<int>::max())
                    throw new ThrownJavaException("Stack overflow|StackOverflowError");
                tmp = tmp.substr(0, ind_operation_begin) + std::to_string(left - right)
                    + tmp.substr(ind_operation_end);
            }

        }
    }
    return tmp;
}
