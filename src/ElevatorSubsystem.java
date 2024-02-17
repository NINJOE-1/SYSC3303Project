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
public class ElevatorSubsystem implements Runnable {
    private final Scheduler scheduler;
    private int currentFloor;
    private Event event = null;
    ElevatorStateMachine currentState = ElevatorStateMachine.CurrentFloorWaiting;
    public boolean testing = false;

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

    public int getCurrentFloor() {
        return currentFloor;
    }

    public enum ElevatorStateMachine {
        CurrentFloorWaiting {
            public ElevatorStateMachine nextState() {
                System.out.println("Elevator is in the waiting state");
                return MovingRequest;
            }
        },

        MovingRequest {
            public ElevatorStateMachine nextState() {
                System.out.println("Elevator is in the moving to request state");
                return ArriveRequest;
            }
        },

        ArriveRequest {
            public ElevatorStateMachine nextState() {
                System.out.println("Elevator is in the arrived at request state");
                return PickUpPassenger;
            }
        },

        PickUpPassenger {
            public ElevatorStateMachine nextState() {
                System.out.println("Elevator is in the picking up passenger state");
                return MovingDestination;
            }
        },

        MovingDestination {
            public ElevatorStateMachine nextState() {
                System.out.println("Elevator is in the moving to destination state");
                return ArriveDestination;
            }
        },

        ArriveDestination {
            public ElevatorStateMachine nextState() {
                System.out.println("Elevator is in the arrived at destination state");
                return DropOffPassenger;
            }
        },

        DropOffPassenger {
            public ElevatorStateMachine nextState() {
                System.out.println("Elevator is in the dropping off passenger state");
                return CurrentFloorWaiting;
            }
        };
        public abstract ElevatorStateMachine nextState();
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
        running:
        while (true) {
            switch (currentState) {
                case CurrentFloorWaiting: {
                    currentState = currentState.nextState();
                    event = scheduler.processEvent();
                    break;
                }

                case MovingRequest: {
                    currentState = currentState.nextState();
                    int diff = currentFloor - event.getCurrentFloor();
                    if(diff>0) {
                        move(currentFloor, Direction.DOWN, event.getCurrentFloor());
                    } else if(diff < 0) {
                        move(currentFloor, Direction.UP, event.getCurrentFloor());
                    }
                    break;
                }

                case ArriveRequest: {
                    currentState = currentState.nextState();
                    break;
                }

                case PickUpPassenger: {
                    currentState = currentState.nextState();
                    break;
                }

                case MovingDestination: {
                    currentState = currentState.nextState();
                    if (event.getDirection() == Direction.UP) {
                        move(currentFloor, Direction.UP, event.getRequestedFloor());
                    } else if (event.getDirection() == Direction.DOWN) {
                        move(currentFloor, Direction.DOWN, event.getRequestedFloor());
                    } else {
                        move(currentFloor, Direction.IDLE, event.getRequestedFloor());
                    }
                    break;
                }

                case ArriveDestination: {
                    currentState = currentState.nextState();
                    break;
                }

                case DropOffPassenger: {
                    Date time = event.getTime();
                    Calendar c = Calendar.getInstance();
                    c.setTime(time);
                    int movingTime = abs(currentFloor - event.getRequestedFloor()) * 6;
                    int waitingTime = 17;
                    int movingTime2 = abs(currentFloor - event.getCurrentFloor()) * 6;
                    c.add(Calendar.SECOND, (movingTime + waitingTime + movingTime2));
                    time = c.getTime();
                    scheduler.complete(time, this.currentFloor, true);
                    currentState = currentState.nextState();
                    if (testing) {
                        break running;
                    }
                    break;
                }
            }
        }
    }
}
