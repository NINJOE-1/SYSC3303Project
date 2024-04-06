/**
 * SYSC 3303 Elevator Project Iteration 5
 * Group 9
 *  Joseph Vretenar - 101234613
 *  Samuel Mauricla - 101233500
 *  Bhavaan Balasubramaniam - 101233825
 *  File written by Joseph Vretenar
 */

// import statements
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The type Floor subsystem.
 */
// create class FloorSubsystem that implements Runnable
public class FloorSubsystem{
    /**
     * The Zero byte.
     */
    static byte zeroByte = (byte) 0;
    /**
     * The Num requests.
     */
    static int numRequests = 0;
    /**
     * The Finished requests.
     */
    static int finishedRequests = 0;
    /**
     * The Delay.
     */
    static long delay;

    private static Runnable sendRequest(Event request, DatagramSocket socket, InetAddress host, int port) throws IOException {
        byte[] requested = newRequest(request);
        DatagramPacket requestPacket = new DatagramPacket(requested, requested.length, host, port);
        socket.send(requestPacket);
        StringBuilder strRequest = new StringBuilder();
        for (byte b : requested) {
            strRequest.append(Integer.toHexString(b));
        }
        System.out.println("Sent request: " + request + " as "+ strRequest);
        return null;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        LinkedList<Event> data = Event.readDataFromFile();
        LocalTime currentTime = LocalTime.now();
        LocalTime targetTime = LocalTime.of(data.getFirst().getTime().getHours(), data.getFirst().getTime().getMinutes(), data.getFirst().getTime().getSeconds());

        long initialDelay = calculateDelay(currentTime, targetTime);
        DatagramSocket socket = new DatagramSocket();
        InetAddress host = InetAddress.getLocalHost();
        int port = 23;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        while (!data.isEmpty()) {
            Event request = data.pop();
            numRequests++;
            delay = calculateDelay(currentTime, LocalTime.of(request.getTime().getHours(), request.getTime().getMinutes(), request.getTime().getSeconds()));
            executor.schedule(() -> sendRequest(request, socket, host, port), delay - initialDelay, TimeUnit.SECONDS);
        }
        while (finishedRequests != numRequests) {
            try {
                byte[] responseData = new byte[5];
                DatagramPacket responsePacket = new DatagramPacket(responseData, 5);
                socket.receive(responsePacket);
                if (responseData[0] == 2) {
                    System.out.println("Potential error with elevator " + Integer.toUnsignedString(responseData[1]));
                } else {
                    System.out.println("Received confirmation of completed Request from elevator " + Integer.toUnsignedString(responseData[4]));
                    finishedRequests++;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static byte[] newRequest(Event request) {
        byte[] output = new byte[10];
        ByteBuffer buffer = ByteBuffer.wrap(output);
        buffer.put(zeroByte);
        byte hours = (byte) request.getTime().getHours();
        byte mins = (byte) request.getTime().getMinutes();
        byte secs = (byte) request.getTime().getSeconds();
        byte from = (byte) request.getCurrentFloor();
        byte to = (byte) request.getRequestedFloor();
        byte direction;
        if (request.getDirection() == Direction.UP) {
            direction = (byte) 1;
        } else {
            direction = (byte) 0;
        }
        buffer.put(hours);
        buffer.put(mins);
        buffer.put(secs);
        buffer.put(zeroByte);
        buffer.put(from);
        buffer.put(zeroByte);
        buffer.put(to);
        buffer.put(zeroByte);
        buffer.put(direction);
        output = buffer.array();
        return output;
    }

    private static long calculateDelay(LocalTime currentTime, LocalTime targetTime) {
        long initialDelay = ChronoUnit.SECONDS.between(currentTime, targetTime);
        if (initialDelay < 0) {
            initialDelay += TimeUnit.DAYS.toSeconds(1);
        }
        return initialDelay;
    }
}