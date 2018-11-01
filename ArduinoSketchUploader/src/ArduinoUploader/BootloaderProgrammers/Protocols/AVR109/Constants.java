package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;

public final class Constants {

    public static final byte Null = (byte)0x00;


    public static final byte CarriageReturn =(byte)0x0d;


    public static final byte CmdSetAddress =(byte) 0x41;

    public static final byte CmdStartBlockLoad = (byte)0x42;

    public static final byte CmdExitBootloader =(byte) 0x45;

    public static final byte CmdLeaveProgrammingMode = (byte)0x4c;

    public static final byte CmdEnterProgrammingMode = (byte)0x50;

    public static final byte CmdReturnSoftwareIdentifier =(byte) 0x53;

    public static final byte CmdSelectDeviceType =(byte) 0x54;

    public static final byte CmdReturnSoftwareVersion =(byte) 0x56;

    public static final byte CmdCheckBlockSupport =(byte) 0x62;

    public static final byte CmdStartBlockRead =(byte) 0x67;

    public static final byte CmdReturnProgrammerType =(byte) 0x70;

    public static final byte CmdReadSignatureBytes = (byte)0x73;

    public static final byte CmdReturnSupportedDeviceCodes =(byte) 0x74;
}