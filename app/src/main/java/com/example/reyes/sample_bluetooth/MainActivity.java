package com.example.reyes.sample_bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private SearchBluetoothDevices bt;
    private ArrayList<BluetoothDevice> bluetoothDevices = null;
    private String[] boundDeviceNames = null;
    private ArrayList<BluetoothDevice> pairedDevices = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDeviceAdapter mNewDevicesArrayAdapter;
    private BluetoothDevice device;
    private ConnectThread ct;
    private ListView btListView;
    private EditText editText_rawData;

    private BroadcastReceiver blueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.d("onReceive: ", "Attempt to get received data...");
            //Toast.makeText(MainActivity.this, "Looking for device...", Toast.LENGTH_SHORT).show();


            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                //Toast.makeText(MainActivity.this, "Device Found!", Toast.LENGTH_SHORT).show();
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //pairedDevices.add(device);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    //Toast.makeText(MainActivity.this, "Device Found!", Toast.LENGTH_SHORT).show();
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
            btListView = (ListView) findViewById(R.id.listView_discoveredDevices);
            btListView.setAdapter(mNewDevicesArrayAdapter);

        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int)msg.arg1;
            int end = (int)msg.arg2;

            switch(msg.what) {
                case 1:
                    String writeMessage = new String(writeBuf);
                    //writeMessage = writeMessage.substring(begin, end);
                    editText_rawData.setText(writeMessage);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        bt = new SearchBluetoothDevices(getApplicationContext(), MainActivity.this);
        ct = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        editText_rawData = findViewById(R.id.editText_rawData);


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

        pairedDevices.clear();

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


        //Toast.makeText(this, "Attempting to discover devices...", Toast.LENGTH_SHORT).show();
        Log.d("DiscBTDevices", "Attempting to discover devices...");

        //IntentFilter will match the action specified
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);

        //broadcast receiver for any matching filter
        //registerReceiver(blueToothReceiver, filter);
        if(mBluetoothAdapter.isDiscovering()) {
            // Bluetooth is already in discovery mode, we cancel to restart it again
            mBluetoothAdapter.cancelDiscovery();
            unregisterReceiver(blueToothReceiver);
        }
        //broadcast receiver for any matching filter
        this.registerReceiver(blueToothReceiver, filter);
        mBluetoothAdapter.startDiscovery();


        // Create the adapter to convert to array of views
        BluetoothDeviceAdapter btdAdapter = new BluetoothDeviceAdapter(this, pairedDevices);

        //Attach the adapter to a ListView
        ListView btListView = (ListView) findViewById(R.id.listView_discoveredDevices);
        btListView.setAdapter(btdAdapter);

        btListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //mNewDevicesArrayAdapter.getItem(i);

                readBtData(i);

            }
        });
    }

    private void GetBluetoothDevices(ArrayList<BluetoothDevice> btDevices) {
        boundDeviceNames = new String[btDevices.size()];
        for(int i = 0; i < btDevices.size(); i++) {
            boundDeviceNames[i] = btDevices.get(i).getName() + " : " + btDevices.get(i).getAddress();
        }
    }

    public void readBtData(int index) {

        device = pairedDevices.get(index);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Device Name: " + device.getName() + "\n" + "Device Address: " + device.getAddress(), Toast.LENGTH_SHORT).show();
            }
        });
        ct = new ConnectThread(device, mBluetoothAdapter, mHandler);
        ct.start();

    }

    public void cancelReadBtData_onClick(View view) {
        ct.cancel();
        //ct = null;
    }

}
