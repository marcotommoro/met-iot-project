package MeTH.server.resources.coap;

import MeTH.server.resources.raw.PirRawSensor;
import MeTH.server.resources.raw.ResourceDataListener;
import MeTH.server.resources.raw.SmartObjectResource;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.californium.core.coap.CoAP;

import java.util.Date;

public class CoapPirResource extends CoapResource {

    private static Logger logger = LoggerFactory.getLogger(CoapPirResource.class);
    private String deviceId;
    private PirRawSensor pirRawSensor;
    private static final String OBJECT_TITLE = "PIR Sensor";
    private Date updateDate;

    public CoapPirResource(String deviceId, String url, PirRawSensor pirRawSensor){
        super(url);

        if (deviceId == null || url == null || pirRawSensor == null) {
            logger.error("Non va un cazzo");
            return;
        }

        this.pirRawSensor = pirRawSensor;
        this.deviceId = deviceId;
        this.updateDate = new Date();
        this.setObservable(true);
        this.setObserveType(CoAP.Type.CON);
        this.getAttributes().setTitle(OBJECT_TITLE);
        this.getAttributes().setObservable();
        this.getAttributes().addAttribute("rt", pirRawSensor.getType());
        this.getAttributes().addAttribute("rt", PirRawSensor.RESOURCE_TYPE);
        this.getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

        this.pirRawSensor.addDataListener(new ResourceDataListener<Date>() {
            @Override
            public void onDataChanged(SmartObjectResource<Date> resource, Date updatedValue) {
                updateDate = updatedValue;
                changed();
            }
        });
    }


    @Override
    public void handleGET(CoapExchange exchange) {
        logger.info(String.valueOf(this.updateDate));
        if (this.updateDate == null)
            exchange.respond(CoAP.ResponseCode.NOT_FOUND );
        else
            exchange.respond(CoAP.ResponseCode.CONTENT,this.updateDate.toString());
    }


}






















