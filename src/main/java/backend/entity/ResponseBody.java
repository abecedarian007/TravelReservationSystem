package backend.entity;

import lombok.Getter;

@Getter
public class ResponseBody<T> extends Response {
    private final T responseObject;

    public ResponseBody(boolean success, String message, T responseObject) {
        super(success, message);
        this.responseObject = responseObject;
    }

}
