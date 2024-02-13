package sheet3.task1_semaphore;

public class CountingSemaphore {

    protected int count;

    public CountingSemaphore(int size){
        count = size;
    }

    public synchronized void acquire() throws InterruptedException{
        while(count == 0){
            wait();
        }
        count--;
    }

    public synchronized void release(){
        count++;
        notify();
    }

    public synchronized boolean tryAcquire() {
        if (count > 0) {
            count--;
            return true;
        }
        return false;
    }
}
