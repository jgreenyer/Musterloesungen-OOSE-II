package sheet4.task2_a_reentrant_lock;

public class ReentrantLock {
    // Variable zum Zählen, wie oft das Lock vom selben Thread erworben wurde.
    private int holdCount = 0;

    // Variable zum Speichern des Threads, der aktuell das Lock besitzt.
    private Thread owner;

    public synchronized void lock() {
        // Überprüfen, ob das Lock frei ist (holdCount == 0).
        if (holdCount == 0) {
            // Setzt den aktuellen Thread als Besitzer, da das Lock frei ist.
            owner = Thread.currentThread();
        } else {
            // Wenn das Lock bereits gehalten wird, überprüfen, ob der aktuelle Thread der Besitzer ist.
            if (Thread.currentThread() != owner) {
                // Wenn der aktuelle Thread nicht der Besitzer ist, muss er warten,
                // bis das Lock freigegeben wird.
                while (holdCount > 0) {
                    try {
                        wait(); // Wartet auf notify() oder notifyAll().
                    } catch (InterruptedException e) {}
                }
                // Nach dem Warten, den aktuellen Thread als Besitzer setzen.
                owner = Thread.currentThread();
            }
        }
        // Erhöht die Anzahl, damit das reentrant-Verhalten richtig funktioniert.
        holdCount++;
    }

    public synchronized void unlock() {
        // Überprüfen, ob das Lock bereits freigegeben ist (holdCount == 0).
        if (holdCount == 0) {
            throw new IllegalMonitorStateException("Lock is already unlocked");
        }
        // Überprüfen, ob der aktuelle Thread der Besitzer des Locks ist.
        if (Thread.currentThread() != owner) {
            throw new IllegalMonitorStateException("Current thread not owner");
        }
        // Verringert die Anzahl, -> reentrant-Verhalten...
        holdCount--;
        // Wenn holdCount auf 0 gesetzt wurde, notify() aufrufen, um wartende Threads zu informieren.
        if (holdCount == 0) {
            notify(); // Warum nicht notifyAll()?
        }
    }

    // Getter-Methode für die holdCount-Variable.
    public int getHoldCount() {
        return holdCount;
    }
}

