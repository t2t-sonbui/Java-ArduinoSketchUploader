package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class ReturnSoftwareVersionRequest extends Request
{
	public ReturnSoftwareVersionRequest()
	{

		setBytes(new byte[] {Constants.CmdReturnSoftwareVersion});
	}
}