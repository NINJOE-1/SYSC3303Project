# SYSC 3303 Elevator Project Iteration 3
## Group 9
Joseph Vretenar 101234613<br>
Samuel Mauricla - 101233500<br>
Bhavaan Balasubramaniam - 101233825

## Breakdown of the tasks:

Joseph Vretenar - Coded ElevatorSubsystem, Scheduler, Event, Direction, and FloorSubsystem.<br>
Samuel Mauricla - Coded FloorSubsystemTest, reviewed other code and documentation.<br>
Bhavaan Balasubramaniam - Wrote README.md, Created the State Machine Diagrams.<br>

## Project Iteration 3: Multiple cars and System Distribution
The purpose of Iteration 3 is to implement the UDP(User Datagram Packets) to communicate between 3 seperate programs. In addition, the subsytem has 2 cars.

## Files:
### [FloorSubsystem.java](src/FloorSubsystem.java)
The Floor class will extract data from an input file and parses each line to create an 'Event' object.
In addition, it will incorporate timestamps, the current floor, movement direction, and the destination floor.

### [Scheduler.java](src/Scheduler.java)
The Scheduler class is reponsible for managing elevator requests.
The requests are pending and completed requests.
It consists of methods such as adding, processing, and completing events, ensuring there's thread-safe interactions.

### [Event.java](src/Event.java)
The Event class is responsible for maintaing elevator-related events which consists of timestamp (time), current floor (currentFloor), movement direction (move), and requested destination floor (destinationFloor).

### [ElevatorSubsystem.java](src/ElevatorSubsystem.java)
The Elevator Subsystem class is repsonsible for the elevator's movement and responses to scheduler events.
Some methods that are there include 'Runnable' which handles the Elevator's movements and tracks down the amount of passenger to drop and pick up. The 'move' method checks the floor movement and updates the current floor and progress regarding that.
The 'run' method checks for accurate simulation of elevator movements and waiting periods.

### [Direction.java](src/Direction.java)
The Direction class consists of three constants UP,DOWN and IDLE.
The constants represent the different possible number of states the Elevator is currently in.
'Enum' represents what direction the floor is in or if its in an IDLE state.

## Setup Instructions
1. Ensure Java is installed on your system
2. Compile the Java files either using an IDE or through command lines
3. Ensure that the input file is in the same Project folder
4. To run the simulation, the files must be run in the following order:<br>
   a. ElevatorSubsystem.java and ElevatorSubsystem2.java<br>
   b. Scheduler.java<br>
   c. FloorSubsystem.java<br>

To edit the floors and requests, edit the inputFile.txt file

## Testing
1. Open the project in your preferred IDE.
2. Make sure JUnit is installed on your system. If not, download and install it.
3. Make sure Jupiter is installed on your system. If not, download and install it.
4. Run the ElevatorSubsystemTest.java file to test the ElevatorSubsystem class.

## Expected Output
### Simulation
When running the simulation, the output should show up on four different console windows, each corresponding to the file that was run.
The output for the ElevatorSubsystem should look like this:
```
Elevator is waiting at 2
Elevator is moving to request floor
Elevator has reached 1
Elevator has arrived at the request floor
Elevator is picking up passenger
Elevator is moving to destination floor
Elevator moved to floor 2
Elevator moved to floor 3
Elevator moved to floor 4
Elevator moved to floor 5
Elevator has reached 6
Elevator has arrived at destination
Elevator is dropping off passenger
Sending response time: 12:0:47 as fc02f

Elevator is waiting at 6
Elevator is moving to request floor
Elevator moved to floor 5
Elevator has reached 4
Elevator has arrived at the request floor
Elevator is picking up passenger
Elevator is moving to destination floor
Elevator moved to floor 3
Elevator moved to floor 2
Elevator has reached 1
Elevator has arrived at destination
Elevator is dropping off passenger
Sending response time: 12:3:35 as fc323
```
The output for the Scheduler should look like this:
```Received request from floor subsystem on floor: 1
Forwarding request to elevator subsystem at: 12:0:0
Received response from elevator subsystem at: 12:0:47
Confirming completion with floor subsystem

Received request from floor subsystem on floor: 3
Forwarding request to elevator subsystem at: 12:1:0
Received response from elevator subsystem at: 12:1:29
Confirming completion with floor subsystem

Received request from floor subsystem on floor: 2
Forwarding request to elevator subsystem at: 12:2:0
Received response from elevator subsystem at: 12:2:53
Confirming completion with floor subsystem

Received request from floor subsystem on floor: 4
Forwarding request to elevator subsystem at: 12:3:0
Received response from elevator subsystem at: 12:3:35
Confirming completion with floor subsystem
```
The Output for the FloorSubsystem should look like this:
```
Sent request: Sat Mar 09 12:00:00 EST 2024 1 UP 6 as 0c00010601
Received confirmation of completed Request

Sent request: Sat Mar 09 12:01:00 EST 2024 3 DOWN 1 as 0c10030100
Received confirmation of completed Request

Sent request: Sat Mar 09 12:02:00 EST 2024 2 UP 8 as 0c20020801
Received confirmation of completed Request

Sent request: Sat Mar 09 12:03:00 EST 2024 4 DOWN 1 as 0c30040100
Received confirmation of completed Request
```

### Testing
When running the Scheduler test case, the output should produce the following results:
```
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
```
This shows the requests that were made, when they were completed based on the current time, and the output should have no errors. If the output has an error then the test has failed.

## Diagrams:

The UML diagram can be found for this project can be found using this [link](Interation1ClassDiagram.png):

The Sequence diagram for all the classes and functions can be found using this [link](SequenceDiagrams):

The State Machine diagram for the ElevatorSubsytem and Scheduler can be found using this [link](StateMachineDiagrams):







