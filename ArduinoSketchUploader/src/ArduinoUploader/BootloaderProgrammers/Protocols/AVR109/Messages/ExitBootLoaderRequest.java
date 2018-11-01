package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class ExitBootLoaderRequest extends Request
{
	public ExitBootLoaderRequest()
	{

		setBytes(new byte[] {Constants.CmdExitBootloader});
	}
}