# Android-App-for-Object-Detection
This project is about develop an application using OpenCV &amp; Neural Networks with object detection goal in mind (TinyYOLOv3 in 
particular).

<p align="center">
  <img src="assets/screen.jpg">
</p>

## Getting Started

Well, the project already started with a basic version off the app made. Which is just an .apk that uses the camera, and with its one 
button, when pressed; processes the frames using [yolov3-tiny](https://pjreddie.com/darknet/yolo/).

Now we are trying to achieve, fullscreen without breaking anything.

After we are gonna be trying to use the camera at 720p 30fps.

Maybe somewhere on the line we will try to get the yolo results to a txt.

## Detection with Smartphone Camera

The files for the object detector is added [here](https://github.com/kantarcise/Android-App-for-Object-Detection/tree/master/TinyYolov3). This is just the whole Android Studio Project; if you are just focused
to the important trio, i got you covered; you can find them down below.

[activitymain.xml](https://github.com/kantarcise/Android-App-for-Object-Detection/blob/master/TinyYolov3/app/src/main/res/layout/activity_main.xml)

[MainActivity.java](https://github.com/kantarcise/Android-App-for-Object-Detection/blob/master/TinyYolov3/app/src/main/java/com/example/tinyyolov3/MainActivity.java)

[AndroidManifest.xml](https://github.com/kantarcise/Android-App-for-Object-Detection/blob/master/TinyYolov3/app/src/main/AndroidManifest.xml)

[OpenCV347](https://opencv.org/releases/) library is implemented for the functions that are used in the mainactivity.java. 

For this code and the tutorial, all credit goes to wonderful: [Ivan Goncharov](https://github.com/ivangrov).

In the development process, I am currently working on sensor data implementation (accelerometer to be specific), to the detector, to 
improve performance of object 
detection. 
(Which is my senior design project by the way =) )

## Detection on a Custom Dataset

For my graduation project, i've recorded videos around my university campus to evaluate results of TinyYolov3. The database of 19 videos can be found [here.](https://www.youtube.com/watch?v=1Pvu0rq3678&list=PLMzonaXew-55493qE290Zo2Sp53DxTXrW&ab_channel=msprITU)

A basic MATLAB routine to access the frames from all the videos after download can be found [here.](https://github.com/kantarcise/Android-App-for-Object-Detection/blob/master/assets/ExtractFramesFromVideo.m)

The results from three videos can be seen in the gifs below.

<p align="center">
  <img src="assets/car.gif">
</p>

<p align="center">
  <img src="assets/person.gif">
</p>

<p align="center">
  <img src="assets/van.gif">
</p>


Also for Precision & Recall & F1 Score plots of YOLOv3 and TinyYOLOv3 you can contact me via email. 


<!--
### Installing
## Running the tests
### Tips And Tricks

-->
## Help, I'm Stuck!
For any questions regarding on how to use the app, feel free to contact [Sezai](mailto:sezaiburakkantarci@gmail.com) from the mail. 
