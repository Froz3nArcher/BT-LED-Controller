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
    private Button enableButton;

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
                enableButton.setText(getText(R.string.button1Connect));
            }
            else
            {
                enableButton.setText(getText(R.string.button1Text));
            }
        }
        else
        {
            // If there's no Bluetooth, we can't go much farther.
            enableButton.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == Constants.REQUEST_ENABLE_BT) && (resultCode == Activity.RESULT_OK))
        {
            if (mBTAdapter.isEnabled())
            {
                enableButton.setText(getText(R.string.button1Connect));
            }
        }
        else if ((requestCode == Constants.REQUEST_DEVICE_BT) && (resultCode == Activity.RESULT_OK))
        {
            Intent result = getIntent();

            //Bundle extras = result.getExtras();

            //String message = extras.getString(Constants.EXTRA_MESSAGE);
            String message = (String) result.getStringExtra(Constants.EXTRA_MESSAGE);

            TextView display = (TextView) findViewById(R.id.selectedDevice);
            display.setText (message);
            //display.setText ("Returned");
        }
    }

    // Function to connect to the Bluetooth device
    public void connectBT (View view)
    {
        if (!mBTAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult (enableBtIntent, Constants.REQUEST_ENABLE_BT);
        }
        else
        {
            Intent intent = new Intent(this, selectBluetooth.class);
            startActivityForResult(intent, Constants.REQUEST_DEVICE_BT);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}
