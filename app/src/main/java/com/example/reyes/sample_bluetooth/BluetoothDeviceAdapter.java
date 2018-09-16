package com.example.reyes.sample_bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {

    // reference - https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
    public BluetoothDeviceAdapter(Context context, ArrayList<BluetoothDevice> btDevices) {
        super(context, 0, btDevices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BluetoothDevice device = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_bluetooth_device, parent, false);
        }

        // Lookup view for data population
        TextView deviceName = (TextView) convertView.findViewById(R.id.bluetooth_name);
        TextView deviceAddress = (TextView) convertView.findViewById(R.id.bluetooth_address);

        // Populate the data into the template view using the data object.
        deviceName.setText(device.getName());
        deviceAddress.setText(device.getAddress());

        // Return the completed view to render on screen
        return convertView;

    }


}
