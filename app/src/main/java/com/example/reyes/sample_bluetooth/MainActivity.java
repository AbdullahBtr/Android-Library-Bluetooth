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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SearchBluetoothDevices bt;
    private String[] boundDeviceNames = null;
    private ArrayList<BluetoothDevice> pairedDevices = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDeviceAdapter mNewDevicesArrayAdapter;
    private BluetoothDevice device;
    private ConnectThread ct;
    private EditText editText_rawData;
    private static Handler mHandler;
    private BroadcastReceiver blueToothReceiver;

    private AlertDialog dialog;

    public MainActivity() {

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                String writeBuf = (String) message.obj;

                switch(message.what) {
                    case 1:
                        editText_rawData.setText(writeBuf);
                        break;
                }
                return false;
            }
        });

        blueToothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d("onReceive: ", "Attempt to get received data...");

                assert action != null;
                if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        pairedDevices.add(device);

                    }
                } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                    if (mNewDevicesArrayAdapter.getCount() == 0) {
                        Toast.makeText(MainActivity.this, "No devices found", Toast.LENGTH_SHORT).show();
                    }
                }
                mNewDevicesArrayAdapter = new BluetoothDeviceAdapter(MainActivity.this, pairedDevices);
                //Attach the adapter to a ListView
                ListView btListView = dialog.findViewById(R.id.id_dialog_bluetooth);
                btListView.setAdapter(mNewDevicesArrayAdapter);

            }
        };

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        bt = new SearchBluetoothDevices(getApplicationContext());
        ct = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        editText_rawData = findViewById(R.id.editText_rawData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch(item.getItemId()) {
            case R.id.id_menu_bluetooth:
                // Do something here for the bluetooth button to be clicked
                openBluetoothDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openBluetoothDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_bluetooth, null);
        Button mCancelButton = mView.findViewById(R.id.id_button_dialog_cancelBluetooth);


        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close Dialog Window here
                dialog.dismiss();
            }
        });

        mBuilder.setView(mView);
        dialog = mBuilder.create();


        dialog.setTitle("My Dialog Title");
        dialog.show();

        //ProgressBar dialogProgressBar = (ProgressBar)dialog.findViewById(R.id.id_bluetooth_progressBar);
        //dialogProgressBar.setVisibility(View.VISIBLE);

    }

    public void search_onClick(final View view) {
        ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>(bt.GetPairedBluetoothDevices());
        GetBluetoothDevices(bluetoothDevices);


        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogStyle));
        builder.setTitle(R.string.title_dialog_pairedDevices)
                .setItems(boundDeviceNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which ) {
                        // The 'which' argument contains the index position

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

        Log.d("DiscBTDevices", "Attempting to discover devices...");

        //IntentFilter will match the action specified
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);

        //broadcast receiver for any matching filter
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
        ListView btListView = findViewById(R.id.listView_discoveredDevices);
        btListView.setAdapter(btdAdapter);

        btListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
    }

}
