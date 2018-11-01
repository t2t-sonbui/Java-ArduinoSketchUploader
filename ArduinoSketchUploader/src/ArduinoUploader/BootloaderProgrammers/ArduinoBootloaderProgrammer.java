package ArduinoUploader.BootloaderProgrammers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeoutException;

import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.Messages.GetSyncResponse;
import ArduinoUploader.Hardware.*;
import CSharpStyle.BitConverter;
import UsbSerialHelper.ISerialPortStream;
import UsbSerialHelper.SerialStreamHelper;
import ArduinoUploader.*;

public abstract class ArduinoBootloaderProgrammer<E extends ISerialPortStream> extends BootloaderProgrammer<E> {
	protected SerialPortConfig SerialPortConfig;

	private Class<E> inferedClass;

	protected ArduinoBootloaderProgrammer(SerialPortConfig serialPortConfig, IMcu mcu) {
		super(mcu);
		SerialPortConfig = serialPortConfig;

	}

	protected ArduinoBootloaderProgrammer(Class<E> clazz, SerialPortConfig serialPortConfig, IMcu mcu) {
		this(serialPortConfig, mcu);
		inferedClass = clazz;
	}

	private E SerialPort;

	protected final E getSerialPort() {
		return SerialPort;
	}

	protected final void setSerialPort(E value) {
		SerialPort = value;
	}

	public void setClazz(Class<E> clazz) {
		inferedClass = clazz;
	}

	@Override
	public void Open() {
		String portName = SerialPortConfig.getPortName();
		int baudRate = SerialPortConfig.getBaudRate();
		if (getLogger() != null)
			getLogger().Info(String.format("Opening serial port %1$s - baudrate %2$s", portName, baudRate));

		// setSerialPort(new SerialPortStream(portName, baudRate));
		setSerialPort(SerialStreamHelper.newInstance(inferedClass, portName, baudRate));
		getSerialPort().setReadTimeout(SerialPortConfig.getReadTimeOut());
		getSerialPort().setWriteTimeout(SerialPortConfig.getWriteTimeOut());

		ArduinoUploader.BootloaderProgrammers.ResetBehavior.IResetBehavior preOpen = SerialPortConfig
				.getPreOpenResetBehavior();
		if (preOpen != null) {
			if (getLogger() != null)
				getLogger().Info(String.format("Executing Post Open behavior (%1$s)...", preOpen));
			setSerialPort((E) preOpen.Reset(getSerialPort(), SerialPortConfig));
		}
		try {
			getSerialPort().open();
		} catch (IllegalStateException ex) {
			throw new ArduinoUploaderException(
					String.format("Unable to open serial port %1$s - %2$s.", portName, ex.getMessage()));
		}
		if (getLogger() != null)
			getLogger().Trace(String.format("Opened serial port %1$s with baud rate %2$s!", portName, baudRate));

		ArduinoUploader.BootloaderProgrammers.ResetBehavior.IResetBehavior postOpen = SerialPortConfig
				.getPostOpenResetBehavior();
		if (postOpen != null) {
			if (getLogger() != null)
				getLogger().Info(String.format("Executing Post Open behavior (%1$s)...", postOpen));
			setSerialPort((E) postOpen.Reset(getSerialPort(), SerialPortConfig));
		}

		int sleepAfterOpen = SerialPortConfig.getSleepAfterOpen();
		if (SerialPortConfig.getSleepAfterOpen() <= 0) {
			return;
		}

		if (getLogger() != null)
			getLogger().Trace(String.format("Sleeping for %1$s ms after open...", sleepAfterOpen));
		try {
			Thread.sleep(sleepAfterOpen);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int newInstance(String portName, int baudRate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void EstablishSync() {
		// Do nothing.
	}

	@Override
	public void Close() {
		ArduinoUploader.BootloaderProgrammers.ResetBehavior.IResetBehavior preClose = SerialPortConfig
				.getCloseResetAction();
		if (preClose != null) {
			if (getLogger() != null)
				getLogger().Info("Resetting...");
			setSerialPort((E) preClose.Reset(getSerialPort(), SerialPortConfig));
		}

		if (getLogger() != null)
			getLogger().Info("Closing serial port...");
		getSerialPort().setDtrEnable(false);
		getSerialPort().setRtsEnable(false);

		try {
			getSerialPort().close();
		} catch (RuntimeException e) {
			e.printStackTrace();
			// Ignore
		}
	}

	protected void Send(IRequest request) {
		byte[] bytes = request.getBytes();
		int length = bytes.length;
		if (getLogger() != null)
			getLogger().Trace(String.format("Sending %1$s bytes: %2$s", length, System.getProperty("line.separator"))
					+ String.format("%1$s", BitConverter.toString(bytes)));
		// getSerialPort().Write(bytes, 0, length);//) la offset tu 0
		getSerialPort().writeBytes​(bytes, length, 0);
	}

	protected <TResponse extends Response> TResponse Receive(TResponse responseType) {
		return Receive(responseType, 1);
	}

	// https://stackoverflow.com/questions/2619429/c-sharp-to-java-where-t-new-syntax
	
	// //C#
	// public E MakeForm<E>() where E : Form { }
	// MyFormType form = MakeForm<MyFormType>();
	//
	//// Java
	// public <E extends Form> E makeForm(Class<E> formType) {
	// //return new instance of formType
	// }
	// makeForm(SomeForm.class).specificSomeFormMethod();

	@SuppressWarnings("unchecked")
	protected final <TResponse extends Response> TResponse Receive(TResponse responseType, int length) {
		TResponse tempVar = null;
		byte[] bytes = ReceiveNext(length);
		if (bytes == null) {
			return null;
		}
		try {
			Class clazz = responseType.getClass();
			tempVar = (TResponse) clazz.newInstance();
			// tempVar = clazz.getConstructor(String.class).newInstance(p);//su dung khi can
			// them bien dau vao de khoi tao
			tempVar.setBytes(bytes);
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			return tempVar;
		}

	}

	protected final int ReceiveNext() {
		byte[] bytes = new byte[1];
		try {
			// getSerialPort().Read(bytes, 0, 1);//0 la offset
			int numRead = getSerialPort().readBytes​(bytes, 1, 0);
			if (numRead == 0)
				throw new TimeoutException();
			if (getLogger() != null)
				getLogger().Trace(String.format("Receiving byte: %1$s", BitConverter.toString(bytes)));

			return bytes[0];
		} catch (TimeoutException ex) {
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	protected final byte[] ReceiveNext(int length) {
		byte[] bytes = new byte[length];
		int retrieved = 0;
		try {
			while (retrieved < length) {
				// retrieved += getSerialPort().Read(bytes, retrieved, length -
				// retrieved);//retrieved la offset
				int numRead = getSerialPort().readBytes​(bytes, length - retrieved, retrieved);
				if (numRead == 0)
					throw new TimeoutException();

				retrieved += numRead;
			}
			if (getLogger() != null)
				getLogger().Trace(String.format("Receiving bytes: %1$s", BitConverter.toString(bytes)));
			return bytes;
		} catch (TimeoutException ex) {
			  throw new ArduinoUploaderException("Timeout read data!");
		} 

	}

}