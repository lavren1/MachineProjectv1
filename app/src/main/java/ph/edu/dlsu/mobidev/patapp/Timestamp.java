package ph.edu.dlsu.mobidev.patapp;

/**
 * Created by Noel Campos on 11/12/2017.
 */

public class Timestamp extends java.sql.Timestamp {

    public Timestamp(long time) {
        super(time);
    }

    public Timestamp(){
        super(System.currentTimeMillis());
    }
}
