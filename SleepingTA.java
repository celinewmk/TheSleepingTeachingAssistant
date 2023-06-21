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
