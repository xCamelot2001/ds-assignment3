//The RMI service interface.
package com.mydomain.adprocess.editing;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IEditingService extends Remote {
    String processAdvertisement(String advertisementDetails) throws RemoteException;
}
