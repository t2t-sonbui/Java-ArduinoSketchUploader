package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.*;

public class GetParameterResponse extends Response {
    public final boolean getIsSuccess() {
        return getBytes().length > 2 && getBytes()[0] == Constants.CmdGetParameter && getBytes()[1] == Constants.StatusCmdOk;
    }

    public final byte getParameterValue() {
        return getBytes()[2];
    }
}