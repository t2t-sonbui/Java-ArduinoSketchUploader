package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class ReturnSoftwareVersionResponse extends Response
{
	public final char getMajorVersion()
	{
		return (char) getBytes()[0];
	}

	public final char getMinorVersion()
	{
		return (char) getBytes()[1];
	}
}