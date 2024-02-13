package sheet4.task2_00_simple_lock;

public class LockDemo {

    private static Thread createLockerDemoThread(String name, Lock lock, int startDelayMillis) {
        return new Thread(
                () -> {
                    try {
                        Thread.sleep(startDelayMillis);
                        System.out.println(name + " trying to lock....");
                        lock.lock();
                        System.out.println(name + " locked.");
                        Thread.sleep(500);
                        System.out.println(name + " unlocking....");
                        lock.unlock();
                        System.out.println(name + " unlocked.");
                    } catch (InterruptedException e) {}
                }
        );
    }
    public static void main(String[] args) {
        Lock lock = new Lock();

        Thread lockerA = createLockerDemoThread("A" , lock, 0);
        Thread lockerB = createLockerDemoThread("B" , lock, 100);

        Thread unlocker = new Thread(()-> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            try{
                System.out.println("Other thread trying to unlock...");
                lock.unlock();
            } catch (IllegalMonitorStateException e){
                System.out.println("Caught exception: " + e);
            }

        });

        lockerA.start();
        lockerB.start();
        unlocker.start();

    }

}
