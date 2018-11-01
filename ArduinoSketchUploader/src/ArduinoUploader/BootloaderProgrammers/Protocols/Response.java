package ArduinoUploader.BootloaderProgrammers.Protocols;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;

public abstract class Response implements IRequest {

    private byte[] Bytes;

    public final byte[] getBytes() {
        return Bytes;
    }

    public final void setBytes(byte[] value) {
        Bytes = value;
    }
}