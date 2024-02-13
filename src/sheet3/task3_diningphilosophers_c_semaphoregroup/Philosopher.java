package sheet3.task3_diningphilosophers_c_semaphoregroup;

import sheet3.task1_semaphore.BinarySemaphore;
import sheet3.task2_semaphoregroup.SemaphoreGroup;

import java.util.Random;

class Philosopher extends Thread {
    private int id;
    private SemaphoreGroup forksSemaphoreGroup;
    private BinarySemaphore rightFork;
    private Random random = new Random();

    private int thinkCounter = 0;

    public Philosopher(int id, SemaphoreGroup forksSemaphoreGroup) {
        this.id = id;
        this.forksSemaphoreGroup = forksSemaphoreGroup;
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking, thinkCounter=" + ++thinkCounter);
    }

    private void eat() throws InterruptedException {
        System.out.println("Philosopher " + id + " is eating.");
    }

    public void run() {
        int[] acquireChange = createAcquireChange();
        int[] releaseChange = createReleaseChange();
        try {
            while (true) {
                think();
                forksSemaphoreGroup.changeValues(acquireChange);
                System.out.println("Philosopher " + id + " picked up both forks.");
                eat();
                forksSemaphoreGroup.changeValues(releaseChange);
                System.out.println("Philosopher " + id + " put down both forks.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Welche Gabeln möchte der Philosopn aufnehmen?
    // Dies muss auf ein entsprechendes Acquire auf der
    // Semaphorengruppe abgebildet werden.
    private int[] createAcquireChange(){
        int[] change = new int[DiningPhilosophersDemo.NUMBER_OF_PHILOSOPHERS];
        change[id] = -1;
        change[(id+1)%DiningPhilosophersDemo.NUMBER_OF_PHILOSOPHERS] = -1;
        return change;
    }
    // genauso das Release für das Niederlegen der Gabeln
    private int[] createReleaseChange(){
        int[] change = new int[DiningPhilosophersDemo.NUMBER_OF_PHILOSOPHERS];
        change[id] = 1;
        change[(id+1)%DiningPhilosophersDemo.NUMBER_OF_PHILOSOPHERS] = 1;
        return change;
    }

}