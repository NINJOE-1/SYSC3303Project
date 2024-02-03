# SYSC3303 Project Group#9:

## Group Members:
Joseph Vretenar 101234613
                          
Samuel Mauricla 101233500

Bhavaan Balasubramaniam 101233825 

## Breakdown of the tasks:
Joseph Vretnar - FloorSubsystem, Scheduler, Event, ElevatorSubsystem, Direction and UML Diagrams

Samuel Mauricla- The FloorSubsystemTest

Bhavaan Balasubramaniam - README.md and Sequence Diagrams

## Project Iteration 1: Establishing Connections between the three subsystems
The purpose of Iteration 1 is to create 3 threads which are the Elevator, Floor and Scheduler. The Elevator and Floor subsystems are the clients, while the Scheduler is the server. The Floor will read in the following format: Time, Floor, Button. Each line of input is sent back to the scheduler and then the Elevators will make calls to the Scheduler replying back when work is needed.The Elevator will then send the data back to the Scheduler who will then send it back to the Floor. In this Iteration, the scheduler is only being used as a communication channel from the Floor thread to the Elevator thread and back again.

## Files:
### [FloorSubsystem.java] (src/FloorSubsystem.java)
The Floor class will extract data from an input file and parses each line to create an 'Event' object.
In addition, it will incorporate timestamps, the current floor, movement direction, and the destination floor. 

### [Scheduler.java] (src/Scheduler.java)
The Scheduler class is reponsible for managing elevator requests. 
The requests are pending and completed requests. 
It consists of methods such as adding, processing, and completing events, ensuring there's thread-safe interactions.

### [Event.java] (src/Event.java)
The Event class is responsible for maintaing elevator- related events which consists of timestamp (time), current floor (currentFloor), movement direction (move), and requested destination floor (destinationFloor).

### [ElevatorSubsystem.java] (src/ElevatorSubsystem.java)
The Elevator Subsystem class is repsonsible for the elevator's movement and responses to scheduler events. 
Some methods that are there include 'Runnable' which handles the Elevator's movements and tracks down the amount of passenger to drop and pick up. The 'move' method checks the floor movement and updates the current floor and progress regarding that. 
The 'run' method checks for accurate simulation of elevator movements and waiting periods.

### [Direction.java] (src/Direction.java)
The Direction class consists of three constants UP,DOWN and IDLE.
The constants represent the different possible number of states the Elevator is currently in.
'Enum' represents what direction the floor is in or if its in an IDLE state.

### [FloorSubsystemTest.java] (src/FloorSubsystemTest.java)
The Testing class is reponsible for testing the Scheduler and ElevatorSubsystem in an elevator simulation when running the program.
The test is executed by passing in default values and simulates the event.
Assertions are implemented to ensure the code is correct. 


## Setup Instructions
1. Ensure Java is installed on your system
2. Compile the Java files either using an IDE or through command lines
3. Ensure that the input file is in the same Project folder
4. Run the simulation using the Scheduler class. [src/Scheduler.java]

To edit the floors and requests, edit the inputFile.txt file

## Expected Output

The code below is a sample output:
Elevator has reached 3
Elevator picking up passengers 
Elevator moved to floor 2
Elevator has reached 1
Elevator dropping off passengers
Elevator completed request at Sat Feb 03 12:05:41 EST 2024

Request at SAT Feb 03 12:10:00 EST 2024 (2->8) is being processed
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

## Logging:
The program uses the `java.util.logging.Logger` for logging any sort of unexpected errors or events. 
Log messages are displayed in the console in the IDE.

## Diagrams:

The UML diagram can be found for this project can be found using the link: 








