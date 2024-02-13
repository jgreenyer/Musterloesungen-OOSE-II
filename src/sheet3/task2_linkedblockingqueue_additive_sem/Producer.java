package sheet3.task2_linkedblockingqueue_additive_sem;

import java.util.Random;

public class Producer extends Thread{

    private String name;
    private LinkedBlockingQueueWAdditiveSem q;

    public Producer(String name, LinkedBlockingQueueWAdditiveSem q){
        this.name = name;
        this.q = q;
    }

    @Override
    public void run() {
        Random random = new Random();
        while(!isInterrupted()){
            try {
                Integer[] randomArray = new Integer[random.nextInt(1,4)];
                for (int i=0; i<randomArray.length; i++){
                    randomArray[i] = random.nextInt(10);
                }
                q.put(randomArray);
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
