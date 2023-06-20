import java.util.concurrent.Semaphore;

public class SleepingTA {

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
        
        //create n students
        Student[] students = new Student[numberStudents];
        for (int i = 0; i < numberStudents; i++){
            students[i] = new Student(i);
            students[i].run();
        }

        //create TA
        TA assistant = new TA();
        assistant.run();

        //create semaphores
        // with number of permits 1 means only 1 student can access TA at a time
        Semaphore sem = new Semaphore(1);

        //put a specific time to end the session? maybe let it run forever
        
    }
}
