package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.Messages;

import ArduinoUploader.Hardware.*;
import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.*;

public class EnableProgrammingModeRequest extends Request {
    public EnableProgrammingModeRequest(IMcu mcu) {

        byte[] cmdBytes = mcu.getCommandBytes().get(Command.PgmEnable);
        setBytes(new byte[]{Constants.CmdEnterProgrmodeIsp, mcu.getTimeout(), mcu.getStabDelay(), mcu.getCmdExeDelay(), mcu.getSynchLoops(), mcu.getByteDelay(), mcu.getPollValue(), mcu.getPollIndex(), cmdBytes[0], cmdBytes[1], cmdBytes[2], cmdBytes[3]});
    }
}