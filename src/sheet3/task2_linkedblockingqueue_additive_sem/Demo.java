package sheet3.task2_linkedblockingqueue_additive_sem;


public class Demo {

    public static void main(String[] args) {

        LinkedBlockingQueueWAdditiveSem<Integer> q = new LinkedBlockingQueueWAdditiveSem<>(5);

        Producer p1 = new Producer("p1", q);
        Producer p2 = new Producer("p2", q);
        Consumer c1 = new Consumer("c1", q);
        Consumer c2 = new Consumer("c2", q);

        c1.start();
        c2.start();
        p1.start();
        p2.start();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        p1.interrupt();
        p2.interrupt();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        c1.interrupt();
        c2.interrupt();

        System.out.println("Done");
    }
}
