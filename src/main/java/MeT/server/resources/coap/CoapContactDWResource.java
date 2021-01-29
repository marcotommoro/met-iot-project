package MeT.server.resources.coap;

import MeT.server.resources.raw.ContactDWRawSensor;
import MeT.server.resources.raw.PirRawSensor;
import MeT.server.resources.raw.ResourceDataListener;
import MeT.server.resources.raw.SmartObjectResource;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class CoapContactDWResource extends CoapResource {

    private static Logger logger = LoggerFactory.getLogger(CoapContactDWResource.class);
    private String deviceId;
    private ContactDWRawSensor contactDWRawSensor;
    private static final String OBJECT_TITLE = "Contact Door/Window Sensor";
    private String status_dw;

    public CoapContactDWResource(String deviceId, String url, ContactDWRawSensor contactDWRawSensor){
        super(url);

        if (deviceId == null || url == null || contactDWRawSensor == null) {
            logger.error("Non va un cazzo");
            return;
        }

        this.contactDWRawSensor = contactDWRawSensor;
        this.deviceId = deviceId;

        this.setObservable(true);
        this.setObserveType(CoAP.Type.CON);
        this.getAttributes().setTitle(OBJECT_TITLE);
        this.getAttributes().setObservable();
        this.getAttributes().addAttribute("rt", PirRawSensor.RESOURCE_TYPE);
        this.getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));



    }

    public void handleGET(CoapExchange exchange){
        if(!(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.TEXT_PLAIN))
            exchange.respond(CoAP.ResponseCode.BAD_REQUEST, "Wrong content type");

        exchange.respond(CoAP.ResponseCode.CONTENT, this.status_dw);
    }
}






















