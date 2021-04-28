package com.example.ecodrive.saferide.Services;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class DetectedActivityService extends IntentService {
    protected static final String TAG = DetectedActivityService.class.getSimpleName();
    LocalBroadcastManager activitybroadcaster;
    final static public String RECOGNITION_SERVICE_BROADCAST =
            DetectedActivityService.class.getName() + "DetectedActivityBroadcast";

    public DetectedActivityService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        activitybroadcaster = LocalBroadcastManager.getInstance(this);

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

        for (DetectedActivity activity : detectedActivities) {
            Log.i(TAG, "Detected activity: " + activity.getType() + ", " + activity.getConfidence());
            broadcastActivity(activity);
        }
    }
    private void broadcastActivity(DetectedActivity activity) {
        Intent intent = new Intent(DetectedActivityService.RECOGNITION_SERVICE_BROADCAST);
        intent.putExtra("type", activity.getType());
        intent.putExtra("confidence", activity.getConfidence());
        activitybroadcaster.sendBroadcast(intent);
    }
}

