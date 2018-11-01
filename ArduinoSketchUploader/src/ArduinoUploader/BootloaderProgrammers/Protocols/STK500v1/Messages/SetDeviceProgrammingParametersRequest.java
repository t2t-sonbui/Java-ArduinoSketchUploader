package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.Messages;

import ArduinoUploader.Hardware.*;
import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.*;

public class SetDeviceProgrammingParametersRequest extends Request {
    public SetDeviceProgrammingParametersRequest(IMcu mcu) {
        ArduinoUploader.Hardware.Memory.IMemory flashMem = mcu.getFlash();
        ArduinoUploader.Hardware.Memory.IMemory eepromMem = mcu.getEeprom();
        int flashPageSize = flashMem.getPageSize();
        int flashSize = flashMem.getSize();
        int epromSize = eepromMem.getSize();
        
        System.out.println(mcu.getClass().getSimpleName()+"-flashPageSize: " + flashPageSize);
        System.out.println(mcu.getClass().getSimpleName()+"-flashSize: " + flashSize);
        System.out.println(mcu.getClass().getSimpleName()+"-epromSize: " + epromSize);

        setBytes(new byte[22]);
        getBytes()[0] = Constants.CmdStkSetDevice;
        getBytes()[1] = mcu.getDeviceCode();
        getBytes()[2] = mcu.getDeviceRevision();
        getBytes()[3] = mcu.getProgType();
        getBytes()[4] = mcu.getParallelMode();
        getBytes()[5] = mcu.getPolling();
        getBytes()[6] = mcu.getSelfTimed();
        getBytes()[7] = mcu.getLockBytes();
        getBytes()[8] = mcu.getFuseBytes();
        getBytes()[9] = flashMem.getPollVal1();
        getBytes()[10] = flashMem.getPollVal2();
        getBytes()[11] = eepromMem.getPollVal1();
        getBytes()[12] = eepromMem.getPollVal2();
        getBytes()[13] = (byte) ((flashPageSize >> 8) & 0x00ff);
        getBytes()[14] = (byte) (flashPageSize & 0x00ff);
        getBytes()[15] = (byte) ((epromSize >> 8) & 0x00ff);
        getBytes()[16] = (byte) (epromSize & 0x00ff);
        getBytes()[17] = (byte) ((flashSize >> 24) & 0xff);
        getBytes()[18] = (byte) ((flashSize >> 16) & 0xff);
        getBytes()[19] = (byte) ((flashSize >> 8) & 0xff);
        getBytes()[20] = (byte) (flashSize & 0xff);
        getBytes()[21] = Constants.SyncCrcEop;
    }
}