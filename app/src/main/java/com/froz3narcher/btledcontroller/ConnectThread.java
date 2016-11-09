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
    // ref: https://developer.android.com/reference/android/bluetooth/BluetoothDevice.html
    // #createRfcommSocketToServiceRecord(java.util.UUID)
    // There's a Hint on this page that says use this common UUID for standard Bluetooth serial boards,
    // which this is.
    static private final UUID thisUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;
    private final BluetoothAdapter mBTAdapter;
    //private ConnectedThread mConnectedThread;
    private final Handler messageHandler;

    public ConnectThread(String address, Handler msgHandler)
    {
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        messageHandler = msgHandler;
        mDevice = mBTAdapter.getRemoteDevice(address);

        BluetoothSocket tmp = null;

        try
        {
            tmp = mDevice.createRfcommSocketToServiceRecord(thisUUID);
            mBTAdapter.cancelDiscovery();
        } catch (IOException e)
        {
        }
        mSocket = tmp;
    }

    public void run()
    {
        try
        {
            mSocket.connect();
        } catch (IOException connectException)
        {
            try
            {
                mSocket.close();
            } catch (IOException closeException)
            {
            }
            return;
        }

//        mConnectedThread = new ConnectedThread(mSocket, messageHandler);
//        mConnectedThread.start();
    }

    public void cancel()
    {
        try
        {
            //mConnectedThread.cancel();
            mSocket.close();
        } catch (IOException e)
        {
        }
    }

    public void sendData(byte[] data)
    {
        //mConnectedThread.write(data);
        try
        {
            mSocket.getOutputStream().write(data);
        } catch (IOException e)
        {
            mBTAdapter.getAddress();
        }
    }
}
