//
// Created by 庄卓鑫 on 16/4/27.
//

#ifndef CUTECHAT_FACE_POINTS_HANDLE_H
#define CUTECHAT_FACE_POINTS_HANDLE_H

void preProcessPointsLocationIndependent(int *points,int len);
void preProcessPointsSizeIndepent(int* points,int len);
void preProcessPointsAngleIndependent(int* points,int len);
void rotate(int x,int y,double a,double b, double c,int points[],int len);
void scale(double k,int x,int y,int *points,int len);

#endif //CUTECHAT_FACE_POINTS_HANDLE_H
