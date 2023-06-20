import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class TA extends Thread{
    
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
