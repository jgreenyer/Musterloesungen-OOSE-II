package sheet4.task1_b_rwaccesscontrol_semaphore_prio_writer_more_efficient;

import sheet3.task1_semaphore.BinarySemaphore;

import java.util.Arrays;
import java.util.Map;

public class RWAccessControl<T> {

    private T[] data;

    // Idee des writeSemaphore:
    // ist er belegt, kann nicht geschrieben werden
    // - wird beim Schreiben belegt, bevor ein Schreiber schreibt;
    //   ist ein Schreiber fertig, gibt er diesem Sem. wieder frei.
    // - wird belegt, bevor der erste Leser liest;
    //   wenn der letzte aktive Leser fertig ist, gibt es diesen Sem
    //   wieder frei.
    private BinarySemaphore writeSemaphore = new BinarySemaphore();
    // Idee des monitorSemaphore:
    // Übernimmt die Rolle des Monitors dieser Instanz;
    // steuert den Eintritt der Leser und Schreiber in kritische Bereiche
    // vor- und nach dem eigentlichen Lesen/Schreiben:
    //  (1) Beim Inkrementieren und Dekrementieren von Zählern.
    //  (2) Beim Aquire und Release des writeSemaphore.
    private BinarySemaphore monitorSemaphore = new BinarySemaphore();

    // Wir müssen uns die Anzahl der aktiven Leser merken
    // Damit ein Leser weiß, dass der writeSemaphore gesetzt werden muss,
    // wenn er der erste Leser ist.
    // Genauso muss ein Leser am Ende des Lesevorgangs wissen, dass
    // der writeSemaphore freigegeben werden muss, wenn er der letzte
    // Leser ist.
    private int activeReaders = 0;

    // Wir verzichten auf den Semaphoren für die wartenden Schreiber
    //private BinarySemaphore waitingWritersSemaphore = new BinarySemaphore();
    // stattdessen nutzen wir nur einen Zähler, der die Anzahl
    // der wartenden Schreiber repräsentiert.
    // Idee: Schreiber inkrementieren diesen Zähler beim Eintritt in die
    // write()-Methode. Wenn Sie dann Schreibzugriff bekommen,
    // dekrementieren sie diesen Wert wieder.
    // Die Leser überprüfen den Wert: Wenn es wartende Schreiber gibt,
    private int waitingWriters = 0;


    public RWAccessControl(T[] initialData){
        data = initialData;
    }

    public void write(Map<Integer,T> changes) throws InterruptedException{
        // In dieser Variante hat der Writer einige Verwaltungstätigkeiten,
        // nämlich das Inkrementieren des Zählers für die wartenden Schreiber.
        monitorSemaphore.acquire();
        waitingWriters++;
        monitorSemaphore.release();

        writeSemaphore.acquire(); // Auf Schreibzugriff warten und absichern

        // Wieder Verwaltungstätigkeiten durch Monitor absichern:
        monitorSemaphore.acquire();
        // Nun wartet dieser Writer nicht mehr
        waitingWriters--;

        monitorSemaphore.release();


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
        // 1. Race-Condition beim Inkrementieren von waitingReaders
        //    und activeReaders vermeiden
        // 2. Gleichzeitig müssen wir den writeSemaphore belegen, wenn
        //    wir aktive Leser haben. Dass muss auch im durch den monitorSemaphore
        //    abgesicherten Bereich passieren, da es sonst zu Race-Conditions
        //    führen kann.
        monitorSemaphore.acquire();

        // Wenn Schreiber warten, dann sollen hier die Leser warten,
        // indem der "Monitor"-Semaphore freigegeben und sofort wieder erworben wird.
        // Diese Methode soll theoretisch anderen Threads, einschließlich Schreibern,
        // die Chance geben, den kritischen Abschnitt zu betreten. Es basiert auf der Annahme,
        // dass das Betriebssystem-Scheduling fair ist und somit wartenden Schreibern
        // irgendwann der Vorrang gegeben wird.
        // Allerdings gibt es keine Garantie für die Bevorzugung von Schreibern.
        // Die tatsächliche Ausführungsreihenfolge (also wer den monitorSemaphore belegen kann)
        // kann nicht präzise gesteuert werden.
        // Dieser Ansatz führt nicht zu klassischem Busy Waiting, da der Thread nicht ständig
        // eine Bedingung überprüft, sondern zwischendurch immer wieder auf den
        // Zugriff eines Semaphoren wartet. Dennoch könnte der Mechanismus zu unnötigem Overhead
        // führen und die Priorisierung von Schreibern nicht effektiv sicherstellen.
        // Eine Alternative ist die Verwendung eines weiteren Semaphores, wie in der
        // Alternativläsung gezeigt. Allerdings hat diese auch ihre Nachteile.
        // Besser wäre die Anwendung von eines java.util.concurrent.locks.ReentrantReadWriteLock
        if(waitingWriters > 0){
            monitorSemaphore.release();
            monitorSemaphore.acquire();
        }

        // Ist dies der erste Leser, der nun anfangen möchte zu Lesen?
        // -> writeSemaphore setzen, damit kein Schreiber aktiv werden kann.
        if (activeReaders == 0){
            writeSemaphore.acquire();
        }
        activeReaders++;

        // Nun haben wir alle Verwaltungsaufgaben abgeschlossen, die vor
        // dem eigentlichen Lesen stattfinden müssen. Wir können nun den
        // monitorSemaphore wieder freigeben, damit auch andere Leser
        // gleichzeitig lesen können.
        monitorSemaphore.release();

        T[] returnValue = data;
        System.out.println("Reading " + Arrays.toString(returnValue));

        // Zweiter kritischer Bereich eines Lesers durch
        // monitorSemaphore abgesichert: Hier passierten noch eine
        // "Verwaltungstätigkeit" am Ende des Lesens:
        // Wir müssen den writeSemaphore freigeben, wenn dies der
        // letzte aktive Leser ist. Da hier ein Zähler überprüft bzw.
        // dekrementiert wird, muss dieser Zugriff durch den monitorSemaphore
        // abgesichert sein, da es sonst zu Race-Conditions führen kann.
        monitorSemaphore.acquire();
        activeReaders--;
        if (activeReaders == 0){
            writeSemaphore.release();
        }
        monitorSemaphore.release();

        return returnValue;
    }
}