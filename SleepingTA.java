import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class SleepingTA {

    private Semaphore mutex;
    private Semaphore waitlist;
    private Semaphore sleepingSemaphore;
    private Student[] students;
    private TA assistant;

    private static final int lengthOfWaitingLine = 3;

    public SleepingTA(int numberStudents) {
        students = new Student[numberStudents];
        for (int i = 0; i < numberStudents; i++) {
            students[i] = new Student(i);
        }
        
        //for mutually exclusive access to TA
        mutex = new Semaphore(1);

        //to synchronise waiting list
        waitlist = new Semaphore(lengthOfWaitingLine);

        //to synchronise sleeping TA
        sleepingSemaphore = new Semaphore(1);

        assistant = new TA(sleepingSemaphore, waitlist);
    }

    public static void main(String[] args) {

        int numberStudents = 0;

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
        
        SleepingTA sleepingTA = new SleepingTA(numberStudents);
        //create semaphores
        // with number of permits 1 means only 1 student can access TA at a time
       
        // //for mutually exclusive access to TA
        // mutex = new Semaphore(1);

        // //to synchronise waiting list
        // Semaphore waitlist = new Semaphore(3);

        // //to sunchronise sleeping TA
        // Semaphore sleepingSemaphore = new Semaphore(1);


        //create n students
        // Student[] students = new Student[numberStudents];
        // for (int i = 0; i < numberStudents; i++){
        //     students[i] = new Student(i);
        //     students[i].run();
        // }

        //create TA
        // TA assistant = new TA(sleepingSemaphore, waitlist);
        // assistant.run();



    }
}

class Student extends Thread {

    private int id;

    public Student(int i) {
        i = id;
    }

    @Override
    public void run(){

        //repeat forever
        //student randomly asks for help to TA
        //if full return

        //if TA asleep, wake TA and get help

        //if TA awake and free, get help

        //if TA awake and busy, go to queue

        return;
    }   
}


class TA extends Thread{
    
    private boolean asleep;
    private ArrayList<Student> studentList; //contains a list of students waiting
    Semaphore sleepingSemaphore;
    Semaphore waitlist;

    public TA(Semaphore s, Semaphore e) {
        asleep = false; //starts awake
        studentList = new ArrayList<>(); // empty initially
        sleepingSemaphore = s;
        waitlist = e;
    }

    @Override
    public void run(){

        //repeat forever
        while (true){

            //if there is student in waiting list
            //help student
            if (studentList.size() != 0){
                
            }else{

            //if waiting list empty
            //sleep
                

            //student informs that TA is sleeping with semaphore
            //TA wakes up

            }


        }

    }

    public void addStudentWaiting(Student student){
        studentList.add(student);
    }

    public void beginNap() {
        System.out.println("TA is now asleep...");
        asleep = true;
    }

    public void wakeUp() {
        System.out.println("TA is now awake!");
        asleep = false;
    }
}