package MeT.server.resources.raw;

import com.fasterxml.jackson.databind.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class SwitchRawController extends SmartObjectResource<Boolean> {

    public static final String RESOURCE_TYPE = "MeT.sensor.SwitchRawController";
    private static Logger logger = LoggerFactory.getLogger(SwitchRawController.class);

    private static final String LOG_DISPLAY_NAME = "SwitchController";

    private Boolean isActive;

    public SwitchRawController() {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
        this.isActive = false;
    }

    public SwitchRawController(boolean initial) {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
        this.isActive = initial;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void toogleActive () {
        this.isActive = !this.isActive;
        notifyUpdate(isActive);
    }

    public void setActive(boolean state){
        if(this.isActive != state)
            toogleActive();
    }

    @Override
    public Boolean loadUpdatedValue() {
        return this.isActive;
    }

}














