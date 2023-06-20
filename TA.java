public class TA extends Thread{
    
    private boolean asleep;
    private String[] waitingList; //contains a list of students waiting

    public TA() {
        asleep = false; //starts awake
        waitingList = new String[] {}; // empty initially
    }

    @Override
    public void run(){

        //repeat forever

        //if there is student in waiting list
        //help student

        //if waiting list empty
        //sleep

        //student informs that TA is sleeping with semaphore
        //TA wakes up
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
