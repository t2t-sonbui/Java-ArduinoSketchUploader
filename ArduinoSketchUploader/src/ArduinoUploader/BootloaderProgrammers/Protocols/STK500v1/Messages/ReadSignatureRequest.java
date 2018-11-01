package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.*;

public class ReadSignatureRequest extends Request
{
	public ReadSignatureRequest()
	{

		setBytes(new byte[] {Constants.CmdStkReadSignature, Constants.SyncCrcEop});
	}
}