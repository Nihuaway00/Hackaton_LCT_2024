package pixels.pro.fit.mapper;

public class ResponseWrapper<T> {
    private T data;

    public ResponseWrapper(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
