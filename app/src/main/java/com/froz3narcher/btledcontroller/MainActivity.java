package com.froz3narcher.btledcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Set;


public class MainActivity extends AppCompatActivity
{
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String> (this, R.layout.activity_main);;

    static final int REQUEST_ENABLE_BT = 1;

    public final static String EXTRA_MESSAGE = "com.example.paul.bluetooth_led.MESSAGE";

    private class redListener implements SeekBar.OnSeekBarChangeListener
    {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            Log.d("DEBUG", "Red Value is: " + progress);
            TextView viewText = (TextView) findViewById(R.id.seekView1);
            viewText.setText("" + progress);
        }

        public void onStartTrackingTouch(SeekBar seekBar)
        {
        }

        public void onStopTrackingTouch(SeekBar seekBar)
        {
        }
    };

    private class greenListener implements SeekBar.OnSeekBarChangeListener
    {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            Log.d("DEBUG", "Green Value is: " + progress);
            TextView viewText = (TextView) findViewById(R.id.seekView2);
            viewText.setText("" + progress);
        }

        public void onStartTrackingTouch(SeekBar seekBar)
        {
        }

        public void onStopTrackingTouch(SeekBar seekBar)
        {
        }
    };

    private class blueListener implements SeekBar.OnSeekBarChangeListener
    {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            Log.d("DEBUG", "Blue Value is: " + progress);
            TextView viewText = (TextView) findViewById(R.id.seekView3);
            viewText.setText("" + progress);
        }

        public void onStartTrackingTouch(SeekBar seekBar)
        {
        }

        public void onStopTrackingTouch(SeekBar seekBar)
        {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar redBar = (SeekBar) findViewById(R.id.seekBar1);
        redBar.setOnSeekBarChangeListener(new redListener ());

        SeekBar greenBar = (SeekBar) findViewById(R.id.seekBar2);
        greenBar.setOnSeekBarChangeListener(new greenListener());

        SeekBar blueBar = (SeekBar) findViewById(R.id.seekBar3);
        blueBar.setOnSeekBarChangeListener(new blueListener());

    };

//    public void sendData (View view)
//    {
//        // Do something
//        Intent intent = new Intent(this, secondActivity.class);
//        //EditText editText1 = (EditText) findViewById (R.id.redInput);
//        //EditText editText2 = (EditText) findViewById (R.id.greenInput);
//        //EditText editText3 = (EditText) findViewById (R.id.blueInput);
//        //Integer result = Integer.valueOf ((editText1.getText().toString()));
//        //result += Integer.valueOf (editText2.getText().toString());
//        //result += Integer.valueOf (editText3.getText().toString());
//
//        //String message = String.valueOf (result);
//        //intent.putExtra (EXTRA_MESSAGE, message);
//        startActivity (intent);
//        //TextView answerText = (TextView) findViewById(R.id.resultText);
//        //answerText.setText(message);
//
//    };

//    public void findBT (View view)
//    {
//        if (mBTAdapter.isDiscovering()) {
//            // the button is pressed when it discovers, so cancel discovery  ??
//            mBTAdapter.cancelDiscovery();
//        }
//        else
//        {
//            mArrayAdapter.clear ();
//            mBTAdapter.startDiscovery();
//
//            registerReceiver (receiverBT, new IntentFilter(BluetoothDevice.ACTION_FOUND));
//
//        }
//    };

    // Function to connect to the Bluetooth device
    public void connectBT (View view)
    {

//        Intent intent = new Intent(this, secondActivity.class);
//        startActivity (intent);

        lv = (ListView) findViewById (R.id.listViewDevices);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBTAdapter == null)
        {
            TextView viewText = (TextView) findViewById(R.id.seekView1);
            viewText.setText ("Bluetooth not supported");
        }
        else
        {

            if (!mBTAdapter.isEnabled())
            {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult (enableBtIntent, REQUEST_ENABLE_BT);
            }

            // Look for paired devices, and show them to the user
            pairedDevices = mBTAdapter.getBondedDevices();

            if (pairedDevices.size() > 0)
            {
                // Loop through devices, displaying them
                //mArrayAdapter = new ArrayAdapter<String> (this, R.layout.activity_main);

                for (BluetoothDevice device : pairedDevices)
                {
                    // Add name and address to array adapter and show in a ListView
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }

                lv.setAdapter (mArrayAdapter);
                //lv.setOnItemClickListener(mBTClickListener);

//                findBT (view);

            }
        }

    };

    private final BroadcastReceiver receiverBT = new BroadcastReceiver ()
    {
        public void onReceive (Context context, Intent intent)
        {
            String action = intent.getAction ();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                // Get the BT Device object from the Intent
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);

                // add the name and MAC address to the arrayAdapter
                mArrayAdapter.add (device.getName() + "\n" + device.getAddress ());
                mArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    // Register the BroacastReceiver
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    registerReceiver (receiverBT, filter);

    @Override
    protected void onDestroy ()
    {
        super.onDestroy();
        unregisterReceiver(receiverBT);
    };

}
