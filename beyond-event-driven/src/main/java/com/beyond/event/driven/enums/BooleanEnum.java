package com.beyond.event.driven.enums;

public enum BooleanEnum {

    FALSE(0),
    TRUE(1);

    private final byte value;

    BooleanEnum(final int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return this.value;
    }

    public boolean equals(Byte value) {
        if (value == null) {
            return false;
        }
        return this.value == value;
    }

    public static BooleanEnum from(boolean value) {
        return value ? TRUE : FALSE;
    }

}
