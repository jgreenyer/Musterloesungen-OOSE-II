package sheet3.task1_nudeln;

import sheet3.task1_semaphore.BinarySemaphore;

public class NudelnKochen {

    public static void main(String[] args) {

        BinarySemaphore semZwiebelnGeschnitten = new BinarySemaphore();
        BinarySemaphore semTomatenGeschnitten = new BinarySemaphore();
        BinarySemaphore semSoßeGekocht = new BinarySemaphore();
        BinarySemaphore semNudelnGekocht = new BinarySemaphore();

        // Initial die Semaphoren belegen, damit entsprechende Methoden nicht ohne Vorbedingung ausgeführt werden können.
        try {
            semZwiebelnGeschnitten.acquire();
            semTomatenGeschnitten.acquire();
            semSoßeGekocht.acquire();
            semNudelnGekocht.acquire();
        } catch (InterruptedException e) {}

        Runnable nudelnKochen = () -> {
            nudelnKochen();
            semNudelnGekocht.release();
        };

        Runnable zwiebelnSchneiden = () -> {
            zwiebelnSchneiden();
            semZwiebelnGeschnitten.release();
        };

        Runnable tomatenSchneiden = () -> {
            tomatenSchneiden();
            semTomatenGeschnitten.release();
        };

        Runnable soßeKochen = () -> {
            try {
                semZwiebelnGeschnitten.acquire();
                semTomatenGeschnitten.acquire();
            } catch (InterruptedException e) {}
            soßeKochen();
            semSoßeGekocht.release();
        };

        Runnable servieren = () -> {
            try {
                semNudelnGekocht.acquire();
                semSoßeGekocht.acquire();
            } catch (InterruptedException e) {}
            servieren();
        };

        // Threads starten
        new Thread(nudelnKochen).start();
        new Thread(zwiebelnSchneiden).start();
        new Thread(tomatenSchneiden).start();
        new Thread(soßeKochen).start();
        new Thread(servieren).start();
    }

    private static void servieren() {
        System.out.println("Essen wird serviert.");
    }

    private static void soßeKochen() {
        System.out.println("Soße wird gekocht.");
    }

    private static void tomatenSchneiden() {
        System.out.println("Tomaten werden geschnitten.");
    }

    private static void zwiebelnSchneiden() {
        System.out.println("Zwiebeln werden geschnitten.");
    }

    public static void nudelnKochen(){
        System.out.println("Nudeln werden gekocht.");
    }
}
