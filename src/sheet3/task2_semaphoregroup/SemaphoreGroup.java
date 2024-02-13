package sheet3.task2_semaphoregroup;

import java.util.Arrays;

public class SemaphoreGroup {

    private int[] values;

    public SemaphoreGroup(int numberOfMembers){
        if (numberOfMembers < 1)
            throw new IllegalArgumentException(
                    "Value must be >grater than 1");
        values = new int[numberOfMembers];
    }

    public synchronized void changeValues(int[] x)
            throws InterruptedException{
        if (x.length != values.length){
            throw new IllegalArgumentException(
                    "Array must be of length" + values.length);
        } else {
            while (!canChange(x)){ wait(); }
            doChange(x);
            notifyAll();
        }
    }

    private boolean canChange(int[] x){
        for (int i = 0; i < values.length; i++) {
            if (values[i] + x[i] < 0) {
                System.out.println("Want to acquire " + Arrays.toString(x)+ ", but current semaphore group state: " + Arrays.toString(values));
                return false;
            }
        }
        return true;
    }
    private void doChange(int[] x){
        for (int i = 0; i < values.length; i++) {
            values[i]+=x[i];
        }
        System.out.println("current semaphore group state: " + Arrays.toString(values));
    }
}
