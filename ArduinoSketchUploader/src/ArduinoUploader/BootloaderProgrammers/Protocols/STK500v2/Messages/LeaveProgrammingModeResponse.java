package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.*;

public class LeaveProgrammingModeResponse extends Response
{
	public final boolean getSuccess()
	{
		return getBytes().length == 2 && getBytes()[0] == Constants.CmdLeaveProgmodeIsp && getBytes()[1] == Constants.StatusCmdOk;
	}
}