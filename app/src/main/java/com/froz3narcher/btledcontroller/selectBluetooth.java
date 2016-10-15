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
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class selectBluetooth extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        BluetoothAdapter mBTAdapter;
        Set<BluetoothDevice> pairedDevices;
        final ListView lv;
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bluetooth);

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

                    String selected = lv.getItemAtPosition(position).toString();
                    Intent intent = new Intent();

                    // Tell the caller what the result is
                    intent.putExtra(Constants.EXTRA_MESSAGE, selected);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });

        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // return BT adapter?
    }
}
