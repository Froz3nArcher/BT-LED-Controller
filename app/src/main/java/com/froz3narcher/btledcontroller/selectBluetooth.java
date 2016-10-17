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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class selectBluetooth extends AppCompatActivity
{
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;

    ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bluetooth);

        // Disable the Scan Button for now, can't get the Device Scanning to work
        // properly yet.
        Button scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setEnabled(false);
        scanButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mArrayAdapter.clear();
                mBTAdapter.startDiscovery();
            }
        });

         mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        // This will display all the paired devices
        lv = (ListView) findViewById (R.id.newDeviceView);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        // We already know the Adapter is non-null
        // Look for paired devices, and show them to the user
        pairedDevices = mBTAdapter.getBondedDevices();

        if (pairedDevices.size() > 0)
        {
            // Loop through devices, displaying them
            for (BluetoothDevice device : pairedDevices)
            {
                // Add name and address to array adapter and show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }

            lv.setAdapter (mArrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {

                    String selectedBT = lv.getItemAtPosition(position).toString();
                    Intent intent = new Intent();

                    // Tell the caller which device was picked
                    intent.putExtra(Constants.DEVICE_RESULT, selectedBT);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });

        }

        // register the Broadcast receiver for the following actions.
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        //registerReceiver(mBTReceiver, filter);

    }

    @Override
    public void onDestroy()
    {
        if (mBTAdapter != null)
        {
            mBTAdapter.cancelDiscovery();
        }

        //unregisterReceiver(mBTReceiver);

        super.onDestroy();
    }

//    private final BroadcastReceiver mBTReceiver = new BroadcastReceiver()
//    {
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//            String action = intent.getAction();

//            // When discovery finds a device, get the object and add it to the array adapters
//            if (BluetoothDevice.ACTION_FOUND.equals(action))
//            {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

//                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());

//                mArrayAdapter.notifyDataSetChanged();
//            }
//            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
//            {
//                mBTAdapter.cancelDiscovery();
//                //Intent display = new Intent(MainActivity.this, DeviceListActivity.class);
//                display.putParcelableArrayListExtra(Constants.DEVICE_LIST, mArrayAdapter);

//            }
//        }
//    };

}
