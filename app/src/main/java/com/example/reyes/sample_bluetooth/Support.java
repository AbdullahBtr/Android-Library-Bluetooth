package com.example.reyes.sample_bluetooth;

import android.content.Context;
import android.widget.Toast;

class Support {

    private static Support support_instance = null;

    private Support() {
    }

    public static Support instance() {
        if(support_instance == null) {
            support_instance = new Support();
        } return support_instance;
    }

    public static void PrintMessage(String message, Context context) {

        Toast.makeText(context, "No devices found", Toast.LENGTH_SHORT).show();
    }

}
