package sheet4.task1_a_rwaccesscontrol_semaphore;

import sheet3.task1_semaphore.BinarySemaphore;

import java.util.Arrays;
import java.util.Map;

// Diese Läsung der RWAccessControl hat zwei Eigenschaften
// oder "Probleme":
// (1) in dieser Lösung kann es passieren, dass wenn ein
// Reader aktiv ist, und dann ein Writer wartet,
// und immer weitere Reader aktiv werden, der Writer
// nie wieder drankommt.
// (2) Andererseits kann es sein, dass wenn ein Writer
// aktiv ist und dann ein Reader wartet und noch ein
// weiterer Writer, dass dann der zweite Writer
// vor dem Reader drankommt, statt dem Reader Vorzug
// zu lassen.
public class RWAccessControl<T> {

    private T[] data;
    private int activeReaders = 0;

    // Idee des writeSemaphore:
    // - ist er belegt, kann nicht geschrieben werden
    // - wird entweder beim Schreiben oder beim Lesen belegt
    private BinarySemaphore writeSemaphore = new BinarySemaphore();
    // Idee des monitorSemaphore:
    // - steuert den Eintritt der Leser in kritische Bereiche vor- und nach dem eigentlichen Lesen
    // - zum Beispiel beim Inkrementieren und Dekrementieren des Counters activeReaders.
    private BinarySemaphore monitorSemaphore = new BinarySemaphore();

    public RWAccessControl(T[] initialData){
        data = initialData;
    }

    public void write(Map<Integer,T> changes) throws InterruptedException{
        writeSemaphore.acquire();

        System.out.println("Writing changes " + changes);
        for (int i = 0; i < data.length; i++) {
            if (changes.containsKey(i)) {
                data[i] = changes.get(i);
            }
        }
        System.out.println("Changed data " + Arrays.toString(data));

        writeSemaphore.release();
    }

    public T[] read() throws InterruptedException{

        // Erster kritischer Bereich eines Lesers durch
        // monitorSemaphore abgesichert:
        // 1. Race-Condition beim Inkrementieren von activeReaders vermeiden
        // 2. Gleichzeitig müssen wir den writeSemaphore belegen, wenn
        //    wir aktive Leser haben. Dass muss auch im durch den monitorSemaphore
        //    abgesicherten Bereich passieren, da es sonst zu Race-Conditions
        //    führen kann.
        monitorSemaphore.acquire();
        if (activeReaders == 0){
            writeSemaphore.acquire();
        }
        activeReaders++;
        monitorSemaphore.release();

        T[] returnValue = data;
        System.out.println("Reading " + Arrays.toString(returnValue));

        // Zweiter kritischer Bereich eines Lesers durch
        // monitorSemaphore abgesichert:
        // Eigentlich wie oben nur "Rückwärts":
        // 1. Race-Condition beim Dekrementieren von activeReaders vermeiden
        // 2. Gleichzeitig müssen wir den writeSemaphore freigeben, wenn
        //    wir keine aktiven Leser mehr haben. Dass muss auch im durch den
        //    monitorSemaphore abgesicherten Bereich passieren, da es sonst zu
        //    Race-Conditions führen kann.
        monitorSemaphore.acquire();
        activeReaders--;
        if (activeReaders == 0){
            writeSemaphore.release();
        }
        monitorSemaphore.release();

        return returnValue;
    }
}