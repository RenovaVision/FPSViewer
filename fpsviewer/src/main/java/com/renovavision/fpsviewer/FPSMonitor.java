package com.renovavision.fpsviewer;

import android.view.Choreographer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alexandr Golovach on 28.11.2015.
 */
public class FPSMonitor implements Choreographer.FrameCallback {

    private Choreographer choreographer;

    private List<FPSListener> listeners = new ArrayList<>();

    private long mFrameStartTime = 0;
    private int mFramesRenderedCount = 0;
    private int mInterval = 500; // should be value in milliseconds

    FPSMonitor() {
        choreographer = Choreographer.getInstance();
    }

    public void start() {
        choreographer.postFrameCallback(this);
    }

    public void stop() {
        mFrameStartTime = 0;
        mFramesRenderedCount = 0;
        choreographer.removeFrameCallback(this);
    }

    public void addListener(FPSListener l) {
        listeners.add(l);
    }

    public void setInterval(int mInterval) {
        this.mInterval = mInterval;
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        long currentTimeMillis = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos);

        if (mFrameStartTime > 0) {

            final long timeStamp = currentTimeMillis - mFrameStartTime;
            mFramesRenderedCount++;

            if (timeStamp > mInterval) {
                final double fps = mFramesRenderedCount * 1000 / (double) timeStamp;

                mFrameStartTime = currentTimeMillis;
                mFramesRenderedCount = 0;

                // notify listeners
                for (FPSListener audience : listeners) {
                    audience.fpsChanged(fps);
                }
            }
        } else {
            mFrameStartTime = currentTimeMillis;
        }

        choreographer.postFrameCallback(this);
    }
}
