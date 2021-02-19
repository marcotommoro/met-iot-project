package MeTH.server.resources.coap;

import MeTH.server.resources.raw.PirRawSensor;
import MeTH.server.resources.raw.SwitchRawController;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoapLightController extends CoapResource {

    private static Logger logger = LoggerFactory.getLogger(CoapLightController.class);
    private String deviceId;
    private SwitchRawController switchRawController;
    private static final String OBJECT_TITLE = "Coap light controller";

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


    }

    public void handleGET(CoapExchange exchange){
        exchange.respond(CoAP.ResponseCode.CONTENT, switchRawController.getActive() ? "ON": "OFF");
    }

    public void handlePOST(CoapExchange exchange){
        try{
            logger.info("MAFFIGA 1");

            if(exchange.getRequestPayload() == null){
                switchRawController.toogleActive();
                logger.info("Resource Status Light Updated: {}", switchRawController.getActive());
                exchange.respond(CoAP.ResponseCode.CHANGED, switchRawController.getActiveState());
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






















