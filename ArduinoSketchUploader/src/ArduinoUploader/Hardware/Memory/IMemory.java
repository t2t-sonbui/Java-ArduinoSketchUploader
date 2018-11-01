package ArduinoUploader.Hardware.Memory;

import ArduinoUploader.*;
import ArduinoUploader.Hardware.*;

public interface IMemory {
    MemoryType getType();

    int getSize();

    int getPageSize();

    byte getPollVal1();

    byte getPollVal2();

    byte getDelay();

    byte[] getCmdBytesRead();

    byte[] getCmdBytesWrite();
}