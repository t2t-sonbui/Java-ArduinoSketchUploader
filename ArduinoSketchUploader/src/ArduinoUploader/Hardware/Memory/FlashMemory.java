package ArduinoUploader.Hardware.Memory;

import ArduinoUploader.*;
import ArduinoUploader.Hardware.*;

public class FlashMemory extends Memory {
    @Override
    public MemoryType getType() {
        return MemoryType.Flash;
    }
}