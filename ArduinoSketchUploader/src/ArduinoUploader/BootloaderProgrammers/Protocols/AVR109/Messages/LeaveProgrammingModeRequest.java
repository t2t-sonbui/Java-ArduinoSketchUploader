package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class LeaveProgrammingModeRequest extends Request
{
	public LeaveProgrammingModeRequest()
	{

		setBytes(new byte[] {Constants.CmdLeaveProgrammingMode});
	}
}