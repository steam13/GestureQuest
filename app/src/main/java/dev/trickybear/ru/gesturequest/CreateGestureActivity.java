package dev.trickybear.ru.gesturequest;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class CreateGestureActivity extends AppCompatActivity {
    private static final String TAG = "CreateGestureActivity";
    private Gesture mCurrentGesture;
    private View mDecorView;
    private boolean mGestureDrawn;
    private GestureLibrary mGestureLib;
    private OnGestureListener mGestureListener = new C02201();
    private String mGestureName = Consts.UNLOCK;

    class C02201 implements OnGestureListener {
        C02201() {
        }

        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
            CreateGestureActivity.this.mGestureDrawn = true;
        }

        public void onGesture(GestureOverlayView overlay, MotionEvent event) {
            CreateGestureActivity.this.mCurrentGesture = overlay.getGesture();
        }

        public void onGestureEnded(GestureOverlayView gestureView, MotionEvent motion) {
        }

        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
        }
    }

    class C02212 implements OnClickListener {
        C02212() {
        }

        public void onClick(View v) {
            CreateGestureActivity.this.finish();
        }
    }

    class C02223 implements OnClickListener {
        C02223() {
        }

        public void onClick(View v) {
            if (CreateGestureActivity.this.mGestureDrawn) {
                CreateGestureActivity.this.saveGesture();
            }
        }
    }

    class C02234 implements OnClickListener {
        C02234() {
        }

        public void onClick(View v) {
            CreateGestureActivity.this.reDrawGestureView();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reDrawGestureView();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                reDrawGestureView();
                break;
            case R.id.save:
                if (this.mGestureDrawn) {
                    saveGesture();
                    break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveGesture() {
        if (this.mGestureLib.getGestures(this.mGestureName) != null) {
            this.mGestureLib.removeEntry(this.mGestureName);
        }
        this.mGestureLib.addGesture(this.mGestureName, this.mCurrentGesture);
        this.mGestureLib.save();
        finish();
    }

    private void resetEverything() {
        this.mGestureDrawn = false;
        this.mCurrentGesture = null;
    }

    private void reDrawGestureView() {
        setContentView(R.layout.activity_create_gesture);
        this.mDecorView = getWindow().getDecorView();
        openOptionsMenu();
        this.mGestureLib = GestureLibraries.fromFile(getFilesDir() + "/gestures.txt");
        this.mGestureLib.load();
        GestureOverlayView gestures = findViewById(R.id.save_gesture);
        gestures.removeAllOnGestureListeners();
        gestures.addOnGestureListener(this.mGestureListener);
        resetEverything();
        findViewById(R.id.iv_back).setOnClickListener(new C02212());
        findViewById(R.id.iv_save).setOnClickListener(new C02223());
        findViewById(R.id.iv_delete).setOnClickListener(new C02234());
        hideSystemUI();
    }

    private void hideSystemUI() {
        this.mDecorView.setSystemUiVisibility(5894);
    }

    private void showSystemUI() {
        this.mDecorView.setSystemUiVisibility(1792);
    }
}
