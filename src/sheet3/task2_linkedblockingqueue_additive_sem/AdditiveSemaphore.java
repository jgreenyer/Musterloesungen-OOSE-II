package sheet3.task2_linkedblockingqueue_additive_sem;

import sheet3.task1_semaphore.CountingSemaphore;

public class AdditiveSemaphore extends CountingSemaphore {


    public AdditiveSemaphore(int size) {
        super(size);
    }

    public synchronized boolean canAcquire(int x){
        return count < x;
    }

    public synchronized void acquire(int x) throws InterruptedException{
        while(count < x){
            wait();
        }
        count-=x;
    }

    public synchronized void release(int x){
        count+=x;
        notify();
    }
}
