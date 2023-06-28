import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

//TODO randomize thread.sleep times

public class SleepingTA {

    public static  Semaphore waitlist;
    public static  Semaphore TAReady; 
    public static  Semaphore studentReady;
    public static int numberOfFreeSeats = 3;

    public static Student[] students;
    public static TA assistant;

    public static int maximumStudentsHelped = 5; // the ta will go home after he helped 5 students

    public static int currentlyHelpingID;

    public static Queue<Student> studentQueue;

    public static void main(String[] args) {

        //initialize semaphores:

        //to count number of waiting seats
        waitlist = new Semaphore(1); //waitlist starts with being available

        //to check if TA is sleeping or not
        TAReady = new Semaphore(0);

        //number of students waiting ready to be assisted
        studentReady = new Semaphore(0);

        //number of students that TA will work with
        int numberStudents;

        studentQueue = new LinkedList<Student>();

        //get arguments
        if (args.length > 0 ) {
            try {
                numberStudents = Integer.parseInt(args[0]);
            } catch (Exception e) {
                System.out.println("Please enter an integer for the number of students!");
                return;
            }

            if (numberStudents <= 0){
                System.out.println("Please enter an integer greater than 0 for the number of students!");
                return;
            }
        }
        else {
            numberStudents = 3; //default value
        }

        System.out.println("Starting a session with " + numberStudents + " students.");


        //create n students
        Student[] students = new Student[numberStudents];
        for (int i = 0; i < numberStudents; i++) {
            //start student threads
            students[i] = new Student(i);
            students[i].start();
        }
        
        //create TA
        TA assistant = new TA();

        //start TA thread
        assistant.start();

        //let all threads finish execution before finishing main thread
        //TODO thread isnt terminating help, either my stop condition isnt working or the JOIN isnt working
        try {
           assistant.join();
           students[0].join();
           students[1].join();
           students[2].join();
          
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Session over!");
    }
}

class Student extends Thread {

    public int studentId;

    public Student(int studentId) {
        this.studentId = studentId;
        System.out.println("Creating student " + studentId);
    }

    @Override
    public String toString() {
        return "Student " + studentId;
    }

    @Override
    public void run(){

        //repeat until n students have been helped

        while (SleepingTA.maximumStudentsHelped > 0){  
            try {
                Random random = new Random();
                int delay = random.nextInt(3000) + 1000; 
                //introduce a delay of 3 seconds between student arrivals
                Thread.sleep(delay); 

                //Wait for seats to be accessible (so that TA is not modifying it at the same time)
                SleepingTA.waitlist.acquire();

                //now we are able to modify free seats
                System.out.println("Student " + studentId + " arrives");
                

                
                //if there are any free seats
                if (SleepingTA.numberOfFreeSeats > 0){

                    SleepingTA.studentQueue.add(this);
                    
                    System.out.println("Student " + studentId + " is modifying seats!");

                    //get a spot in the waitlist
                    System.out.println("There are seats left. So student " + studentId + " sits down");
                    // sits down
                    SleepingTA.numberOfFreeSeats--;
                    
                    System.out.println("NUMBER OF FREE SEATS : " + SleepingTA.numberOfFreeSeats);
                    System.out.println("Queue now is: "  + SleepingTA.studentQueue.toString());

                    //tell TA theres a student waiting
                    System.out.println("Student " + studentId + " is waiting for the TA");
                    
                    SleepingTA.studentReady.release(); //make student available for the TA

                    //unlock seats
                    SleepingTA.waitlist.release();

                    //wake up TA
                    SleepingTA.TAReady.acquire();

                    //SleepingTA.currentlyHelpingID = studentId; //for debugging

                } else {
                    System.out.println("No seats left. So student " + studentId + " leaves");
                    //go back
                    SleepingTA.waitlist.release(); //release lock on seats to allow modifying

                }

            } catch (InterruptedException e) {
                System.out.println("Error occured: " + e.getMessage());
            }
        }

        System.out.println("Student " + studentId + " is DONE");

    }   
}


class TA extends Thread{

    public TA() {
        System.out.println("TA created!");
    }

    @Override
    public void run(){
        
        //repeat until 20 students have been helped

        while (SleepingTA.maximumStudentsHelped > 0){    
            try {

                if (SleepingTA.studentQueue.size() == 0){
                    System.out.println("The TA is sleeping");
                    //is there a student ready? acquire a student
                    SleepingTA.studentReady.acquire();
                    //if we were able to acquire, then student wakes up!
                    System.out.println("The TA wakes up");
                }else{
                    // if waiting list is not empty, just take a student
                    SleepingTA.studentReady.acquire();
                }
                
                //Can I access the waitlist? wait until no student is modifying it
                SleepingTA.waitlist.acquire();

                
                //get the student to be helped
                Student currentlyHelped = SleepingTA.studentQueue.poll();
                System.out.println("The TA chooses the student " + currentlyHelped.studentId);

                System.out.println("TA is now modifying waitlist");

                
                SleepingTA.numberOfFreeSeats++;
                
                
                System.out.println("NUMBER OF FREE SEATS : " + SleepingTA.numberOfFreeSeats);

                
                SleepingTA.TAReady.release();

                //introduce a delay, the TA is helping the student for 5 seconds
                Thread.sleep(5000);
                
                System.out.println("TA has finished helping student " + currentlyHelped.studentId + " , he is now free :)");
                System.out.println("Queue now is: "  + SleepingTA.studentQueue.toString());
                
                SleepingTA.maximumStudentsHelped--; //decrement students helped so that TA is able to finish the session
                System.out.println("Students left to help before terminating : " + SleepingTA.maximumStudentsHelped);

                //can allow waitlist to freely modify within student class
                SleepingTA.waitlist.release();

                System.out.println("TA releases waitlist lock");

            } catch (InterruptedException e) {
                System.out.println("Error occured: " + e.getMessage());
            }

        }

        System.out.println("TA thread is DONE");
        SleepingTA.TAReady.release(3); //release all permits so students can end
    }
}