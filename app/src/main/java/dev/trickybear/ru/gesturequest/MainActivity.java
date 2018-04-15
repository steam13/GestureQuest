package dev.trickybear.ru.gesturequest;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final boolean BITMAP_RENDERING_ANTIALIAS = true;
    private static final boolean BITMAP_RENDERING_DITHER = true;
    private static final int BITMAP_RENDERING_WIDTH = 32;
    private static final String TAG = "MainActivity";
    private OnGesturePerformedListener handleGesture = new C02283();
    private CountDownTimer mCountDownTimer;
    private int mCounter = 0;
    private View mDecorView;
    private GestureLibrary mGestureLib;
    private GestureOverlayView mGestureOverlay;
    private ImageView mIvResult;
    private Gesture mSettingsGesture;
    private Gesture mUnlockGesture;

    class C02251 implements OnClickListener {
        C02251() {
        }

        public void onClick(View v) {
            if (MainActivity.this.mCountDownTimer != null) {
                MainActivity.this.mCountDownTimer.cancel();
            }
            MainActivity.this.mCounter = MainActivity.this.mCounter + 1;
            MainActivity.this.mCountDownTimer = new CountDownTimer(2000, 100) {
                public void onTick(long millisUntilFinished) {
                    Log.d(MainActivity.TAG, "CountDownTimer onTick: " + millisUntilFinished);
                }

                public void onFinish() {
                    MainActivity.this.mCounter = 0;
                    Log.d(MainActivity.TAG, "CountDownTimer onFinish: ");
                }
            };
            MainActivity.this.mCountDownTimer.start();
            Log.d(MainActivity.TAG, "onClick: ");
        }
    }

    class C02272 implements OnLongClickListener {

        class C02261 implements AnimatorListener {
            C02261() {
            }

            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                MainActivity.this.mGestureOverlay.setGestureVisible(true);
                MainActivity.this.mIvResult.setVisibility(4);
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        }

        C02272() {
        }

        public boolean onLongClick(View v) {
            Log.d(MainActivity.TAG, "onLongClick: " + MainActivity.this.mCounter);
            if (MainActivity.this.mCounter > 9) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            } else if (MainActivity.this.mCounter > 3) {
                MainActivity.this.mIvResult.animate().alpha(0.0f).setDuration(300).setListener(new C02261());
            }
            return false;
        }
    }

    class C02283 implements OnGesturePerformedListener {
        C02283() {
        }

        public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
            ArrayList<Prediction> predictions = MainActivity.this.mGestureLib.recognize(gesture);
            Log.d(MainActivity.TAG, "recog thayu");
            if (predictions != null && predictions.size() > 0 && ((Prediction) predictions.get(0)).score > Consts.accuracy) {
                MainActivity.this.mGestureOverlay.setGestureVisible(false);
                MainActivity.this.mIvResult.setAlpha(0.0f);
                MainActivity.this.mIvResult.setVisibility(0);
                MainActivity.this.mIvResult.setImageBitmap(MainActivity.this.toBmp(MainActivity.this.mIvResult.getWidth(), MainActivity.this.mIvResult.getWidth(), 32, 16, -65536));
                MainActivity.this.mIvResult.animate().alpha(1.0f).setDuration(300).setListener(null);
                Handler handler = new Handler();
                handler.postDelayed(
                        new Runnable() {
                            public void run() {
                                MainActivity.this.mGestureOverlay.setGestureVisible(true);
                                MainActivity.this.mIvResult.setVisibility(View.INVISIBLE);
                            }
                        }, 3000L);

            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mDecorView = getWindow().getDecorView();
        this.mIvResult = findViewById(R.id.iv_result);
        this.mIvResult.setVisibility(View.INVISIBLE);
    }

    protected void onResume() {
        super.onResume();
        this.mGestureLib = GestureLibraries.fromFile(getFilesDir() + "/gestures.txt");
        this.mGestureLib.setOrientationStyle(1);
        this.mGestureLib.setSequenceType(1);
        this.mGestureLib.load();
        for (String gestureName : this.mGestureLib.getGestureEntries()) {
            ArrayList<Gesture> gestures = this.mGestureLib.getGestures(gestureName);
            if (Consts.UNLOCK.equals(gestureName)) {
                this.mUnlockGesture = (Gesture) gestures.get(0);
            }
        }
        this.mGestureOverlay = findViewById(R.id.gestures);
        this.mGestureOverlay.addOnGesturePerformedListener(this.handleGesture);
        this.mGestureOverlay.setGestureStrokeAngleThreshold(90.0f);
        this.mGestureOverlay.setOnClickListener(new C02251());
        this.mGestureOverlay.setOnLongClickListener(new C02272());
        hideSystemUI();
    }

    public Bitmap toBmp(int width, int height, int edge, int numSample, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate((float) edge, (float) edge);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Style.STROKE);
        paint.setStrokeJoin(Join.ROUND);
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeWidth(32.0f);
        paint.setMaskFilter(new BlurMaskFilter(16.0f, Blur.SOLID));
        ArrayList<GestureStroke> strokes = this.mUnlockGesture.getStrokes();
        int count = strokes.size();
        for (int i = 0; i < count; i++) {
            canvas.drawPath(((GestureStroke) strokes.get(i)).toPath((float) (width - (edge * 2)), (float) (height - (edge * 2)), numSample), paint);
        }
        return bitmap;
    }

    private Bitmap toBmp(int width, int height, int inset, int color) {
        float scale;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Style.STROKE);
        paint.setStrokeJoin(Join.ROUND);
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeWidth(32.0f);
        Path path = toPath();
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        float sx = ((float) (width - (inset * 2))) / bounds.width();
        float sy = ((float) (height - (inset * 2))) / bounds.height();
        if (sx > sy) {
            scale = sy;
        } else {
            scale = sx;
        }
        paint.setStrokeWidth(32.0f / scale);
        path.offset((-bounds.left) + ((((float) width) - (bounds.width() * scale)) / 2.0f), (-bounds.top) + ((((float) height) - (bounds.height() * scale)) / 2.0f));
        canvas.translate((float) inset, (float) inset);
        canvas.scale(scale, scale);
        canvas.drawPath(path, paint);
        return bitmap;
    }

    public Path toPath() {
        return toPath(null);
    }

    public Path toPath(Path path) {
        if (path == null) {
            path = new Path();
        }
        ArrayList<GestureStroke> strokes = this.mUnlockGesture.getStrokes();
        int count = strokes.size();
        for (int i = 0; i < count; i++) {
            path.addPath(((GestureStroke) strokes.get(i)).getPath());
        }
        return path;
    }

    private void hideSystemUI() {
        this.mDecorView.setSystemUiVisibility(5894);
    }

    private void showSystemUI() {
        this.mDecorView.setSystemUiVisibility(1792);
    }
}
