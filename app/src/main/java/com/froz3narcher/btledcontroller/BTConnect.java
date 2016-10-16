package com.froz3narcher.btledcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.UUID;

public class BTConnect extends AppCompatActivity
{
    static final UUID thisUUID = UUID.fromString("67e02d0f-6c1a-4420-a296-31338cffc1aa");

    BluetoothAdapter mAdapter = null;
    BluetoothSocket mSocket = null;
    private boolean isConnected = false;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btconnect);

        Intent addressData = getIntent();
        address = (String) addressData.getStringExtra(Constants.MAC_ADDRESS);

        new ConnectBT().execute();

    }

    public void sendData (String messageData)
    {
        if (mSocket != null)
        {
            try
            {
                mSocket.getOutputStream().write(messageData.getBytes());
            }
            catch (IOException e)
            {
                // display message?
            }
        }
    }

    public void Disconnect ()
    {
        if (mSocket != null)
        {
            try
            {
                mSocket.close();
            }
            catch (IOException e)
            {
                // ?? display message?
            }
            finish();
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>
    {
        private boolean connectSuccess = true;

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (mSocket == null || !isConnected)
                {
                    mAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice mDevice = mAdapter.getRemoteDevice(address);
                    mSocket = mDevice.createInsecureRfcommSocketToServiceRecord(thisUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mSocket.connect();
                }
            }
            catch (IOException e)
            {
                connectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!connectSuccess)
            {
                finish();
            }
            else
            {
                isConnected = true;
            }
        }

    }
}
