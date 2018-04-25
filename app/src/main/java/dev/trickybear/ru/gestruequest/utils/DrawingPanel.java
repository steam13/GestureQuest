package dev.trickybear.ru.gestruequest.utils;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;

import dev.trickybear.ru.gestruequest.R;

public class DrawingPanel extends View implements View.OnTouchListener {
    private CountDownTimer countDownTimer;
    private int counter = 0;
    private static final String TAG = "DrawView";
    private double startTime, deltaTime;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private LinkedList<Path> paths = new LinkedList<Path>();
    private SettingsCallback callback;

    public interface SettingsCallback {
        void openSettings();

        void hideResult();
    }

    public void setCallback(SettingsCallback callback) {
        this.callback = callback;
    }

    public DrawingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);

        mPaint = new Paint();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getResources().getColor(R.color.colorYellow));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(32.0f);
        mPaint.setMaskFilter(new BlurMaskFilter(16.0f, BlurMaskFilter.Blur.SOLID));
        mCanvas = new Canvas();
        mPath = new Path();
        paths.add(mPath);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Path p : paths) {
            canvas.drawPath(p, mPaint);
        }
    }


    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        reset();
        animate().cancel();
        setAlpha(1.0f);
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        animate().alpha(0.0f).setDuration(500);


    }

    public void reset() {
        paths.clear();
        mPath = new Path();
        paths.add(mPath);
        invalidate();
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                startTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                deltaTime = (System.currentTimeMillis() - startTime);
                if (deltaTime > 1000) {

                    if (counter > 9) {
                        callback.openSettings();
                        reset();
                        break;
                    }
                    if (counter > 3) {
                        callback.hideResult();
                        reset();
                        break;
                    }
                }

                touch_up();
                invalidate();
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                counter++;
                countDownTimer = new CountDownTimer(2000, 100) {
                    public void onTick(long millisUntilFinished) {
                        Log.d(TAG, "CountDownTimer onTick: " + millisUntilFinished);
                    }

                    public void onFinish() {
                        counter = 0;
                        Log.d(TAG, "CountDownTimer onFinish: ");
                    }
                };
                countDownTimer.start();
                Log.d(TAG, "onClick: ");
                break;
        }
        return true;
    }

    public int getCounter() {
        return counter;
    }
}