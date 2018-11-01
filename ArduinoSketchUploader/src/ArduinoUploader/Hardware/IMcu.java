package ArduinoUploader.Hardware;

import ArduinoUploader.Hardware.Memory.*;
import ArduinoUploader.*;

import java.util.*;

public interface IMcu {
    byte getDeviceCode();

    String getDeviceSignature();

    byte getDeviceRevision();

    byte getProgType();

    byte getParallelMode();

    byte getPolling();

    byte getSelfTimed();

    byte getLockBytes();

    byte getFuseBytes();

    byte getTimeout();

    byte getStabDelay();

    byte getCmdExeDelay();

    byte getSynchLoops();

    byte getByteDelay();

    byte getPollValue();

    byte getPollIndex();

    Map<Command, byte[]> getCommandBytes();

    List<IMemory> getMemory();

    IMemory getFlash();

    IMemory getEeprom();
}