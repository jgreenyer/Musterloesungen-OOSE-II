package sheet3.task3_diningphilosophers_d_tryacquire;

import sheet3.task1_semaphore.BinarySemaphore;

public class DiningPhilosophersDemo {

    public static final int NUMBER_OF_PHILOSOPHERS = 3;
    public static void main(String[] args) {


        Philosopher[] philosophers = new Philosopher[NUMBER_OF_PHILOSOPHERS];
        BinarySemaphore[] forks = new BinarySemaphore[NUMBER_OF_PHILOSOPHERS];

        for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++) {
            forks[i] = new BinarySemaphore();
        }

        for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++) {
            philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % NUMBER_OF_PHILOSOPHERS]);
            philosophers[i].start();
        }

    }
}
