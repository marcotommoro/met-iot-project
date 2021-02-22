package MeTH.server.resources.raw;

public interface ResourceDataListener<T> {
    public void onDataChanged(SmartObjectResource<T> resource, T updatedValue);
}
