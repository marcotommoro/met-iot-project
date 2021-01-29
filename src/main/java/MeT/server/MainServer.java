package MeT.server;

import MeT.server.resources.coap.CoapAlarmController;
import MeT.server.resources.coap.CoapContactDWResource;
import MeT.server.resources.coap.CoapLightController;
import MeT.server.resources.coap.CoapPirResource;
import MeT.server.resources.raw.*;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Date;
import java.util.UUID;

public class MainServer extends CoapServer{
    private static Logger logger = LoggerFactory.getLogger(MainServer.class);

    public MainServer(){
        super();
        String deviceId = String.format("dipi:iot:%s", UUID.randomUUID().toString());

        PirRawSensor pirRawSensor = new PirRawSensor();
        ContactDWRawSensor contactDWRawSensor = new ContactDWRawSensor(5000);
        SwitchRawController switchRawControllerLight = new SwitchRawController(false);
        SwitchRawController switchRawControllerAlarm = new SwitchRawController();

        CoapPirResource coapPirResource = new CoapPirResource(deviceId, "pir", pirRawSensor);
        CoapContactDWResource coapContactDWResource = new CoapContactDWResource(deviceId, "contact-dw", contactDWRawSensor);
        CoapLightController coapLightController = new CoapLightController(deviceId, "light", switchRawControllerLight);
        CoapAlarmController coapAlarmController = new CoapAlarmController(deviceId, "alarm", switchRawControllerAlarm);

        this.add(coapPirResource);
        this.add(coapContactDWResource);
        this.add(coapLightController);
        this.add(coapAlarmController);

        pirRawSensor.addDataListener(new ResourceDataListener<Date>() {
            @Override
            public void onDataChanged(SmartObjectResource<Date> resource, Date updatedValue) {
                if(resource == null || updatedValue == null) return;
                logger.info("PIR Device -> Ti sono entrati in casa, s'ta 'tenti - SERVER", updatedValue);
                switchRawControllerAlarm.setActive(true);
                switchRawControllerLight.setActive(true);
            }
        });

        contactDWRawSensor.addDataListener(new ResourceDataListener<String>() {
            @Override
            public void onDataChanged(SmartObjectResource<String> resource, String updatedValue) {
                if(resource == null || updatedValue == null) return;
                logger.info("Contact dw Device -> Ti sono entrati in casa, s'ta 'tenti - SERVER", updatedValue);
                switchRawControllerAlarm.setActive(true);
                switchRawControllerLight.setActive(true);
            }
        });

        switchRawControllerLight.addDataListener(new ResourceDataListener<Boolean>() {
            @Override
            public void onDataChanged(SmartObjectResource<Boolean> resource, Boolean updatedValue) {
                if(resource != null && updatedValue != null)
                    logger.info("Light Device {}-> Ti sono entrati in casa, s'ta 'tenti - SERVER",  updatedValue);
            }
        });

        switchRawControllerAlarm.addDataListener(new ResourceDataListener<Boolean>() {
            @Override
            public void onDataChanged(SmartObjectResource<Boolean> resource, Boolean updatedValue) {
                if(resource != null && updatedValue != null)
                    logger.info("Alarm Device {} -> Ti sono entrati in casa, s'ta 'tenti - SERVER", updatedValue);
            }
        });



    }
    public static void main(String[] args){
        MainServer server = new MainServer();
        server.start();
        logger.info("server started on port {}", server.toString());
    }
}
