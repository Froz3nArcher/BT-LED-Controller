package com.froz3narcher.btledcontroller;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by paul on 10/17/16.
 */

public class ConnectedThread extends Thread
{
    private final BluetoothSocket mSocket;
    private final InputStream mInStream;
    private final OutputStream mOutStream;
    private final Handler mHandler;

    public ConnectedThread(BluetoothSocket socket, Handler msgHandler)
    {
        mSocket = socket;
        mHandler = msgHandler;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try
        {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e)
        {
        }

        mInStream = tmpIn;
        mOutStream = tmpOut;
    }

    public void run()
    {
        byte[] buffer = new byte[1024];
        int begin = 0;
        int bytes = 0;

        while (true)
        {
            try
            {
                bytes = mInStream.read(buffer);

                mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
//                bytes += mInStream.read(buffer, bytes, buffer.length - bytes);
//                for (int i = begin; i < bytes; i++) {
//                    if (buffer[i] == "#".getBytes()[0]) {
//                        mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
//                        begin = i + 1;
//                        if (i == bytes - 1) {
//                            bytes = 0;
//                            begin = 0;
//                        }
//                    }
            } catch (IOException e)
            {
                break;
            }
        }
    }

    public void write(byte[] bytes)
    {
        try
        {
            mOutStream.write(bytes);
            mOutStream.flush();
        } catch (IOException e)
        {
        }
    }


    public void cancel()
    {
        try
        {
            mSocket.close();
        } catch (IOException e)
        {
        }
    }

}
