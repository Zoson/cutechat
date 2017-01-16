// IMsgServer.aidl
package com.druson.cycle.ipc;
// Declare any non-default types here with import statements
import com.druson.cycle.service.comm.IMOpState;
interface IMsgServer {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    IMOpState sendMessage(String sender,String content);
    IMOpState sendFile(String sender,String filepath);
    IMOpState login(String account,String password);
    IMOpState register(String account,String password);
    void addUser(String sender,String name);
    void removeUser(String sender);
    List<String> getAllUsers();
    void setPath(String path);
    void logout();
    void setHost(String host);
    void setResource(String res);

}
