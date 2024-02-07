package backend.entity;

import lombok.Getter;

@Getter
public enum ObjectType {
    FLIGHT(1),
    HOTEL(2),
    BUS(3),
    CUSTOMER(4),
    RESERVATION(5),
    RESERVATION_DETAIL(6);

    private final int value;

    ObjectType(int value) {
        this.value = value;
    }

    public static ObjectType valueOf(int value) {
        for (ObjectType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid ReservationType value: " + value);
    }

}
