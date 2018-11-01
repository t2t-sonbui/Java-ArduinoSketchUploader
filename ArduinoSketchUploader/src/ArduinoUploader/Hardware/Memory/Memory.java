package ArduinoUploader.Hardware.Memory;

import ArduinoUploader.*;
import ArduinoUploader.Hardware.*;

public abstract class Memory implements IMemory {
    public abstract MemoryType getType();

    private int Size;

    public final int getSize() {
        return Size;
    }

    public final void setSize(int value) {
        Size = value;
    }

    private int PageSize;

    public final int getPageSize() {
        return PageSize;
    }

    public final void setPageSize(int value) {
        PageSize = value;
    }

    private byte PollVal1;

    public final byte getPollVal1() {
        return PollVal1;
    }

    public final void setPollVal1(byte value) {
        PollVal1 = value;
    }

    private byte PollVal2;

    public final byte getPollVal2() {
        return PollVal2;
    }

    public final void setPollVal2(byte value) {
        PollVal2 = value;
    }

    private byte Delay;

    public final byte getDelay() {
        return Delay;
    }

    public final void setDelay(byte value) {
        Delay = value;
    }

    private byte[] CmdBytesRead;

    public final byte[] getCmdBytesRead() {
        return CmdBytesRead;
    }

    public final void setCmdBytesRead(byte[] value) {
        CmdBytesRead = value;
    }

    private byte[] CmdBytesWrite;

    public final byte[] getCmdBytesWrite() {
        return CmdBytesWrite;
    }

    public final void setCmdBytesWrite(byte[] value) {
        CmdBytesWrite = value;
    }
}