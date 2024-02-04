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
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * The type Floor subsystem.
 */
// create class FloorSubsystem that implements Runnable
public class FloorSubsystem implements Runnable {
    private final Scheduler scheduler;
    LinkedList<Event> data = new LinkedList<>();

    /**
     * Instantiates a new Floor subsystem.
     *
     * @param scheduler the scheduler
     */
    // create constructor that takes in a scheduler and reads data from a file
    public FloorSubsystem(Scheduler scheduler) {

        this.scheduler = scheduler;
        readDataFromFile();
    }

    // create method readDataFromFile that reads data from a file and adds it to the data list
    private void readDataFromFile() {
        File inputFile = new File("inputFile.txt");
        try {
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] splitLine = line.split(" ");
                Direction move = Direction.DOWN;
                if (splitLine[2].equals("Up")) {
                    move = Direction.UP;
                }
                Date inputTime = new SimpleDateFormat("HH:mm:ss.SSS").parse(splitLine[0]);
                Date date = new Date();
                date.setHours(inputTime.getHours());
                date.setMinutes(inputTime.getMinutes());
                date.setSeconds(inputTime.getSeconds());
                int currentFloor = Integer.parseInt(splitLine[1]);
                int destinationFloor = Integer.parseInt(splitLine[3]);
                data.add(new Event(date, currentFloor, move, destinationFloor));
            }
            reader.close();
        } catch (FileNotFoundException | NumberFormatException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    // create method run that runs the floorSubsystem
    public synchronized void run() {
        while (true) {
            while (!data.isEmpty()) {

                scheduler.addEvent(data.pop());

            }
            while (scheduler.isCompletedEmpty()) {
                try {
                    wait();
                } catch (InterruptedException ignored) {

                }
            }
            System.out.println(scheduler.getCompleted() + " has been completed!");
        }
    }
}