package com.example.reyes.sample_bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/***
 * Sending and Receiving Data via Bluetooth with and Android Device
 * Reference - https://www.egr.msu.edu/classes/ece480/capstone/spring14/group01/docs/appnote/Wirsing-SendingAndReceivingDataViaBluetoothWithAnAndroidDevice.pdf
 *
 */

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private InputStream mmInStream;
    //private final OutputStream mmOutStream;
    private final Handler mHandler;

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket;
        mHandler = handler;
        InputStream tmpIn = null;
        mmInStream = null;
        //OutputStream tmpOut = null;
        try {
            mmInStream = mmSocket.getInputStream();
            //tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) { }
        //mmInStream = tmpIn;
        //mmOutStream = tmpOut;
    }
    public void run() {
        //byte[] buffer = new byte[1024];
        int begin = 0;
        int availableBytes = 0;
        int bytes = 0;
        while (true) {
            try {
                availableBytes = mmInStream.available();

                if(availableBytes > 0 ) {
                    byte[] buffer = new byte[availableBytes];
                    bytes = mmInStream.read(buffer);

                    Log.d("mmInStream.read(buffer)", new String(buffer));
                    if( bytes > 0 ) {
                        mHandler.obtainMessage(1, bytes, -1, buffer).sendToTarget();
                    }
                }


            } catch (IOException e) {
                Log.d("Error reading", e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }

    private String CollectSensorData() {
        String sensorData = "";



        return sensorData;
    }
    //public void write(byte[] bytes) {
    //    try {
    //        //mmOutStream.write(bytes);
    //    } catch (IOException e) { }
   // }
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}

