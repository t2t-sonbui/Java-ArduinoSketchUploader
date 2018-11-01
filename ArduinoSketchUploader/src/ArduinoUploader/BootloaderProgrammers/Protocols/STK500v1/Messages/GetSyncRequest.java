package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.*;

public class GetSyncRequest extends Request
{
	public GetSyncRequest()
	{
		setBytes(new byte[] {Constants.CmdStkGetSync, Constants.SyncCrcEop});
	}
}