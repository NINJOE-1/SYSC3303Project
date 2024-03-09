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
public class ElevatorSubsystem {
    private static int currentFloor = 2;
    static ElevatorStateMachine currentState = ElevatorStateMachine.CurrentFloorWaiting;
    static InetAddress clientAddress;
    static int clientPort;
    static byte zeroByte = (byte) 0;
    static byte oneByte = (byte) 1;
    static byte fByte = (byte) 15;
    static boolean requested = false;

    public int getCurrentFloor() {
        return currentFloor;
    }

    public enum ElevatorStateMachine {
        CurrentFloorWaiting {
            public ElevatorStateMachine nextState() {
                return MovingRequest;
            }
        },

        MovingRequest {
            public ElevatorStateMachine nextState() {
                return PickUpPassenger;
            }
        },

        PickUpPassenger {
            public ElevatorStateMachine nextState() {
                return MovingDestination;
            }
        },

        MovingDestination {
            public ElevatorStateMachine nextState() {
                return DropOffPassenger;
            }
        },

        DropOffPassenger {
            public ElevatorStateMachine nextState() {
                return CurrentFloorWaiting;
            }
        };
        public abstract ElevatorStateMachine nextState();
    }


    // create method move that takes in the current floor, direction, and desired floor
    private static void move(Direction d, int desiredFloor, DatagramSocket socket) throws InterruptedException, IOException {
        if (currentFloor == desiredFloor) {
            System.out.println("Elevator has reached " + currentFloor);
        } else if (d == Direction.UP) {
            while (currentFloor < desiredFloor) {
                sleep(600);
                currentFloor++;
                byte[] sendFloor = floor(currentFloor);
                DatagramPacket sendFloorPacket = new DatagramPacket(sendFloor, sendFloor.length, clientAddress, clientPort);
                socket.send(sendFloorPacket);
                if (currentFloor < desiredFloor)
                    System.out.println("Elevator moved to floor " + currentFloor);
                else
                    System.out.println("Elevator has reached " + desiredFloor);
            }
        } else if (d == Direction.DOWN) {
            while (currentFloor > desiredFloor) {
                sleep(600);
                currentFloor--;
                byte[] sendFloor = floor(currentFloor);
                DatagramPacket sendFloorPacket = new DatagramPacket(sendFloor, sendFloor.length, clientAddress, clientPort);
                socket.send(sendFloorPacket);
                if (currentFloor > desiredFloor)
                    System.out.println("Elevator moved to floor " + currentFloor);
                else
                    System.out.println("Elevator has reached " + desiredFloor);
            }
        }
    }

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(69);

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
                requested = true;
                while(requested) {
                    switch (currentState) {
                        case CurrentFloorWaiting: {
                            System.out.println("Elevator is waiting at " + currentFloor);
                            currentState = currentState.nextState();
                            break;
                        }

                        case MovingRequest: {
                            System.out.println("Elevator is moving to request floor");
                            if (currentFloor > request.currentFloor) {
                                move(Direction.DOWN, request.currentFloor, socket);
                            } else if (currentFloor < request.currentFloor) {
                                move(Direction.UP, request.currentFloor, socket);
                            }
                            currentState = currentState.nextState();
                            break;
                        }

                        case PickUpPassenger: {
                            System.out.println("Elevator has arrived at the request floor");
                            System.out.println("Elevator is picking up passenger");
                            sleep(1200);
                            currentState = currentState.nextState();
                            break;
                        }

                        case MovingDestination: {
                            System.out.println("Elevator is moving to destination floor");
                            move(request.getDirection(), request.getRequestedFloor(), socket);
                            currentState = currentState.nextState();
                            break;
                        }

                        case DropOffPassenger: {
                            System.out.println("Elevator has arrived at destination");
                            System.out.println("Elevator is dropping off passenger");
                            Calendar c = Calendar.getInstance();
                            c.setTime(time);
                            int movingTime = abs(currentFloor - request.getRequestedFloor()) * 6;
                            int waitingTime = 17;
                            int movingTime2 = abs(currentFloor - request.getCurrentFloor()) * 6;
                            c.add(Calendar.SECOND, (movingTime + waitingTime + movingTime2));
                            sleep(1200);
                            byte[] response = response(c);
                            DatagramPacket responsePacket = new DatagramPacket(response, response.length, clientAddress, clientPort);
                            String responseTime = Integer.toUnsignedString(response[1]) + ":" + Integer.toUnsignedString(response[2]) + ":" + Integer.toUnsignedString(response[3]);
                            String strResponse = "";
                            for (int i = 0; i < response.length; i++){
                                strResponse += Integer.toHexString(response[i]);
                            }
                            for (int i = 0; i < response.length; i++) {

                            }
                            System.out.println("Sending response time: " + responseTime + " as " + strResponse + "\n");
                            socket.send(responsePacket);
                            currentState = currentState.nextState();
                            requested = false;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static byte[] response(Calendar c) {
        byte[] output = new byte[4];
        ByteBuffer buffer = ByteBuffer.wrap(output);
        buffer.put(fByte);
        byte hours = (byte) c.getTime().getHours();
        byte mins = (byte) c.getTime().getMinutes();
        byte secs = (byte) c.getTime().getSeconds();
        buffer.put(hours);
        buffer.put(mins);
        buffer.put(secs);
        output = buffer.array();
        return output;
    }

    private static byte[] floor(int floor) {
        byte[] output = new byte[4];
        ByteBuffer buffer = ByteBuffer.wrap(output);
        buffer.put(zeroByte);
        buffer.put(oneByte);
        buffer.put((byte) floor);
        buffer.put(zeroByte);
        output = buffer.array();
        return output;
    }
}