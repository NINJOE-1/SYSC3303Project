/**
 * SYSC 3303 Elevator Project Iteration 3
 * Group 9
 *  Joseph Vretenar - 101234613
 *  Samuel Mauricla - 101233500
 *  Bhavaan Balasubramaniam - 101233825
 *  File written by Joseph Vretenar
 */

// import statements
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import static java.lang.Math.abs;

/**
 * The type Scheduler.
 */
public class Scheduler {
    // create class Scheduler
    private static LinkedList<Event> requests = new LinkedList<>();
    schedulerStateMachine currentState = schedulerStateMachine.waiting;
    static int elevator1Floor = 2;
    static int elevator2Floor = 4;
    static int serverPort = 69;
    static int elevatorUsed;

    /**
     * Add event.
     *
     * @param r the r
     */
    // create method addEvent that takes in an event and adds it to the requests list
    public synchronized void addEvent(Event r) {
        requests.add(r);
        if (currentState.equals(Scheduler.schedulerStateMachine.waiting)) {
            currentState = currentState.nextState();
        }
        notifyAll();
    };

    /**
     * Process event.
     *
     * @return the event
     */
    // create method processEvent that returns the first request in the list
    public synchronized Event processEvent() {
        while(requests.peek()==null) {
            try {
                wait();
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        Event r = requests.peek();
        System.out.println("Request at " + r.getTime().toString() + " (" + r.getCurrentFloor() + " -> " + r.getRequestedFloor() + ") is being processed");
        currentState = currentState.nextState();
        return r;
    }

    public enum schedulerStateMachine {
        waiting {
            public schedulerStateMachine nextState() {
                System.out.println("Scheduler is in the waiting state");
                return working;
            }
        },

        working {
            public schedulerStateMachine nextState() {
                System.out.println("Scheduler is in the working state");
                return finished;
            }
        },

        finished {
            public schedulerStateMachine nextState() {
                System.out.println("Scheduler is in the finished request state");
                if (!requests.isEmpty()) {
                    return working;
                } else {
                    return waiting;
                }
            }
        };
        public abstract schedulerStateMachine nextState();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    // create main method
    public static void main(String[] args) {
        try {
            DatagramSocket receiveSocket = new DatagramSocket(23);
            DatagramSocket sendSocket = new DatagramSocket();

            while (true) {
                byte[] requestData = new byte[10];
                DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length);
                receiveSocket.receive(requestPacket);

                String requestFloor = Integer.toHexString(requestData[5]);
                System.out.println("Received request from floor subsystem on floor: " + requestFloor);

                InetAddress serverAddress = InetAddress.getLocalHost();
                if (abs(elevator1Floor - requestData[5]) > abs(elevator2Floor - requestData[5])) {
                    serverPort = 70;
                    elevatorUsed = 2;
                } else {
                    serverPort = 69;
                    elevatorUsed = 1;
                }

                DatagramPacket forwardPacket = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);
                String requestTime = Integer.toUnsignedString(requestData[1]) + ":" + Integer.toUnsignedString(requestData[2]) + ":" + Integer.toUnsignedString(requestData[3]);
                System.out.println("Forwarding request to elevator subsystem at: " + requestTime);
                sendSocket.send(forwardPacket);

                byte[] responseData = new byte[4];
                DatagramPacket responsePacket = new DatagramPacket(responseData, 4);
                while (true) {
                    sendSocket.receive(responsePacket);
                    if (responseData[0] == 15) {
                        String completionTime = Integer.toUnsignedString(responseData[1]) + ":" + Integer.toUnsignedString(responseData[2]) + ":" + Integer.toUnsignedString(responseData[3]);
                        System.out.println("Received response from elevator subsystem at: " + completionTime);
                        break;
                    } else {
                        if (responseData[1] == 1) {
                            elevator1Floor = responseData[2];
                        } else {
                            elevator2Floor = responseData[2];
                        }
                    }
                }

                InetAddress clientAddress = requestPacket.getAddress();
                int clientPort = requestPacket.getPort();
                DatagramPacket confirmation = new DatagramPacket(responseData, 1, clientAddress, clientPort);
                System.out.println("Confirming completion with floor subsystem\n");
                receiveSocket.send(confirmation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}