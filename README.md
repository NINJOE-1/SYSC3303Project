# SYSC3303Project
Joseph Vretenar 101234613
                          
Samuel Mauricla 101233500

Bhavaan Balasubramaniam 101233825 

## Project Iteration 1: Establishing Connections between the three subsystems
The purpose of Iteration 1 is to create 3 threads which are the Elevator, Floor and Scheduler. The Elevator and Floor subsystems are the clients, while the Scheduler is the server. The Floor will read in the following format: Time, Floor, Button. Each line of input is sent back to the scheduler and then the Elevators will make calls to the Scheduler replying back when work is needed.The Elevator will then send the data back to the Scheduler who will then send it back to the Floor. In this Iteration, the scheduler is only being used as a communication channel from the Floor thread to the Elevator thread and back again.

## Files:
### [FloorSubsystem.java] (src/FloorSubsystem.java)
This is the Floor class, it will initialize a new Floor subsystem and will send the data to the Scheduler which includes the Hour, Minutes and Seconds. 

### [Scheduler.java] (src/Scheduler.java)
This is the Scheduler class, this is reponsible for managing elevator requests. 
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


