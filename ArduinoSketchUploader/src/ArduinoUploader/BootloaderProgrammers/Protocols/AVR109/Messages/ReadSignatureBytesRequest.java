package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class ReadSignatureBytesRequest extends Request
{
	public ReadSignatureBytesRequest()
	{

		setBytes(new byte[] {Constants.CmdReadSignatureBytes});
	}
}