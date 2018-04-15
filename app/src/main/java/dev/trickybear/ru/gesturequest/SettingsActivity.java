package dev.trickybear.ru.gesturequest;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.os.Bundle;
import android.support.v4.view.InputDeviceCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    private Button mBtnDone;
    private Button mBtnUnlockAdd;
    private Button mBtnUnlockEdit;
    private SeekBar mSbGestureAccuracy;
    private View mDecorView;
    private GestureLibrary mGestureLib;
    private ImageView mIvUnlockPreview;
    private LinearLayout mLlUnlockContainer;
    private LinearLayout mLlUnlockUndefinedContainer;
    private Gesture mSettingsGesture;
    private Gesture mUnlockGesture;

    class C02301 implements OnClickListener {
        C02301() {
        }

        public void onClick(View v) {
            Consts.accuracy = mSbGestureAccuracy.getProgress();
            SettingsActivity.this.finish();
        }
    }

    class C02312 implements OnClickListener {
        C02312() {
        }

        public void onClick(View v) {
            SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, CreateGestureActivity.class));
        }
    }

    class C02323 implements OnClickListener {
        C02323() {
        }

        public void onClick(View v) {
            SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, CreateGestureActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.mDecorView = getWindow().getDecorView();
        this.mLlUnlockUndefinedContainer = findViewById(R.id.ll_unlock_undefined_container);
        this.mBtnUnlockAdd = findViewById(R.id.btn_unlock_add);
        this.mSbGestureAccuracy = findViewById(R.id.sb_accuracy);
        this.mLlUnlockContainer = findViewById(R.id.ll_unlock_container);
        this.mIvUnlockPreview = findViewById(R.id.iv_unlock_preview);
        this.mBtnUnlockEdit = findViewById(R.id.btn_unlock_edit);
        this.mBtnDone = findViewById(R.id.btn_done);
        this.mBtnDone.setOnClickListener(new C02301());
    }

    protected void onResume() {
        super.onResume();
        loadGestures();
        this.mSbGestureAccuracy.setProgress(Consts.accuracy);
        if (this.mUnlockGesture == null) {
            this.mLlUnlockUndefinedContainer.setVisibility(0);
            this.mBtnUnlockAdd.setOnClickListener(new C02312());
            this.mLlUnlockContainer.setVisibility(8);
        } else {
            this.mLlUnlockUndefinedContainer.setVisibility(8);
            this.mLlUnlockContainer.setVisibility(0);
            this.mIvUnlockPreview.setImageBitmap(this.mUnlockGesture.toBitmap(480, 480, 3, -65281));
            this.mBtnUnlockEdit.setOnClickListener(new C02323());
        }
        hideSystemUI();
    }

    private void loadGestures() {
        this.mUnlockGesture = null;
        this.mSettingsGesture = null;
        try {
            this.mGestureLib = GestureLibraries.fromFile(getFilesDir() + "/gestures.txt");
            this.mGestureLib.load();
            for (String gestureName : this.mGestureLib.getGestureEntries()) {
                ArrayList<Gesture> gestures = this.mGestureLib.getGestures(gestureName);
                if (Consts.UNLOCK.equals(gestureName)) {
                    this.mUnlockGesture = (Gesture) gestures.get(0);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "loadGestures: ", e);
        }
    }

    private void hideSystemUI() {
        this.mDecorView.setSystemUiVisibility(5894);
    }

    private void showSystemUI() {
        this.mDecorView.setSystemUiVisibility(1792);
    }
}
