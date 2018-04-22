package dev.trickybear.ru.gestruequest;

import android.app.Application;

import dev.trickybear.ru.gestruequest.bluetooth.BluetoothService;


public class GestureQuest extends Application {
    private static BluetoothService bluetoothService;

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothService = new BluetoothService(this);
    }

    public static BluetoothService getBluetoothService() {
        return bluetoothService;
    }
}
