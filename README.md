# SYSC 3303 Elevator Project Iteration 1
## Group 9
Joseph Vretenar 101234613<br>
Samuel Mauricla - 101233500<br>
Bhavaan Balasubramaniam - 101233825

## Breakdown of the tasks:

Joseph Vretenar - Coded ElevatorSubsystem, Scheduler, Event, Direction, and FloorSubsystem.<br>
Samuel Mauricla - Coded FloorSubsystemTest, reviewed other code and documentation.<br>
Bhavaan Balasubramaniam - Wrote README.md, created UML class and sequence diagrams.

## Project Iteration 1: Establishing Connections between the three subsystems
The purpose of Iteration 1 is to create 3 threads which are the Elevator, Floor and Scheduler. The Elevator and Floor subsystems are the clients, while the Scheduler is the server. The Floor will read in the following format: Time, Floor, Button. Each line of input is sent back to the scheduler and then the Elevators will make calls to the Scheduler replying back when work is needed.The Elevator will then send the data back to the Scheduler who will then send it back to the Floor. In this Iteration, the scheduler is only being used as a communication channel from the Floor thread to the Elevator thread and back again.

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
When running the simulation the output will be the following for the elevator moving from floor 2 -> 8:
```
Request at Sat Feb 03 12:10:00 EST 2024 (2 -> 8) is being processed
Elevator has reached 2
Elevator picking up passengers
Elevator moved to floor 3
Elevator moved to floor 4
Elevator moved to floor 5
Elevator moved to floor 6
Elevator moved to floor 7
Elevator has reached 8
Elevator dropping off passengers
Elevator completed request at Sat Feb 03 12:10:59 EST 2024
```
This shows the general layout that should be expected when running the simulation. The elevator will move to the floor that the button was pressed on, pick up the passengers, then move to the floor for the button they pressed in the elevator, once the elevator has reached the floor, it will drop off the passengers and complete the request.

### Testing
When running the test case, the output should produce the following results:
```
Request at Sat Feb 03 12:00:00 EST 2024 (1 -> 5) is being processed
Event 1: Sat Feb 03 12:00:00 EST 2024 1 UP 5
Elevator completed request at Sat Feb 03 17:04:22 EST 2024

Request at Sat Feb 03 12:05:00 EST 2024 (3 -> 1) is being processed
Elevator completed request at Sat Feb 03 17:04:22 EST 2024

Request at Sat Feb 03 12:10:00 EST 2024 (2 -> 8) is being processed
Elevator completed request at Sat Feb 03 17:04:22 EST 2024
```
This shows the requests that were made, when they were completed based on the current time, and the output should have no errors. If the output has an error then the test has failed.

## Diagrams:

The UML diagram can be found for this project can be found using the [link](Interation1ClassDiagram.png): 

The Sequence diagram for all the classes and functions can be found using this [link](SequenceDiagrams):








