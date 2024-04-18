package com.mydomain.adprocess.editing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditingDepartmentServerTest {

    private ExecutorService executorService;
    private final int testPort = EditingDepartmentServer.EDITING_PORT;
    private Socket testSocket;

    @Before
    public void setUp() throws IOException {
        // Set up the executor to run the server in a separate thread
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> EditingDepartmentServer.main(new String[]{}));
        
        // Give the server time to start
        try {
            Thread.sleep(2000); // Adjust this time as necessary
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testServerAcceptsConnections() throws IOException {
        // Attempt to connect to the server
        testSocket = new Socket("localhost", testPort);
        
        // Test that the connection is successful
        assertTrue(testSocket.isConnected());
    }

    @After
    public void tearDown() throws IOException {
        // Close test resources
        if (testSocket != null) {
            testSocket.close();
        }
        // Stop the server
        executorService.shutdownNow();
    }
}
