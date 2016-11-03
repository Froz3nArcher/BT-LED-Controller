package com.froz3narcher.btledcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
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
    private Button disableButton;

    private final Integer RED_POS = 0;
    private final Integer GREEN_POS = 1;
    private final Integer BLUE_POS = 2;

    private boolean connected = false;

    // member object for the Bluetooth connection
    ConnectThread mConnectThread = null;
    Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        class RGBListener implements SeekBar.OnSeekBarChangeListener
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                int viewId = 0;
                int index = 0;
                switch (seekBar.getId())
                {
                    case R.id.seekBar1:
                        index = RED_POS;
                        viewId = R.id.seekView1;
                        break;
                    case R.id.seekBar2:
                        index = GREEN_POS;
                        viewId = R.id.seekView2;
                        break;
                    case R.id.seekBar3:
                        index = BLUE_POS;
                        viewId = R.id.seekView3;
                        break;
                }

                TextView viewText = (TextView) findViewById(viewId);
                viewText.setText(String.valueOf (index) + " " + String.valueOf (progress));
                //sendData(viewText.getText().toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        }

        // Create one listener for the three seekbars.
        RGBListener barListener = new RGBListener ();

        // Red Seekbar selector
        SeekBar redBar = (SeekBar) findViewById(R.id.seekBar1);
        redBar.setOnSeekBarChangeListener(barListener);

        // Green seekbar selector
        SeekBar greenBar = (SeekBar) findViewById(R.id.seekBar2);
        greenBar.setOnSeekBarChangeListener(barListener);

        // Blue seekbar selector
        SeekBar blueBar = (SeekBar) findViewById(R.id.seekBar3);
        blueBar.setOnSeekBarChangeListener(barListener);


        // Enable/Connect button
        enableButton = (Button) findViewById(R.id.button1);

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

        enableButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!mBTAdapter.isEnabled())
                {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
                } else if (!connected)
                {
                    Intent connectIntent = new Intent(MainActivity.this, selectBluetooth.class);
                    startActivityForResult(connectIntent, Constants.REQUEST_DEVICE_BT);
                }
            }
        });

        // Disable button
        disableButton = (Button) findViewById(R.id.disableButton);
        disableButton.setBackgroundColor(Color.RED);
        disableButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BluetoothAdapter thisAdapter = BluetoothAdapter.getDefaultAdapter();
                if (thisAdapter != null)
                {
                    thisAdapter.disable();
                }
            }
        });

        mHandler = new Handler()
        {
            @Override
            public void handleMessage (Message msg)
            {
                byte[] writeBuf = (byte[]) msg.obj;
                int begin = (int) msg.arg1;
                int end = (int) msg.arg2;
                switch (msg.what)
                {
                    case 1:
                        String writeMessage = new String(writeBuf);
                        writeMessage = writeMessage.substring(begin, end);
                        break;
                }
            }
        };
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

                    mConnectThread = new ConnectThread(address, mHandler);
                    mConnectThread.start();

                    TextView display = (TextView) findViewById(R.id.selectedDevice);
                    display.setText(message + " " + address);
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


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}
