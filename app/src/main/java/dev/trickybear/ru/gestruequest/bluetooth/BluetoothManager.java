package dev.trickybear.ru.gestruequest.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.UUID;
import co.lujun.lmbluetoothsdk.BluetoothController;
import co.lujun.lmbluetoothsdk.base.BluetoothListener;

public class BluetoothManager {

    private static final String ID = "00001101-0000-1000-8000-00805f9b34fb";

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

    public void sendAction(String action) {
        bluetoothController.write(action.getBytes());
    }
}