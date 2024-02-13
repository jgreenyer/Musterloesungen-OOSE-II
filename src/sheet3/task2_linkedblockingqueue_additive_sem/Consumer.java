package sheet3.task2_linkedblockingqueue_additive_sem;

public class Consumer extends Thread{

    private String name;
    private LinkedBlockingQueueWAdditiveSem<Integer> q;

    public Consumer(String name, LinkedBlockingQueueWAdditiveSem<Integer> q){
        this.name = name;
        this.q = q;
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            try {
                int value = q.get();
            } catch (InterruptedException e){
                break; // terminate Thread.
            }
        }
        System.out.println("Consumer terminated");
    }

    @Override
    public String toString() {
        return name;
    }
}
