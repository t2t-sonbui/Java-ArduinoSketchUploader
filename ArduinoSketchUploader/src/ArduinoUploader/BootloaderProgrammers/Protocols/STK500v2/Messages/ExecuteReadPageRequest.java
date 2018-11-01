package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.Messages;

import ArduinoUploader.Hardware.Memory.*;
import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.*;

public class ExecuteReadPageRequest extends Request {

    public ExecuteReadPageRequest(byte readCmd, IMemory memory) {
        int pageSize = memory.getPageSize();
        byte cmdByte = memory.getCmdBytesRead()[0];
        setBytes(new byte[]{readCmd, (byte) (pageSize >> 8), (byte) (pageSize & 0xff), cmdByte});
    }
}