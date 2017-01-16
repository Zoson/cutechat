//
// Created by 庄卓鑫 on 16/4/27.
//

/*
 * 对特征点进行预处理
 */
#include<math.h>
#include <android/log.h>
#include "face_points_handle.h"
#define MODULE_NAME "face_points_handle"


void reversePoints(int points[],int pos[],int len){
    int x = pos[0];
    for (int i=0;i<len/2;i++){
        points[i*2] = 2*x - points[i*2] ;
    }
}

//位置比例无关处理
void preProcessPointsLocationIndependent(int *points,int len){
    //52号为中间点
    int middle_before_x = points[104];
    int middle_before_y = points[105];
    int middle_after_x = 500;
    int middle_after_y = 500;
    int diff_x = middle_after_x - middle_before_x;
    int diff_y = middle_after_y - middle_before_y;
    for (int i=0;i<len;i++){
        if (i%2==0){
            points[i] += diff_x;
        }else{
            points[i] += diff_y;
        }
    }
}

void preProcessPointsSizeIndepent(int* points,int len){
    int x_52 = points[52*2];
    int y_52 = points[52*2+1];
    int x_6 = points[6*2];
    int y_6 = points[6*2+1];

    int diff_x_6_52 = x_52 - x_6;
    int diff_y_6_52 = y_52 - y_6;

    int dis_tranf_6_52 = 500;
    double dis_6_52 = pow(diff_x_6_52*diff_x_6_52+diff_y_6_52*diff_y_6_52,0.5);
    double k = dis_tranf_6_52/dis_6_52;
    scale(k,x_52,y_52,points,len);
}

void preProcessPointsAngleIndependent(int* points,int len){
    int eTe[2];
    eTe[0] = (points[21*2]+points[22*2])/2;
    eTe[1] = (points[21*2+1]+points[22*2+1])/2;
    //System.out.println("x_ete:"+eTe[0]+" x56:"+points[56*2]+" y_ete:"+eTe[1]+" y56:"+points[56*2+1]);
    if (eTe[0]==points[56*2]){
        __android_log_print(ANDROID_LOG_ERROR, MODULE_NAME,"no should preProcessPointsAngleIndependent");
        return;
    }
    bool isReverse = false;
    if (eTe[0]>points[56*2]){
        __android_log_print(ANDROID_LOG_ERROR, MODULE_NAME,"isReverse start");
        reversePoints(points,eTe,len);
        isReverse = true;
    }
    double tranf_56[2];
    int diff_x_ete_56 = eTe[0]-points[56*2];
    int diff_y_ete_56 = eTe[1]-points[56*2+1];
    double distance_52_56 = pow(diff_x_ete_56*diff_x_ete_56+diff_y_ete_56*diff_y_ete_56,0.5);
    tranf_56[0] = eTe[0];
    tranf_56[1] = distance_52_56+eTe[1];
    double diff_x_56_tranf_56 = points[56*2]-tranf_56[0];
    double diff_y_56_tranf_56 = points[56*2+1]-tranf_56[1];
    double distance_56_tranf56 = pow(diff_x_56_tranf_56*diff_x_56_tranf_56+diff_y_56_tranf_56*diff_y_56_tranf_56,0.5);
    rotate(eTe[0], eTe[1], distance_56_tranf56, distance_52_56, distance_52_56, points,len);

    if (isReverse){
        reversePoints(points,eTe,len);
        __android_log_print(ANDROID_LOG_ERROR, MODULE_NAME,"isReverse end");
    }

}


void rotate(int x,int y,double a,double b, double c,int points[],int len)
{
    if (a==0)return;
    int point[2];
    double matrix_left[9];
    double matrix_right[3];
    double matrix_result[3];
    /*
    正玄定理
    cosA=(b^2+c^2-a^2)/2*b*c
     */
    double cosa = (b*b+c*c-a*a)/(2*b*c);
    double sina = pow(1-cosa*cosa,0.5);

    matrix_left[0] = cosa;
    matrix_left[1] = -sina;
    matrix_left[2] = 0;
    matrix_left[3] = sina;
    matrix_left[4] = cosa;
    matrix_left[5] = 0;
    matrix_left[6] = 0;
    matrix_left[7] = 0;
    matrix_left[8] = 1;

    /*
    __android_log_print(ANDROID_LOG_ERROR, MODULE_NAME,"a == "+a+" b == "+b +" c == "+c);
    __android_log_print(ANDROID_LOG_ERROR, MODULE_NAME,"cosa == "+cosa+" sina == "+sina);
     */

    for (int i=0;i<len/2;i++){
        matrix_right[0] = points[i*2]-x;
        matrix_right[1] = points[i*2+1]-y;
        matrix_right[2] = 1;

        matrix_result[0] = matrix_left[0]*matrix_right[0]+matrix_left[1]*matrix_right[1]+matrix_left[2]*matrix_right[2];
        matrix_result[1] = matrix_left[3]*matrix_right[0]+matrix_left[4]*matrix_right[1]+matrix_left[5]*matrix_right[2];
        //System.out.println("pos ="+i+" tanf before x = "+points[i*2]+" tanf before y = "+points[i*2+1]+ " tanf after x = "+(int)(matrix_result[0]+x)+" tanf after y = "+(int)(matrix_result[1]+y));
        //__android_log_print(ANDROID_LOG_ERROR, MODULE_NAME,"pos ="+i+" tanf before x = "+points[i*2]+" tanf before y = "+points[i*2+1]+ " tanf after x = "+(int)(matrix_result[0]+x)+" tanf after y = "+(int)(matrix_result[1]+y));

        points[i*2] = (int)matrix_result[0]+x;
        points[i*2+1] = (int)matrix_result[1]+y;
    }
}

void scale(double k,int x,int y,int *points,int len)
{
    int point[2];
    double matrix_left[9];
    double matrix_right[3];
    double matrix_result[3];

    matrix_left[0] = k;
    matrix_left[1] = 0;
    matrix_left[2] = 0;
    matrix_left[3] = 0;
    matrix_left[4] = k;
    matrix_left[5] = 0;
    matrix_left[6] = 0;
    matrix_left[7] = 0;
    matrix_left[8] = 1;

    for (int i=0;i<len/2;i++){
        matrix_right[0] = points[i*2] - x;
        matrix_right[1] = points[i*2+1] - y;
        matrix_right[2] = 1;

        matrix_result[0] = matrix_left[0]*matrix_right[0]+matrix_left[1]*matrix_right[1]+matrix_left[2]*matrix_right[2];
        matrix_result[1] = matrix_left[3]*matrix_right[0]+matrix_left[4]*matrix_right[1]+matrix_left[5]*matrix_right[2];

        points[i*2] = (int)matrix_result[0]+x;
        points[i*2+1] = (int)matrix_result[1]+x;
    }
}

