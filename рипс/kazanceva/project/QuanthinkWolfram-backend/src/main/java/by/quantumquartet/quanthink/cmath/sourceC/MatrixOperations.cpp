#include "../includeC/by_quantumquartet_quanthink_cmath_NativeMath_MatrixOperations.h"
#include "../includeC/by_quantumquartet_quanthink_cmath_NativeMath_ThreadPool.h"
#include <string>
/*
 * Class:     by_quantumquartet_quanthink_cmath_NativeMath
 * Method:    MatrixSumC
 * Signature: (Lby/quantumquartet/quanthink/cmath/MatrixC;Lby/quantumquartet/quanthink/cmath/MatrixC;I)Lby/quantumquartet/quanthink/cmath/TimeStruct;
 */
JNIEXPORT jobject JNICALL Java_by_quantumquartet_quanthink_cmath_NativeMath_MatrixSumC
  (JNIEnv *env, jobject, jobject m1, jobject m2, jint threads)
  {
      auto begin = std::chrono::steady_clock::now();
      jmethodID cnstrctr;
      jclass c = env->FindClass("by/quantumquartet/quanthink/cmath/MatrixC");
      cnstrctr = env->GetMethodID(c, "<init>", "(II[[D)V");

      Matrix mC1 = JavaMatrixToCMatrix(env, m1);
      Matrix mC2 = JavaMatrixToCMatrix(env, m2);

      int* size1 = mC1.getSize();
      int* size2 = mC2.getSize();
      double** new_data = new double*[size1[0]];
      for (int i = 0; i < (sizeof(size2)/sizeof(*size2)); i++)
          if (size1[i] != size2[i])
          {
              std::string ex_type = "java/lang/IllegalArgumentException";
              NewJavaException(env, ex_type.c_str(), "Invalid matrixes sizes");
          }
      if (threads > 1){
          ThreadPool pool(threads);
          for (int i = 0; i < size1[0]; ++i) {
                  double* rM1 = mC1.getRow(i);
                  double* rM2 = mC2.getRow(i);
                  new_data[i] = new double[size2[1]];
                  pool.enqueue([&new_data,i,size2,rM1,rM2] {
                      for (int j = 0; j < size2[1]; j++)
                        new_data[i][j] = rM1[j] + rM2[j];
                  });
          }
      }
      else {
          for (int i = 0; i < size2[0]; i++){
            new_data[i] = new double[size1[1]];
            for (int j = 0; j < size2[1]; j++)
             new_data[i][j] = mC1.getElement(i,j) + mC2.getElement(i, j);
          }


      }
      jclass doubleArrayClass = env->FindClass("[D");

      // Create the returnable 2D array
      jobjectArray java_new_data_arr = env->NewObjectArray((jsize) size2[0], doubleArrayClass, NULL);
      for (unsigned int i = 0; i < size2[0]; i++)
          {
              jdoubleArray doubleArray = env->NewDoubleArray(size2[1]);
              env->SetDoubleArrayRegion(doubleArray, (jsize) 0, (jsize) size2[0], (jdouble*) new_data[i]);
              env->SetObjectArrayElement(java_new_data_arr, (jsize) i, doubleArray);
              env->DeleteLocalRef(doubleArray);
          }
      auto end = std::chrono::steady_clock::now();
      auto elapsed_ms = std::chrono::duration_cast<std::chrono::milliseconds>(end - begin);
      long duration = elapsed_ms.count();

      jobject ob = env->NewObject(c, cnstrctr,mC1.getSize()[0],mC1.getSize()[1], java_new_data_arr);
      env->DeleteLocalRef(c);
      jmethodID cnstrctrTimeC;
      jclass cc = env->FindClass("by/quantumquartet/quanthink/cmath/TimeStruct");
      cnstrctrTimeC = env->GetMethodID(cc, "<init>", "(Lby/quantumquartet/quanthink/cmath/MatrixC;J)V");
      return env->NewObject(cc, cnstrctrTimeC, ob, (jlong)duration);
  }


/*
 * Class:     by_quantumquartet_quanthink_cmath_NativeMath
 * Method:    MatrixSubC
 * Signature: (Lby/quantumquartet/quanthink/cmath/MatrixC;Lby/quantumquartet/quanthink/cmath/MatrixC;I)Lby/quantumquartet/quanthink/cmath/TimeStruct;
 */
JNIEXPORT jobject JNICALL Java_by_quantumquartet_quanthink_cmath_NativeMath_MatrixSubC
  (JNIEnv *env, jobject, jobject m1, jobject m2, jint threads)
  {
          auto begin = std::chrono::steady_clock::now();
          jmethodID cnstrctr;
          jclass c = env->FindClass("by/quantumquartet/quanthink/cmath/MatrixC");
          cnstrctr = env->GetMethodID(c, "<init>", "(II[[D)V");

          Matrix mC1 = JavaMatrixToCMatrix(env, m1);
          Matrix mC2 = JavaMatrixToCMatrix(env, m2);

          int* size1 = mC1.getSize();
          int* size2 = mC2.getSize();
          double** new_data = new double*[size1[0]];
          for (int i = 0; i < (sizeof(size2)/sizeof(*size2)); i++)
              if (size1[i] != size2[i])
              {
                    std::string ex_type = "java/lang/IllegalArgumentException";
                    NewJavaException(env, ex_type.c_str(), "Invalid matrixes sizes");
              }
          if (threads > 1){
              ThreadPool pool(threads);
              for (int i = 0; i < size1[0]; ++i) {
                      double* rM1 = mC1.getRow(i);
                      double* rM2 = mC2.getRow(i);
                      new_data[i] = new double[size2[1]];
                      pool.enqueue([&new_data,i,size2,rM1,rM2] {
                          for (int j = 0; j < size2[1]; j++)
                            new_data[i][j] = rM1[j] - rM2[j];
                      });
              }
          }
          else {
              for (int i = 0; i < size2[0]; i++){
                new_data[i] = new double[size1[1]];
                for (int j = 0; j < size2[1]; j++)
                 new_data[i][j] = mC1.getElement(i,j) - mC2.getElement(i, j);
              }


          }
          jclass doubleArrayClass = env->FindClass("[D");

          // Create the returnable 2D array
          jobjectArray java_new_data_arr = env->NewObjectArray((jsize) size2[0], doubleArrayClass, NULL);
          for (unsigned int i = 0; i < size2[0]; i++)
              {
                  jdoubleArray doubleArray = env->NewDoubleArray(size2[1]);
                  env->SetDoubleArrayRegion(doubleArray, (jsize) 0, (jsize) size2[0], (jdouble*) new_data[i]);
                  env->SetObjectArrayElement(java_new_data_arr, (jsize) i, doubleArray);
                  env->DeleteLocalRef(doubleArray);
              }
           auto end = std::chrono::steady_clock::now();
            auto elapsed_ms = std::chrono::duration_cast<std::chrono::milliseconds>(end - begin);
            long duration = elapsed_ms.count();
            jmethodID cnstrctrTimeC;
            jclass cc = env->FindClass("by/quantumquartet/quanthink/cmath/TimeStruct");
            cnstrctrTimeC = env->GetMethodID(cc, "<init>", "(Lby/quantumquartet/quanthink/cmath/MatrixC;J)V");
            return env->NewObject(cc, cnstrctrTimeC, env->NewObject(c, cnstrctr,mC1.getSize()[0],mC1.getSize()[1], java_new_data_arr), (jlong)duration);
  }

/*
 * Class:     by_quantumquartet_quanthink_cmath_NativeMath
 * Method:    MatrixMulC
 * Signature: (Lby/quantumquartet/quanthink/cmath/MatrixC;Lby/quantumquartet/quanthink/cmath/MatrixC;I)Lby/quantumquartet/quanthink/cmath/TimeStruct;
 */
JNIEXPORT jobject JNICALL Java_by_quantumquartet_quanthink_cmath_NativeMath_MatrixMulC__Lby_quantumquartet_quanthink_cmath_MatrixC_2Lby_quantumquartet_quanthink_cmath_MatrixC_2I
  (JNIEnv *env, jobject, jobject m1, jobject m2 , jint threads)
  {
      auto begin = std::chrono::steady_clock::now();
      jmethodID cnstrctr;
      jclass c = env->FindClass("by/quantumquartet/quanthink/cmath/MatrixC");
      cnstrctr = env->GetMethodID(c, "<init>", "(II[[D)V");

      Matrix mC1 = JavaMatrixToCMatrix(env, m1);
      Matrix mC2 = JavaMatrixToCMatrix(env, m2);

      int* size1 = mC1.getSize();
      int* size2 = mC2.getSize();
      double** new_data = new double*[size1[0]];
      if (size1[1] != size2[0])
      {
        std::string ex_type = "java/lang/IllegalArgumentException";
        NewJavaException(env, ex_type.c_str(), "Invalid matrixes sizes");
      }
      if (threads > 1){
          ThreadPool pool(threads);
          for (int i = 0; i < size1[0]; ++i) {
                  double* rM1 = mC1.getRow(i);
                  new_data[i] = new double[size2[1]];
                  for (int j = 0; j < size2[1]; j++)
                  {
                    double* cM2 = mC2.getColumn(j);
                    pool.enqueue([&new_data,i,j,size2,rM1,cM2] {
                          double sum = 0;
                          for (int q = 0; q < size2[0]; q++)
                            sum += rM1[q] * cM2[q];
                            new_data[i][j] = sum;
                      });
                  }

          }
      }
      else {
          for (int i = 0; i < size1[0]; i++){
            new_data[i] = new double[size2[1]];
            double* row = mC1.getRow(i);
            for (int j = 0; j < size2[1]; j++)
             {
                double* column = mC2.getColumn(j);
                double sum = 0;
                for (int q = 0; q < size2[0]; q++)
                    sum += row[q] * column[q];

                new_data[i][j] = sum;
             }
          }


      }
      jclass doubleArrayClass = env->FindClass("[D");
      // Create the returnable 2D array
      jobjectArray java_new_data_arr = env->NewObjectArray((jsize) size1[0], doubleArrayClass, NULL);
      for (unsigned int i = 0; i < size1[0]; i++)
          {
              jdoubleArray doubleArray = env->NewDoubleArray(size2[1]);
              env->SetDoubleArrayRegion(doubleArray, (jsize) 0, (jsize) size2[1], (jdouble*) new_data[i]);
              env->SetObjectArrayElement(java_new_data_arr, (jsize) i, doubleArray);
              env->DeleteLocalRef(doubleArray);
          }
      auto end = std::chrono::steady_clock::now();
      auto elapsed_ms = std::chrono::duration_cast<std::chrono::milliseconds>(end - begin);
      long duration = elapsed_ms.count();
      jmethodID cnstrctrTimeC;
      jclass cc = env->FindClass("by/quantumquartet/quanthink/cmath/TimeStruct");
      cnstrctrTimeC = env->GetMethodID(cc, "<init>", "(Lby/quantumquartet/quanthink/cmath/MatrixC;J)V");
      return env->NewObject(cc, cnstrctrTimeC, env->NewObject(c, cnstrctr, size1[0], size2[1], java_new_data_arr), (jlong)duration);
  }

/*
 * Class:     by_quantumquartet_quanthink_cmath_NativeMath
 * Method:    MatrixMulC
 * Signature: (Lby/quantumquartet/quanthink/cmath/MatrixC;DI)Lby/quantumquartet/quanthink/cmath/TimeStruct;
 */
JNIEXPORT jobject JNICALL Java_by_quantumquartet_quanthink_cmath_NativeMath_MatrixMulC__Lby_quantumquartet_quanthink_cmath_MatrixC_2DI
  (JNIEnv *env, jobject, jobject m, jdouble num, jint threads)
  {
          auto begin = std::chrono::steady_clock::now();
          jmethodID cnstrctr;
          jclass c = env->FindClass("by/quantumquartet/quanthink/cmath/MatrixC");
          cnstrctr = env->GetMethodID(c, "<init>", "(II[[D)V");

          Matrix mC1 = JavaMatrixToCMatrix(env, m);

          int* size = mC1.getSize();
          double** new_data = mC1.getData();
          if (threads > 1){
              ThreadPool pool(threads);
              double cnum = (double)num;
              for (int i = 0; i < size[0]; ++i) {
                pool.enqueue([&new_data,i,cnum,size] {
                      for (int j = 0; j < size[1]; j++)
                        new_data[i][j] *= cnum;
                  });
              }
          }
          else {
              for (int i = 0; i < size[0]; i++)
                for (int j = 0; j < size[1]; j++)
                    new_data[i][j] += (double)num;
          }
          jclass doubleArrayClass = env->FindClass("[D");

          // Create the returnable 2D array
          jobjectArray java_new_data_arr = env->NewObjectArray((jsize) size[0], doubleArrayClass, NULL);
          for (unsigned int i = 0; i < size[0]; i++)
              {
                  jdoubleArray doubleArray = env->NewDoubleArray(size[1]);
                  env->SetDoubleArrayRegion(doubleArray, (jsize) 0, (jsize) size[0], (jdouble*) new_data[i]);
                  env->SetObjectArrayElement(java_new_data_arr, (jsize) i, doubleArray);
                  env->DeleteLocalRef(doubleArray);
              }
        auto end = std::chrono::steady_clock::now();
        auto elapsed_ms = std::chrono::duration_cast<std::chrono::milliseconds>(end - begin);
        long duration = elapsed_ms.count();
        jmethodID cnstrctrTimeC;
        jclass cc = env->FindClass("by/quantumquartet/quanthink/cmath/TimeStruct");
        cnstrctrTimeC = env->GetMethodID(cc, "<init>", "(Lby/quantumquartet/quanthink/cmath/MatrixC;J)V");
        return env->NewObject(cc, cnstrctrTimeC, env->NewObject(c, cnstrctr,mC1.getSize()[0],mC1.getSize()[1], java_new_data_arr), (jlong)duration);
  }

/*
 * Class:     by_quantumquartet_quanthink_cmath_NativeMath
 * Method:    GetTransposeMatrix
 * Signature: (Lby/quantumquartet/quanthink/cmath/MatrixC;)Lby/quantumquartet/quanthink/cmath/TimeStruct;
 */
JNIEXPORT jobject JNICALL Java_by_quantumquartet_quanthink_cmath_NativeMath_GetTransposeMatrixC
  (JNIEnv *env, jobject, jobject m)
  {
    auto begin = std::chrono::steady_clock::now();
    jmethodID cnstrctr;
    jclass c = env->FindClass("by/quantumquartet/quanthink/cmath/MatrixC");
    cnstrctr = env->GetMethodID(c, "<init>", "(II[[D)V");

    Matrix mC = JavaMatrixToCMatrix(env, m);
    int* size = mC.getSize();
    double** new_data = new double*[size[1]];
    for (int i = 0 ; i < size[1]; i++){
        new_data[i] = mC.getColumn(i);
    }
    jclass doubleArrayClass = env->FindClass("[D");

              // Create the returnable 2D array
    jobjectArray java_new_data_arr = env->NewObjectArray((jsize) size[0], doubleArrayClass, NULL);
    for (unsigned int i = 0; i < size[0]; i++){
      jdoubleArray doubleArray = env->NewDoubleArray(size[1]);
      env->SetDoubleArrayRegion(doubleArray, (jsize) 0, (jsize) size[0], (jdouble*) new_data[i]);
      env->SetObjectArrayElement(java_new_data_arr, (jsize) i, doubleArray);
      env->DeleteLocalRef(doubleArray);
    }
    auto end = std::chrono::steady_clock::now();
    auto elapsed_ms = std::chrono::duration_cast<std::chrono::milliseconds>(end - begin);
    long duration = elapsed_ms.count();
    jmethodID cnstrctrTimeC;
    jclass cc = env->FindClass("by/quantumquartet/quanthink/cmath/TimeStruct");
    cnstrctrTimeC = env->GetMethodID(cc, "<init>", "(Lby/quantumquartet/quanthink/cmath/MatrixC;J)V");
    return env->NewObject(cc, cnstrctrTimeC, env->NewObject(c, cnstrctr, mC.getSize()[0],mC.getSize()[1], java_new_data_arr), (jlong)duration);
  }

/*
 * Class:     by_quantumquartet_quanthink_cmath_NativeMath
 * Method:    GetReverseMatrix
 * Signature: (Lby/quantumquartet/quanthink/cmath/MatrixC;)Lby/quantumquartet/quanthink/cmath/TimeStruct;
 */
JNIEXPORT jobject JNICALL Java_by_quantumquartet_quanthink_cmath_NativeMath_GetReverseMatrixC
  (JNIEnv *env, jobject, jobject m)
  {
       auto begin = std::chrono::steady_clock::now();
      jmethodID cnstrctr;
      jclass c = env->FindClass("by/quantumquartet/quanthink/cmath/MatrixC");
      cnstrctr = env->GetMethodID(c, "<init>", "(II[[D)V");

      Matrix mC = JavaMatrixToCMatrix(env, m);
      int* size = mC.getSize();
      if (size[0] != size[1])
      {
            std::string ex_type = "java/lang/IllegalArgumentException";
            NewJavaException(env, ex_type.c_str(), "Matrix must be square!");
      }
      double tmp;
      double** A = mC.getData();
      double** E = new double*[size[0]];
      for (int i = 0; i < size[0]; i++)
          E[i] = new double[size[0]];
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
      jclass doubleArrayClass = env->FindClass("[D");

                // Create the returnable 2D array
      jobjectArray java_new_data_arr = env->NewObjectArray((jsize) size[0], doubleArrayClass, NULL);
      for (unsigned int i = 0; i < size[0]; i++){
        jdoubleArray doubleArray = env->NewDoubleArray(size[1]);
        env->SetDoubleArrayRegion(doubleArray, (jsize) 0, (jsize) size[0], (jdouble*) A[i]);
        env->SetObjectArrayElement(java_new_data_arr, (jsize) i, doubleArray);
        env->DeleteLocalRef(doubleArray);
      }
      auto end = std::chrono::steady_clock::now();
      auto elapsed_ms = std::chrono::duration_cast<std::chrono::milliseconds>(end - begin);
      long duration = elapsed_ms.count();
      jmethodID cnstrctrTimeC;
      jclass cc = env->FindClass("by/quantumquartet/quanthink/cmath/TimeStruct");
      cnstrctrTimeC = env->GetMethodID(cc, "<init>", "(Lby/quantumquartet/quanthink/cmath/MatrixC;J)V");
      jobject m_r =  env->NewObject(c, cnstrctr, mC.getSize()[0],mC.getSize()[1], java_new_data_arr);
      jobject res = env->NewObject(cc, cnstrctrTimeC, m_r, (jlong)duration);
      return env->NewObject(cc, cnstrctrTimeC, m_r, (jlong)duration);
  }

/*
 * Class:     by_quantumquartet_quanthink_cmath_NativeMath
 * Method:    GetDeterminantC
 * Signature: (Lby/quantumquartet/quanthink/cmath/MatrixC;I)D
 */
JNIEXPORT jobject JNICALL Java_by_quantumquartet_quanthink_cmath_NativeMath_GetDeterminantC
  (JNIEnv *env, jobject, jobject m, jint threads)
  {
    auto begin = std::chrono::steady_clock::now();

           Matrix mC = JavaMatrixToCMatrix(env, m);
           int* size = mC.getSize();

          if (mC.getSize()[0] != mC.getSize()[1]){
                 std::string ex_type = "java/lang/IllegalArgumentException";
                 NewJavaException(env, ex_type.c_str(), "Matrix must be square!");
             }
             jdouble res;
             if (threads == 1)
             {
                 res = 0.0;
                 for (int i = 0; i < mC.getSize()[0]; i++){
                     double** subArray = UtilFunctions::GenerateSubArray (mC.getData(), mC.getSize()[0], i);
                     res += pow(-1.0, 1.0 + i + 1.0) * mC.getElement(0,i)
                             * UtilFunctions::Determinant(subArray, mC.getSize()[0] - 1);
                 }
             }
             else {
                 res = 0.0;
                 double** data = mC.getData();
                 ThreadPool pool(threads);
                   int s1=size[0];
                   for (int i = 0; i < size[0]; i++) {
                     pool.enqueue([&data,s1,i,&res] {
                            double** subArray = UtilFunctions::GenerateSubArray(data, s1, i);
                            res += pow(-1.0, 1.0 + i + 1.0) * data[0][i] * UtilFunctions::Determinant(subArray, s1 - 1);
                       });
                   }
             }
        auto end = std::chrono::steady_clock::now();
        auto elapsed_ms = std::chrono::duration_cast<std::chrono::milliseconds>(end - begin);
        long duration = elapsed_ms.count();
        jmethodID cnstrctrTimeC;
        jclass cc = env->FindClass("by/quantumquartet/quanthink/cmath/TimeStruct");
        cnstrctrTimeC = env->GetMethodID(cc, "<init>", "(DJ)V");
        return env->NewObject(cc, cnstrctrTimeC, res, (jlong)duration);
  }

/*
 * Class:     by_quantumquartet_quanthink_cmath_NativeMath
 * Method:    SolveSystemC
 * Signature: (Lby/quantumquartet/quanthink/cmath/MatrixC;Lby/quantumquartet/quanthink/cmath/MatrixC;I)Lby/quantumquartet/quanthink/cmath/TimeStruct;
 */
JNIEXPORT jobject JNICALL Java_by_quantumquartet_quanthink_cmath_NativeMath_SolveSystemC
  (JNIEnv *env, jobject y, jobject A, jobject f, jint threads)
  {
    auto begin = std::chrono::steady_clock::now();
    jmethodID cnstrctr;
      jmethodID cnstrctrTimeC;
      jclass cc = env->FindClass("by/quantumquartet/quanthink/cmath/TimeStruct");
      cnstrctrTimeC = env->GetMethodID(cc, "<init>", "(Ljava/lang/String;J)V");
      jclass c = env->FindClass("by/quantumquartet/quanthink/cmath/MatrixC");
      cnstrctr = env->GetMethodID(c, "<init>", "(II[[D)V");
      jmethodID getMatrixFromTS = env->GetMethodID(cc, "getMatrixResult", "()Lby/quantumquartet/quanthink/cmath/MatrixC;");
      Matrix AC = JavaMatrixToCMatrix(env, A);
      Matrix fC = JavaMatrixToCMatrix(env, f);
      if (AC.getSize()[0] != AC.getSize()[1])
      {
            std::string ex_type = "java/lang/IllegalArgumentException";
            NewJavaException(env, ex_type.c_str(), "Matrix must be square!");
      }
      if (AC.getSize()[0] != fC.getSize()[0])
      {
            std::string ex_type = "java/lang/IllegalArgumentException";
            NewJavaException(env, ex_type.c_str(), "Both matrix and vector must have the same numbers of rows!");
      }
      if (fC.getSize()[1] != 1)
      {
             std::string ex_type = "java/lang/IllegalArgumentException";
             NewJavaException(env, ex_type.c_str(), "The second argument isn't vertical vector!");
      }
      jobject TS_Reversed_M = Java_by_quantumquartet_quanthink_cmath_NativeMath_GetReverseMatrixC(env,y, A);
      jobject Reversed_M = env->CallObjectMethod(TS_Reversed_M, getMatrixFromTS);
      jobject TS_answer = Java_by_quantumquartet_quanthink_cmath_NativeMath_MatrixMulC__Lby_quantumquartet_quanthink_cmath_MatrixC_2Lby_quantumquartet_quanthink_cmath_MatrixC_2I(env, y, Reversed_M, f, threads);
      jobject answer = env->CallObjectMethod(TS_answer, getMatrixFromTS);
      Matrix answerC = JavaMatrixToCMatrix(env, answer);
      std::string str = "";
      for (int i = 0; i < answerC.getSize()[0]; i++)
          str.append(UtilFunctions::FloatRemover(std::to_string(answerC.getElement(i, 0)))).append(" ");
      str.pop_back();
      auto end = std::chrono::steady_clock::now();
      auto elapsed_ms = std::chrono::duration_cast<std::chrono::milliseconds>(end - begin);
      long duration = elapsed_ms.count();

      jstring s = env->NewStringUTF(str.c_str());
      return env->NewObject(cc, cnstrctrTimeC, s, (jlong)duration);
  }

Matrix JavaMatrixToCMatrix(JNIEnv *env,  jobject m){
      jmethodID getData;
      jmethodID getSize;
      jclass c = env->FindClass("by/quantumquartet/quanthink/cmath/MatrixC");
      getData = env->GetMethodID(c, "getData", "()[[D");
      getSize = env->GetMethodID(c, "getSize", "()[I");
      jobject sizes = env->CallObjectMethod(m, getSize);
      jintArray SizesArr = reinterpret_cast<jintArray>(sizes);
      jint* sizeData = env->GetIntArrayElements(SizesArr, 0);
      int rows = (int)sizeData[0];
      int cols = (int)sizeData[1];
      jobject mvdata = env->CallObjectMethod(m, getData);
            // Cast it to a jdoublearray
      jobjectArray* arr = reinterpret_cast<jobjectArray*>(&mvdata);
      jarray jniarray;
      double** data = new double*[rows];
      for (jint i = 0; i < rows; i++)
      {
          jniarray = (jdoubleArray)env->GetObjectArrayElement(*arr, i);
          jdouble*coldata = env->GetDoubleArrayElements((jdoubleArray)jniarray, nullptr);
          data[i]= new double[cols];
          for (jint j = 0; j < cols; j++)
          {
              data[i][j]=(double)(coldata[j]);
          }
          env->ReleaseDoubleArrayElements((jdoubleArray)jniarray, coldata, 0);
      }
      Matrix matrix(rows, cols, data);
      return matrix;
}
