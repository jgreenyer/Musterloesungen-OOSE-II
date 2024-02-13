package sheet3.task2_semaphoregroup;

import java.util.Arrays;
import java.util.Random;

public class SemaphoreGroupDemo {
    public static void main(String[] args) {
        SemaphoreGroup group = new SemaphoreGroup(3); // Create a semaphore group with 3 semaphores
        Random random = new Random();

        // Thread 1: Releases one of the semaphores randomly
        Thread releaser = new Thread(() -> {
            try {
                while (true) {
                    int index = random.nextInt(3); // Choose a random index (0, 1, or 2)
                    int[] newValues = new int[3];
                    newValues[index] = 1;
                    System.out.println("Releasing " + Arrays.toString(newValues));
                    group.changeValues(newValues);
                    Thread.sleep(200); // Sleep for 1 second
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Thread 2: Acquires semaphores based on random numbers
        Thread acquierer = new Thread(() -> {
            try {
                while (true) {
                    int x = random.nextInt(-2,1); // Random number -2, -1, or 0 for x
                    int y = random.nextInt(-2,1); // Random number -2, -1, or 0 for y
                    int z = random.nextInt(-2,1); // Random number -2, -1, or 0 for z
                    int[] newValues = {x, y, z};
                    group.changeValues(newValues);
                    System.out.println("Acquired " + Arrays.toString(newValues));
                    Thread.sleep(500); // Sleep for 1 second
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        releaser.start();
        acquierer.start();

        // Let the threads run for a while
        try {
            Thread.sleep(5000); // Run for 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Interrupt the threads and stop the program
        releaser.interrupt();
        acquierer.interrupt();
    }
}