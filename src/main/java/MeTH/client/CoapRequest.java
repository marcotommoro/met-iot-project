package MeTH.client;

import java.util.Map;

public interface CoapRequest {
    String base = "coap://127.0.0.1:";

    Map<String, String> LOCALHOSTS = Map.of("GARAGE", base+"5683/", "KITCHEN", base+"5684/");

    public void onDataChanged(String updatedValue);
}
