/**
 * SYSC 3303 Elevator Project Iteration 5
 * Group 9
 *  Joseph Vretenar - 101234613
 *  Samuel Mauricla - 101233500
 *  Bhavaan Balasubramaniam - 101233825
 *  File written by Joseph Vretenar
 */

// import statements
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * The type Event.
 */
// create class Event that implements Serializable
public class Event implements Serializable {
    /**
     * The Move.
     */
    Direction move;
    /**
     * The Current floor.
     */
    int currentFloor;
    /**
     * The Time.
     */
    Date time;
    /**
     * The Destination floor.
     */
    int destinationFloor;

    /**
     * Instantiates a new Event.
     *
     * @param time             the time
     * @param currentFloor     the current floor
     * @param direction        the direction
     * @param destinationFloor the destination floor
     */
// create constructor that takes in time, current floor, direction, and destination floor
    public Event(Date time, int currentFloor, Direction direction, int destinationFloor) {
        this.time = time;
        this.currentFloor = currentFloor;
        this.move = direction;
        this.destinationFloor = destinationFloor;
    }

    /**
     * Instantiates a new Event.
     */
    public Event() {

    }

    /**
     * Gets time.
     *
     * @return the time
     */
// create method getTime that returns the time
    public Date getTime() {
        return this.time;
    }

    /**
     * Gets current floor.
     *
     * @return the current floor
     */
// create method getCurrentFloor that returns the current floor
    public int getCurrentFloor() {
        return this.currentFloor;
    }

    /**
     * Gets requested floor.
     *
     * @return the requested floor
     */
// create method getRequestedFloor that returns the destination floor
    public int getRequestedFloor() {
        return this.destinationFloor;
    }

    /**
     * Gets direction.
     *
     * @return the direction
     */
// create method getDirection that returns the direction
    public Direction getDirection() {
        return this.move;
    }

    /**
     * Read data from file linked list.
     *
     * @return the linked list
     */
    public static LinkedList<Event> readDataFromFile() {
        File inputFile = new File("inputFile.txt");
        LinkedList<Event> data = null;
        try {
            data = new LinkedList<>();
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] splitLine = line.split(" ");
                Direction move = Direction.DOWN;
                if (splitLine[2].equals("Up")) {
                    move = Direction.UP;
                }
                Date inputTime = new SimpleDateFormat("HH:mm:ss.SSS").parse(splitLine[0]);
                Date date = new Date();
                date.setHours(inputTime.getHours());
                date.setMinutes(inputTime.getMinutes());
                date.setSeconds(inputTime.getSeconds());
                int currentFloor = Integer.parseInt(splitLine[1]);
                int destinationFloor = Integer.parseInt(splitLine[3]);
                data.add(new Event(date, currentFloor, move, destinationFloor));
            }
            reader.close();
            return data;
        } catch (FileNotFoundException | NumberFormatException | ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    // create toString method
    public String toString() {
        return (this.getTime() + " " + this.getCurrentFloor() + " " + this.getDirection() + " " + this.getRequestedFloor());
    }
}