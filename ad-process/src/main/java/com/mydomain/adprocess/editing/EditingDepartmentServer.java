// RMI server for editing department
package com.mydomain.adprocess.editing;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class EditingDepartmentServer {
    public static void main(String[] args) {
        try {
            IEditingService service = new EditingServiceImpl();
            LocateRegistry.createRegistry(1099); // RMI default port
            Naming.rebind("rmi://localhost/EditingService", service);
            System.out.println("Editing Department Service is running...");
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
