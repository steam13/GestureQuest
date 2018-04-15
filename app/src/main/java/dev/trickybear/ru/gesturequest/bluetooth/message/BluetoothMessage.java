package dev.trickybear.ru.gesturequest.bluetooth.message;

/**
 * Created by nikolay on 31.08.16.
 */
public class BluetoothMessage {

    private Action action;
    private Object[] data;

    public BluetoothMessage(Action action, Object... data) {
        this.action = action;
        this.data = data;
    }

    public Action getAction() {
        return action;
    }

    public Object[] getData() {
        return data;
    }
}
