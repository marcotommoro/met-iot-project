package MeT.server.resources.raw;

import org.eclipse.californium.core.coap.BlockOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.DocFlavor;
import java.util.*;

public class ContactDWRawSensor extends SmartObjectResource<String> {

    public static final String RESOURCE_TYPE = "MeT.sensor.contactDW";
    private static Logger logger = LoggerFactory.getLogger(ContactDWRawSensor.class);

    private int MIN_DW_OPEN_CLOSE = 0;
    private int MAX_DW_OPEN_CLOSE = 0;

    private int MIN_RANDOM_VALUE = 0;
    private int MAX_RANDOM_VALUE = 100;

    public static final long UPDATE_PERIOD = 1000;
    private static final long TASK_DELAY_TIME = 0;

    private Timer timer;
    private Random random;
    private int delay;
    private String status_dw;
    private String before_status_dw;


    public ContactDWRawSensor(int delay) {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
        random = new Random();
        this.delay = delay;
        this.status_dw = "close";
        this.before_status_dw = "close";
        init();
    }

    private String generateEvent(){
        int number = random.nextInt( this.MAX_RANDOM_VALUE - this.MIN_RANDOM_VALUE ) + this.MIN_RANDOM_VALUE;

        if (number >= this.MIN_DW_OPEN_CLOSE && number <= this.MAX_DW_OPEN_CLOSE) return this.status_dw == "close" ? "open" : "close";

        return null;
    }

    private void init() {
        try{
            logger.info("Starting periodic Contact DW Update Task with Period: {} ms", UPDATE_PERIOD);

            this.timer = new Timer();
            this.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    String event = generateEvent();
                    if (event == null) return;

                    logger.info("generated event " + event);
                    status_dw = event;

                    if (event == "open"){
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (status_dw == "open") notifyUpdate("open");
                            }
                        }, delay);
                    }
                }
            }, TASK_DELAY_TIME, UPDATE_PERIOD);

        }catch (Exception e ){
            logger.error("Fuck qualcosa non va");
        }

    }

    @Override
    public String loadUpdatedValue() {
        return this.status_dw;
    }


    public static void main(String[] args){
        ContactDWRawSensor dwRawSensor = new ContactDWRawSensor(3000);
        logger.info("PIR resource created");

        dwRawSensor.addDataListener(new ResourceDataListener<String>() {
            @Override
            public void onDataChanged(SmartObjectResource<String> resource, String updatedValue) {
                if(resource != null && updatedValue != null)
                    if (updatedValue == "open")
                        logger.info("ContactDW Device: {} -> V'a c'at ghè la fnestra verta! Sera ch'ha ghe fret ", resource.getId(), updatedValue);
                    else
                        logger.info("ContactDW Device: {} ->  Oh bravo putel c'ha tlè serada ", resource.getId(), updatedValue);

            }
        });
    }

}















