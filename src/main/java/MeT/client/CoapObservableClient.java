package MeT.client;

import MeT.server.resources.raw.ResourceDataListener;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.californium.core.coap.Request;

import java.util.ArrayList;
import java.util.List;


public class CoapObservableClient extends CoapClient implements CoapRequest {
    private static Logger logger =  LoggerFactory.getLogger(CoapObservableClient.class);

    CoapObserveRelation relation;
    Request request;
    private List<CoapRequest> resourceListenerList;

    public CoapObservableClient(String url) {
        super(LOCALHOST + url);
        relation = null;
        this.request = Request.newGet().setURI(LOCALHOST + url).setObserve();
        this.request.setConfirmable(true);
        this.resourceListenerList = new ArrayList<>();
        System.out.println(LOCALHOST + url);
    }

    public void startObserve(){
        if (relation != null) return;
        relation = this.observe(this.request, new CoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                String content = coapResponse.getResponseText();
                System.out.println("test-"+content+"---");
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
        else
            logger.error("Empty or Null Resource Data Listener ! Nothing to notify ...");
    }

    public void addDataListener(CoapRequest listener){
        if(this.resourceListenerList != null)
            this.resourceListenerList.add(listener);
    }
}
