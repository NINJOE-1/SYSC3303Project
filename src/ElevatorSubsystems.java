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
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;

/**
 * The type ElevatorSubsystem.
 */
// create class elevatorSubsystem that implements Runnable
public class ElevatorSubsystems {
    private int currentFloor;
    private ElevatorStateMachine currentState = ElevatorStateMachine.CurrentFloorWaiting;
    private InetAddress clientAddress;
    private int clientPort;
    /**
     * The Zero byte.
     */
    static byte zeroByte = (byte) 0;
    /**
     * The F byte.
     */
    static byte fByte = (byte) 15;
    private final String ElevatorName;
    private final int port;
    private byte elevatorByte = 0;

    /**
     * Instantiates a new Elevator subsystems.
     *
     * @param currentFloor the current floor
     * @param ElevatorName the elevator name
     * @param port         the port
     */
    public ElevatorSubsystems(int currentFloor, String ElevatorName, int port) {
        this.currentFloor = currentFloor;
        this.ElevatorName = ElevatorName;
        this.port = port;
        this.elevatorByte = (byte) (port-68);
        System.out.println(ElevatorName + " is waiting at " + currentFloor);
    }

    /**
     * The enum Elevator state machine.
     */
    public enum ElevatorStateMachine {
        /**
         * The Current floor waiting.
         */
        CurrentFloorWaiting {
            public ElevatorStateMachine nextState() {
                return MovingRequest;
            }
        },

        /**
         * The Moving request.
         */
        MovingRequest {
            public ElevatorStateMachine nextState() {
                return PickUpPassenger;
            }
        },

        /**
         * The Pick up passenger.
         */
        PickUpPassenger {
            public ElevatorStateMachine nextState() {
                return MovingDestination;
            }
        },

        /**
         * The Moving destination.
         */
        MovingDestination {
            public ElevatorStateMachine nextState() {
                return DropOffPassenger;
            }
        },

        /**
         * The Drop off passenger.
         */
        DropOffPassenger {
            public ElevatorStateMachine nextState() {
                return CurrentFloorWaiting;
            }
        };

        /**
         * Next state elevator state machine.
         *
         * @return the elevator state machine
         */
        public abstract ElevatorStateMachine nextState();
    }


    // create method move that takes in the current floor, direction, and desired floor
    private void move(Direction d, DatagramSocket socket) throws InterruptedException, IOException {
        Thread.sleep(6000);
        if (d == Direction.UP)
            currentFloor++;
        else if (d == Direction.DOWN)
            currentFloor--;
        byte[] sendFloor = floor(currentFloor);
        DatagramPacket sendFloorPacket = new DatagramPacket(sendFloor, sendFloor.length, clientAddress, clientPort);
        socket.send(sendFloorPacket);
        System.out.println(ElevatorName + " moved to floor " + currentFloor);
    }

    /**
     * Run subsystem.
     */
    public void runSubsystem() {
        try {
            DatagramSocket socket = new DatagramSocket(port);

            while (true) {
                byte[] requestData = new byte[10];
                DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length);
                socket.receive(requestPacket);
                clientAddress = requestPacket.getAddress();
                clientPort = requestPacket.getPort();
                Event request = new Event();
                Date time = new Date();
                time.setHours(requestData[1]);
                time.setMinutes(requestData[2]);
                time.setSeconds(requestData[3]);
                request.time = time;
                request.currentFloor = requestData[5];
                request.destinationFloor = requestData[7];
                if (requestData[9] == 1) {
                    request.move = Direction.UP;
                } else {
                    request.move = Direction.DOWN;
                }
                boolean requested = true;
                while(requested) {
                    boolean newRequest = false;
                    switch (currentState) {
                        case CurrentFloorWaiting: {
                            currentState = currentState.nextState();
                            break;
                        }

                        case MovingRequest: {
                            System.out.println(ElevatorName + " is moving to request floor");
                            if (currentFloor == request.currentFloor) {
                                currentState = currentState.nextState();
                            } else while (currentFloor != request.currentFloor && !newRequest) {
                                if (currentFloor > request.currentFloor)
                                    move(Direction.DOWN, socket);
                                else
                                    move(Direction.UP, socket);
                            }
                            break;
                        }

                        case PickUpPassenger: {
                            System.out.println(ElevatorName + " has arrived at the request floor");
                            System.out.println(ElevatorName + " is picking up passenger");
                            sleep(12000);
                            byte[] updateScheduler = {1, (byte) (port - 68), 0, 0, 0};
                            DatagramPacket sendFloorPacket = new DatagramPacket(updateScheduler, updateScheduler.length, clientAddress, clientPort);
                            socket.send(sendFloorPacket);
                            currentState = currentState.nextState();
                            break;
                        }

                        case MovingDestination: {
                            System.out.println(ElevatorName + " is moving to destination floor");
                            while (currentFloor != request.destinationFloor && !newRequest) {
                                move(request.move, socket);
                            }
                            currentState = currentState.nextState();
                            break;
                        }

                        case DropOffPassenger: {
                            System.out.println(ElevatorName + " has arrived at destination");
                            System.out.println(ElevatorName + " is dropping off passenger");
                            Calendar c = Calendar.getInstance();
                            c.setTime(time);
                            int movingTime = abs(currentFloor - request.getRequestedFloor()) * 6;
                            int waitingTime = 17;
                            int movingTime2 = abs(currentFloor - request.getCurrentFloor()) * 6;
                            c.add(Calendar.SECOND, (movingTime + waitingTime + movingTime2));
                            sleep(12000);
                            byte[] response = response(c);
                            DatagramPacket responsePacket = new DatagramPacket(response, response.length, clientAddress, clientPort);
                            String responseTime = Integer.toUnsignedString(response[1]) + ":" + Integer.toUnsignedString(response[2]) + ":" + Integer.toUnsignedString(response[3]);
                            StringBuilder strResponse = new StringBuilder();
                            for (byte b : response) {
                                strResponse.append(Integer.toHexString(b));
                            }
                            System.out.println("Sending response time: " + responseTime + " as " + strResponse);
                            socket.send(responsePacket);
                            currentState = currentState.nextState();
                            requested = false;
                            System.out.println(ElevatorName + " is waiting at " + currentFloor + "\n");
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private byte[] response(Calendar c) {
        byte[] output = new byte[5];
        ByteBuffer buffer = ByteBuffer.wrap(output);
        buffer.put(fByte);
        byte hours = (byte) c.getTime().getHours();
        byte mins = (byte) c.getTime().getMinutes();
        byte secs = (byte) c.getTime().getSeconds();
        byte elevator = (byte) (port - 68);
        buffer.put(hours);
        buffer.put(mins);
        buffer.put(secs);
        buffer.put(elevator);
        output = buffer.array();
        return output;
    }

    private byte[] floor(int floor) {
        byte[] output = new byte[4];
        ByteBuffer buffer = ByteBuffer.wrap(output);
        buffer.put(zeroByte);
        buffer.put(elevatorByte);
        buffer.put((byte) floor);
        buffer.put(zeroByte);
        output = buffer.array();
        return output;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ElevatorSubsystems elevator1 = new ElevatorSubsystems(1, "Elevator 1", 69);
        ElevatorSubsystems elevator2 = new ElevatorSubsystems(2, "Elevator 2", 70);
        ElevatorSubsystems elevator3 = new ElevatorSubsystems(3, "Elevator 3", 71);
        ElevatorSubsystems elevator4 = new ElevatorSubsystems(4, "Elevator 4", 72);

        new Thread(elevator1::runSubsystem).start();
        new Thread(elevator2::runSubsystem).start();
        new Thread(elevator3::runSubsystem).start();
        new Thread(elevator4::runSubsystem).start();
    }
}