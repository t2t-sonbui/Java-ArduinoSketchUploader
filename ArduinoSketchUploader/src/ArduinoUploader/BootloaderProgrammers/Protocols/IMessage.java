package ArduinoUploader.BootloaderProgrammers.Protocols;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;

public interface IMessage {
    byte[] getBytes();

    void setBytes(byte[] value);
}