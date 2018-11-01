package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.*;

public class GetParameterRequest extends Request
{

	public GetParameterRequest(byte param)
	{

		setBytes(new byte[] {Constants.CmdGetParameter, param});
	}
}