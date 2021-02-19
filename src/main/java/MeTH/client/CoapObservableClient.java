package MeTH.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.californium.core.coap.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CoapObservableClient extends CoapClient implements CoapRequest {
    private static Logger logger =  LoggerFactory.getLogger(CoapObservableClient.class);

    CoapObserveRelation relation;
    Request request;
    private List<CoapRequest> resourceListenerList;
    private String useCase;
    private String sensor;

    public CoapObservableClient(String url, String useCase) {
        super(LOCALHOSTS.get(useCase.toUpperCase(Locale.ROOT)) + url);
        System.out.println(LOCALHOSTS.get(useCase.toUpperCase(Locale.ROOT)) + url);
        this.useCase = useCase;
        this.sensor = url.toUpperCase(Locale.ROOT);
        this.relation = null;
        this.request = Request.newGet().setURI(LOCALHOSTS.get(useCase.toUpperCase(Locale.ROOT)) + url).setObserve();
        this.request.setConfirmable(true);
        this.resourceListenerList = new ArrayList<>();
    }

    public void startObserve(){
        if (relation != null) return;
        System.out.println("comincio osservazione");
        relation = this.observe(this.request, new CoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                String content = coapResponse.getResponseText();
                System.out.println("ciao" + content);
                if (content != null && content.length() > 0) notifyUpdate(content);
            }

            @Override
            public void onError() {
                logger.error("OBSERVING FAILED");
            }
        });
    }

    public void stopObserve(){
    }

    @Override
    public void onDataChanged(String updatedValue) {}

    protected void notifyUpdate(String updatedValue){
        if(this.resourceListenerList != null && this.resourceListenerList.size() > 0)
            this.resourceListenerList.forEach(resourceDataListener -> {
                if(resourceDataListener != null)
                    resourceDataListener.onDataChanged( updatedValue);
            });
    }

    public void addDataListener(CoapRequest listener){
        if(this.resourceListenerList != null)
            this.resourceListenerList.add(listener);
    }

    public String getUseCase() {
        return useCase;
    }

    public String getSensor() {
        return sensor;
    }
}
