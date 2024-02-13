package sheet4.task1_a_rwaccesscontrol_semaphore_prio_reader;

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

    // Um den Lesern Priorität zu geben, führen wir einen weiteren
    // Sempahore ein. Dieser wird gesetzt, wenn es wartende Leser gibt.
    // Ist dieser Semaphore gesetzt, kann der Schreiber keinen Schreibzugriff
    // setzen
    private BinarySemaphore waitingReadersSemaphore = new BinarySemaphore();


    // Damit wir wissen, ob wir den waitingReadersSemaphore gesetzt oder
    // freigegeben werden muss, führen wir einen Zähler ein, der die Anzahl
    // der wartenden Leser repräsentiert.
    // Idee: Leser inkrementieren diesen Zähler beim Eintritt in die
    // read()-Methode. Wenn Sie dann zu aktiven Lesern werden,
    // dekrementieren sie diesen Wert wieder.
    // Der erste Leser, der den wartenden Zustand erreicht, setzt den
    // waitingReadersSemaphore; der letzte gibt waitingReadersSemaphore
    // wieder frei
    private int waitingReaders = 0;


    public RWAccessControl(T[] initialData){
        data = initialData;
    }

    public void write(Map<Integer,T> changes) throws InterruptedException{
        waitingReadersSemaphore.acquire(); // kein Schreibzugriff, wenn Leser warten.
        writeSemaphore.acquire(); // Auf Schreibzugriff warten und absichern
        waitingReadersSemaphore.release(); // gleich wieder freigeben!

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

        // ist dies der erste Leser, der auf Zugriff wartet?
        // -> waitingReadersSemaphore belegen.
        if(waitingReaders == 0){
            waitingReadersSemaphore.acquire();
        }
        waitingReaders++;

        // Ist dies der erste Leser, der nun anfangen möchte zu Lesen?
        // -> writeSemaphore setzen, damit kein Schreiber aktiv werden kann.
        if (activeReaders == 0){
            writeSemaphore.acquire();
        }
        activeReaders++;

        // Dieser Leser ist nun aktiv und wartet nicht mehr: Daher den
        // entsprechenden Zähler dekrementieren. Der letzte wartende
        // Leser gibt dann den waitingReadersSemaphore wieder frei.
        waitingReaders--;
        if(waitingReaders == 0){
            waitingReadersSemaphore.release();
        }
        // Nun haben wir alle Verwaltungsaufgben abgeschlossen, die vor
        // dem eigentlichen Lesen stattfinden müssen. Wir können nun den
        // monitorSemaphore wieder freigeben, damit auch andere Leser
        // gleichzeitig lesen können.
        monitorSemaphore.release();

        T[] returnValue = data;
        System.out.println("Reading " + Arrays.toString(returnValue));

        // Zweiter kritischer Bereich eines Lesers durch
        // monitorSemaphore abgesichert: Hier passierten noch eine
        // "Verwaltungstätigkeite" am Ende des Lesens:
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