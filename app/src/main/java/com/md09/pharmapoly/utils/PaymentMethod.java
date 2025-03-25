package com.md09.pharmapoly.utils;

public enum PaymentMethod {
    COD("COD"),
    ONLINE("ONLINE");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentMethod fromString(String value) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.value.equalsIgnoreCase(value)) {
                return method;
            }
        }
        return COD;
    }
}
