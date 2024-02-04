/**
 * SYSC 3303 Elevator Project Iteration 1
 * Group 9
 *  Joseph Vretenar - 101234613
 *  Samuel Mauricla - 101233500
 *  Bhavaan Balasubramaniam - 101233825
 * Due Febuary 3rd 2024
 *
 *  File written by Joseph Vretenar
 */

// import statements
import java.io.Serializable;
import java.util.Date;


/**
 * The type Event.
 */
// create class Event that implements Serializable
public class Event implements Serializable {
    Direction move;
    private final int currentFloor;
    private final Date time;
    private final int destinationFloor;

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

    @Override
    // create toString method
    public String toString() {
        return (this.getTime() + " " + this.getCurrentFloor() + " " + this.getDirection() + " " + this.getRequestedFloor());
    }
}