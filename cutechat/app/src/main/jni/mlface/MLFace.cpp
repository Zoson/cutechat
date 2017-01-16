#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/ml/ml.hpp>

double inputArr[10][13] =

{
    
    1,0.708333,1,1,-0.320755,-0.105023,-1,1,-0.419847,-1,-0.225806,0,1,
    
    -1,0.583333,-1,0.333333,-0.603774,1,-1,1,0.358779,-1,-0.483871,0,-1,
    
    1,0.166667,1,-0.333333,-0.433962,-0.383562,-1,-1,0.0687023,-1,-0.903226,-1,-1,
    
    -1,0.458333,1,1,-0.358491,-0.374429,-1,-1,-0.480916,1,-0.935484,0,-0.333333,
    
    -1,0.875,-1,-0.333333,-0.509434,-0.347032,-1,1,-0.236641,1,-0.935484,-1,-0.333333,
    
    -1,0.5,1,1,-0.509434,-0.767123,-1,-1,0.0534351,-1,-0.870968,-1,-1,
    
    1,0.125,1,0.333333,-0.320755,-0.406393,1,1,0.0839695,1,-0.806452,0,-0.333333,
    
    1,0.25,1,1,-0.698113,-0.484018,-1,1,0.0839695,1,-0.612903,0,-0.333333,
    
    1,0.291667,1,1,-0.132075,-0.237443,-1,1,0.51145,-1,-0.612903,0,0.333333,
    
    1,0.416667,-1,1,0.0566038,0.283105,-1,1,0.267176,-1,0.290323,0,1
    
};


double testArr[]=

{
    
    0.25,1,1,-0.226415,-0.506849,-1,-1,0.374046,-1,-0.83871,0,-1
    
};


float svm()

{
    
    CvSVM svm;
    
    CvSVMParams param;
    
    param.svm_type = 100;
    
    param.kernel_type = 1;
    
    param.degree = 4;
    
    param.gamma = 4;
    
    param.coef0 = 1;
    
    
    CvMat *dataMat = cvCreateMat(10, 12, CV_32FC1);
    
    CvMat *labelMat = cvCreateMat(10, 1, CV_32SC1);
    
    for (int i=0; i<10; i++)
        
    {
        
        for (int j=0; j<12; j++)
            
        {
            
            cvSetReal2D(dataMat, i, j, inputArr[i][j+1]);
            
        }
        
        cvSetReal2D(labelMat, i, 0, inputArr[i][0]);
        
    }
    
    svm.train(dataMat, labelMat, NULL, NULL, param);
    
    
    CvMat *testMat = cvCreateMat(1, 12, CV_32FC1);
    
    for (int i=0; i<12; i++)
        
    {
        
        cvSetReal2D(testMat, 0, i, testArr[i]);
        
    }
    
    float flag = 0;
    
    flag = svm.predict(testMat);
    
    
    cvReleaseMat(&dataMat);
    
    cvReleaseMat(&labelMat);
    
    cvReleaseMat(&testMat);
    
    return flag;
    
}

