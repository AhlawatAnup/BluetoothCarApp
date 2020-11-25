package com.example.bluetoothcarapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT=0;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    Button statusBtn,connectBtn,forwardBtn,backwardBtn,leftBtn,rightBtn;
    BluetoothAdapter mBtAdapter;
    BluetoothSocket btSocket = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusBtn=findViewById(R.id.status_button);
        connectBtn=findViewById(R.id.connect_button);
        //getting direction button
        forwardBtn=findViewById(R.id.forward_button);
        backwardBtn=findViewById(R.id.backward_button);
        leftBtn=findViewById(R.id.left_button);
        rightBtn=findViewById(R.id.right_button);



        mBtAdapter=BluetoothAdapter.getDefaultAdapter();
        bluetoothInstances();

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBtAdapter.isEnabled()){

                 // displayDeviceList();
                  connectionToController();
                   // new FragmentPairedDeviceDialog().show(getSupportFragmentManager(),"Paired Device Dialog");
                }
                else{
                    bluetoothInstances();
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode==RESULT_OK){
                    statusBtn.setText("Bluetooth ON");
                }
                else{
                    statusBtn.setText("BT permission Denied");
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //initializing the component
    public void bluetoothInstances(){
        if(mBtAdapter==null){
            statusBtn.setText("Bluetooth not supported");
            //statusBtn.setBackgroundColor();
        }
        else
        if(mBtAdapter.isEnabled()){
            statusBtn.setText("Bluetooth ON");
        }
        else

        if(!mBtAdapter.isEnabled()){

            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQUEST_ENABLE_BT);

        }

    }

    //checking bluetooth status and providing reuseability
    public void resetStatus(View view){
        bluetoothInstances();
    }

    //displaying the device name and connection info
    public void displayDeviceList(){
        final ListView listView=new ListView(MainActivity.this);

        final List<String> deviceName= new ArrayList<>();
        List<String> deviceAddress= new ArrayList<>();

        Set<BluetoothDevice> btDevices =mBtAdapter.getBondedDevices();
        for(BluetoothDevice btDevice: btDevices){
            deviceName.add(btDevice.getName());
            deviceAddress.add(btDevice.toString());
            System.out.println("name: "+btDevice.getName()+" : "+btDevice.toString());

           // Toast.makeText(MainActivity.this, btDevice.getName()+"\n"+btDevice, Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> deviceAdapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,deviceName);
        listView.setAdapter(deviceAdapter);

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setView(listView);
        AlertDialog dialog=builder.create();
        dialog.show();


    }

    //connection and data stream to controller
    public void connectionToController(){
        String connectedDeviceMAC="98:D3:37:00:BD:7A";

        BluetoothDevice connDevice = mBtAdapter.getRemoteDevice(connectedDeviceMAC); //setting up connection to micro-controller BT


        int connCounter = 0;


        do {
            try {
                btSocket = connDevice.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);
                btSocket.connect();
                System.out.println(btSocket.isConnected());
            } catch (IOException e) {
               // e.printStackTrace();
               // Toast.makeText(this, e.toString()+" cant connect to Device", Toast.LENGTH_SHORT).show();
            }
            connCounter++;
        } while (!btSocket.isConnected() && connCounter<2);


        if(!btSocket.isConnected()){


            Toast.makeText(this, "Cant connect to this...try again after some time", Toast.LENGTH_SHORT).show();
        }
        else {
            connectBtn.setText("Connected to the car");

            Toast.makeText(this, "Connected to HC-05", Toast.LENGTH_SHORT).show();



         }




    }

    public void directionButtons(char direction){
        int i=1;


            if (btSocket.isConnected()) {
                try {
                    OutputStream outputStream = btSocket.getOutputStream();
                    outputStream.write(direction);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Connected to the car first", Toast.LENGTH_SHORT).show();
            }
        }


    public void closeConnectionToController(){
        try {
            btSocket.close();
            System.out.println(btSocket.isConnected());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onF(View view){
        directionButtons('F');
    }
    public void onB(View view){
        directionButtons('B');
    }
    public void onR(View view){
        directionButtons('R');
    }
    public void onL(View view){
        directionButtons('L');
    }

}