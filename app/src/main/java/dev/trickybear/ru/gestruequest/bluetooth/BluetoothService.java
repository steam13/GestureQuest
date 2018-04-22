package dev.trickybear.ru.gestruequest.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import co.lujun.lmbluetoothsdk.base.BluetoothListener;
import co.lujun.lmbluetoothsdk.base.State;

public class BluetoothService {

    private final BluetoothManager bluetoothManager;
    private boolean isMonitorReady;
    private Handler bluetoothMessagesHandler;

    public BluetoothService(Context context) {
        this.bluetoothManager = new BluetoothManager(context);
        setBluetoothEventListener();
    }

    public boolean isMonitorReady() {
        return isMonitorReady;
    }

    public void setBluetoothMessagesCallback(Handler.Callback callback) {
        this.bluetoothMessagesHandler = new Handler(callback);
    }

    public void disconnectBluetooth() {
        bluetoothManager.disconnect();
    }

    public BluetoothDevice getConnectedDevice() {
        return bluetoothManager.getConnectedDevice();
    }

    public int getBluetoothConnectionState() {
        return bluetoothManager.getConnectionState();
    }

    public boolean isBluetoothEnabled() {
        return bluetoothManager.isEnabled();
    }

    public boolean isBluetoothAvailable() {
        return bluetoothManager.isAvailable();
    }

    public boolean startBluetoothScan() {
        return bluetoothManager.startScan();
    }

    public boolean cancelBluetoothScan() {
        return bluetoothManager.cancelScan();
    }

    public void connectBluetoothDevice(String device) {
        bluetoothManager.cancelScan();
        bluetoothManager.disconnect();
        bluetoothManager.connect(device);
    }

    public void sendAction(String action) {
        if (isMonitorReady)
            bluetoothManager.sendAction(action);
    }


    private void setBluetoothEventListener() {
        BluetoothListener bluetoothListener = new BluetoothListener() {
            @Override
            public void onReadData(final BluetoothDevice device, final byte[] data) {

                String command = new String(data).substring(0,1);
                Message message = new Message();
                message.what = MessagesCodes.BLUETOOTH_DATA_RECEIVED;
                message.obj = command;

                if (!command.isEmpty()) {
                    isMonitorReady = true;
                }

                sendMessage(message);
            }

            @Override
            public void onActionStateChanged(final int preState, final int state) {
            }

            @Override
            public void onActionDiscoveryStateChanged(final String discoveryState) {
                Message message = new Message();
                message.what = MessagesCodes.DISCOVERY_STATE;
                message.obj = discoveryState;
                sendMessage(message);
            }

            @Override
            public void onActionScanModeChanged(final int preScanMode, final int scanMode) {
            }

            @Override
            public void onBluetoothServiceStateChanged(final int state) {

                Message message = new Message();
                message.what = MessagesCodes.BLUETOOTH_STATE;
                message.obj = state;
                sendMessage(message);

                if (state == State.STATE_CONNECTED) {
                    isMonitorReady = true;
                }


                if (state == State.STATE_DISCONNECTED) {
                    isMonitorReady = false;
                    bluetoothManager.disconnect();
                    Log.d("BlDisconnect", "disconnected");
                }
            }

            @Override
            public void onActionDeviceFound(final BluetoothDevice device, final short rssi) {
                Message message = new Message();
                message.what = MessagesCodes.DEVICE_FOUND;
                message.obj = device;
                sendMessage(message);
            }

            private void sendMessage(Message message) {
                if (bluetoothMessagesHandler != null)
                    bluetoothMessagesHandler.sendMessage(message);
            }
        };
        bluetoothManager.setBluetoothListener(bluetoothListener);
    }
}
