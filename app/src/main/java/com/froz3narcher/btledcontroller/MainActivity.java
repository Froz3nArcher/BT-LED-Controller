package com.froz3narcher.btledcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Set;


public class MainActivity extends AppCompatActivity
{
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private ArrayAdapter<String> mArrayAdapter; // = new ArrayAdapter<String> (this, R.layout.activity_main);
    private Button enableButton;

    static final int REQUEST_ENABLE_BT = 1;

    public final static String EXTRA_MESSAGE = "com.example.paul.bluetooth_led.MESSAGE";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        class redListener implements SeekBar.OnSeekBarChangeListener
        {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                TextView viewText = (TextView) findViewById(R.id.seekView1);
                viewText.setText("" + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        }

        class greenListener implements SeekBar.OnSeekBarChangeListener
        {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                TextView viewText = (TextView) findViewById(R.id.seekView2);
                viewText.setText("" + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        }

        class blueListener implements SeekBar.OnSeekBarChangeListener
        {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                TextView viewText = (TextView) findViewById(R.id.seekView3);
                viewText.setText("" + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        }

        enableButton = (Button) findViewById(R.id.button1);

        SeekBar redBar = (SeekBar) findViewById(R.id.seekBar1);
        redBar.setOnSeekBarChangeListener(new redListener ());

        SeekBar greenBar = (SeekBar) findViewById(R.id.seekBar2);
        greenBar.setOnSeekBarChangeListener(new greenListener());

        SeekBar blueBar = (SeekBar) findViewById(R.id.seekBar3);
        blueBar.setOnSeekBarChangeListener(new blueListener());

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBTAdapter != null)
        {
            if (mBTAdapter.isEnabled())
            {
                enableButton.setText("Connect Device");
            }
            else
            {
                enableButton.setText("Enable BT");
            }
        }
    };

    // Function to connect to the Bluetooth device
    public void connectBT (View view)
    {

//        Intent intent = new Intent(this, secondActivity.class);
//        startActivity (intent);

        lv = (ListView) findViewById (R.id.newDeviceView);

//        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBTAdapter != null)
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
                mArrayAdapter = new ArrayAdapter<String> (this, R.layout.activity_main);

                for (BluetoothDevice device : pairedDevices)
                {
                    // Add name and address to array adapter and show in a ListView
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }

                lv.setAdapter (mArrayAdapter);
                lv.setOnItemClickListener(mBTClickListener);

                //ListView newDevicesListView = (ListView) findViewById (R.id.newDeviceView);
                // register for broadcast when a device is discovered
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                this.registerReceiver(receiverBT, filter);

            }
        }

    };

    protected void onDestroy()
    {
        this.unregisterReceiver(receiverBT);
    }

    private AdapterView.OnItemClickListener mBTClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3)
        {
            // Get MAC address, last 17 chars in view
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start the next activity
            Intent intent = new Intent();
            intent.putExtra (EXTRA_DEVICE_ADDRESS, address);

            // set result and finish activity
            setResult (Activity.RESULT_OK, intent);
            finish ();
        }
    };

    private final BroadcastReceiver receiverBT = new BroadcastReceiver ()
    {
        @Override
        public void onReceive (Context context, Intent intent)
        {
            String action = intent.getAction ();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BT Device object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // If it's already paired, skip it, because it's listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }

            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals (action))
            {
                if (mArrayAdapter.getCount() == 0)
                {
                    String noDevices = "none found";
                    mArrayAdapter.add (noDevices);
                }
//                // add the name and MAC address to the arrayAdapter
//                mArrayAdapter.add (device.getName() + "\n" + device.getAddress ());
//                mArrayAdapter.notifyDataSetChanged();
            }
        }
    };

//    // Register the BroacastReceiver
//    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//    registerReceiver (receiverBT, filter);

//    @Override
//    protected void onDestroy ()
//    {
//        super.onDestroy();
//        unregisterReceiver(receiverBT);
//    };

}
