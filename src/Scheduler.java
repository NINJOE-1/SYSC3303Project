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
import java.util.Date;
import java.util.LinkedList;

/**
 * The type Scheduler.
 */
public class Scheduler {
    // create class Scheduler
    private static LinkedList<Event> requests = new LinkedList<>();
    private final LinkedList<Event> completed = new LinkedList<>();
    schedulerStateMachine currentState = schedulerStateMachine.waiting;

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

    /**
     * Is completed empty boolean.
     *
     * @return the boolean
     */
    // create method isCompletedEmpty that returns true if the completed list is empty
    public synchronized boolean isCompletedEmpty() {
        return completed.isEmpty();
    }

    /**
     * Gets completed.
     *
     * @return the completed
     */
    // create method getCompleted that returns the first request in the completed list
    public synchronized Event getCompleted() {
        return completed.pop();
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
     * Complete.
     *
     * @param completionTime        the completion time
     * @param currentFloor          the current floor
     * @param visitedRequestedFloor the visited requested floor
     */
    // create method complete that takes in a completion time, current floor, and a boolean visitedRequestedFloor
    public synchronized void complete(Date completionTime, int currentFloor, boolean visitedRequestedFloor) {
        if (currentFloor == requests.peek().getRequestedFloor() && visitedRequestedFloor) {
            System.out.println("Elevator complete request at " + completionTime.toString());
            Event completedRequest = requests.pop();
            completed.add(completedRequest);
            notifyAll();
            currentState = currentState.nextState();
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    // create main method
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        Thread floorThread = new Thread(new FloorSubsystem(scheduler));
        Thread elevatorThread = new Thread(new ElevatorSubsystem(scheduler));
        elevatorThread.start();
        floorThread.start();
    }
}
