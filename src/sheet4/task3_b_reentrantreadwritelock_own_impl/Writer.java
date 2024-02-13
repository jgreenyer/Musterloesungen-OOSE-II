package sheet4.task3_b_reentrantreadwritelock_own_impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Writer extends Thread{

    RWAccessControl<Integer> ac;

    public Writer(RWAccessControl<Integer> ac){
        this.ac = ac;
    }

    @Override
    public void run() {
        Random random = new Random();
        while(!isInterrupted()){
            Map<Integer,Integer> changeMap = new HashMap<>();
            changeMap.put(0, random.nextInt(10));
            changeMap.put(1, random.nextInt(10));
            changeMap.put(2, random.nextInt(10));
            try {
                sleep(random.nextInt(3000));
                ac.write(changeMap);
            } catch (InterruptedException e) {break;}
        }
    }
}
