#include <jni.h>
#include "unistd.h"
#include "GLES/gl.h"
#include "GLES2/gl2ext.h"
#include "opencv2/opencv.hpp"
//#include "opencv/cv.h"
#include "android/log.h"

//#define LOGD(TAG,...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
using namespace cv ;
using namespace std ;
extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_ahmed_opengl_MyRenderer_create(JNIEnv* env, jobject thiz, jstring path, jint texID);
JNIEXPORT void JNICALL Java_com_example_ahmed_opengl_MyRenderer_draws(JNIEnv* env, jobject thiz, jint program);
JNIEXPORT jstring JNICALL Java_com_example_ahmed_opengl_CameraView_setImage(JNIEnv* env, jobject thiz, jlong addr);
}
GLfloat identity[] = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
GLfloat coords[] =
        {-1.0f,1.0f,0.0f,
         -1.0f,-1.0f,0.0f,
         1.0f,-1.0f,0.0f,
         1.0f,-1.0f,0.0f,
         1.0f,1.0f,0.0f,
         -1.0f,1.0f,0.0f};
GLfloat texCoords[] =
        {0,1,
         0,0,
         1,0,
         1,0,
         1,1,
         0,1};
GLfloat color[] = {255,0,0,1};
Mat mat;

CascadeClassifier classifier;
GLuint glassesTexID;


JNIEXPORT jstring JNICALL Java_com_example_ahmed_opengl_MyRenderer_create(JNIEnv* env, jobject thiz, jstring path, jint texID){
    classifier = CascadeClassifier(env->GetStringUTFChars(path,NULL));
    glassesTexID = texID;

   // cvtColor(mat, mat, CV_BGR2RGB);
    env->NewStringUTF("Created");
}
JNIEXPORT jstring JNICALL Java_com_example_ahmed_opengl_CameraView_setImage(JNIEnv* env, jobject thiz, jlong addr){
    mat = *(Mat*)addr;
   // __android_log_print(ANDROID_LOG_DEBUG, "PIXEL", "%d", mat->cols * mat->rows);
    /*for(int i = 0; i < data.cols * data.rows; i++){
        __android_log_print(ANDROID_LOG_DEBUG, "PIXEL", "%d", data.data[i]);
    }*/
}

GLuint loadTexture(){
    const unsigned char* data = mat.data;

    GLuint texID;
    glEnable(GL_TEXTURE_2D);
    glGenTextures(1,&texID);
    glBindTexture(GL_TEXTURE_2D,texID);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
    glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,mat.cols,mat.rows,0,GL_RGB,GL_UNSIGNED_BYTE,data);

    return texID;
}
GLfloat* xRotationMatrix(GLfloat theta){
    //__android_log_print(ANDROID_LOG_DEBUG, "COS", "%f", cos(theta));
    GLfloat matrix[] = {1, 0 , 0, 0, 0,cos(theta),-1*sin(theta),0,0,sin(theta),cos(theta),0,0,0,0,1};
    return matrix;
}
GLfloat* yRotationMatrix(GLfloat theta){
    //__android_log_print(ANDROID_LOG_DEBUG, "COS", "%f", cos(theta));
    GLfloat matrix[] = {cos(theta), 0 , sin(theta),0,0,1,0,0,-1*sin(theta),0,cos(theta),0,0,0,0,1};
    return matrix;
}
GLfloat* zRotationMatrix(GLfloat theta){
    //__android_log_print(ANDROID_LOG_DEBUG, "COS", "%f", cos(theta));
    GLfloat matrix[] = {cos(theta), -1*sin(theta) , 0,0,sin(theta),cos(theta),0,0,0,0,1,0,0,0,0,1};
    return matrix;
}
GLfloat* matrixMultiply(GLfloat* matrix1, GLfloat* matrix2){
    GLfloat result[16];
    for(int i=0;i<16;i++){
        GLfloat sum = 0.0f;
        int y = (i / 4);
        int x = (i % 4);
        for(int j=0;j<4;j++){
            sum += (matrix1[(4 * y) + j] * matrix2[(4 * j) + x]);
        }
        result[i] = sum;
    }
    return result;
}

vector<Rect> filterEyes(vector<Rect> eyes){


    vector<Rect> result;
    vector<double> xMids;
    vector<double> yMids;
    for(int i=0;i<eyes.size();i++){
        int x1 = eyes[i].x;
        int x2 = (eyes[i].x + eyes[i].width);
        int y1 = eyes[i].y;
        int y2 = (eyes[i].y + eyes[i].height);
        double midx = (x1 + x2) / 2;
        double midy = (y1 + y2) / 2;
        xMids.push_back(midx);
        yMids.push_back(midy);
    }
    for(int i=0;i<(int)(eyes.size()-1);i++){
        bool flag = false;
        for(int j=i+1;j<eyes.size();j++){
            if(xMids[i] != xMids[j]){
                double slope = (yMids[i] - yMids[j]) / (xMids[i] - xMids[j]);
                if(slope > - 0.4 && slope < 0.4){
                    flag = true;
                    result.push_back(eyes[i]);
                    result.push_back(eyes[j]);
                    break;
                }
            }
        }
        if(flag){
            break;
        }
    }
    return result;
}

void drawTexture(GLuint texID, GLfloat transformationMatrix[], int program){

   // __android_log_print(ANDROID_LOG_DEBUG, "JNIIIIIIIII", "%d", program);
    glUseProgram(program);
    int posHandle = glGetAttribLocation(program, "vPosition");
    int textureCoordHandle = glGetAttribLocation(program, "vTextureCoord");
    int textureHandle = glGetUniformLocation(program, "vTexture");
    int matrixHandle = glGetUniformLocation(program, "MVPmatrix");
    int colorHandle = glGetUniformLocation(program, "vColor");

    glVertexAttribPointer(posHandle, 3, GL_FLOAT, false, 12, coords );
    glEnableVertexAttribArray(posHandle);

    glUniformMatrix4fv(matrixHandle, 1, false, transformationMatrix);

    glUniform4fv(colorHandle, 1, color);

    glEnable(GL_TEXTURE_2D);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, texID);
    glUniform1i(textureHandle, 0);

    glVertexAttribPointer(textureCoordHandle, 2, GL_FLOAT, false, 8, texCoords);
    glEnableVertexAttribArray(textureCoordHandle);

    glDrawArrays(GL_TRIANGLES, 0, 6);

    glDisableVertexAttribArray(textureCoordHandle);
    glDisableVertexAttribArray(posHandle);

    //__android_log_print(ANDROID_LOG_DEBUG, "JNIIIIIIIII", "%d", program);
}

GLfloat rotationAngle = 0.0f;
double scaleFactor = 1.0;
double slope = 0.0;
double glMidX = 0.0;
double glMidY = 0.0;




JNIEXPORT void JNICALL Java_com_example_ahmed_opengl_MyRenderer_draws(JNIEnv* env, jobject thiz, jint program){
    //__android_log_print(ANDROID_LOG_DEBUG, "JNIIIIIIIII", "%d", program);
    //delete pixels;

    glClear(GL_COLOR_BUFFER_BIT);
    //drawTexture(glassesTexID , identity, program1);


    //__android_log_print(ANDROID_LOG_DEBUG, "JNIIIIIIIII", "%f", mat.data[1000]);


    //flip(mat,mat,1);

    //__android_log_print(ANDROID_LOG_DEBUG, "JNIIIIIIIII", "%d", program);

    //int dim = 256;
    //Mat newMat;
    //resize(mat,newMat,Size(dim,dim));
    //float xScale = (float)mat.cols / (float)newMat.cols;
    //float yScale = (float)mat.rows / (float)newMat.rows;
    float xScale = 1;
    float yScale = 1;
    vector<Rect> eyes;
    classifier.detectMultiScale(mat,eyes,1.1,2,0,Size(5,5));
    eyes = filterEyes(eyes);


    if(eyes.size() == 2){
        int x1 = eyes[0].x * xScale;
        int x2 = (eyes[0].x + eyes[0].width) * xScale;
        int y1 = eyes[0].y * yScale;
        int y2 = (eyes[0].y + eyes[0].height) * yScale;
        double mid1x = (x1 + x2) / 2;
        double mid1y = (y1 + y2) / 2;
        x1 = eyes[1].x * xScale;
        x2 = (eyes[1].x + eyes[1].width) * xScale;
        y1 = eyes[1].y * yScale;
        y2 = (eyes[1].y + eyes[1].height) * yScale;
        double mid2x = (x1 + x2) / 2;
        double mid2y = (y1 + y2) / 2;
        double dist = sqrt(pow(mid1x - mid2x,2) + pow(mid1y - mid2y,2));
        scaleFactor = (2 * dist) / (double)mat.cols;
        slope = (mid1y - mid2y) / (mid1x - mid2x);

        double midmidx = (mid1x + mid2x) / 2;
        double midmidy = (mid1y + mid2y) / 2;
        glMidX = midmidx - ((double)mat.cols / 2.0);
        glMidY = ((double)mat.rows / 2.0) - midmidy;
    }
    double xShift = glMidX * (2.0 / (double)mat.cols);
    double yShift = glMidY * (2.0 / (double)mat.rows);
    GLfloat scaleMatrix[] = {scaleFactor, 0, 0, 0, 0, scaleFactor, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
    GLfloat translationMatrix[] = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, xShift, yShift, 0, 1};
    rotationAngle = atan(slope);
    GLfloat* rotationPointer = zRotationMatrix(rotationAngle);
    GLfloat rotationMatrix[16];
    for(int i=0;i<16;i++){
        rotationMatrix[i] = rotationPointer[i];
    }
    GLfloat* resultPointer = matrixMultiply(translationMatrix, rotationMatrix);
    GLfloat resultMatrix[16];
    for(int i=0;i<16;i++){
        resultMatrix[i] = resultPointer[i];
    }
    resultPointer = matrixMultiply(resultMatrix, scaleMatrix);
    for(int i=0;i<16;i++){
        resultMatrix[i] = resultPointer[i];
    }
   // __android_log_print(ANDROID_LOG_DEBUG, "SCALEEEE", "%f", scaleFactor);


    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    drawTexture(glassesTexID , resultMatrix, program);
    //free(mat);
}
