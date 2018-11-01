package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class CheckBlockSupportResponse extends Response
{
	public final boolean getHasBlockSupport()
	{

		return getBytes()[0] == (byte) 'Y';
	}

	public final int getBufferSize()
	{
		return (getBytes()[1] << 8) + getBytes()[2];
	}
}