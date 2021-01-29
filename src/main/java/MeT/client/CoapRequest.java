package MeT.client;

public interface CoapRequest {
    String LOCALHOST = "coap://127.0.0.1:5683/";

    public void onDataChanged(String updatedValue);
}
