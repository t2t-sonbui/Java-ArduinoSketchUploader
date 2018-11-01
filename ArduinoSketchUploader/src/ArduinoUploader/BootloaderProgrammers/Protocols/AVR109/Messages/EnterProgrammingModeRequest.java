package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class EnterProgrammingModeRequest extends Request
{
	public EnterProgrammingModeRequest()
	{

		setBytes(new byte[] {Constants.CmdEnterProgrammingMode});
	}
}