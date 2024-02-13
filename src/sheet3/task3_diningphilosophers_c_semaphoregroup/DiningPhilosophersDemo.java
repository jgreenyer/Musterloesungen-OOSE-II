package sheet3.task3_diningphilosophers_c_semaphoregroup;

import sheet3.task1_semaphore.BinarySemaphore;
import sheet3.task2_semaphoregroup.SemaphoreGroup;

import java.util.Arrays;

public class DiningPhilosophersDemo {

    public static final int NUMBER_OF_PHILOSOPHERS = 3;
    public static void main(String[] args) {


        Philosopher[] philosophers = new Philosopher[NUMBER_OF_PHILOSOPHERS];
        SemaphoreGroup forksSemaphoreGroup = new SemaphoreGroup(NUMBER_OF_PHILOSOPHERS);
        int[] forksSemaphoreGroupValue = new int[NUMBER_OF_PHILOSOPHERS];
        Arrays.fill(forksSemaphoreGroupValue, 1);

        try {
            forksSemaphoreGroup.changeValues(forksSemaphoreGroupValue);
        } catch (InterruptedException e) {}

        for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++) {
            philosophers[i] = new Philosopher(i, forksSemaphoreGroup);
            philosophers[i].start();
        }

    }
}
