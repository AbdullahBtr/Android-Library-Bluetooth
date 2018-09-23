package com.example.reyes.sample_bluetooth;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.MainThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private SearchBluetoothDevices bt;
    private ArrayList<BluetoothDevice> bluetoothDevices = null;
    private String[] boundDeviceNames = null;
    private ArrayList<BluetoothDevice> pairedDevices = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDeviceAdapter mNewDevicesArrayAdapter;
    private BluetoothDevice device;

    private BroadcastReceiver blueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.d("onReceive: ", "Attempt to get received data...");
            Toast.makeText(MainActivity.this, "Looking for device...", Toast.LENGTH_SHORT).show();


            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                Toast.makeText(MainActivity.this, "Device Found!", Toast.LENGTH_SHORT).show();
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //pairedDevices.add(device);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(MainActivity.this, "Device Found!", Toast.LENGTH_SHORT).show();
                    //mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    pairedDevices.add(device);
                    //mNewDevicesArrayAdapter.notifyDataSetChanged();

                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "No devices found", Toast.LENGTH_SHORT).show();
                    //mNewDevicesArrayAdapter.add("No new device found");
                    //mNewDevicesArrayAdapter.notifyDataSetChanged();
                }
            }
            mNewDevicesArrayAdapter = new BluetoothDeviceAdapter(MainActivity.this, pairedDevices);
            //Attach the adapter to a ListView
            ListView btListView = (ListView) findViewById(R.id.listView_discoveredDevices);
            btListView.setAdapter(mNewDevicesArrayAdapter);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = new SearchBluetoothDevices(getApplicationContext(), MainActivity.this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void search_onClick(final View view) {
        bluetoothDevices = new ArrayList<>(bt.GetPairedBluetoothDevices());
        GetBluetoothDevices(bluetoothDevices);


        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogStyle));
        builder.setTitle(R.string.title_dialog_pairedDevices)
                .setItems(boundDeviceNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which ) {
                        // The 'which' argument contains the index position
                        BluetoothDevice bluetoothDevice = bluetoothDevices.get(which);

                        // Connect to the bluetooth device
                    }
                }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do something for ok.
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do something for cancel
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    public void discover_onClick(View view) {
        // This code was needed for permissions, had issues trying to find devices via 'discovery'
        // reference - https://stackoverflow.com/questions/37638665/broadcastreceiver-for-bluetooth-device-discovery-works-on-one-device-but-not-on
        final int CODE = 5; // app defined constant used for onRequestPermissionsResult

        String[] permissionsToRequest =
                {
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };

        boolean allPermissionsGranted = true;

        for(String permission : permissionsToRequest)
        {
            allPermissionsGranted = allPermissionsGranted && (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
        }

        if(!allPermissionsGranted)
        {
            ActivityCompat.requestPermissions(this, permissionsToRequest, CODE);
        }


        Toast.makeText(this, "Attempting to discover devices...", Toast.LENGTH_SHORT).show();
        Log.d("DiscBTDevices", "Attempting to discover devices...");

        //IntentFilter will match the action specified
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        //broadcast receiver for any matching filter
        this.registerReceiver(blueToothReceiver, filter);

        //broadcast receiver for any matching filter
        registerReceiver(blueToothReceiver, filter);
        if(mBluetoothAdapter.isDiscovering()) {
            // Bluetooth is already in discovery mode, we cancel to restart it again
            mBluetoothAdapter.cancelDiscovery();
            unregisterReceiver(blueToothReceiver);
        } mBluetoothAdapter.startDiscovery();


        // Create the adapter to convert to array of views
        BluetoothDeviceAdapter btdAdapter = new BluetoothDeviceAdapter(this, pairedDevices);

        //Attach the adapter to a ListView
        ListView btListView = (ListView) findViewById(R.id.listView_discoveredDevices);
        btListView.setAdapter(btdAdapter);
    }

    private void GetBluetoothDevices(ArrayList<BluetoothDevice> btDevices) {
        boundDeviceNames = new String[btDevices.size()];
        for(int i = 0; i < btDevices.size(); i++) {
            boundDeviceNames[i] = btDevices.get(i).getName() + " : " + btDevices.get(i).getAddress();
        }
    }
}
