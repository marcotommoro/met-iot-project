package MeTH.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainCoapObservable {
    private static Logger logger = LoggerFactory.getLogger(MainCoapObservable.class);

    private static final String USECASE = "GARAGE";

    public static void main(String[] args){
        CoapObservableClient[] observableClients = new CoapObservableClient[2];

        observableClients[0] = new CoapObservableClient("pir", USECASE);
        observableClients[1] = new CoapObservableClient("contact-dw", USECASE);

        for (CoapObservableClient client : observableClients) {
            client.startObserve();
            client.addDataListener(new CoapRequest() {
                @Override
                public void onDataChanged(String updatedValue) {
                    System.out.println(client.getUseCase() + " - " + client.getSensor() + " updated: " +  updatedValue);
                }
            });
        }

        try{
            Thread.sleep(300000);
        }catch (Exception e){}

    }
}
