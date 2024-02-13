package sheet4.task2_c_reentrant_lock_trylock_lockinterruptibly;

public class ReentrantLockDemo {


    private static Thread createLockerDemoThread(String name, ReentrantLockFair lock, int startDelayMillis) {
        return new Thread(
                () -> {
                    try {
                        Thread.sleep(startDelayMillis);
                        System.out.println(name + " trying to lock; holdcount=" + lock.getHoldCount());
                        lock.lockInterruptibly();
                        System.out.println(name + " locked; holdcount=" + lock.getHoldCount());
                        System.out.println(name + " trying to lock again; holdcount=" + lock.getHoldCount());
                        lock.lockInterruptibly();
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
        ReentrantLockFair lock = new ReentrantLockFair();

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++){
            Thread thread = createLockerDemoThread("T"+i, lock, i*100);
            thread.start();
            threads[i] = thread;
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {}
        for (int i = 0; i < 10; i++){
            threads[i].interrupt();
        }




    }

}
