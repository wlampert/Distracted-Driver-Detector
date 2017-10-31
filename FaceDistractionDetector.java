package com.google.android.gms.samples.vision.face.facetracker;

import android.graphics.PointF;
import android.util.Log;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by WillyLampert on 2/25/17.
 */


public class FaceDistractionDetector {

    Runnable onDistractionDetected;
    Runnable onDistractionStopped;
    Face mFace;

    boolean eyeTimerRunning = false;
    Timer eyeTimer = new Timer();

    boolean euleryTimerRunning = false;
    Timer euleryTimer = new Timer();


    public FaceDistractionDetector(Runnable onDistractionDetected, Runnable onDistractionStopped) {
        this.onDistractionDetected = onDistractionDetected;
        this.onDistractionStopped = onDistractionStopped;
    }


    void isDistracted(Face face) {
        mFace = face;

        boolean isEitherEyeClosed = mFace.getIsLeftEyeOpenProbability() < .5 ||
                mFace.getIsRightEyeOpenProbability() < .5;
        if (isEitherEyeClosed && !eyeTimerRunning) {
            eyeTimer = new Timer();
            eyeTimer.schedule(new TimerTask() {
                public void run() {
                    onDistractionDetected.run();
                }
            }, TimeUnit.SECONDS.toMillis(2));
            eyeTimerRunning = true;
        } else if (!isEitherEyeClosed && eyeTimerRunning) {
            eyeTimer.cancel();
            eyeTimerRunning = false;
            onDistractionStopped.run();
        }

        if (Math.abs(mFace.getEulerY()) >= 20 && !euleryTimerRunning) {
            euleryTimer = new Timer();
            euleryTimer.schedule(new TimerTask() {
                public void run() {
                    onDistractionDetected.run();
                }
            }, TimeUnit.SECONDS.toMillis(2));
            euleryTimerRunning = true;
        } else if (Math.abs(mFace.getEulerY()) < 20 && euleryTimerRunning) {
            euleryTimer.cancel();
            euleryTimerRunning = false;
            onDistractionStopped.run();
        }
    }
}
        /**
        boolean foundMouth = false;
        for (Landmark landmark : face.getLandmarks()) {
            if (landmark.getType() == Landmark.BOTTOM_MOUTH) {
                foundMouth = true;
            }
        }

        if (!foundMouth) {

            framesNoMouth++;

        }
**/

//        PointF nosePosition = null;
//        PointF mouthPosition = null;
//        PointF eyePosition = null;
//        List<Landmark> landmarkList = face.getLandmarks();
//        for (Landmark landmark : landmarkList) {
//            if (landmark.getType() == Landmark.BOTTOM_MOUTH) {
//                mouthPosition = landmark.getPosition();
//            } else if (landmark.getType() == Landmark.NOSE_BASE) {
//                nosePosition = landmark.getPosition();
//            } else if (landmark.getType() == Landmark.LEFT_EYE) {
//                eyePosition = landmark.getPosition();
//            }
//        }
//        if (nosePosition != null && mouthPosition != null) {
//            Log.i("Landmark", "Y dist from nose to mouth: " + (mouthPosition.y - nosePosition.y));
//        } else if (nosePosition == null) {
//            Log.i("Landmark", "Could not find nose");
//        } else if (mouthPosition == null) {
//            Log.i("Landmark", "Could not find mouth");
//        }
//
//        if (nosePosition != null && eyePosition != null) {
//            Log.i("Landmark", "Y dist from nose to eye: " + (nosePosition.y - eyePosition.y));
//        } else if (nosePosition == null) {
//            Log.i("Landmark", "Could not find nose");
//        } else if (eyePosition == null) {
//            Log.i("Landmark", "Could not find eye");
//        }
