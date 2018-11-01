package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class ReadSignatureBytesResponse extends Response
{

	public final byte[] getSignature()
	{

		return new byte[] {getBytes()[2], getBytes()[1], getBytes()[0]};
	}
}