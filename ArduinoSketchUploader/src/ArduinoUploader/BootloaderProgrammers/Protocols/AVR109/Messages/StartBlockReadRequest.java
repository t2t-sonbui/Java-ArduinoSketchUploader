package ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.Messages;

import ArduinoUploader.Hardware.Memory.*;
import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;

public class StartBlockReadRequest extends Request
{
	public StartBlockReadRequest(MemoryType memType, int blockSize)
	{
		setBytes(new byte[] {Constants.CmdStartBlockRead, (byte)(blockSize >> 8), (byte)(blockSize & 0xff), (byte)(memType == MemoryType.Flash ? 'F' : 'E')});
	}
}