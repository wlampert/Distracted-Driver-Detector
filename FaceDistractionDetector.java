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
