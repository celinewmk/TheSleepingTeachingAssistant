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
            students[i] = new Student();
        }

        //create TA
        TA assistant = new TA();

        //create semaphores
        
    }
}
