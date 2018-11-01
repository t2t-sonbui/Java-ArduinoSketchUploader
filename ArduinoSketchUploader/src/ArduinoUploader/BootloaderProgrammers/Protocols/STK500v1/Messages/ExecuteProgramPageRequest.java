package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.Messages;

import ArduinoUploader.Hardware.Memory.*;
import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.*;

public class ExecuteProgramPageRequest extends Request {

    public ExecuteProgramPageRequest(IMemory memory, byte[] bytesToCopy) {
        int size = bytesToCopy.length;
        setBytes(new byte[size + 5]);
        int i = 0;
        getBytes()[i++] = Constants.CmdStkProgPage;
//C# TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
//ORIGINAL LINE: Bytes[i++] = (byte)((size >> 8) & 0xff);
        getBytes()[i++] = (byte) ((size >> 8) & 0xff);
        getBytes()[i++] = (byte) (size & 0xff);
        getBytes()[i++] = (byte) (memory.getType() == MemoryType.Eeprom ? 'E' : 'F');
        System.arraycopy(bytesToCopy, 0, getBytes(), i, size);
        i += size;
        getBytes()[i] = Constants.SyncCrcEop;
    }
}