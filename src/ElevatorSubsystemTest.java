import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ElevatorSubsystemTest {
    static byte zeroByte = (byte) 0;
    static InetAddress serverAddress;

    static {
        try {
            serverAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public ElevatorSubsystemTest() throws UnknownHostException {
    }

    public static void main(String[] args) throws IOException {

        // Instantiate ElevatorSubsystem instances
        ElevatorSubsystem elevatorSubsystem1 = new ElevatorSubsystem();
        ElevatorSubsystem2 elevatorSubsystem2 = new ElevatorSubsystem2();

        // Simulate elevator system operation
        checkFloor(elevatorSubsystem1, elevatorSubsystem2, 2, 4);
        simulateElevatorSystem(elevatorSubsystem1, elevatorSubsystem2);
        checkFloor(elevatorSubsystem1, elevatorSubsystem2, 2, 4);
    }

    private static void simulateElevatorSystem(ElevatorSubsystem elevatorSubsystem1, ElevatorSubsystem2 elevatorSubsystem2) throws IOException {
        DatagramSocket sendSocket = new DatagramSocket();
        byte[] test1 = createTest(12, 0, 0, 7, 2, 1);
        byte[] test2 = createTest(12, 1, 0, 1, 4, 0);
        DatagramPacket elevator1 = new DatagramPacket(test1, test1.length, serverAddress, 69);
        sendSocket.send(elevator1);
        DatagramPacket elevator2 = new DatagramPacket(test2, test2.length, serverAddress, 70);
        sendSocket.send(elevator2);
    }

    private static byte[] createTest(int hours, int mins, int secs, int from, int to, int direction) {
        byte[] output = new byte[10];
        ByteBuffer buffer = ByteBuffer.wrap(output);
        buffer.put(zeroByte);
        byte bhours = (byte) hours;
        byte bmins = (byte) mins;
        byte bsecs = (byte) secs;
        byte bfrom = (byte) from;
        byte bto = (byte) to;
        byte bdirection = (byte) direction;
        buffer.put(bhours);
        buffer.put(bmins);
        buffer.put(bsecs);
        buffer.put(zeroByte);
        buffer.put(bfrom);
        buffer.put(zeroByte);
        buffer.put(bto);
        buffer.put(zeroByte);
        buffer.put(bdirection);
        output = buffer.array();
        return output;
    }

    private static void checkFloor(ElevatorSubsystem elevatorSubsystem1, ElevatorSubsystem2 elevatorSubsystem2, int floor1, int floor2) {
        // Check whether each elevator is at the final floor when they finish
        int finalFloor1 = elevatorSubsystem1.getCurrentFloor();
        int finalFloor2 = elevatorSubsystem2.getCurrentFloor();

        System.out.println("Elevator 1 is at correct floor: " + (finalFloor1 == floor1));
        System.out.println("Elevator 2 is at correct floor: " + (finalFloor2 == floor2));
    }
}