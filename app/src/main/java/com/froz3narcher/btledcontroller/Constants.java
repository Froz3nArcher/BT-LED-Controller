package com.froz3narcher.btledcontroller;

/**
 * Created by paul on 10/14/16.
 */

public class Constants
{
    static final int MAC_ADDRESS_SIZE = 17;  // size of MAC address string

    // Various Intent requests
    static final int REQUEST_ENABLE_BT = 1;
    static final int REQUEST_DEVICE_BT = 2;
    static final int REQUEST_DISCONNECT = 3;
    static final int REQUEST_CONNECT = 4;

    // String used to pass data between Activities
    // Prepend com.froz3narcher.btledcontroller for unique-ness. Get into the habit of
    // doing this to avoid conflict with other Android apps
    static final String DEVICE_RESULT = "com.froz3narcher.btledcontroller.DEVICE_RESULT";
    static final String MAC_ADDRESS = "com.froz3narcher.btledcontroller.MAC_ADDRESS";

}
