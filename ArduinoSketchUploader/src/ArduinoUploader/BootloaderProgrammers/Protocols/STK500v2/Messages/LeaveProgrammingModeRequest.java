package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.*;

public class LeaveProgrammingModeRequest extends Request
{
	public LeaveProgrammingModeRequest()
	{

		setBytes(new byte[] {Constants.CmdLeaveProgmodeIsp, (byte) 0x01, (byte) 0x01});
	}
}