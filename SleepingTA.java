/**
 * CSI 3531 - Devoir 3
 * The Sleeping Teaching Assistant
 * @author Kanjanokphat Kitisuwanakul - 300170040
 * @author Céline Wan Min Kee - 300193369
 */
import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

// main thread 
public class SleepingTA {

    public static Semaphore waitlist;
    public static Semaphore TAReady;
    public static Semaphore studentReady;
    public static int numberOfFreeSeats = 3;

    public static Student[] students;       // array of all the students in the session
    public static TA assistant;             // TA helping the student

    // the ta will end the session after helping students 10 times
    public static int maximumAmountOfHelpToProvide = 10;  // this number can be changed to fit required situation
    public static int count = 10;                        // count should be the same value as maximumAmountOfHelpToProvide as it keeps track of the number left

    public static int numberOfStudents; // the number of students in the session that TA will work with

    public static Queue<Student> studentQueue; // queue to keep track of which student is waiting

    public static void main(String[] args) {

        //initialize semaphores:

        //to count number of waiting seats
        waitlist = new Semaphore(1); //waitlist starts with being available

        //to check if TA is sleeping or not
        TAReady = new Semaphore(0);

        //number of students waiting ready to be assisted
        studentReady = new Semaphore(0);

        // queue to keep track of which student is in the queue
        studentQueue = new LinkedList<Student>();

        //get arguments
        if (args.length > 0) {
            try {
                numberOfStudents = Integer.parseInt(args[0]);
            } catch (Exception e) {
                System.out.println("Please enter an integer for the number of students!");
                return;
            }

            if (numberOfStudents <= 0) {
                System.out.println("Please enter an integer greater than 0 for the number of students!");
                return;
            }
        } else {
            numberOfStudents = 3; //default value
        }
        System.out.println("--------------------------------------------- Description ---------------------------------------------------------");
        System.out.println("\nThe session contains " + numberOfStudents + " students.");
        System.out.println("During this session, the TA will provide help on " + maximumAmountOfHelpToProvide
                + " different occasions before ending the session.");
        System.out.println(
                "A student can ask for help more than 1 time if the TA hasn't reached the maximum amount of times they can help.");
        System.out.println("A student can choose to not ask for help too.\n");
        System.out.println("----------------------------------------- Starting the session ----------------------------------------------------");

        //create n students
        Student[] students = new Student[numberOfStudents];
        for (int i = 0; i < numberOfStudents; i++) {
            //start student threads
            students[i] = new Student(i);
            students[i].start();
        }

        //create TA
        TA assistant = new TA();

        //start TA thread
        assistant.start();

        //let all threads finish execution before finishing main thread
        try {
            assistant.join();
            for (int i = 0; i < numberOfStudents; i++) {
                students[i].join();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Session over!");
    }
}

// Student thread
class Student extends Thread {

    public int studentId;     // id to identify the student

    /**
     * Constructor for Student class
     * @param studentId integer representing the id of the student
     */
    public Student(int studentId) {
        this.studentId = studentId;
        System.out.println("Student " + studentId + " is programming");
    }

    /**
     * toString method that displays properly Student class
     */
    @Override
    public String toString() {
        return "Student " + studentId;
    }

    /**
     * run method that runs when the Student thread starts
     */
    @Override
    public void run(){

        // repeat until n students have been helped

        while (SleepingTA.count > 0) {
            try {
                //introduce a delay between student arrivals
                Random random = new Random();
                int delay = random.nextInt(3000) + 1000;
                Thread.sleep(delay);

                //Wait for seats to be accessible (so that TA is not modifying it at the same time)
                SleepingTA.waitlist.acquire();
                
                // if the TA still is helping the students
                if (!TA.sessionOver) {
                    //now we are able to modify free seats
                    System.out.println("Student " + studentId + " arrives");

                    //if there are any free seats
                    if (SleepingTA.numberOfFreeSeats > 0) {

                        SleepingTA.studentQueue.add(this); // add student to the queue

                        //check if it is an immediate student
                        if (SleepingTA.studentQueue.size() == 1 && !TA.isCurrentlyHelping) {
                            System.out.println(
                                    "Student " + studentId
                                            + " wakes up the TA since there's no one. Student " + studentId
                                    + " sits down until the TA ask them to enter the office to get help");
                        } else {
                            //otherwise get a spot in the waitlist
                            System.out.println("There are seats left. So student " + studentId + " sits down");
                        }

                        // sits down
                        SleepingTA.numberOfFreeSeats--;

                        //make student available for the TA
                        SleepingTA.studentReady.release();

                        //unlock seats
                        SleepingTA.waitlist.release();

                        //wake up TA
                        SleepingTA.TAReady.acquire();
                    } else {
                        System.out.println("No seats left. So student " + studentId + " leaves TA's office and goes back to programming...");
                        //go back
                        SleepingTA.waitlist.release(); //release lock on seats to allow modifying
                    }
                } else {
                    SleepingTA.waitlist.release(); // session is over so the student leaves
                }
            } catch (InterruptedException e) {
                System.out.println("Error occured: " + e.getMessage());
            }
        }
        System.out.println("Student "+ studentId + " leaves");
    }   
}

// TA thread
class TA extends Thread {

    public static boolean isCurrentlyHelping = false;
    public static boolean sessionOver = false;

    /**
     * Constructor for TA class
     */
    public TA() {
        System.out.println("TA is in their office");
    }

    /**
     * run method that runs when the TA thread starts
     */
    @Override
    public void run(){
        
        //repeat until TA has helped students 10 times

        while (SleepingTA.count > 0) {
            try {

                if (SleepingTA.studentQueue.size() == 0) {
                    System.out.println("The TA is sleeping");
                    //is there a student ready? acquire a student
                    SleepingTA.studentReady.acquire();
                    //if we were able to acquire, then student wakes up!
                    System.out.println("The TA wakes up");
                } else {
                    // if waiting list is not empty, just take a student
                    SleepingTA.studentReady.acquire();
                }

                //Can I access the waitlist? wait until no student is modifying it
                SleepingTA.waitlist.acquire();

                //get the student to be helped
                Student currentlyHelped = SleepingTA.studentQueue.poll();
                System.out.println("The TA asks student " + currentlyHelped.studentId
                        + " to come in their office, and starts helping them");
                isCurrentlyHelping = true;
                
                SleepingTA.numberOfFreeSeats++;

                // signal TA is ready
                SleepingTA.TAReady.release();

                // introduce a delay when the TA is helping the student
                Random random = new Random();
                int timeHelping = random.nextInt(6000) + 1000;
                Thread.sleep(timeHelping);

                System.out.println(
                        "TA has finished helping student " + currentlyHelped.studentId + ", they are now free :)");
                isCurrentlyHelping = false;

                SleepingTA.count--; //decrement students helped so that TA is able to finish the session

                if (SleepingTA.count == 0) { // TA has reached the maximum number of times they can help, so they will end the session
                    System.out.println("TA has helped " + (SleepingTA.maximumAmountOfHelpToProvide - SleepingTA.count)
                                    + " times which is the maximum number of times they can help");
                    sessionOver = true;
                }

                //can allow waitlist to freely modify within student class
                SleepingTA.waitlist.release();

            } catch (InterruptedException e) {
                System.out.println("Error occured: " + e.getMessage());
            }
        }
        SleepingTA.TAReady.release(SleepingTA.numberOfStudents); //release all permits so students can end
        System.out.println("The TA informs the students that they will stop helping them since we've reached the end of the session and it's time to go home");
    }
}