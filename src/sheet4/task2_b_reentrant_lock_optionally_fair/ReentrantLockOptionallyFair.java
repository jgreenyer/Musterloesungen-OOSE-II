package sheet4.task2_b_reentrant_lock_optionally_fair;

import java.util.LinkedList;

public class ReentrantLockOptionallyFair {

    private int holdCount = 0;
    private Thread owner;

    private LinkedList<Thread> queue = new LinkedList<>();

    private boolean fair;

    public ReentrantLockOptionallyFair(boolean fair) {
        this.fair = fair;
    }

    public synchronized void lock(){
        // dasselbe wie in der fairen bzw. nicht-fairen Lösung -> einfach den Code
        // in if- und else-Branch kopiert...
        if (fair){
            // Wann muss dieser Thread warten?
            // (1) nicht reentrant: holdCount > 0, aber aufrufender Thread ist nicht owner.
            // (2) aufrufender Thread kann nicht als Nächstes drankommen:
            //     holdCount ist 0, aber Warteschlange ist leer und aufrufender Thread
            //     ist nicht als Nächstes an der Reihe.
            if(!isReentrant() && !currentThreadCanGetLockNext()){
                // Wenn dieser Thread warten muss, dann wird er in die Warteschlange
                // eingefügt. Bis dieser Thread dann als Nächstes drankommen kann
                // (Warteschlange leer oder aufrufender Thread ganz vorne), muss der
                // Thread warten.
                queue.addLast(Thread.currentThread());
                while(!currentThreadCanGetLockNext()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {}
                }
                // Sobald der Thread nicht mehr warten muss, muss er aus der
                // Warteschlange entfernt werden.
                queue.removeFirst();
            }
        } else {
            // Verhalten wie vorher in nicht-fairer Variante
            if(Thread.currentThread() != owner){
                while(holdCount > 0) {
                    try {
                        wait();
                    } catch (InterruptedException e) {}
                }
                owner = Thread.currentThread();
            }
        }
        owner = Thread.currentThread();
        holdCount++;
    }

    private boolean isReentrant(){
        return holdCount > 0 && Thread.currentThread() == owner;
    }

    private boolean currentThreadCanGetLockNext(){
        return holdCount == 0 &&
                (queue.isEmpty() ||  queue.getFirst() == Thread.currentThread());
    }


    public synchronized void unlock(){
        if(holdCount == 0){
            throw new IllegalMonitorStateException("Lock is already unlocked");
        }
        if(Thread.currentThread() != owner){
            throw new IllegalMonitorStateException("Current thread not owner");
        }
        holdCount--;
        notifyAll();
    }

    public int getHoldCount() {
        return holdCount;
    }
}
