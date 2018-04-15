package dev.trickybear.ru.gesturequest.bluetooth;

import android.content.Context;
import android.util.Log;

import com.studiomobile.bluetooth.message.Action;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import rfid.studiomobile.com.entities.Event;
import rfid.studiomobile.com.entities.banners.Banner;
import rfid.studiomobile.com.entities.order.OrderItem;
import rfid.studiomobile.com.entities.tools.ALog;
import timber.log.Timber;

/**
 * Created by nikolay on 24.08.16.
 */
public class BluetoothService extends BluetoothManager {


    public BluetoothService(Context context) {
        super(context);
    }

    public void sendOrderItem(OrderItem orderItem) {
        Timber.d("sendOrderItem() called with: orderItem = [" + orderItem + "]");
        try {
            JSONObject orderItemJson = new JSONObject();
            JSONObject productItemJson = new JSONObject(orderItem.getProduct().getDataMap());
            orderItemJson.put("action", Action.ORDER_ITEM);
            orderItemJson.put("product", productItemJson);
            orderItemJson.put("quantity", orderItem.getCount());

            bluetoothController.write(orderItemJson.toString().getBytes());
        } catch (JSONException e) {}
    }

    public void sendEventData(Event event, List<Float> definedTips) {
        try {
            JSONObject eventObject = new JSONObject(event.getDataMap());


            StringBuilder definedTipsStringBuilder = new StringBuilder();

            for (Float i : definedTips) {
                definedTipsStringBuilder.append(i);
                definedTipsStringBuilder.append('#');
            }

            eventObject.put("defined_tips", definedTipsStringBuilder.toString());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", Action.EVENT_DATA);
            jsonObject.put("event", eventObject);

            bluetoothController.write(jsonObject.toString().getBytes());
        } catch (JSONException e) {}
    }

    public void sendPaymentStatus(int status) {
        try {
            JSONObject statusJson = new JSONObject();
            statusJson.put("action", Action.PAYMENT_STATUS);
            statusJson.put("status", status);

            bluetoothController.write(statusJson.toString().getBytes());

        } catch (JSONException e) {}
    }

    public void sendOrderClear() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", Action.CLEAR_ORDER);

            bluetoothController.write(jsonObject.toString().getBytes());

        } catch (JSONException e) {}

    }

    public void sendOrderFormed() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", Action.ORDER_FORMED);

            bluetoothController.write(jsonObject.toString().getBytes());

        } catch (JSONException e) {}
    }

    public void sendOrderFormedQR() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", Action.ORDER_FORMED_QR);

            bluetoothController.write(jsonObject.toString().getBytes());

        } catch (JSONException e) {}
    }

    public void sendBackToOrder() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", Action.BACK_TO_ORDER);

            bluetoothController.write(jsonObject.toString().getBytes());

        } catch (JSONException e) {}
    }

    public void sendTryAgainPayment() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", Action.TRY_AGAIN_PAYMENT);

            bluetoothController.write(jsonObject.toString().getBytes());

        } catch (JSONException e) {}
    }

    // TODO: 14/06/2017 run it async
    //We have to make a pause between banner's messages otherwise monitor can receive a few banners in a single message
    public void sendBanners(List<Banner> banners) {
        Collections.sort(banners);
        try {
            for (Banner bannerItem : banners) {
                JSONObject bannerJson = new JSONObject();
                bannerJson.put("image",bannerItem.getImageUrl());
                bannerJson.put("position", bannerItem.getPosition());
                if(banners.indexOf(bannerItem) < banners.size() - 1)
                    bannerJson.put("action", Action.BANNER_DATA);
                else
                    bannerJson.put("action", Action.LAST_BANNER);
                Thread.sleep(300);
                bluetoothController.write(bannerJson.toString().getBytes());

            }
        } catch (JSONException ex) {
            ALog.debug("Error while creating JSON array with banners " + ex.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
