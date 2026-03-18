package com.example.rimasbiy.MyRecipeTable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

public class AirPlaneReceiver extends BroadcastReceiver {
    private Button save;// 4.1
    public AirPlaneReceiver(Button buttonsaverecipe) {//4.2
        save=buttonsaverecipe;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if the action is Airplane Mode change
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())) {
            boolean isEnabled = intent.getBooleanExtra("state", false);
            if (isEnabled) {
                Toast.makeText(context, "System: Airplane Mode is ON. Sync is disabled.", Toast.LENGTH_LONG).show();
                save.setEnabled(false);//4.3
                save.setText("AirPalne is on");//4.3
            } else {
                Toast.makeText(context, "System: Airplane Mode is OFF. Sync is back!", Toast.LENGTH_LONG).show();
                save.setEnabled(true);//4.3
                save.setText("Save");//4.3

            }
        }

    }
}