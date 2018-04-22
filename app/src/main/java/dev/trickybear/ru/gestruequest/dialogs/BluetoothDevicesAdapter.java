package dev.trickybear.ru.gestruequest.dialogs;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import dev.trickybear.ru.gestruequest.R;
import dev.trickybear.ru.gestruequest.GestureQuest;


class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.DeviceViewHolder> {

    private Context context;
    private Map<String, BluetoothDevice> devices;

    BluetoothDevicesAdapter(Context context) {
        this.context = context;
        devices = new HashMap<>();
    }

    void addDevice(BluetoothDevice device) {
        if (device.getName() != null) {
            devices.put(device.getAddress(), device);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_device_view, null);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.update(position);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int position;
        private boolean connected;
        private TextView buttonTextView;

        private TextView nameTextView;
        private TextView statusTextView;

        DeviceViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.device_name_text_view);
            statusTextView = (TextView) itemView.findViewById(R.id.order_status_text_view);

            buttonTextView = (TextView) itemView.findViewById(R.id.connect_button_text_view);

            itemView.findViewById(R.id.connect_button).setOnClickListener(this);
        }

        void update(int position) {
            this.position = position;
            BluetoothDevice bluetoothDevice = (BluetoothDevice) devices.values().toArray()[position];

            nameTextView.setText(bluetoothDevice.getName());

            connected = GestureQuest.getBluetoothService().getConnectedDevice() != null
                    && GestureQuest.getBluetoothService().getConnectedDevice().getAddress().equals(bluetoothDevice.getAddress());


            Context context = nameTextView.getContext();
            if (connected) {
                statusTextView.setText(context.getString(R.string.connected_status));
                buttonTextView.setText(context.getString(R.string.disconnect));
                buttonTextView.setTextColor(context.getResources().getColor(R.color.gray_with_opacity));
            }
            else {
                statusTextView.setText(context.getString(R.string.not_connected_status));
                buttonTextView.setText(context.getString(R.string.connect));
                buttonTextView.setTextColor(context.getResources().getColor(R.color.gold_color_with_opacity));
            }
        }

        @Override
        public void onClick(View view) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice) devices.values().toArray()[position];
            if (connected) {
                new AlertDialog.Builder(context)
                        .setIcon(ContextCompat.getDrawable(context, R.drawable.monitor_icon))
                        .setTitle(R.string.disconnect)
                        .setMessage(R.string.disconnect_from_monitor)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GestureQuest.getBluetoothService().disconnectBluetooth();
                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            }
            else {
                GestureQuest.getBluetoothService().connectBluetoothDevice(bluetoothDevice.getAddress());
            }
        }
    }
}
