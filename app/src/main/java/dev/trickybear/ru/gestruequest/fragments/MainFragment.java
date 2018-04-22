package dev.trickybear.ru.gestruequest.fragments;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Fragment;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import dev.trickybear.ru.gestruequest.Consts;
import dev.trickybear.ru.gestruequest.GestureQuest;
import dev.trickybear.ru.gestruequest.MainActivity;
import dev.trickybear.ru.gestruequest.R;
import dev.trickybear.ru.gestruequest.bluetooth.Action;
import dev.trickybear.ru.gestruequest.utils.GestureImageUtils;


public class MainFragment extends Fragment implements OnGesturePerformedListener {
    public static final String TAG = "MainFragment";
    private CountDownTimer countDownTimer;
    private int counter = 0;
    private GestureLibrary gestureLib;
    private GestureOverlayView gestureOverlay;
    private ImageView resultImageView;
    private Gesture unlockGesture;
    private MainActivity mainActivity;
    private boolean isRecognizeAvailable = false;
    private Handler unlockedStateHandler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        resultImageView = view.findViewById(R.id.iv_result);
        resultImageView.setVisibility(View.INVISIBLE);

        gestureOverlay = view.findViewById(R.id.gestures);
        gestureOverlay.addOnGesturePerformedListener(this);
        gestureOverlay.setGestureStrokeAngleThreshold(90.0f);
        gestureOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        gestureOverlay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: " + counter);
                if (counter > 9) {
                    mainActivity.showSettingsFragment();
                } else if (counter > 3) {
                    resultImageView.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            gestureOverlay.setGestureVisible(true);
                            resultImageView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        gestureLib = GestureLibraries.fromFile(getActivity().getFilesDir() + "/gestures.txt");
        gestureLib.setOrientationStyle(1);
        gestureLib.setSequenceType(1);
        gestureLib.load();
        for (String gestureName : gestureLib.getGestureEntries()) {
            ArrayList<Gesture> gestures = gestureLib.getGestures(gestureName);
            if (gestureName.equals(Consts.UNLOCK)) {
                unlockGesture = gestures.get(0);
            }
        }
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
        Log.d(TAG, "recog thayu");
        if (isRecognizeAvailable && predictions != null && predictions.size() > 0 && (predictions.get(0)).score > Consts.accuracy) {
            gestureOverlay.setGestureVisible(false);
            resultImageView.setAlpha(0.0f);
            resultImageView.setVisibility(View.VISIBLE);
            resultImageView.setImageBitmap(GestureImageUtils.toBitmap(resultImageView, unlockGesture));
            resultImageView.animate().alpha(1.0f).setDuration(300).setListener(null);
            GestureQuest.getBluetoothService().sendAction(Action.UNLOCKED);
            unlockedStateHandler = new Handler();
            unlockedStateHandler.postDelayed(
                    new Runnable() {
                        public void run() {
                            gestureOverlay.setGestureVisible(true);
                            resultImageView.setVisibility(View.INVISIBLE);
                        }
                    }, 60L * 1000 * 15);

        }
    }

    public void setRecognizing(boolean ability) {
        isRecognizeAvailable = ability;
    }

    public void restart() {
        if (unlockedStateHandler != null) {
            unlockedStateHandler.removeCallbacksAndMessages(null);
            gestureOverlay.setGestureVisible(true);
            resultImageView.setVisibility(View.INVISIBLE);
            isRecognizeAvailable = false;
        }
    }


}
