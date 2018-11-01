package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.*;

public class EnableProgrammingModeResponse extends Response {

    public final byte getAnswerId() {
        return getBytes()[0];
    }

    public final byte getStatus() {
        return getBytes()[1];
    }
}