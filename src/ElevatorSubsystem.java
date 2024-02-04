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
import java.util.Calendar;
import java.util.Date;
import static java.lang.Math.abs;

/**
 * The type ElevatorSubsystem.
 */
// create class elevatorSubsystem that implements Runnable
public class ElevatorSubsystem implements Runnable{
    private final Scheduler scheduler;
    private int currentFloor;

    /**
     * Instantiates a new ElevatorSubsystem.
     *
     * @param scheduler the scheduler
     */
    // create constructor that takes in a scheduler and sets the current floor to 0
    public ElevatorSubsystem(Scheduler scheduler) {
        this.scheduler = scheduler;
        currentFloor = 0;
    }

    // create method move that takes in the current floor, direction, and desired floor
    private void move(int currentFloor, Direction d, int desiredFloor) {
        if(d == Direction.UP) {
            for(int i = currentFloor; i <desiredFloor; i++) {
                this.setCurrentFloor(i+1);
                if (i+1 < desiredFloor)
                    System.out.println("Elevator moved to floor " + (i+1));
                else
                    System.out.println("Elevator has reached " + desiredFloor);
            }
        }
        else if(d == Direction.DOWN) {
            for(int i = currentFloor; i > desiredFloor; i--) {
                this.setCurrentFloor(i-1);
                if (i-1 > desiredFloor)
                    System.out.println("Elevator moved to floor " + (i-1));
                else
                    System.out.println("Elevator has reached " + desiredFloor);
            }
        }
        else {
            this.currentFloor = desiredFloor;
            System.out.println("Elevator has reached " + currentFloor);
        }
    }

    /**
     * Sets current floor.
     *
     * @param newFloor the new floor
     */
    // create method setCurrentFloor that takes in an int and sets the current floor to that int
    public void setCurrentFloor(int newFloor) {
        this.currentFloor = newFloor;
    }

    @Override
    // create run method
    public void run() {
        while(true) {
            Event event = scheduler.processEvent();
            int diff = currentFloor - event.getCurrentFloor();
            if(diff>0) {
                move(currentFloor, Direction.DOWN, event.getCurrentFloor());
                System.out.println("Elevator picking up passengers");
            } else if(diff < 0) {
                move(currentFloor, Direction.UP, event.getCurrentFloor());
                System.out.println("Elevator picking up passengers");
            }

            int move = abs(currentFloor - event.getRequestedFloor());
            if(event.getDirection() == Direction.UP) {
                move(currentFloor, Direction.UP, event.getRequestedFloor());
                System.out.println("Elevator dropping off passengers");
            } else if(event.getDirection() == Direction.DOWN) {
                move(currentFloor, Direction.DOWN, event.getRequestedFloor());
                System.out.println("Elevator dropping off passengers");
            } else {
                move(currentFloor, Direction.IDLE, event.getRequestedFloor());
            }

            Date time = event.getTime();
            Calendar c = Calendar.getInstance();
            c.setTime(time);
            int movingTime = abs(diff) * 6;
            int movingTime2 = move * 6;
            int waitingTime = 17;
            c.add(Calendar.SECOND, (movingTime + waitingTime + movingTime2));
            time = c.getTime();
            scheduler.complete(time, this.currentFloor, true);
        }

    }
}