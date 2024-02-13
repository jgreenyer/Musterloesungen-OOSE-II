package sheet4.task3_a_reentrantreadwritelock_java;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWAccessControl<T> {

    private T[] data;

    ReadWriteLock rwlock = new ReentrantReadWriteLock();

    public RWAccessControl(T[] initialData){
        data = initialData;
    }

    public void write(Map<Integer,T> changes){
        rwlock.writeLock().lock();
        for (int i=0; i<data.length;i++){
            if (changes.containsKey(i)){
                data[i]=changes.get(i);
            }
            System.out.println("data written: " + Arrays.toString(data));
        }
        rwlock.writeLock().unlock();
    }

    public T[] read() {
        rwlock.readLock().lock();
        try {
            System.out.println("reading values " + Arrays.toString(data));
            return data;
        } finally {
            rwlock.readLock().unlock();
        }
    }
}
