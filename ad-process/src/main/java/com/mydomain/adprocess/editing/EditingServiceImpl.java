// Implementation of the RMI service
package com.mydomain.adprocess.editing;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EditingServiceImpl extends UnicastRemoteObject implements IEditingService {

    protected EditingServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String processAdvertisement(String advertisementDetails) throws RemoteException {
        // Process the advertisement here
        // For now, we just append "Processed" to the ad content
        System.out.println("Received advertisement: " + advertisementDetails);
        return "Processed: " + advertisementDetails;
    }
}
