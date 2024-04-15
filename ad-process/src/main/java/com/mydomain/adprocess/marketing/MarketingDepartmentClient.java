// Client for the RMI service in marketing
package com.mydomain.adprocess.marketing;

import com.mydomain.adprocess.editing.IEditingService;
import java.rmi.Naming;

public class MarketingDepartmentClient {

    public static void main(String[] args) {
        try {
            IEditingService service = (IEditingService) Naming.lookup("rmi://localhost/EditingService");
            // Suppose we create a string that represents advertisement details
            String advertisementDetails = "New Ad Content";
            String response = service.processAdvertisement(advertisementDetails);
            System.out.println("Editing Department response: " + response);
        } catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
