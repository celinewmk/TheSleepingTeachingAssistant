import java.util.concurrent.Semaphore;

//TODO print messages when TA is working and when new student arrives etc
// im not sure about line 23 and the wait and release things since its diff from wait() and notify()

public class SleepingTA {

    public static  Semaphore waitlist;
    public static  Semaphore TAReady; 
    public static  Semaphore studentReady;
    public static int numberOfFreeSeats = 3;

    public static Student[] students;

    public static TA assistant;

    public static void main(String[] args) {

        //initialize semaphores:

        //to count number of waiting seats
        waitlist = new Semaphore(1);
        waitlist.release(); //waitlist starts with being available

        //to check if TA is sleeping or not
        TAReady = new Semaphore(1);

        //number of students waiting ready to be assisted
        studentReady = new Semaphore(1);

        //number of students that TA will work with
        int numberStudents;

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
            numberStudents = 10; //default value
        }

        System.out.println("Starting a session with " + numberStudents + " students.");

        //create TA
        TA assistant = new TA();

        //start TA thread
        assistant.run();
        
        //create n students
        Student[] students = new Student[numberStudents];
        for (int i = 0; i < numberStudents; i++) {
            students[i] = new Student(i);
            //start student threads
            students[i].run();
        }
    }
}

class Student extends Thread {

    private int studentId;

    public Student(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public void run(){

        //run forever
        while (true){  
            try {
                //Wait for seats to be accessible (so that TA is not modifying it at the same time)
                SleepingTA.waitlist.acquire();

                //now we are able to modify free seats
                System.out.println("Student " + studentId + " arrives");
                
                //if there are any free seats
                if (SleepingTA.numberOfFreeSeats > 0){
                    //get a spot in the waitlist
                    System.out.println("There are seats left. So student" + studentId + " sits down");
                    // sits down
                    SleepingTA.numberOfFreeSeats--; 

                    //tell TA theres a student waiting
                    SleepingTA.studentReady.release();

                    //unlock seats
                    SleepingTA.waitlist.release();

                    //wait until TA is ready, waiting for TAReady.release()
                    SleepingTA.TAReady.acquire();

                } else {
                    System.out.println("No seats left. So student" + studentId + " leaves");
                    //go back
                    SleepingTA.waitlist.release(); //release lock on seats to allow modifying
                }
            } catch (InterruptedException e) {
                System.out.println("Error occured: " + e.getMessage());
            }
        }
    }   
}


class TA extends Thread{

    @Override
    public void run(){
        //repeat forever
        while (true){    
            try {
                System.out.println("The TA is sleeping");
                //is there a student ready? if none then just sleep
                SleepingTA.studentReady.acquire();
            
                //Can I increment free spots? if unsuccessful then sleep
                SleepingTA.waitlist.acquire();

                //acquiring seat successful, increment
                SleepingTA.numberOfFreeSeats++;
                System.out.println("The TA wakes up");
                System.out.println("The TA is helping the student");
                //TA assists students
                SleepingTA.TAReady.release();

                //can allow waitlist to freely modify within student class
                SleepingTA.waitlist.release();
                System.out.println("The TA finished helping the student");


            } catch (InterruptedException e) {
                System.out.println("Error occured: " + e.getMessage());
            }

        }
    }
}