import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {

    @Test
    void testSchedulerInitialState() {
        Scheduler scheduler = new Scheduler();
        assertEquals(Scheduler.schedulerStateMachine.waiting, scheduler.currentState);
    }

    @Test
    void testSchedulerStateTransition() {
        Scheduler scheduler = new Scheduler();

        // Add an event to trigger a state transition
        Event event = new Event(new Date(), 1, Direction.UP, 2);
        scheduler.addEvent(event);

        // After adding an event, the scheduler should transition to the working state
        assertEquals(Scheduler.schedulerStateMachine.working, scheduler.currentState);

        // Process the event to trigger another state transition
        scheduler.processEvent();

        // After processing the event, the scheduler should transition to the finished state
        assertEquals(Scheduler.schedulerStateMachine.finished, scheduler.currentState);

        // Complete the request to trigger another state transition
        scheduler.complete(new Date(), 2, true);

        // After completing the request, the scheduler should transition back to the waiting state
        assertEquals(Scheduler.schedulerStateMachine.waiting, scheduler.currentState);

        // Add another event to check if the scheduler transitions to the working state again
        scheduler.addEvent(new Event(new Date(), 3, Direction.UP, 4));

        // After adding another event, the scheduler should transition to the working state
        assertEquals(Scheduler.schedulerStateMachine.working, scheduler.currentState);
    }
}
