package com.renovavision.fpsdemo;

import android.app.Application;
import android.graphics.Color;
import android.util.Log;

import com.renovavision.fpsviewer.FPSListener;
import com.renovavision.fpsviewer.FPSViewer;

/**
 * Created by Alexandr Golovach on 29.11.2015.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FPSViewer.initialize(this)
                .setInterval(250)
                .setTextAlpha(0.5f)
                .setTextColor(Color.WHITE)
                .setTextSize(16f)
                .setViewGravity(FPSViewer.FPSViewGravity.BOTTOM_RIGHT)
                .setListener(new FPSListener() {
                    @Override
                    public void fpsChanged(double fps) {
                        Log.d("FPS Viewer!", fps + " fps");
                    }
                }).start();
    }

    @Override
    public void onTerminate() {
        FPSViewer.finish();
        super.onTerminate();
    }
}
