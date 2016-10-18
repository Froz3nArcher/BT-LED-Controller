package com.froz3narcher.btledcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by paul on 10/16/16.
 */

public class ConnectThread extends Thread
{
    static private final UUID thisUUID = UUID.fromString("67e02d0f-6c1a-4420-a296-31338cffc1aa");

    private final BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;
    private final BluetoothAdapter mBTAdapter;
    private final ConnectedThread mConnectedThread;

    public ConnectThread (String address, Handler msgHandler)
    {
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        mDevice = mBTAdapter.getRemoteDevice (address);

        BluetoothSocket tmp = null;

        try
        {
            tmp = mDevice.createRfcommSocketToServiceRecord(thisUUID);
        }
        catch (IOException e)
        {
        }
        mSocket = tmp;

        mConnectedThread = new ConnectedThread(mSocket, msgHandler);
        mConnectedThread.start();
    }

    public void run()
    {
        mBTAdapter.cancelDiscovery();
        try
        {
            mSocket.connect();
        }
        catch (IOException connectException)
        {
            try
            {
                mSocket.close();
            }
            catch (IOException closeException)
            {
            }
            return;
        }
    }

    public void cancel()
    {
        try
        {
            mSocket.close();
        }
        catch (IOException e)
        {
        }
    }
}
