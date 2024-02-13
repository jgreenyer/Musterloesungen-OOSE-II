package sheet4.task3_a_reentrantreadwritelock_java;

import java.util.Arrays;
import java.util.Random;

public class Reader extends Thread{

    RWAccessControl<Integer> ac;

    public Reader(RWAccessControl<Integer> ac){
        this.ac = ac;
    }

    @Override
    public void run() {
        Random random = new Random();
        while(!isInterrupted()){
            Integer[] values = new Integer[0];
            try {
                sleep(random.nextInt(4000));
                values = ac.read();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
