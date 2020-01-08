package com.example.tinyyolov3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;


//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.*;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;


//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.*;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.dnn.Dnn;
import org.opencv.utils.Converters;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    // To be used for the name of the file
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
    String currentDateandTime = sdf.format(new Date());

    // Never used but, i don't know.
    private String fileName = "YOLOv3-tiny";

    // Tag used for logging
    private static final String TAG = "YOLOv3-tiny";

    // 2 objects from 2 classes.
    CameraBridgeViewBase cameraBridgeViewBase;
    //This one is used to check if openCV loaded succesfully.
    BaseLoaderCallback baseLoaderCallback;

    // Condition to be used in Detection
    boolean startYolo = false;

    // Another condition to be used in detection
    boolean firstTimeYolo = false;

    //Net is from openCv.dnn.
    // * This class allows to create and manipulate comprehensive artificial neural networks.
    Net tinyYolo;

    // To be used in logging.
    String logInfo;

    // We out here counting frames fam.
    int frameCounter = 1;
    //only if you have object every time, i think

    //changing the filename to time.

    String outputFileName = Environment.getExternalStorageDirectory() + "/" +
            Environment.DIRECTORY_DCIM + "/YoloResults/" + currentDateandTime +"_Results" ;

    // Old code, updated to Date&Time to be logged.
    //       String outputFileName = Environment.getExternalStorageDirectory() + "/" +
    //       Environment.DIRECTORY_DCIM + "/YoloResults/" + fileName +"_Results" ;


    // take the yolo from files, form the Net, get out of here.
    public void YOLO(View Button){


        if (startYolo == false){

            startYolo = true;

            //if the apk running for the first time, we get the files with this condition

            if(firstTimeYolo == false){

                firstTimeYolo = true;

                String tinyYoloCFG = Environment.getExternalStorageDirectory() + "/dnns/yolov3-tiny.cfg";
                String tinyYoloWeights = Environment.getExternalStorageDirectory() + "/dnns/yolov3-tiny.weights";

                //This is the way we get cfg and weights, and declare tinyYolo, opencv function
                tinyYolo = Dnn.readNetFromDarknet(tinyYoloCFG, tinyYoloWeights);

            }
        }

        else{

            startYolo = false;

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Your code in addition to the existing code in the onCreate() of the parent class.
        super.onCreate(savedInstanceState);

        // Basically the app will look like the layout you have in activity main.
        setContentView(R.layout.activity_main);


        // Giving meaning to our object
        // From activity_main.xml, the Button, CameraView.
        // The the cameraBridgeViewBase listens to the button.

        cameraBridgeViewBase = (JavaCameraView)findViewById(R.id.CameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);


        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Using baseLoaderCallback, enableView when it is successfully loaded.
        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);

                switch(status){

                    case BaseLoaderCallback.SUCCESS:
                         // This method is provided for clients, so they can enable the camera connection.
                         // The actual onCameraViewStarted callback will be delivered only after both this method is called and surface is available.
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };

        // Check for writing external storage permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Writing external memory permission is granted");
        }
        // Ask for writing external storage permission if needed
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        // Check for writing external storage permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Reading external memory permission is granted");
        }
        // Ask for writing external storage permission if needed
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        //Take rgba frame

        Mat frame = inputFrame.rgba();

        //RGBA Converted into RGB
        //Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);


        if (startYolo == true) {

            // Initially nothing in the logInfo.
            logInfo = "";

            //RGBA Converted into RGB
            Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);


            //A blob is a 4D version of an image, that we usually put into CNNs
            //0.00392 = 1/255 from colors , scale factor is used for to convert all values to percentages
            //For example [255 , 0 , 126] to [1 , 0 , .5]
            //with new size, we are resizing the image 416*416
            //Scalar for substracting certain values from pixels, some NNs need it some don't
            //OpenCV works in BGR, almost everything else works in RGB, so we gotta swap the B and R

            Mat imageBlob = Dnn.blobFromImage(frame, 0.00392, new Size(416,416),new Scalar(0, 0, 0),/*swapRB*/false, /*crop*/false);

            // We defined the tinyYolo Net earlier.
            // use the Blob as the input.
            tinyYolo.setInput(imageBlob);

            // Okay, some blog info here.
            // If you check the cfg file of tinyyolov3, you will see 48 layers.
            // Here we are only interested in Unconnected Out layers,
            // Which has the names of yolo_16 and yolo_23
            // The reason is that we can use these layers, on .forward() stuff
            // On real YOLOV3 there are 3 layers.


            // First we define the result list, they will be filled with two YOLO layers
            java.util.List<Mat> result = new java.util.ArrayList<Mat>(2);
            // If i wanted to run YOLOv3 i would make this list has 3 initial Capacity


            // We make this list of 2 values, of two YOLO layers
            List<String> outBlobNames = new java.util.ArrayList<>();
            outBlobNames.add(0, "yolo_16");
            outBlobNames.add(1, "yolo_23");

            // For big YOLO there are 3 layers? if you need to run big yolo just add the element

            // The tinyYolov3 layers we are looking for is from outBlobNames, and we define where to put them; which is "result"
            tinyYolo.forward(result,outBlobNames);

            // From this point forward, its all over the place. Different Sources for the code.
            // This is the process that post-processes the output and draws the rectangles

            // Confidence Threshold
            float confThreshold = 0.3f;

            // List of Class ID's
            List<Integer> clsIds = new ArrayList<>();
            // List of Confidences
            List<Float> confs = new ArrayList<>();
            // List of Rectangles, the coordinates of the bounding boxes
            List<Rect> rects = new ArrayList<>();


            // Iterate over the size of the list
            // If this is running for small YOLO, 2 layers here
            // If this runs on big YOLO, 3 layers here
            // Simply, for our case; this is just 2.

            for (int i = 0; i < result.size(); ++i)
            {

                // level is just the yololayer for each case.
                // level = yolo_16 , for the first one
                // level = yolo_23 , for the second one

                Mat level = result.get(i);


                // Each row is a candidate detection, the 1st 4 numbers are
                // [center_x, center_y, width, height], followed by (N-4) class probabilities

                // Iterate over each of it's rows, of yolo layers.
                for (int j = 0; j < level.rows(); ++j)
                {

                    // We access a row.
                    // Still same thing, yolo_16 or yolo_23
                    Mat row = level.row(j);

                    // We define scores as level without the first 5 neurons(4 for bbox, and 1 for objectness score)
                    // Which is how likely there is something in the bbox,and what's left are 1,2,3...80 coco classes or
                    // however your custom model has, the max value location there will be
                    // the class , and max value - conf

                    // scores is defined with the row, from 5 to end of the level line.
                    Mat scores = row.colRange(5, level.cols());

                    // Defined from OpenCV , scores, used for finding max score
                    // The confidence will be the max value of that guy

                    // Core is a openCV class.
                    // For every number in "scores", apart from the first 5, i need the max value.
                    // Which will be mm.

                    Core.MinMaxLocResult mm = Core.minMaxLoc(scores);

                    // For 80 Neurons you get different values, we use the highest one.
                    float confidence = (float)mm.maxVal;

                    // We determine the class with class.id, which has the highest value
                    // Defined with maxLoc.
                    // The one that has the highest number, is defined as classIdPoint
                    // For example; highest one is 48th and its "a bird" or something, from the classIds list.
                    Point classIdPoint = mm.maxLoc;


                    // The way YOLO predicts the outputs, first off all it predicts in percentage values
                    // in a frame, exactly middle, 0.5x , 0.5y
                    // At first we extract those percentages and convert to pixels,
                    // If it's bigger than threshold than we will process it
                    if (confidence > confThreshold)
                    {

                        // This was good the way that it is, but we need pixels.
                        int centerX = (int)(row.get(0,0)[0] * frame.cols());
                        int centerY = (int)(row.get(0,1)[0] * frame.rows());

                        int width   = (int)(row.get(0,2)[0] * frame.cols());
                        int height  = (int)(row.get(0,3)[0] * frame.rows());

                        // Possible addition here. If we need to make the pixels that is always positive or zero
                        // we might need to add the condition saying, if it is negative; make it zero.


                        //We calculate the top-left point coordinates
                        int left    = centerX - width  / 2;
                        int top     = centerY - height / 2;

                        // Possible problem here. Wouldn't it be bottom left point, if we subtract the height?


                        // Each classIdPoint is added to the clsIds list
                        clsIds.add((int)classIdPoint.x);

                        // Each confidence value is added to the confs list.
                        confs.add((float)confidence);

                        // Remember, we are still in the if, so if confidence is higher than threshold, we pass the values to rects.
                        // We construct the rect object
                        // And we add it to the rects list.
                        rects.add(new Rect(left, top, width, height));
                    }
                }
            }

            // How many of those rows went through 2 YOLO layers and was added to the confs list.
            int ArrayLength = confs.size();

            if (ArrayLength>=1) {

                // Apply non-maximum suppression procedure.
                // What is max suppression ? For example for a mobile phone, there may be 2 BBOXes for the same object
                // With max suppression, it decides which boxes overlap, chose the one with highest confidence

                // Non maximum suppression threshold
                float nmsThresh = 0.2f;


                // Pre-process for non maximum suppression

                // Convert the vector float to matrix to get confidences to matrix of floats; from confs list.
                MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confs));

                // Define boxesArray, from rects list values.
                Rect[] boxesArray = rects.toArray(new Rect[0]);

                // Hmm. We defined a rect[] i guess boxes should be a MatOfRect
                MatOfRect boxes = new MatOfRect(boxesArray);

                MatOfInt indices = new MatOfInt();


                // Define the NMSBoxes from our values that we calculated
                // Performs non maximum suppression given boxes and corresponding scores.
                Dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThresh, indices);


                // Draw result boxes.
                // The ones surpass the nms are indices , "ind"
                // Basically these are the best boxes to represent each object that is found in the frame.

                // Define ind array, from indices.
                int[] ind = indices.toArray();

                for (int i = 0; i < ind.length; ++i) {

                    //second test
                    //int frameCounter = 1;
                    //somehow doesnt work

                    // int objectCounter = 1;

                    int idx = ind[i];

                    Rect box = boxesArray[idx];

                    int idGuy = clsIds.get(idx);

                    float conf = confs.get(idx);


                    List<String> cocoNames = Arrays.asList("a person", "a bicycle", "a motorbike", "an airplane", "a bus", "a train", "a truck", "a boat", "a traffic light", "a fire hydrant", "a stop sign", "a parking meter", "a car", "a bench", "a bird", "a cat", "a dog", "a horse", "a sheep", "a cow", "an elephant", "a bear", "a zebra", "a giraffe", "a backpack", "an umbrella", "a handbag", "a tie", "a suitcase", "a frisbee", "skis", "a snowboard", "a sports ball", "a kite", "a baseball bat", "a baseball glove", "a skateboard", "a surfboard", "a tennis racket", "a bottle", "a wine glass", "a cup", "a fork", "a knife", "a spoon", "a bowl", "a banana", "an apple", "a sandwich", "an orange", "broccoli", "a carrot", "a hot dog", "a pizza", "a doughnut", "a cake", "a chair", "a sofa", "a potted plant", "a bed", "a dining table", "a toilet", "a TV monitor", "a laptop", "a computer mouse", "a remote control", "a keyboard", "a cell phone", "a microwave", "an oven", "a toaster", "a sink", "a refrigerator", "a book", "a clock", "a vase", "a pair of scissors", "a teddy bear", "a hair drier", "a toothbrush");


                    // The number that is pressed
                    int intConf = (int) (conf * 100);

                    // The class text
                    Imgproc.putText(frame,cocoNames.get(idGuy) + " " + intConf + "%",box.tl(),Core.FONT_HERSHEY_SIMPLEX, 2, new Scalar(255,255,0),2);

                    // The box.
                    Imgproc.rectangle(frame, box.tl(), box.br(), new Scalar(0, 255, 0), 2);


                    // update the info to log
                    logInfo = logInfo  + Integer.toString(frameCounter) + "," + cocoNames.get(idGuy) + "," + Integer.toString(box.x) +
                    "," + Integer.toString(box.y) + "," + Integer.toString(box.width) + "," + Integer.toString(box.height) + "," + conf + "\n";


                    // Possible improvement later. Not now i guess.
                    // objectCounter = objectCounter + 1 ;

                    try {

                        FileOutputStream logFile = new FileOutputStream(new File(outputFileName + ".txt"), true);

                        logFile.write((logInfo).getBytes());
                        logFile.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // If we increase the counter here, it just gives 1,2,3,4,5,6,.. as the frames that are found object in.
                    frameCounter = frameCounter +1;
                }

            }
            // Possible usage for a different choice.
            // If we increase the counter here; it counts every frame, only gives the object ones. {2,4,8,10,11,12,15,...}
            // frameCounter = frameCounter + 1;
        }
        return frame;
    }


    @Override
    public void onCameraViewStarted(int width, int height) {


        //If you turn off display and turn it back on, sometimes it causes issues
        //If it's suppose to be showing yolo it will launch it again
        if (startYolo == true){

            String tinyYoloCfg = Environment.getExternalStorageDirectory() + "/dnns/yolov3-tiny.cfg" ;
            String tinyYoloWeights = Environment.getExternalStorageDirectory() + "/dnns/yolov3-tiny.weights";

            tinyYolo = Dnn.readNetFromDarknet(tinyYoloCfg, tinyYoloWeights);


        }



    }


    @Override
    public void onCameraViewStopped() {

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"There's a problem, yo!", Toast.LENGTH_SHORT).show();
        }

        else
        {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase!=null){

            cameraBridgeViewBase.disableView();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }
}