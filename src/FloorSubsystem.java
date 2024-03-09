/**
 * SYSC 3303 Elevator Project Iteration 3
 * Group 9
 *  Joseph Vretenar - 101234613
 *  Samuel Mauricla - 101233500
 *  Bhavaan Balasubramaniam - 101233825
 *  File written by Joseph Vretenar
 */

// import statements
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * The type Floor subsystem.
 */
// create class FloorSubsystem that implements Runnable
public class FloorSubsystem{
    static byte zeroByte = (byte) 0;

    public static void main(String[] args) {
        LinkedList<Event> data = Event.readDataFromFile();
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress host = InetAddress.getLocalHost();
            int port = 23;
            while (true) {
                while (!data.isEmpty()) {
                    Event request = data.pop();
                    byte[] requested = newRequest(request);
                    DatagramPacket requestPacket = new DatagramPacket(requested, requested.length, host, port);
                    socket.send(requestPacket);
                    String strRequest = "";
                    for (int i = 0; i < requested.length; i++){
                        strRequest += Integer.toHexString(requested[i]);
                    }
                    System.out.println("Sent request: " + request + " as "+ strRequest);
                    byte[] responseData = new byte[1];
                    DatagramPacket responsePacket = new DatagramPacket(responseData, 1);
                    socket.receive(responsePacket);
                    if (responseData[0] == 15) {
                        System.out.println("Received confirmation of completed Request\n");
                    } else {
                        System.out.println("Waiting for request confirmation");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}