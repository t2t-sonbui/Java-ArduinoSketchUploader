package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.*;

public class GetParameterRequest extends Request {

    public GetParameterRequest(byte param) {

        setBytes(new byte[]{Constants.CmdStkGetParameter, param, Constants.SyncCrcEop});
    }
}