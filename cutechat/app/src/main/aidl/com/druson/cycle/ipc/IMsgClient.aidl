// IMsgClient.aidl
package com.druson.cycle.ipc;
import com.druson.cycle.service.comm.IMMsg;
// Declare any non-default types here with import statements
interface IMsgClient {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void getMessage(in IMMsg msg);
    void disconnect(String msg);
}
