package pixels.pro.fit.mapper;

public class ErrorWrapper<T> {
    private T error;

    public ErrorWrapper(T data) {
        this.error = data;
    }

    public T getData() {
        return error;
    }

    public void setData(T data) {
        this.error = data;
    }
}
