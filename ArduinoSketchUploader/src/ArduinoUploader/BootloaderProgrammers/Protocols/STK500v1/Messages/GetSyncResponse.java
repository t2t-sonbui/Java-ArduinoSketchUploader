package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.*;

public class GetSyncResponse extends Response
{
	public final boolean getIsInSync()
	{
		return getBytes().length > 0 && getBytes()[0] == Constants.RespStkInsync;
	}
}