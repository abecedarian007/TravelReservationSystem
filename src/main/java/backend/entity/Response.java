package backend.entity;

import lombok.Getter;

public class Response {
    private final Boolean success;
    @Getter
    private final String message;

    public Response() {
        this.success = null;
        this.message = null;
    }

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return Boolean.TRUE.equals(success);
    }

}
