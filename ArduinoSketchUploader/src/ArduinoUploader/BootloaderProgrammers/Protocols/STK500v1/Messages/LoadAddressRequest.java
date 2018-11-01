package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.*;

public class LoadAddressRequest extends Request
{
	public LoadAddressRequest(int address)
	{

		setBytes(new byte[] {Constants.CmdStkLoadAddress, (byte)(address & 0xff), (byte)((address >> 8) & 0xff), Constants.SyncCrcEop});
	}
}