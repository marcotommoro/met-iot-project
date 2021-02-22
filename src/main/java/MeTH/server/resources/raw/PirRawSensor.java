package MeTH.server.resources.raw;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PirRawSensor extends SmartObjectResource<Date> {

    public static final String RESOURCE_TYPE = "MeTH.sensor.pir";
    private static Logger logger = LoggerFactory.getLogger(PirRawSensor.class);

    private int MIN_PRESENCE_DETECTED = 0;
    private int MAX_PRESENCE_DETECTED = 5;
    private int MIN_RANDOM_VALUE = 0;
    private int MAX_RANDOM_VALUE = 100;

    public static final long UPDATE_PERIOD = 3000;
    private static final long TASK_DELAY_TIME = 1000;

    private Timer timer;
    private Date updateDate;
    private Random random;


    public PirRawSensor() {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
        random = new Random();
        init();
    }

    private boolean generatePresence(){
        int check = random.nextInt( this.MAX_RANDOM_VALUE - this.MIN_RANDOM_VALUE ) + this.MIN_RANDOM_VALUE;
        if (check >= this.MIN_PRESENCE_DETECTED && check <= this.MAX_PRESENCE_DETECTED) return true;
        return false;

    }

    private void init() {
        try{
            logger.info("Starting periodic PIR Update Task with Period: {} ms", UPDATE_PERIOD);

            this.timer = new Timer();
            this.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    updateDate = new Date();
                    if (generatePresence()) notifyUpdate(updateDate);

                }
            }, TASK_DELAY_TIME, UPDATE_PERIOD);

        }catch (Exception e ){
            logger.error("Fuck qualcosa non va");
        }

    }


    @Override
    public Date loadUpdatedValue() {
        return this.updateDate;
    }

}















