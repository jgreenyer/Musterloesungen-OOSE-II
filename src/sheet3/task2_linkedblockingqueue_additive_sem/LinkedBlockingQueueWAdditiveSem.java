package sheet3.task2_linkedblockingqueue_additive_sem;


import sheet3.task1_semaphore.BinarySemaphore;

import java.util.Arrays;
import java.util.LinkedList;

public class LinkedBlockingQueueWAdditiveSem<T> {

    private LinkedList<T> data = new LinkedList();

    // Die Idee ist, dass wir uns mit einem additiven
    // fullSemaphore nutzen, um abzusichern, ob noch
    // Werte mit einer bestimmten Länge in die Warteschlange
    // passen. Die put-Methode soll blockieren, wenn
    // das Einfügen mehrerer Werte die Warteschlange zum
    // Überlaufen bringen würde.
    private AdditiveSemaphore fullSemaphore;
    // Mit einem additiven emptySemaphore merken wir uns
    // wie viele Elemente sich in der Warteschlange befinden,
    // um das Lesen zu blockieren, wenn die Warteschlange leer ist.
    private AdditiveSemaphore emptySemaphore = new AdditiveSemaphore(0);

    private BinarySemaphore mutex = new BinarySemaphore();

    LinkedBlockingQueueWAdditiveSem(int capacity){
        fullSemaphore = new AdditiveSemaphore(capacity);
    }

    public void put(T[] values) throws InterruptedException{
        fullSemaphore.acquire(values.length);
        mutex.acquire();
        emptySemaphore.release(values.length);
        data.addAll(Arrays.asList(values));
        System.out.println("Value added:  " + Arrays.toString(values) + " (Current queue size: " + data.size() + " )");
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
