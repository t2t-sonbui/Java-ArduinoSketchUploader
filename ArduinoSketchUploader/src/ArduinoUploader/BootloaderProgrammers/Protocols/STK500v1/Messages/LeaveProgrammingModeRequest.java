package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.*;

public class LeaveProgrammingModeRequest extends Request
{
	public LeaveProgrammingModeRequest()
	{

		setBytes(new byte[] {Constants.CmdStkLeaveProgmode, Constants.SyncCrcEop});
	}
}