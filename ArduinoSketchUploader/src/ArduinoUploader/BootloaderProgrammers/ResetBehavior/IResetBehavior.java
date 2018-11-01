package ArduinoUploader.BootloaderProgrammers.ResetBehavior;

import ArduinoUploader.BootloaderProgrammers.*;
import UsbSerialHelper.ISerialPortStream;

public interface IResetBehavior
{
	ISerialPortStream Reset(ISerialPortStream serialPort, SerialPortConfig config);
}