package MeT.client;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainCoapObservable {
    private static Logger logger = LoggerFactory.getLogger(MainCoapObservable.class);

    public static void main(String[] args){
        CoapObservableClient[] observableClients = new CoapObservableClient[2];
        CoapGetPostClient[] clients = new CoapGetPostClient[2];

        observableClients[0] = new CoapObservableClient("pir");
        observableClients[1] = new CoapObservableClient("contact-dw");
        clients[0] = new CoapGetPostClient("light");
        clients[1] = new CoapGetPostClient( "alarm");

        observableClients[0].startObserve();
        observableClients[1].startObserve();

        observableClients[0].addDataListener(new CoapRequest() {
            @Override
            public void onDataChanged(String updatedValue) {
                System.out.println("PIR-UPDATED "+ updatedValue);
            }
        });

       observableClients[1].addDataListener(new CoapRequest() {
            @Override
            public void onDataChanged(String updatedValue) {
                System.out.println("CONTACT DW-UPDATED "+ updatedValue);
            }
        });

        try{
            Thread.sleep(300000);
        }catch (Exception e){}

    }
}
