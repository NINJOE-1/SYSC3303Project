/**
 * SYSC 3303 Elevator Project Iteration 3
 * Group 9
 *  Joseph Vretenar - 101234613
 *  Samuel Mauricla - 101233500
 *  Bhavaan Balasubramaniam - 101233825
 *  File written by Joseph Vretenar
 */

// import statements
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.LinkedList;
import static java.lang.Math.abs;

/**
 * The type Scheduler.
 */
public class Scheduler extends JFrame{
    // create class Scheduler
    private static LinkedList<Event> requests = new LinkedList<>();
    /**
     * The Current state.
     */
    schedulerStateMachine currentState = schedulerStateMachine.waiting;
    /**
     * The Elevator floors.
     */
    static int[] elevatorFloors = {1, 2, 3, 4};
    /**
     * The Elevator used.
     */
    static int[] elevatorUsed = {0, 0, 0, 0};

    static int[] numPeople = {0, 0, 0, 0};

    static Direction[] elevatorDirections = {Direction.IDLE, Direction.IDLE, Direction.IDLE, Direction.IDLE};
    /**
     * The Server port.
     */
    static int serverPort = 68;
    /**
     * The Client address.
     */
    static InetAddress clientAddress;
    /**
     * The Client port.
     */
    static int clientPort;
    /**
     * The Start time.
     */
    static long[] startTime = new long[4];

    static ImageIcon buttonIcon = new ImageIcon("button1.png");

    static ImageIcon pressedIcon = new ImageIcon("button2.png");

    static JLabel[] floorLabels = new JLabel[4];

    static JLabel[] directionLabels = new JLabel[4];

    static JLabel[][] buttonLabels = new JLabel[6][4];

    static JPanel buttonPanel = new JPanel(new GridLayout(6, 4));

    static JPanel mainPanel = new JPanel(new BorderLayout());

    static int[] elevatorRequest = {0, 0, 0, 0};

    /**
     * The enum Scheduler state machine.
     */
    public enum schedulerStateMachine {
        /**
         * The Waiting.
         */
        waiting {
            public schedulerStateMachine nextState() {
                System.out.println("Scheduler is in the waiting state");
                return working;
            }
        },

        /**
         * The Working.
         */
        working {
            public schedulerStateMachine nextState() {
                System.out.println("Scheduler is in the working state");
                return finished;
            }
        },

        /**
         * The Finished.
         */
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

        /**
         * Next state scheduler state machine.
         *
         * @return the scheduler state machine
         */
        public abstract schedulerStateMachine nextState();
    }

    private Scheduler() {
        setTitle("Scheduler");
        JPanel elevatorPanel = new JPanel(new GridLayout(1, 5));
        ImageIcon imageIcon = new ImageIcon("elevator.png");
        for (int i = 0; i < 4; i++) {
            JLabel imageLabel = new JLabel(imageIcon);
            JLabel numberLabel = new JLabel("Elevator " + (i + 1), JLabel.CENTER);
            floorLabels[i] = new JLabel("Floor: " + elevatorFloors[i], JLabel.CENTER);
            directionLabels[i] = new JLabel("Direction: " + elevatorDirections[i], JLabel.CENTER);
            JPanel subPanel = new JPanel(new BorderLayout());
            subPanel.add(imageLabel, BorderLayout.CENTER);
            JPanel textPanel = new JPanel(new GridLayout(3, 4));
            textPanel.add(numberLabel, BorderLayout.SOUTH);
            textPanel.add(floorLabels[i], BorderLayout.SOUTH);
            textPanel.add(directionLabels[i], BorderLayout.SOUTH);
            subPanel.add(textPanel, BorderLayout.SOUTH);
            elevatorPanel.add(subPanel);

        }
        mainPanel.add(elevatorPanel, BorderLayout.WEST);
        JPanel buttonPanel = new JPanel(new GridLayout(6, 4));
        int floor = 24;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                buttonLabels[j][i] = new JLabel(buttonIcon);
                JLabel numberLabel = new JLabel("" + floor, JLabel.CENTER);
                JPanel subPanel = new JPanel(new BorderLayout());
                subPanel.add(buttonLabels[j][i], BorderLayout.CENTER);
                subPanel.add(numberLabel, BorderLayout.SOUTH);
                buttonPanel.add(subPanel);
                floor--;
            }
        }
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        add(mainPanel);
        pack();
    }

    private static void resetButtons() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                buttonLabels[j][i].setIcon(buttonIcon);
            }
        }
    }

    private static void updateGUI() {
        for (int i = 0; i < 4; i++) {
            floorLabels[i].setText("Floor: " + elevatorFloors[i]);
            directionLabels[i].setText("Direction: " + elevatorDirections[i]);
        }
    }

    private static void updateButtonImages(int floor) {
        int i = (24 - floor) % 6;
        int j = (24 - floor) / 6;
        buttonLabels[i][j].setIcon(pressedIcon);
    }

    private static int selectElevator(int requestFloor) {
        int elevator1Distance = abs(elevatorFloors[0] - requestFloor);
        int elevator2Distance = abs(elevatorFloors[1] - requestFloor);
        int elevator3Distance = abs(elevatorFloors[2] - requestFloor);
        int elevator4Distance = abs(elevatorFloors[3] - requestFloor);
        if (((elevator1Distance == 0) || (elevator1Distance < elevator2Distance && elevator1Distance < elevator3Distance && elevator1Distance < elevator4Distance)) && elevatorUsed[0] == 0 && numPeople[0] < 6) {
            return 1;
        } else if (((elevator2Distance == 0) || (elevator2Distance < elevator1Distance && elevator2Distance < elevator3Distance && elevator2Distance < elevator4Distance)) && elevatorUsed[1] == 0 && numPeople[1] < 6) {
            return 2;
        } else if (((elevator3Distance == 0) || (elevator3Distance < elevator1Distance && elevator3Distance < elevator2Distance && elevator3Distance < elevator4Distance)) && elevatorUsed[2] == 0 && numPeople[2] < 6) {
            return 3;
        } else if (elevatorUsed[3] == 0){
            return 4;
        } else {
            return 0;
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
// create main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Scheduler gui = new Scheduler();
            gui.setVisible(true);
        });
        try {
            DatagramSocket receiveSocket = new DatagramSocket(23);
            DatagramSocket sendSocket = new DatagramSocket();
            while (true) {
                byte[] requestData = new byte[10];
                DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length);
                receive : try {
                    receiveSocket.setSoTimeout(1000);
                    receiveSocket.receive(requestPacket);
                    clientAddress = requestPacket.getAddress();
                    clientPort = requestPacket.getPort();
                    String requestFloor = Integer.toHexString(requestData[5]);
                    System.out.println("Received request from floor subsystem on floor: " + requestFloor);

                    InetAddress serverAddress = InetAddress.getLocalHost();
                    int selection = selectElevator(requestData[5]);
                    elevatorUsed[selection - 1] = 1;
                    elevatorRequest[selection - 1] = 1;
                    numPeople[selection - 1] += 1;
                    serverPort = 68 + selection;
                    updateButtonImages(requestData[5]);

                    DatagramPacket forwardPacket = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);
                    String requestTime = Integer.toUnsignedString(requestData[1]) + ":" + Integer.toUnsignedString(requestData[2]) + ":" + Integer.toUnsignedString(requestData[3]);
                    System.out.println("Forwarding request to elevator subsystem at: " + requestTime);
                    sendSocket.send(forwardPacket);
                    startTime[selection - 1] = System.currentTimeMillis();
                } catch (SocketTimeoutException e) {
                    break receive;
                }

                byte[] responseData = new byte[6];
                DatagramPacket responsePacket = new DatagramPacket(responseData, 5);
                elevator : try {
                    for (int i = 0; i < 5; i++) {
                        serverPort = 69 + i;
                        sendSocket.setSoTimeout(1000);
                        sendSocket.receive(responsePacket);
                        if (responseData[0] == 15) {
                            elevatorUsed[i] = 0;
                            numPeople[i] -= 1;
                            elevatorDirections[responseData[4] - 1] = Direction.IDLE;
                            updateGUI();
                            String completionTime = Integer.toUnsignedString(responseData[1]) + ":" + Integer.toUnsignedString(responseData[2]) + ":" + Integer.toUnsignedString(responseData[3]);
                            System.out.println("Received response from elevator subsystem at: " + completionTime);
                            DatagramPacket confirmation = new DatagramPacket(responseData, 5, clientAddress, clientPort);
                            System.out.println("Confirming completion with floor subsystem");
                            receiveSocket.send(confirmation);
                        } else {
                            if (responseData[0] == 0) {
                                elevatorFloors[responseData[1] - 1] = responseData[2];
                                if (responseData[3] == 1) {
                                    elevatorDirections[responseData[1] - 1] = Direction.UP;
                                } else if (responseData[3] == 2) {
                                    elevatorDirections[responseData[1] - 1] = Direction.DOWN;
                                } else {
                                    elevatorDirections[responseData[1] - 1] = Direction.IDLE;
                                }
                                if (elevatorRequest[responseData[1] - 1] == 1 && elevatorDirections[responseData[1] - 1] != Direction.IDLE) {
                                    elevatorRequest[responseData[1] - 1] = 0;
                                    resetButtons();
                                }
                                updateGUI();
                                mainPanel.repaint();
                                if (System.currentTimeMillis() - startTime[responseData[1] - 1] > 12000) {
                                    System.out.println("Elevator " + Integer.toUnsignedString(responseData[1]) + " is taking too long to close doors, potential error");
                                    byte[] error = {2, responseData[1], 0, 0, 0};
                                    DatagramPacket confirmation = new DatagramPacket(error, error.length, clientAddress, clientPort);
                                    receiveSocket.send(confirmation);
                                    elevatorUsed[responseData[1] - 1] = 2;
                                }
                                startTime[responseData[1] - 1] = System.currentTimeMillis();
                            } else {
                                if (System.currentTimeMillis() - startTime[responseData[1] - 1] > 20000) {
                                    System.out.println("Elevator " + Integer.toUnsignedString(responseData[1]) + " is taking too long to close doors, potential error");
                                    byte[] error = {2, responseData[1], 0, 0, 0};
                                    DatagramPacket confirmation = new DatagramPacket(error, error.length, clientAddress, clientPort);
                                    receiveSocket.send(confirmation);
                                    elevatorUsed[responseData[1] - 1] = 2;
                                }
                                startTime[responseData[1] - 1] = System.currentTimeMillis();
                            }
                        }
                    }
                } catch (SocketTimeoutException e) {
                    break elevator;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}