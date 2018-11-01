package ArduinoUploader.Config;

import ArduinoUploader.*;


public enum Protocol {
    Stk500v1,
    Stk500v2,
    Avr109;

    public static final int SIZE = java.lang.Integer.SIZE;

    public int getValue() {
        return this.ordinal();
    }

    public static Protocol forValue(int value) {
        return values()[value];
    }
}