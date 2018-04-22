package dev.trickybear.ru.gestruequest.fragments;

import android.app.Fragment;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.ArrayList;

import dev.trickybear.ru.gestruequest.Consts;
import dev.trickybear.ru.gestruequest.MainActivity;
import dev.trickybear.ru.gestruequest.R;


public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";
    private SeekBar gestureAccuracyBar;
    private GestureLibrary gestureLib;
    private ImageView gestureImagePreview;
    private LinearLayout unlockContainer;
    private LinearLayout unlockUndefinedContainer;
    private Gesture unlockGesture;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        gestureLib = GestureLibraries.fromFile(getActivity().getFilesDir() + "/gestures.txt");
        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unlockContainer = view.findViewById(R.id.ll_unlock_container);
        unlockUndefinedContainer = view.findViewById(R.id.ll_unlock_undefined_container);
        gestureAccuracyBar = view.findViewById(R.id.sb_accuracy);
        gestureImagePreview = view.findViewById(R.id.iv_unlock_preview);

        view.findViewById(R.id.btn_unlock_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.showSetGestureFragment();
            }
        });

        view.findViewById(R.id.btn_unlock_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.showSetGestureFragment();
            }
        });

        view.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Consts.accuracy = gestureAccuracyBar.getProgress();
                mainActivity.goBack();
            }
        });

        view.findViewById(R.id.btn_bluetooth_dg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.showBluetoothDevicesDialog();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        loadGestures();
        gestureAccuracyBar.setProgress(Consts.accuracy);
        if (unlockGesture == null) {
            unlockUndefinedContainer.setVisibility(View.VISIBLE);
            unlockContainer.setVisibility(View.GONE);
        } else {
            unlockUndefinedContainer.setVisibility(View.GONE);
            unlockContainer.setVisibility(View.VISIBLE);
            gestureImagePreview.setImageBitmap(unlockGesture.toBitmap(480, 480, 3, -65281));
        }
    }

    private void loadGestures() {
        unlockGesture = null;
        try {
            gestureLib = GestureLibraries.fromFile(getActivity().getFilesDir() + "/gestures.txt");
            gestureLib.load();
            for (String gestureName : gestureLib.getGestureEntries()) {
                ArrayList<Gesture> gestures = gestureLib.getGestures(gestureName);
                if (Consts.UNLOCK.equals(gestureName)) {
                    unlockGesture = gestures.get(0);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "loadGestures: ", e);
        }
    }
}
