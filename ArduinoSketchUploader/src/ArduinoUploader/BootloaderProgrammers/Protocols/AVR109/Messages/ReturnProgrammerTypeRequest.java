package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class ReturnProgrammerTypeRequest extends Request
{
	public ReturnProgrammerTypeRequest()
	{

		setBytes(new byte[] {Constants.CmdReturnProgrammerType});
	}
}