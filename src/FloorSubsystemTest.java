/**
 * SYSC 3303 Elevator Project Iteration 1
 * Group 9
 *  Joseph Vretenar - 101234613
 *  Samuel Mauricla - 101233500
 *  Bhavaan Balasubramaniam - 101233825
 * Due Febuary 3rd 2024
 *
 *  File written by Samuel Mauricla
 */

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FloorSubsystemTest {

    @Test
    void testFloorSubsystemAndSchedulerIntegration() throws IOException, InterruptedException {
        Scheduler scheduler = new Scheduler();
        Thread floorThread = new Thread(new FloorSubsystem(scheduler));

        // Create some sample data to be written to the inputFile.txt
        String sampleData = "12:00:00.000 1 Up 5\n" +
                "12:05:00.000 3 Down 1\n" +
                "12:10:00.000 2 Up 8";

        // Write the sample data to the inputFile.txt (for testing purposes)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inputFile.txt"))) {
            writer.write(sampleData);
        }

        // Start the FloorSubsystem thread
        floorThread.start();

        // Wait for a short period to allow the FloorSubsystem to read the data
        Thread.sleep(1000);

        // Get the current time to simulate the Scheduler processing events
        Date currentTime = new Date();

        // Simulate the Scheduler processing events
        Event event1 = scheduler.processEvent();
        assertNotNull(event1);
        assertEquals(1, event1.getCurrentFloor());
        System.out.println("Event 1: " + event1);

        assertEquals(5, event1.getRequestedFloor());

        // Complete the event (assuming it was processed successfully)
        scheduler.complete(currentTime, 5, true);

        // Simulate the Scheduler processing the next event
        Event event2 = scheduler.processEvent();
        assertNotNull(event2);
        assertEquals(3, event2.getCurrentFloor());
        assertEquals(1, event2.getRequestedFloor());

        // Complete the event (assuming it was processed successfully)
        scheduler.complete(currentTime, 1, true);

        // Simulate the Scheduler processing the last event
        Event event3 = scheduler.processEvent();
        assertNotNull(event3);
        assertEquals(2, event3.getCurrentFloor());
        assertEquals(8, event3.getRequestedFloor());

        // Complete the event (assuming it was processed successfully)
        scheduler.complete(currentTime, 8, true);

        // Wait for a short period to allow the FloorSubsystem to print the completion message
        Thread.sleep(1000);

        // Stop the FloorSubsystem thread
        floorThread.interrupt();

        // Ensure that the completed events match the expected results
        assertEquals(5, scheduler.getCompleted().getRequestedFloor());
        assertEquals(1, scheduler.getCompleted().getRequestedFloor());
        assertEquals(8, scheduler.getCompleted().getRequestedFloor());
    }
}