package sheet4.task2_a_reentrant_lock;

import sheet4.task2_00_simple_lock.Lock;

public class ReentrantLockDemo {


    private static Thread createLockerDemoThread(String name, ReentrantLock lock, int startDelayMillis) {
        return new Thread(
                () -> {
                    try {
                        Thread.sleep(startDelayMillis);
                        System.out.println(name + " trying to lock; holdcount=" + lock.getHoldCount());
                        lock.lock();
                        System.out.println(name + " locked; holdcount=" + lock.getHoldCount());
                        System.out.println(name + " trying to lock again; holdcount=" + lock.getHoldCount());
                        lock.lock();
                        System.out.println(name + " locked; holdcount=" + lock.getHoldCount());
                        Thread.sleep(500);
                        lock.unlock();
                        System.out.println(name + " unlocked; holdcount=" + lock.getHoldCount());
                        lock.unlock();
                        System.out.println(name + " unlocked; holdcount=" + lock.getHoldCount());
                    } catch (InterruptedException e) {}
                }
        );
    }

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        Thread lockerA = createLockerDemoThread("A", lock, 0);
        Thread lockerB = createLockerDemoThread("B", lock, 100);

        Thread unlocker = new Thread(()-> {
            for (int i=0; i<5; i++){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {}
                try{
                    System.out.println("Other thread trying to unlock...");
                    lock.unlock();
                } catch (IllegalMonitorStateException e){
                    System.out.println("Caught exception: " + e);
                }
            }
        });

        lockerA.start();
        lockerB.start();
        unlocker.start();

    }

}
