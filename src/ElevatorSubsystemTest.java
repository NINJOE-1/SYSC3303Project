import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

class ElevatorSubsystemTest {

    @Test
    void testElevatorInitialState() {
        Scheduler scheduler = new Scheduler();
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(scheduler);
        assertEquals(ElevatorSubsystem.ElevatorStateMachine.CurrentFloorWaiting, elevatorSubsystem.currentState);
    }

    @Test
    void testElevatorStateTransition() {
        Scheduler scheduler = new Scheduler();
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(scheduler);

        // Assert the initial state
        assertEquals(ElevatorSubsystem.ElevatorStateMachine.CurrentFloorWaiting, elevatorSubsystem.currentState);

        // Add an event to trigger a state transition
        Event event = new Event(new Date(), 1, Direction.UP, 2);
        scheduler.addEvent(event);

        // Run the elevatorSubsystem to process the event
        elevatorSubsystem.testing = true;
        elevatorSubsystem.run();

        // Assert the final state after processing the event
        assertEquals(ElevatorSubsystem.ElevatorStateMachine.CurrentFloorWaiting, elevatorSubsystem.currentState);
    }
}
