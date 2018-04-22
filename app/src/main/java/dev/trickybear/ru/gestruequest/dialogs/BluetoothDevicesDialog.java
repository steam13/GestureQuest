package dev.trickybear.ru.gestruequest.dialogs;


import android.app.Activity;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import co.lujun.lmbluetoothsdk.base.State;
import dev.trickybear.ru.gestruequest.MainActivity;
import dev.trickybear.ru.gestruequest.R;
import dev.trickybear.ru.gestruequest.GestureQuest;


public class BluetoothDevicesDialog extends DialogFragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private ProgressBar progressBar;
    private BluetoothDevicesAdapter adapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        adapter = new BluetoothDevicesAdapter(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
        adapter = new BluetoothDevicesAdapter(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.monitor_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getDialog().setCancelable(false);

        progressBar =  view.findViewById(R.id.progress_bar);

        RecyclerView devicesView = view.findViewById(R.id.devices_recycler_view);
        devicesView.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false));
        devicesView.setAdapter(adapter);

        view.findViewById(R.id.arrow_back).setOnClickListener(this);
        view.findViewById(R.id.close_button).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        GestureQuest.getBluetoothService().startBluetoothScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        GestureQuest.getBluetoothService().cancelBluetoothScan();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.arrow_back:
                dismiss();
                break;
            case R.id.close_button:
                dismiss();
                break;

        }
    }

    public void deviceFound(BluetoothDevice device) {
        if (adapter != null) {
            adapter.addDevice(device);
        } else {
            Log.d("BT","Failed to add device");
        }
    }

    public void notifyConnectionStateChanged() {
        if (adapter == null) {
            Log.d("BT","Failed to update connection state UI");
            return;
        }
        adapter.notifyDataSetChanged();
        if (GestureQuest.getBluetoothService().getBluetoothConnectionState() == State.STATE_CONNECTING)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
    }

}
