package com.example.bluetoothcarapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Set;

public class FragmentPairedDeviceDialog extends DialogFragment {
    // creating a onCreate dialog method for dialog box

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BluetoothAdapter fBtAdapter;
        fBtAdapter=BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> btDevices =fBtAdapter.getBondedDevices();
        final String deviceNames[]=new String[btDevices.size()];
        final String deviceAddress[]=new String[btDevices.size()];
        int i=0;
        for(BluetoothDevice btDevice: btDevices){
            deviceNames[i]=btDevice.getName();
            deviceAddress[i]=btDevice.toString();
            i++;
           // Toast.makeText(getActivity(), btDevice.getName()+"\n"+btDevice, Toast.LENGTH_SHORT).show();
        }




          AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        builder.setTitle("Choose Paired Device");
        builder.setItems(deviceNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"Connecting to :"+deviceNames[which],Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}
