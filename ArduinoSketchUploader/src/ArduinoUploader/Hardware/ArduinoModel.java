package ArduinoUploader.Hardware;

import ArduinoUploader.*;

public enum ArduinoModel {
    Leonardo,
    Mega1284,
    Mega2560,
    Micro,
    NanoR2,
    NanoR3,
    UnoR3;

    public static final int SIZE = java.lang.Integer.SIZE;

    public int getValue() {
        return this.ordinal();
    }

    public static ArduinoModel forValue(int value) {
        return values()[value];
    }
}