package sheet4.task2_b_reentrant_lock_optionally_fair;

public class ReentrantLockDemo {


    private static Thread createLockerDemoThread(String name, ReentrantLockOptionallyFair lock, int startDelayMillis) {
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
        ReentrantLockOptionallyFair lock = new ReentrantLockOptionallyFair(true);

        for (int i = 0; i < 10; i++){
            createLockerDemoThread("T"+i, lock, i*100).start();
        }

    }

}
