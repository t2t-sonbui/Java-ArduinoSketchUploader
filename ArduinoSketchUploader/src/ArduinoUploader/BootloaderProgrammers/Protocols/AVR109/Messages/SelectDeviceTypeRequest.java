package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class SelectDeviceTypeRequest extends Request
{

	public SelectDeviceTypeRequest(byte deviceCode)
	{

		setBytes(new byte[] {Constants.CmdSelectDeviceType, deviceCode});
	}
}