package sheet4.task1_a_rwaccesscontrol_semaphore;

public class Main {

    public static void main(String[] args) {
        Integer[] integers = new Integer[]{1,2,3};
        RWAccessControl<Integer> ac = new RWAccessControl<>(integers);
        new Writer(ac).start();
        new Writer(ac).start();
        new Reader(ac).start();
        new Reader(ac).start();
        new Reader(ac).start();
        new Reader(ac).start();
        new Reader(ac).start();
    }
}
