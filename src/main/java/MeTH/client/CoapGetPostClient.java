package MeTH.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Locale;


public class CoapGetPostClient extends CoapClient implements CoapRequest {
    private static Logger logger = LoggerFactory.getLogger(CoapGetPostClient.class);

    public CoapGetPostClient(String url, String useCase){
        super(LOCALHOSTS.get(useCase.toUpperCase(Locale.ROOT)) + url);
    }

    public void request(String method)  {
        Request request = new Request(method == "GET" ? CoAP.Code.GET : CoAP.Code.POST);
        request.setConfirmable(true);
        request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.TEXT_PLAIN));
        CoapResponse response;
        try {
            response = this.advanced(request);

            String text = response.getResponseText();

            logger.info("Stato: {}", text);

        } catch (ConnectorException | IOException  | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataChanged(String updatedValue) {

    }
}
