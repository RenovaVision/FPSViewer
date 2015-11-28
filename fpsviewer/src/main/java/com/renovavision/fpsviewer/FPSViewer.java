package com.renovavision.fpsviewer;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by Alexandr Golovach on 28.11.2015.
 */
public class FPSViewer {

    // gravity for fps view
    public enum FPSViewGravity {
        TOP_RIGHT(Gravity.TOP | Gravity.END),
        TOP_LEFT(Gravity.TOP | Gravity.START),
        BOTTOM_RIGHT(Gravity.BOTTOM | Gravity.END),
        BOTTOM_LEFT(Gravity.BOTTOM | Gravity.START);

        private int gravity;

        FPSViewGravity(int gravity) {
            this.gravity = gravity;
        }

        public int getGravity() {
            return gravity;
        }
    }

    private final static FPSViewerSession program = new FPSViewerSession();

    private FPSViewer() {
    }

    public static FPSViewerSession initialize(Context context) {
        return program.initialize(context);
    }

    public static void finish() {
        program.stop();
    }

    public static final class FPSViewerSession {

        private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.0' fps'");

        private FPSMonitor mFPSMonitor;

        private WindowManager mWindowManager;
        private View mFpsView;
        private TextView mFpsText;
        private WindowManager.LayoutParams mWindowParams;

        private boolean mIsShown = true;
        private boolean mIsPlaying = false;

        private FPSListener mFpsListener = new FPSListener() {
            @Override
            public void fpsChanged(double fps) {
                if (mFpsText != null) {
                    mFpsText.setText(DECIMAL_FORMAT.format(fps));
                }
            }
        };

        private FPSViewerSession() {
        }

        private FPSViewerSession initialize(Context context) {
            mFPSMonitor = new FPSMonitor();
            createWindowLayoutParams();

            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            LayoutInflater inflater = LayoutInflater.from(context);
            mFpsView = inflater.inflate(R.layout.fps_view, new RelativeLayout(context));
            mFpsText = (TextView) mFpsView.findViewById(R.id.fps_text_view);

            setListener(mFpsListener);

            return this;
        }

        private void createWindowLayoutParams() {
            mWindowParams = new WindowManager.LayoutParams();
            mWindowParams.width = LayoutParams.WRAP_CONTENT;
            mWindowParams.height = LayoutParams.WRAP_CONTENT;
            mWindowParams.type = LayoutParams.TYPE_TOAST;
            mWindowParams.flags = LayoutParams.FLAG_KEEP_SCREEN_ON | LayoutParams.FLAG_NOT_FOCUSABLE
                    | LayoutParams.FLAG_NOT_TOUCH_MODAL;
            mWindowParams.format = PixelFormat.TRANSLUCENT;
            mWindowParams.gravity = FPSViewGravity.BOTTOM_RIGHT.getGravity();
            mWindowParams.x = 10;
        }

        // start fps monitoring
        public void start() {
            mFPSMonitor.start();
            if (mIsShown && !mIsPlaying) {
                mWindowManager.addView(mFpsView, mWindowParams);
                mIsPlaying = true;
            }
        }

        // stop fps monitoring
        public void stop() {
            mFPSMonitor.stop();
            if (mIsShown && mFpsView != null) {
                mWindowManager.removeView(mFpsView);
                mIsPlaying = false;
            }
        }

        public FPSViewerSession setTextAlpha(float alpha) {
            mFpsText.setAlpha(alpha);
            return this;
        }

        public FPSViewerSession setTextColor(int color) {
            mFpsText.setTextColor(color);
            return this;
        }

        public FPSViewerSession setTextSize(float size) {
            mFpsText.setTextSize(size);
            return this;
        }

        public FPSViewerSession setInterval(int ms) {
            mFPSMonitor.setInterval(ms);
            return this;
        }

        public FPSViewerSession setListener(FPSListener audience) {
            mFPSMonitor.addListener(audience);
            return this;
        }

        public FPSViewerSession setViewGravity(FPSViewGravity seat) {
            mWindowParams.gravity = seat.getGravity();
            return this;
        }
    }

}
