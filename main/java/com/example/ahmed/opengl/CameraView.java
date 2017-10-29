package com.example.ahmed.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Matrix;
import android.util.Log;

import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Ahmed on 3/2/2017.
 */
public class CameraView extends JavaCameraView implements CameraBridgeViewBase.CvCameraViewListener{

    MyView renderer;
    Context context;
    public CameraView(Context context, int cameraId, MyView renderer) {
        super(context, cameraId);
        this.renderer = renderer;
        this.context = context;
        setCvCameraViewListener(this);
    }



    @Override
    public void onCameraViewStarted(int width, int height) {
        bitmap = Bitmap.createBitmap(width / factor, height / factor, Bitmap.Config.ARGB_8888);
        viewMatrix = new float[16];
        projectionMatrix = new float[16];
    }

    @Override
    public void onCameraViewStopped() {

    }

    public static FaceDet det;
    public static boolean loaded = false;

    Bitmap bitmap;
    public static long camCounter = 0;
    public static long glCounter = 0;
    int factor = 4;

    public static void matrixTranspose(float[] transpose, float[] original){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                transpose[(j * 4) + i] = original[(i * 4) + j];
            }
        }
    }
    public static void matrixMultiply(float[] result, float[] left, float[] right){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                float sum = 0.0f;
                for(int k = 0; k < 4; k++){
                    sum += left[(i * 4) + k] * right[(k * 4) + j];
                }
                result[(i * 4) + j] = sum;
            }
        }
    }
    static float[] viewMatrix;
    static float[] projectionMatrix;

    @Override
    public Mat onCameraFrame(Mat inputFrame) {

        Core.flip(inputFrame, inputFrame, 1);

        if(loaded && !MyRenderer.loading){

            MyRenderer.loading = false;
            Mat scaled = new Mat();
            Imgproc.resize(inputFrame, scaled, new Size(inputFrame.cols() / factor, inputFrame.rows() / factor));
            Utils.matToBitmap(scaled, bitmap);
            List<VisionDetRet> faces = det.detect(bitmap);
            if(faces.size() > 0){
                /*Imgproc.rectangle(inputFrame, new Point(faces.get(0).getLeft() * factor, faces.get(0).getTop() * factor),
                        new Point(faces.get(0).getRight() * factor, faces.get(0).getBottom() * factor), new Scalar(255,0,0), 3);
                */
                ArrayList<android.graphics.Point> landmarks = faces.get(0).getFaceLandmarks();
                ArrayList<Integer> indices = new ArrayList<>();
                indices.add(8);
                indices.add(30);
                indices.add(36);
                indices.add(45);
                indices.add(48);
                indices.add(54);

                Point3[] facePoints3D = new Point3[6];
                Point[] facePoints2D = new Point[6];

                //nose
                facePoints3D[0] = new Point3(-0.82638, 62.40493, 128.38879);
                facePoints2D[0] = new Point(landmarks.get(30).x * factor, landmarks.get(30).y * factor);

                //left eye
                facePoints3D[1] = new Point3(-51.43462, 104.56438, 75.49118);
                facePoints2D[1] = new Point(landmarks.get(36).x * factor, landmarks.get(36).y * factor);

                //right eye
                facePoints3D[2] = new Point3(51.43462, 104.56438, 75.49118);
                facePoints2D[2] = new Point(landmarks.get(45).x * factor, landmarks.get(45).y * factor);

                //chin
                facePoints3D[3] = new Point3(-0.82638, -30.99968, 94.09066);
                facePoints2D[3] = new Point(landmarks.get(8).x * factor, landmarks.get(8).y * factor);

                //left mouse
                facePoints3D[4] = new Point3(-26.55948, 19.16039, 90.49114);
                facePoints2D[4] = new Point(landmarks.get(48).x * factor, landmarks.get(48).y * factor);

                //right mouse
                facePoints3D[5] = new Point3(26.55948, 19.16039, 90.49114);
                facePoints2D[5] = new Point(landmarks.get(54).x * factor, landmarks.get(54).y * factor);

                MatOfPoint3f points3D = new MatOfPoint3f(facePoints3D);

                MatOfPoint2f points2D = new MatOfPoint2f(facePoints2D);


                double focal_length = inputFrame.cols();

                Point center = new Point(inputFrame.cols()/2,inputFrame.rows()/2);
                Mat cameraMatrix = new Mat(3, 3, CvType.CV_64FC1);

                cameraMatrix.put(0, 0, focal_length);
                cameraMatrix.put(0, 1, 0);
                cameraMatrix.put(0, 2, center.x);
                cameraMatrix.put(1, 0, 0);
                cameraMatrix.put(1, 1,focal_length);
                cameraMatrix.put(1, 2, center.y);
                cameraMatrix.put(2, 0, 0);
                cameraMatrix.put(2, 1, 0);
                cameraMatrix.put(2, 2, 1);

                MatOfDouble coeffs = new MatOfDouble(0, 0, 0, 0);

                MatOfDouble rotation = new MatOfDouble();
                MatOfDouble translation = new MatOfDouble();

                boolean success = Calib3d.solvePnP(points3D, points2D, cameraMatrix, coeffs, rotation, translation);

                if(success && rotation != null && translation != null){
                    /*
                    for(int index : indices){
                        android.graphics.Point point = landmarks.get(index);
                        Imgproc.putText(inputFrame, index +"", new Point(point.x * factor, point.y * factor),Core.FONT_HERSHEY_SIMPLEX, 1
                                , new Scalar(255,0 ,0));
                    }
                    */
                    projectionMatrix =
                            new float[]{
                                    (2.0f * (float) focal_length) / (float) inputFrame.cols(), 0.0f, (2.0f * (float) center.x / (float) inputFrame.cols()) - 1,
                                    0.0f,
                                    0.0f, (-2.0f * (float) focal_length) / (float) inputFrame.rows(), -(2.0f * (float) center.y / (float) inputFrame.rows()) + 1,
                                    0.0f,
                                    0.0f, 0.0f, 1.0f, 0.0f,
                                    0.0f, 0.0f, 1.0f, 0.0f
                            };

                    Calib3d.Rodrigues(rotation, rotation);

                    viewMatrix = new float[16];

                    for(int i = 0; i < 3; i++){
                        for(int j = 0; j < 3; j++){
                            viewMatrix[(i * 4) + j] = (float) rotation.get(i , j)[0];
                        }
                        viewMatrix[(i * 4) + 3] = (float) translation.get(i, 0)[0];
                    }

                    viewMatrix[15] = 1;

                    float[] viewMatrixTranspose = new float[16];

                    matrixTranspose(viewMatrixTranspose, viewMatrix);

                    viewMatrix = viewMatrixTranspose;

                    float[] projectionMatrixTranspose = new float[16];

                    matrixTranspose(projectionMatrixTranspose, projectionMatrix);

                    projectionMatrix = projectionMatrixTranspose;

                    camCounter = glCounter + 1;
                    renderer.requestRender();
                    while(camCounter != glCounter);
                }
                else{

                }
            }
        }
        else{
            Imgproc.putText(inputFrame, "Loading Glasses", new Point(inputFrame.cols()/2, inputFrame.rows()/2),
                    Core.FONT_HERSHEY_SIMPLEX, 2
                    , new Scalar(255,0 ,0));
        }
        return inputFrame;
    }
}
