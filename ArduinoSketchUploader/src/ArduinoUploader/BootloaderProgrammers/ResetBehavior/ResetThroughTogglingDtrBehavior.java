package ArduinoUploader.BootloaderProgrammers.ResetBehavior;



import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import UsbSerialHelper.ISerialPortStream;

public class ResetThroughTogglingDtrBehavior implements IResetBehavior {
	private boolean Toggle;

	private boolean getToggle() {
		return Toggle;
	}

	public ResetThroughTogglingDtrBehavior(boolean toggle) {
		Toggle = toggle;
	}

	@Override
	public final ISerialPortStream Reset(ISerialPortStream serialPort, SerialPortConfig config) {
		serialPort.setDtrEnable(getToggle());
		return serialPort;
	}
}