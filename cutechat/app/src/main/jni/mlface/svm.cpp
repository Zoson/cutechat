#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/ml/ml.hpp>

using namespace cv;

int main()
{
    // Data for visual representation
    int width = 512, height = 512;
    Mat image = Mat::zeros(height, width, CV_8UC3);
    
    // Set up training data
    float labels[5] = {1.0, -1.0, -1.0, -1.0,1.0};
    Mat labelsMat(5, 1, CV_32FC1, labels);
    
    
    float trainingData[5][2] = { {501, 10}, {255, 10}, {501, 255}, {10, 501},{501,128} };
    Mat trainingDataMat(5, 2, CV_32FC1, trainingData);
    
    //设置支持向量机的参数
    CvSVMParams params;
    params.svm_type    = CvSVM::C_SVC;//SVM类型：使用C支持向量机
    params.kernel_type = CvSVM::LINEAR;//核函数类型：线性
    params.term_crit   = cvTermCriteria(CV_TERMCRIT_ITER, 100, 1e-6);//终止准则函数：当迭代次数达到最大值时终止
    
    //训练SVM
    //建立一个SVM类的实例
    CvSVM SVM;
    //训练模型，参数为：输入数据、响应、XX、XX、参数（前面设置过）
    SVM.train(trainingDataMat, labelsMat, Mat(), Mat(), params);
    
    Vec3b green(0,255,0), blue (255,0,0);
    //显示判决域
    for (int i = 0; i < image.rows; ++i)
        for (int j = 0; j < image.cols; ++j)
        {
            Mat sampleMat = (Mat_<float>(1,2) << i,j);
            //predict是用来预测的，参数为：样本、返回值类型（如果值为ture而且是一个2类问题则返回判决函数值，否则返回类标签）、
            float response = SVM.predict(sampleMat);
            
            if (response == 1)
                image.at<Vec3b>(j, i)  = green;
            else if (response == -1)
                image.at<Vec3b>(j, i)  = blue;
        }
    
    //画出训练数据
    int thickness = -1;
    int lineType = 8;
    circle( image, Point(501,  10), 5, Scalar(  0,   0,   0), thickness, lineType);//画圆
    circle( image, Point(255,  10), 5, Scalar(255, 255, 255), thickness, lineType);
    circle( image, Point(501, 255), 5, Scalar(255, 255, 255), thickness, lineType);
    circle( image, Point( 10, 501), 5, Scalar(255, 255, 255), thickness, lineType);
    circle(image, Point( 501, 128), 5, Scalar(0, 0, 0), thickness, lineType);
    
    //显示支持向量
    thickness = 2;
    lineType  = 8;
    //获取支持向量的个数
    int c     = SVM.get_support_vector_count();
    
    for (int i = 0; i < c; ++i)
    {
        //获取第i个支持向量
        const float* v = SVM.get_support_vector(i);
        //支持向量用到的样本点，用灰色进行标注
        circle( image,  Point( (int) v[0], (int) v[1]),   6,  Scalar(128, 128, 128), thickness, lineType);
    }
    
    imwrite("result.png", image);        // save the image
    
    imshow("SVM Simple Example", image); // show it to the user
    waitKey(0);
    
}