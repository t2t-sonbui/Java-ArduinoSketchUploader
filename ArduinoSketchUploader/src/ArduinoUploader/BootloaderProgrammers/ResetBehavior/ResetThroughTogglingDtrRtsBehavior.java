package ArduinoUploader.BootloaderProgrammers.ResetBehavior;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import UsbSerialHelper.ISerialPortStream;

public class ResetThroughTogglingDtrRtsBehavior implements IResetBehavior {
	private static IArduinoUploaderLogger getLogger() {
		return ArduinoSketchUploader.getLogger();
	}

	private int Wait1;

	private int getWait1() {
		return Wait1;
	}

	private int Wait2;

	private int getWait2() {
		return Wait2;
	}

	private boolean Invert;

	private boolean getInvert() {
		return Invert;
	}

	public ResetThroughTogglingDtrRtsBehavior(int wait1, int wait2) {
		this(wait1, wait2, false);
	}

	public ResetThroughTogglingDtrRtsBehavior(int wait1, int wait2, boolean invert) {
		Wait1 = wait1;
		Wait2 = wait2;
		Invert = invert;
	}

	@Override
	public final ISerialPortStream Reset(ISerialPortStream serialPort, SerialPortConfig config) {
		if (getLogger() != null)
			getLogger().Trace("Toggling DTR/RTS...");
		serialPort.setDtrEnable(getInvert());
		serialPort.setRtsEnable(getInvert());
		try {
			Thread.sleep(getWait1());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		serialPort.setDtrEnable(!getInvert());
		serialPort.setRtsEnable(!getInvert());

		try {
			Thread.sleep(getWait2());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return serialPort;
	}
}