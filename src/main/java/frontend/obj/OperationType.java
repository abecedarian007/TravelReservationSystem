package frontend.obj;

import lombok.Getter;

@Getter
public enum OperationType {
    LOAD("LOAD"),
    UPDATE("UPDATE"),
    SUBMIT("SUBMIT"),
    BOOK("BOOK"),
    QUERY("QUERY");

    private final String buttonText;

    OperationType(String buttonText) {
        this.buttonText = buttonText;
    }

}

