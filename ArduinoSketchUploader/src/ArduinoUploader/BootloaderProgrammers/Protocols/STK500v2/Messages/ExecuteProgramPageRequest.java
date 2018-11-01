package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.Messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ArduinoUploader.Hardware.Memory.*;
import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.*;

public class ExecuteProgramPageRequest extends Request {

    //ORIGINAL LINE: internal ExecuteProgramPageRequest(byte writeCmd, IMemory memory, IReadOnlyCollection<byte> data)
    public ExecuteProgramPageRequest(byte writeCmd, IMemory memory, final byte[] data) {
        int len = data.length;
        final byte mode = (byte) 0xc1;
        byte[] headerBytes = new byte[]{writeCmd, (byte) (len >> 8), (byte) (len & 0xff), mode, memory.getDelay(), memory.getCmdBytesWrite()[0], memory.getCmdBytesWrite()[1], memory.getCmdBytesRead()[0], memory.getPollVal1(), memory.getPollVal2()};
        setBytes(Concat(headerBytes, data));
    }

    static byte[] Concat(byte[] a, byte[] b) {
        byte[] output = new byte[a.length + b.length];
        for (int i = 0; i < a.length; i++)
            output[i] = a[i];
        for (int j = 0; j < b.length; j++)
            output[a.length + j] = b[j];
        return output;
    }
}