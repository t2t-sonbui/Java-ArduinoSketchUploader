package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class SetAddressRequest extends Request
{
	public SetAddressRequest(int offset)
	{

		setBytes(new byte[] {Constants.CmdSetAddress, (byte)((offset >> 8) & 0xff), (byte)(offset & 0xff)});
	}
}