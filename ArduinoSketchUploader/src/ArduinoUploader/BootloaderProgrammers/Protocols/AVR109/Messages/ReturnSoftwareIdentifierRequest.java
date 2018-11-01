package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class ReturnSoftwareIdentifierRequest extends Request
{
	public ReturnSoftwareIdentifierRequest()
	{

		setBytes(new byte[] {Constants.CmdReturnSoftwareIdentifier});
	}
}