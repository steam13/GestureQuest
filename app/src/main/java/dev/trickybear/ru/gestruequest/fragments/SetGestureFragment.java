package dev.trickybear.ru.gestruequest.fragments;

import android.app.Fragment;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import dev.trickybear.ru.gestruequest.Consts;
import dev.trickybear.ru.gestruequest.MainActivity;
import dev.trickybear.ru.gestruequest.R;


public class SetGestureFragment extends Fragment {
    private static final String TAG = "SetGestureFragment";
    private Gesture currentGesture;
    private boolean isGestureDrawn;
    private GestureLibrary gestureLib;
    GestureOverlayView gestureOverlay;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        gestureLib = GestureLibraries.fromFile(getActivity().getFilesDir() + "/gestures.txt");
        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_set_gestrue, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        gestureOverlay = view.findViewById(R.id.gesture_view);
        gestureOverlay.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
            @Override
            public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
                isGestureDrawn = true;
            }

            @Override
            public void onGesture(GestureOverlayView overlay, MotionEvent event) {
                currentGesture = overlay.getGesture();
            }

            @Override
            public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
            }

            @Override
            public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
            }
        });

        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mainActivity.goBack();
            }
        });

        view.findViewById(R.id.iv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGestureDrawn) {
                    saveGesture();
                }
            }
        });

        view.findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGestureDrawn = false;
                currentGesture = null;
                gestureOverlay.clear(false);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        gestureLib.load();
        isGestureDrawn = false;
        currentGesture = null;
        gestureOverlay.clear(false);
    }

    private void saveGesture() {
        if (gestureLib.getGestures(Consts.UNLOCK) != null) {
            gestureLib.removeEntry(Consts.UNLOCK);
        }
        gestureLib.addGesture(Consts.UNLOCK, currentGesture);
        gestureLib.save();
        mainActivity.goBack();
    }


}
