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

    private final Integer RED_POS = 0;
    private final Integer GREEN_POS = 1;
    private final Integer BLUE_POS = 2;

    private boolean connected = false;

    String BTMessage;

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
                updateMessage (RED_POS, progress);
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
                enableButton.setText(getText(R.string.button1Enable));
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

        switch (requestCode)
        {
            case Constants.REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK)
                {
                    if (mBTAdapter.isEnabled())
                    {
                        enableButton.setText(getText(R.string.button1Connect));
                    }
                }
                break;

            case Constants.REQUEST_DEVICE_BT:
                if (resultCode == Activity.RESULT_OK)
                {
                    connected = true;

                    enableButton.setText(getText(R.string.button1Disconnect));

                    String message = data.getStringExtra(Constants.DEVICE_RESULT);

                    // Get the last 17 characters, which are the MAC Address of the chosen
                    // Bluetooth device
                    String address = message.substring(message.length() - Constants.MAC_ADDRESS_SIZE);

                    Intent openDevice = new Intent(this, BTConnect.class);
                    openDevice.putExtra(Constants.MAC_ADDRESS, address);
                    startActivity(openDevice);

                    //TextView display = (TextView) findViewById(R.id.selectedDevice);
                    //display.setText(message);
                }
                break;

            case Constants.REQUEST_DISCONNECT:
                if (resultCode == Activity.RESULT_OK)
                {
                    enableButton.setText(getText(R.string.button1Connect));
                }
                break;

            default:
                break;
        }

    }

    private void updateMessage (Integer index, int value)
    {
        // TODO - crap, this can't work - the startActivity call will instantiate
        // TODO - a copy of the class, but this call expects to call from the class
        // TODO - or an instantiation of the class. But don't know how to do that yet.
        BTMessage = String.valueOf(index) + " " + String.valueOf(value);
        //BTConnect.sendData (BTMessage);
    }

    // Function to connect to the Bluetooth device
    public void connectBT (View view)
    {
        if (!mBTAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult (enableBtIntent, Constants.REQUEST_ENABLE_BT);
        }
        else if (!connected)
        {
            Intent intent = new Intent(this, selectBluetooth.class);
            startActivityForResult(intent, Constants.REQUEST_DEVICE_BT);
        }
        else
        {
            // TODO - crap, this can't work - the startActivity call will instantiate
            // TODO - a copy of the class, but this call expects to call from the class
            // TODO - or an instantiation of the class. But don't know how to do that yet.
            BTConnect.Disconnect();

        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}
