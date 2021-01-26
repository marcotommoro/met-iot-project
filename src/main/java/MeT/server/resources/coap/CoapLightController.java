package MeT.server.resources.coap;

import MeT.server.resources.raw.PirRawSensor;
import MeT.server.resources.raw.ResourceDataListener;
import MeT.server.resources.raw.SmartObjectResource;
import MeT.server.resources.raw.SwitchRawController;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class CoapLightController extends CoapResource {

    private static Logger logger = LoggerFactory.getLogger(CoapLightController.class);
    private String deviceId;
    private SwitchRawController switchRawController;
    private static final String OBJECT_TITLE = "Coap light controller";
    private boolean isActive;

    public CoapLightController(String deviceId, String url, SwitchRawController switchRawController){
        super(url);

        if (deviceId == null || url == null || switchRawController == null) {
            logger.error("Non va un cazzo");
            return;
        }

        this.switchRawController = switchRawController;
        this.deviceId = deviceId;

        this.setObservable(true);
        this.setObserveType(CoAP.Type.CON);
        this.getAttributes().setTitle(OBJECT_TITLE);
        this.getAttributes().setObservable();
        this.getAttributes().addAttribute("rt", PirRawSensor.RESOURCE_TYPE);
        this.getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

        this.switchRawController.addDataListener(new ResourceDataListener<Boolean>() {
            @Override
            public void onDataChanged(SmartObjectResource<Boolean> resource, Boolean updatedValue) {
                if(resource == null || updatedValue == null) return;
                isActive = updatedValue;
                logger.info("Light controller Device: {} -> Ti sono entrati in casa, s'ta 'tenti - Resource", resource.getId(), updatedValue);
            }
        });
    }

    public void handleGET(CoapExchange exchange){
        if(!(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.TEXT_PLAIN))
            exchange.respond(CoAP.ResponseCode.BAD_REQUEST, "Wrong content type");

        exchange.respond(CoAP.ResponseCode.CONTENT, this.isActive ? "ON": "OFF");
    }

    public void handlePOST(CoapExchange exchange){
        try{
            if(exchange.getRequestPayload() == null){
                this.isActive = !this.isActive;
                this.switchRawController.toogleActive();
                logger.info("Resource Status Light Updated: {}", this.isActive);

                exchange.respond(CoAP.ResponseCode.CHANGED);
            }
            else{
                exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
            }

        }catch (Exception e){
            logger.error("Error Handling POST -> {}", e.getLocalizedMessage());
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

}






















