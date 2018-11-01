package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;

public final class Constants {

    public static final byte CmdSignOn = (byte)0x01;

    public static final byte CmdGetParameter = (byte)0x03;

    public static final byte CmdLoadAddress = (byte)0x06;

    public static final byte CmdEnterProgrmodeIsp = (byte)0x10;

    public static final byte CmdLeaveProgmodeIsp = (byte)0x11;

    public static final byte CmdProgramFlashIsp = (byte)0x13;

    public static final byte CmdReadFlashIsp = (byte)0x14;

    public static final byte CmdProgramEepromIsp = (byte)0x15;

    public static final byte CmdReadEepromIsp =(byte) 0x16;

    public static final byte CmdSpiMulti =(byte) 0x1d;


    public static final byte StatusCmdOk = (byte)0x00;

    public static final byte MessageStart =(byte) 0x1b;

    public static final byte Token =(byte) 0x0e;

    public static final byte ParamHwVer = (byte) 0x90;

    public static final byte ParamSwMajor = (byte) 0x91;

    public static final byte ParamSwMinor = (byte) 0x92;

    public static final byte ParamVTarget = (byte) 0x94;
}