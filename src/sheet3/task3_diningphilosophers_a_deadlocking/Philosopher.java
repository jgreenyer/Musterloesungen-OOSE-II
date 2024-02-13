package sheet3.task3_diningphilosophers_a_deadlocking;

import sheet3.task1_semaphore.BinarySemaphore;

import java.util.Random;

class Philosopher extends Thread {
    private int id;
    private BinarySemaphore leftFork;
    private BinarySemaphore rightFork;
    private Random random = new Random();

    // Counter, um zu zählen, wie oft ein Philosoph nachgedacht hat,
    // bevor ein Deadlock eintritt
    private int thinkCounter = 0;

    public Philosopher(int id, BinarySemaphore leftFork, BinarySemaphore rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking, thinkCounter=" + ++thinkCounter);
    }

    private void eat() throws InterruptedException {
        System.out.println("Philosopher " + id + " is eating.");
    }

    public void run() {
        try {
            while (true) {
                think();
                if (random.nextInt(2) == 0){ // first left fork, then right fork
                    leftFork.acquire(); // Acquire the left fork
                    System.out.println("Philosopher " + id + " picked up the left fork.");
                    rightFork.acquire(); // Acquire the right fork
                    System.out.println("Philosopher " + id + " picked up the right fork.");
                    eat();
                    leftFork.release(); // Release the left fork
                    System.out.println("Philosopher " + id + " put down the left fork.");
                    rightFork.release(); // Release the right fork
                    System.out.println("Philosopher " + id + " put down the right fork.");
                } else { // first right fork, then left fork
                    rightFork.acquire(); // Acquire the right fork
                    System.out.println("Philosopher " + id + " picked up the right fork.");
                    leftFork.acquire(); // Acquire the left fork
                    System.out.println("Philosopher " + id + " picked up the left fork.");
                    eat();
                    rightFork.release(); // Release the right fork
                    System.out.println("Philosopher " + id + " put down the right fork.");
                    leftFork.release(); // Release the left fork
                    System.out.println("Philosopher " + id + " put down the left fork.");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}