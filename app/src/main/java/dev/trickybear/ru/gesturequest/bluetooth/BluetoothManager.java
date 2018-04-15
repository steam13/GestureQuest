package dev.trickybear.ru.gesturequest.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.UUID;

import co.lujun.lmbluetoothsdk.BluetoothController;
import co.lujun.lmbluetoothsdk.base.BluetoothListener;

/**
 * Created by nikolay on 24.08.16.
 */
public class BluetoothManager {

    public static final String BLUETOOTH_LOG = "bluetooth ";
    private static final String ID = "57b22e5b-945a-4f40-8e78-c12ecb7ae142";

    protected BluetoothController bluetoothController;

    public BluetoothManager(Context context) {

        bluetoothController = BluetoothController.getInstance().build(context);
        bluetoothController.setAppUuid(UUID.fromString(ID));
    }

    public void disconnect() {
        bluetoothController.disconnect();
    }

    public void setBluetoothListener(BluetoothListener listener) {
        bluetoothController.setBluetoothListener(listener);
    }

    public void connect(BluetoothDevice bluetoothDevice) {
        bluetoothController.connect(bluetoothDevice.getAddress());
    }

    public BluetoothDevice getConnectedDevice() {
        return bluetoothController.getConnectedDevice();
    }

    public int getConnectionState() {
        return bluetoothController.getConnectionState();
    }

    public boolean isAvailable() {
        return bluetoothController.isAvailable();
    }

    public boolean isEnabled() {
        return bluetoothController.isEnabled();
    }

    public boolean startScan() {
        return bluetoothController.startScan();
    }

    public boolean cancelScan() {
        return bluetoothController.cancelScan();
    }
}
