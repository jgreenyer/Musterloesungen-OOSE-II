package sheet4.task1_a_rwaccesscontrol_semaphore_prio_reader;

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
            changeMap.put(random.nextInt(3), random.nextInt(10));
            changeMap.put(random.nextInt(3), random.nextInt(10));
            changeMap.put(random.nextInt(3), random.nextInt(10));
            try {
                sleep(random.nextInt(3000));
                ac.write(changeMap);
            } catch (InterruptedException e) {break;}
        }
    }
}
