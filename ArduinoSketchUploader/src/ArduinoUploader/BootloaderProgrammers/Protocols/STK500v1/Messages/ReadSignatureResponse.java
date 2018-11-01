package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.*;

public class ReadSignatureResponse extends Response
{
	public final boolean getIsCorrectResponse()
	{
		return getBytes().length == 4 && getBytes()[3] == Constants.RespStkOk;
	}

	public final byte[] getSignature()
	{

		return new byte[] {getBytes()[0], getBytes()[1], getBytes()[2]};
	}
}