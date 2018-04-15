package dev.trickybear.ru.gesturequest.bluetooth.message;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import rfid.studiomobile.com.entities.tools.ALog;
import rfid.studiomobile.com.entities.tools.JsonHelper;

public class BluetoothDataParser {

    public static BluetoothMessage parseBluetoothData(byte[] data) {

        try {
            String dataString = new String(data);

            JSONObject rootObject = new JSONObject(dataString);
            String actionString = JsonHelper.getStringOrEmptyString(rootObject, "action");

            Action action = Action.valueOf(actionString);

            switch (action) {
                case RFID_DATA:
                    String rfidTocheck = JsonHelper.getStringOrEmptyString(rootObject, "rfid");
                    Log.d("bluetooth", "receive RFID to check : " + rfidTocheck);
                    return new BluetoothMessage(action, rfidTocheck);

                case DRINK_TICKET_DATA:
                    String drinkTicketUid = JsonHelper.getStringOrEmptyString(rootObject, "rfid");
                    String drinkTicketPin = JsonHelper.getStringOrEmptyString(rootObject, "pin");

                    Log.d("bluetooth", "receive order data rfid : " + drinkTicketUid + " pin : " + drinkTicketPin);
                    return new BluetoothMessage(action, drinkTicketUid, drinkTicketPin);

                case ORDER_DATA:
                    String rfid = JsonHelper.getStringOrEmptyString(rootObject, "rfid");
                    String pin = JsonHelper.getStringOrEmptyString(rootObject, "pin");

                    Log.d("bluetooth", "receive order data rfid : " + rfid + " pin : " + pin);
                    return new BluetoothMessage(action, rfid, pin);

                case MONITOR_READY:
                    return new BluetoothMessage(action);

                case TRY_AGAIN_PAYMENT:
                    return new BluetoothMessage(action);

                case TIPS_SIZE:
                    int size = Integer.parseInt(JsonHelper.getStringOrEmptyString(rootObject, "percent"));

                    Log.d("bluetooth", "receive tips size : " + size);

                    return new BluetoothMessage(action, size);

                default:
                    Log.d("bluetooth", "receive data");
            }

        } catch (JSONException j) {
            ALog.error(j);
        }

        return new BluetoothMessage(Action.UNKNOWN);

    }

}
