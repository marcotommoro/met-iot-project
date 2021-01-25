package MeT.server;

import MeT.server.resources.coap.CoapPirResource;
import MeT.server.resources.raw.PirRawSensor;
import MeT.server.resources.raw.ResourceDataListener;
import MeT.server.resources.raw.SmartObjectResource;
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
        logger.info("ciao", pirRawSensor);
        System.out.println("ciao");
        CoapPirResource coapPirResource = new CoapPirResource(deviceId, "pir", pirRawSensor);

        this.add(coapPirResource);

        pirRawSensor.addDataListener(new ResourceDataListener<Date>() {
            @Override
            public void onDataChanged(SmartObjectResource<Date> resource, Date updatedValue) {
                if(resource != null && updatedValue != null)
                    logger.info("PIR Device: {} -> Ti sono entrati in casa, s'ta 'tenti - SERVER", resource.getId(), updatedValue);
            }
        });
    }
    public static void main(String[] args){
        MainServer server = new MainServer();
        server.start();
        logger.info("server started on port {}", server.toString());
    }
}
