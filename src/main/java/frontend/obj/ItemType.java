package frontend.obj;

public enum ItemType {
    FLIGHT("flight", "FLIGHT", "Flight"),
    HOTEL("hotel", "HOTEL", "Hotel"),
    BUS("bus", "BUS", "Bus"),
    CUSTOMER("customer", "CUSTOMER", "Customer"),
    RESERVATION("reservation", "RESERVATION", "Reservation"),
    RESERVATION_DETAIL("reservation(detail)", "RESERVATION(DETAIL)", "Reservation(Detail)");

    private final String lowerCase;
    private final String upperCase;
    private final String camelCase;

    ItemType(String lowerCase, String upperCase, String camelCase) {
        this.lowerCase = lowerCase;
        this.upperCase = upperCase;
        this.camelCase = camelCase;
    }

    public static ItemType fromString(String value) {
        for (ItemType type : ItemType.values()) {
            if (type.lowerCase.equals(value.toLowerCase()) ||
                    type.upperCase.equals(value.toUpperCase()) ||
                    type.camelCase.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
