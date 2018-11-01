package ArduinoUploader.Hardware.Memory;

import ArduinoUploader.*;
import ArduinoUploader.Hardware.*;

public enum MemoryType {
    Flash,
    Eeprom;

    public static final int SIZE = java.lang.Integer.SIZE;

    public int getValue() {
        return this.ordinal();
    }

    public static MemoryType forValue(int value) {
        return values()[value];
    }
}