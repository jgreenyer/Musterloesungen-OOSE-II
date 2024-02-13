package sheet4.task2_00_simple_lock;

public class Lock {

    private boolean isLocked = false;
    private Thread owner;

    public synchronized void lock(){
        while(isLocked) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        isLocked = true;
        owner = Thread.currentThread();
    }

    public synchronized void unlock(){
        if(Thread.currentThread() != owner){
            throw new IllegalMonitorStateException("Current thread not owner");
        }
        isLocked = false;
        notify();
    }

}
