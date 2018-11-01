package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;

public final class Constants {

    public static final byte CmdStkGetSync = (byte)0x30;

    public static final byte CmdStkGetParameter = (byte)0x41;

    public static final byte CmdStkSetDevice = (byte)0x42;

    public static final byte CmdStkEnterProgmode = (byte)0x50;

    public static final byte CmdStkLeaveProgmode = (byte)0x51;

    public static final byte CmdStkLoadAddress = (byte)0x55;

    public static final byte CmdStkProgPage = (byte)0x64;

    public static final byte CmdStkReadPage = (byte)0x74;

    public static final byte CmdStkReadSignature = (byte)0x75;

    public static final byte SyncCrcEop = (byte)0x20;


    public static final byte RespStkOk = (byte)0x10;

    public static final byte RespStkFailed = (byte)0x11;

    public static final byte RespStkNodevice = (byte)0x13;

    public static final byte RespStkInsync = (byte)0x14;

    public static final byte RespStkNosync = (byte)0x15;


    public static final byte ParmStkSwMajor = (byte) 0x81;

    public static final byte ParmStkSwMinor = (byte) 0x82;
}