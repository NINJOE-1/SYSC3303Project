# SYSC 3303 Elevator Project Iteration 2
## Group 9
Joseph Vretenar 101234613<br>
Samuel Mauricla - 101233500<br>
Bhavaan Balasubramaniam - 101233825

## Breakdown of the tasks:

Joseph Vretenar - Coded ElevatorSubsystem, Scheduler, Event, Direction, and FloorSubsystem.<br>
Samuel Mauricla - Coded FloorSubsystemTest, reviewed other code and documentation.<br>
Bhavaan Balasubramaniam - Wrote README.md, Created the State Machine Diagrams.<br>

## Project Iteration 2: Adding the Scheduler and Elevator Subsystems
The purpose of Iteration 2 is to implement State Machines for the Scheduler and Elevator Subsystems and assume that only 1 elevator is present. In addition, the Elevator Subsystem will notify the scheduler when an elevator reaches the floor.

## Files:
### [FloorSubsystem.java](src/FloorSubsystem.java)
The Floor class will extract data from an input file and parses each line to create an 'Event' object.
In addition, it will incorporate timestamps, the current floor, movement direction, and the destination floor. 

### [Scheduler.java](src/Scheduler.java)
The Scheduler class is reponsible for managing elevator requests. 
The requests are pending and completed requests. 
It consists of methods such as adding, processing, and completing events, ensuring there's thread-safe interactions.

### [Event.java](src/Event.java)
The Event class is responsible for maintaing elevator- related events which consists of timestamp (time), current floor (currentFloor), movement direction (move), and requested destination floor (destinationFloor).

### [ElevatorSubsystem.java](src/ElevatorSubsystem.java)
The Elevator Subsystem class is repsonsible for the elevator's movement and responses to scheduler events. 
Some methods that are there include 'Runnable' which handles the Elevator's movements and tracks down the amount of passenger to drop and pick up. The 'move' method checks the floor movement and updates the current floor and progress regarding that. 
The 'run' method checks for accurate simulation of elevator movements and waiting periods.

### [Direction.java](src/Direction.java)
The Direction class consists of three constants UP,DOWN and IDLE.
The constants represent the different possible number of states the Elevator is currently in.
'Enum' represents what direction the floor is in or if its in an IDLE state.

### [FloorSubsystemTest.java](src/FloorSubsystemTest.java)
The Testing class is reponsible for testing the Scheduler and ElevatorSubsystem in an elevator simulation when running the program.
The test is executed by passing in default values and simulates the event.
Assertions are implemented to ensure the code is correct. 

### 
The SchedulerTest class will test the transitions through 'working' and 'finished' states when events are added, processed, and requests are completed.
It will ensure the sample data displayed correctly.

### 
The ElevatorSubsystemTest class will ensure that the initial state of the ElevatorSubsystem is set to CurrentFloorWaiting.
It will also ensure that the upon processing an event triggered by a Scheduler, the ElevatorSubsystem transitions to the MovingRequest state.

## Setup Instructions
1. Ensure Java is installed on your system
2. Compile the Java files either using an IDE or through command lines
3. Ensure that the input file is in the same Project folder
4. Run the simulation using the Scheduler class. [src/Scheduler.java]

To edit the floors and requests, edit the inputFile.txt file

## Testing
1. Open the project in your preferred IDE.
2. Make sure JUnit is installed on your system. If not, download and install it.
3. Make sure Jupiter is installed on your system. If not, download and install it.
4. Run the FloorSubsystemTest file to run the test cases.

## Expected Output
### Simulation
When running the simulation the output will be the following for the elevator moving from floor 1 -> 5:
```
Elevator is in the waiting state
Scheduler is in the waiting state
Request at Sat Feb 17 12:00:00 EST 2024 (1 -> 5) is being processed
Scheduler is in the working state
Elevator is in the moving to request state
Elevator has reached 1
Elevator is in the arrived at request state
Elevator is in the picking up passenger state
Elevator is in the moving to destination state
Elevator moved to floor 2
Elevator moved to floor 3
Elevator moved to floor 4
Elevator has reached 5
Elevator is in the arrived at destination state
Elevator complete request at Sat Feb 17 12:00:41 EST 2024
Scheduler is in the finished request state
Elevator is in the dropping off passenger state
Elevator is in the waiting state
```
This shows the general layout that should be expected when running the simulation. The elevator will move to the floor that the button was pressed on, pick up the passengers, then move to the floor for the button they pressed in the elevator, once the elevator has reached the floor, it will drop off the passengers and complete the request.

### Testing
When running the Scheduler test case, the output should produce the following results:

Scheduler is in the waiting state
Request at Sat Feb 17 17:56:38 EST 2024 (1 -> 2) is being processed
Scheduler is in the working state
Elevator complete request at Sat Feb 17 17:56:38 EST 2024
Scheduler is in the finished request state
Scheduler is in the waiting state

When running the ElevatorSubsystem test case, the output should produce the following results:
Elevator is in the waiting state
Scheduler is in the waiting state
Request at Sat Feb 17 12:00:00 EST 2024 (1 -> 5) is being processed
Scheduler is in the working state
Elevator is in the moving to request state
Elevator has reached 1
Elevator is in the arrived at request state
Elevator is in the picking up passenger state
Elevator is in the moving to destination state
Elevator moved to floor 2
Elevator moved to floor 3
Elevator moved to floor 4
Elevator has reached 5
Elevator is in the arrived at destination state
Elevator complete request at Sat Feb 17 12:00:41 EST 2024
Scheduler is in the finished request state
Elevator is in the dropping off passenger state
Elevator is in the waiting state

This shows the requests that were made, when they were completed based on the current time, and the output should have no errors. If the output has an error then the test has failed.

## Diagrams:

The UML diagram can be found for this project can be found using this [link](Interation1ClassDiagram.png): 

The Sequence diagram for all the classes and functions can be found using this [link](SequenceDiagrams):

The State Machine diagram for the ElevatorSubsytem and Scheduler can be found using this [link](StateMachineDiagrams):







