package sheet4.task3_b_reentrantreadwritelock_own_impl;

import sheet4.task2_a_reentrant_lock.ReentrantLock;


public class ReadWriteLock {

    private int activeReaders = 0;
    private int waitingReaders = 0;
    private boolean writerWriting = false;

    static class ReadLock implements Lock{

        private final ReadWriteLock readWriteLock;


        public ReadLock(ReadWriteLock readWriteLock){
            this.readWriteLock = readWriteLock;
        }

        @Override
        public void lock() {
            // hier kann das Verhalten wie auf den Folien
            // implementiert werden
            // (Siehe Leser-Schreiber-Problem -> "Lösung mit synchronize/wait"
            // Dies ist der Teil vor T[] returnValue = data;)
            synchronized (readWriteLock){
                readWriteLock.waitingReaders++;
                while(readWriteLock.writerWriting) {
                    try {
                        readWriteLock.wait();
                    } catch (InterruptedException e) {}
                }
                readWriteLock.activeReaders++;
                readWriteLock.waitingReaders--;
            }
        }

        @Override
        public void unlock() {
            // auch hier
            // (Siehe Leser-Schreiber-Problem -> "Lösung mit synchronize/wait")
            // Dies ist der Teil nach T[] returnValue = data;)
            synchronized (readWriteLock){
                readWriteLock.activeReaders--;
                readWriteLock.notifyAll();
            }
        }
    };
    static class WriteLock implements Lock{

        private final ReadWriteLock readWriteLock;

        private Thread owner;

        public WriteLock(ReadWriteLock readWriteLock){
            this.readWriteLock = readWriteLock;
        }


        @Override
        public void lock() {
            // auch hier ...
            // (Siehe Leser-Schreiber-Problem -> "Lösung mit synchronize/wait"
            // Dies ist der Teil vor dem eigentlichen Schreiben)
            synchronized (readWriteLock){
                while (readWriteLock.activeReaders > 0
                        || readWriteLock.waitingReaders > 0
                        || readWriteLock.writerWriting){
                    try {
                        readWriteLock.wait();
                    } catch (InterruptedException e) {}
                }
                readWriteLock.writerWriting = true;
                owner = Thread.currentThread();
            }
        }

        @Override
        public void unlock() {
            // auch hier ...
            // (Siehe Leser-Schreiber-Problem -> "Lösung mit synchronize/wait"
            // Dies ist der Teil nach dem eigentlichen Schreiben)
            if (owner != Thread.currentThread()){
                throw new IllegalMonitorStateException("Current thread not owner.");
            }
            synchronized (readWriteLock){
                readWriteLock.writerWriting = false;
                readWriteLock.notifyAll();
            }
        }
    };

    private ReadLock readLock;
    private WriteLock writeLock;

    public ReadWriteLock(){
        readLock = new ReadLock(this);
        writeLock = new WriteLock(this);
    }


    public ReadLock readLock() {
        return readLock;
    }

    public WriteLock writeLock() {
        return writeLock;
    }
}

