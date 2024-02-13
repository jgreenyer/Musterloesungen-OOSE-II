package sheet3.task2_linkedblockingqueue;

import java.util.Random;

public class Producer extends Thread{

    private String name;
    private LinkedBlockingQueue q;

    public Producer(String name, LinkedBlockingQueue q){
        this.name = name;
        this.q = q;
    }

    @Override
    public void run() {
        Random random = new Random();
        while(!isInterrupted()){
            try {
                int value = random.nextInt(10);
                q.put(value);
            } catch (InterruptedException e) {
                break; // terminate thread.
            }
        }
        System.out.println("Producer " + name + " done!");
    }


    @Override
    public String toString() {
        return name;
    }
}
