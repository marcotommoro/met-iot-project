package MeTH.server;

import MeTH.server.resources.coap.CoapAlarmController;
import MeTH.server.resources.coap.CoapContactDWResource;
import MeTH.server.resources.coap.CoapLightController;
import MeTH.server.resources.coap.CoapPirResource;
import MeTH.server.resources.raw.*;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import ch.qos.logback.classic.util.ContextInitializer;

public class MainServer extends CoapServer{
    private static Logger logger = LoggerFactory.getLogger(MainServer.class);
    private String deviceId;

    private ArrayList<PirRawSensor> pirRawSensors;
    private ArrayList<ContactDWRawSensor> contactDWRawSensor;
    private ArrayList<SwitchRawController> switchRawControllerLight;
    private SwitchRawController switchRawControllerAlarm;

    private ArrayList<CoapPirResource> coapPirResource;
    private ArrayList<CoapLightController>coapLightController;
    private ArrayList<CoapContactDWResource>coapContactDWResource;
    private CoapAlarmController coapAlarmController;

    public static String GARAGE_NAME = "garage";
    public static int GARAGE_PORT = 5683;
    public static String KITCHEN_NAME = "kitchen";
    public static int KITCHEN_PORT = 5684;


    public MainServer(String deviceId, Integer port, Map<String, Integer>sensors){
        super(NetworkConfig.createStandardWithoutFile(), port);
        this.deviceId = deviceId;

        sensors.forEach(( sensor, value) ->{
            switch (sensor){
                case "pir":
                    pirRawSensors = new ArrayList<>();
                    coapPirResource = new ArrayList<>();

                    for (int i = 0; i < value; i++) {
                        pirRawSensors.add(new PirRawSensor());
                        coapPirResource.add(new CoapPirResource(deviceId, "pir", pirRawSensors.get(i)));
                        this.add(coapPirResource.get(i));
                    }
                    break;
                case "light":
                    switchRawControllerLight = new ArrayList<>();
                    coapLightController = new ArrayList<>();
                    for (int i = 0; i < value; i++) {
                        switchRawControllerLight.add(new SwitchRawController());
                        coapLightController.add(new CoapLightController(deviceId, "light", switchRawControllerLight.get(i)));
                        this.add(coapLightController.get(i));
                    }
                    break;
                case "alarm":
                        switchRawControllerAlarm = new SwitchRawController();
                        coapAlarmController = new CoapAlarmController(deviceId, "alarm", switchRawControllerAlarm);
                        this.add(coapAlarmController);
                    break;
                case "contact-dw":
                    contactDWRawSensor = new ArrayList<>();
                    coapContactDWResource = new ArrayList<>();
                    for (int i = 0; i < value; i++) {
                        contactDWRawSensor.add(new ContactDWRawSensor(1000));
                        coapContactDWResource.add(new CoapContactDWResource(deviceId, "contact-dw", contactDWRawSensor.get(i)));
                        this.add(coapContactDWResource.get(i));
                    }
                    break;
                default:
                    break;

            }
        });


       pirRawSensors.forEach(pirRawSensor -> pirRawSensor.addDataListener(new ResourceDataListener<Date>() {
           @Override
           public void onDataChanged(SmartObjectResource<Date> resource, Date updatedValue) {
               if(resource == null || updatedValue == null) return;
               logger.info("PIR Device -> Ti sono entrati in "+ deviceId + " s'ta 'tenti", updatedValue);
               lightsAlarmON();
           }
       }));

       contactDWRawSensor.forEach(contactDWRawSensor -> contactDWRawSensor.addDataListener(new ResourceDataListener<String>() {
            @Override
            public void onDataChanged(SmartObjectResource<String> resource, String updatedValue) {
                if(resource == null || updatedValue == null) return;
                logger.info("Contact dw Device -> Ti sono entrati in "+ deviceId + " s'ta 'tenti", updatedValue);
                lightsAlarmON();
            }
       }));

    }

    public void lightsAlarmON(){
        switchRawControllerAlarm.setActive(true);
        switchRawControllerLight.forEach(switchRawController -> switchRawController.setActive(true));
    }

    public static void main(String[] args){
        MainServer[] servers = new MainServer[2];
        servers[0] = new MainServer(GARAGE_NAME, GARAGE_PORT, Map.of("pir",2, "light", 6, "alarm", 1, "contact-dw", 3));
        servers[1] = new MainServer(KITCHEN_NAME, KITCHEN_PORT, Map.of("pir",1, "light", 1, "alarm", 1, "contact-dw", 1));
        servers[0].start();
        servers[1].start();
        logger.info("servers started");
    }
}
