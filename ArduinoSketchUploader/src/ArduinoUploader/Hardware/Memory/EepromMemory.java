package ArduinoUploader.Hardware.Memory;

import ArduinoUploader.*;
import ArduinoUploader.Hardware.*;

public class EepromMemory extends Memory {
    @Override
    public MemoryType getType() {
        return MemoryType.Eeprom;
    }
}