package sheet3.task2_linkedblockingqueue;


import sheet3.task1_semaphore.BinarySemaphore;
import sheet3.task1_semaphore.CountingSemaphore;

import java.util.LinkedList;

public class LinkedBlockingQueue<T> {

    private LinkedList<T> data = new LinkedList();

    // wird im Konstruktor entsprechend der Kapazitöt der Warteschlange initialisiert.
    // Idee: Ist der Wert 0, also ist der Semaphore belegt, kann nichts mehr
    // in die Warteschlange gelegt werden.
    private CountingSemaphore fullSemaphore;

    // Idee: Ist der Wert des emptySemaphore 0, also ist der Semaphore belegt,
    // ist die Warteschlange leer und es kann nichts mehr gelesen werden.
    private CountingSemaphore emptySemaphore = new CountingSemaphore(0);

    private BinarySemaphore mutex = new BinarySemaphore();

    LinkedBlockingQueue(int capacity){
        // Der fullSemaphore muss mit der spezifizierten Kapazität der Warteschlange
        // initialisiert werden. Ist die Kapazität 1, muss z.B. auch der fullSemaphore
        // mit 1 initialisiert werden, damit ein Schreibvorgang ermöglicht wird.
        fullSemaphore = new CountingSemaphore(capacity);
    }

    public void put(T value) throws InterruptedException{
        // erst den fullSemaphore belegen, dies muss vor dem Setzen des Mutex geschehen.
        // Wird der Mutex zuerst gesetzt, kommt es zum Deadlock -- kannst Du erklären warum?
        fullSemaphore.acquire();
        mutex.acquire();
        // Das Freigeben des emptySemaphore kann erst geschehen, wenn der Schreiber
        // den Mutex hat. Kannst Du Dir vorstellen, was passiert, wenn das vorher passiert?
        // Dann könnte der Leser bereits Wert lesen bevor der Schreiber ihn geschrieben hat!
        emptySemaphore.release();
        // Das Schreiben der Werte kann vor oder nach emptySemaphore.release() passieren
        // Die Reihenfolge ist egal, da der Schreiber hier noch den Mutex belegt und
        // kein Leser lesen kann, bevor weiter unten der Mutext wieder frei wird.
        data.add(value);
        System.out.println("Value added:  " + value + " (Current queue size: " + data.size() + " )");
        mutex.release();
    }

    public T get() throws InterruptedException{
        emptySemaphore.acquire();
        mutex.acquire();
        fullSemaphore.release();
        T value = data.poll();
        System.out.println("Value polled: " + value + " (Current queue size: " + data.size() + " )");
        mutex.release();
        return value;
    }

}
