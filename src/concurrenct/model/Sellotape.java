package concurrenct.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huhai on 2015/2/18.
 */
public class Sellotape {
    private static AtomicInteger counter = new AtomicInteger(0);

    private final int serialId;

    private Sellotape(int serialId){
        this.serialId = serialId;
    }

    public String toString(){
        return "Sellotape " + serialId;
    }

    public static Sellotape create(){
        return new Sellotape(counter.incrementAndGet());
    }
}
