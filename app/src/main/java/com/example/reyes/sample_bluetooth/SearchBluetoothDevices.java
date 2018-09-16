package com.example.reyes.sample_bluetooth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class SearchBluetoothDevices {

    private static final int REQUEST_ENABLE_BT = 1;
    private Activity myActivity;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice device;
    private ArrayList<BluetoothDevice> pairedDevices = new ArrayList<>();
    private BluetoothDeviceAdapter mNewDevicesArrayAdapter;
    ProgressDialog dialog;
    Context _context;
    BluetoothAdapter adapter;


    public SearchBluetoothDevices(Context context, Activity activity) {

        myActivity = activity;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        _context = context;
        dialog = new ProgressDialog(_context);
        adapter = BluetoothAdapter.getDefaultAdapter();
        //blueToothReceiver = null;
        mNewDevicesArrayAdapter = new BluetoothDeviceAdapter(_context, pairedDevices);
    }

    public ArrayList<BluetoothDevice> GetPairedBluetoothDevices() {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList<BluetoothDevice> alDevices = new ArrayList<BluetoothDevice>();

        if(pairedDevices.size() > 0) {
            Toast.makeText(_context, "Finished with the discovery!", Toast.LENGTH_LONG).show();
            for (BluetoothDevice bt : pairedDevices) {
                alDevices.add(bt);
            }
            return alDevices;
        } else {
            return null;
        }
    }

    public void DiscoverBluetoothDevices(BroadcastReceiver blueToothReceiver) {

        Toast.makeText(_context, "Attempting to discover devices...", Toast.LENGTH_SHORT).show();
        Log.d("DiscBTDevices", "Attempting to discover devices...");

        //IntentFilter will match the action specified
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        //broadcast receiver for any matching filter
        _context.registerReceiver(blueToothReceiver, filter);

        if(mBluetoothAdapter.isDiscovering()) {
            // Bluetooth is already in discovery mode, we cancel to restart it again
            mBluetoothAdapter.cancelDiscovery();
            _context.unregisterReceiver(blueToothReceiver);
        } mBluetoothAdapter.startDiscovery();

        //return pairedDevices;
    }

}
