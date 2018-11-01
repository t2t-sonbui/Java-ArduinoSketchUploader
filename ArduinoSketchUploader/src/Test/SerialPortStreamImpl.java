package Test;

import java.util.ArrayList;
import java.util.List;

import com.fazecast.jSerialComm.SerialPort;

import UsbSerialHelper.ISerialPortStream;

public class SerialPortStreamImpl implements ISerialPortStream {

	protected String portName;
	protected int baudRate;
	protected final SerialPort serialPort;
	private int readTimeout = 0;
	private int writeTimeout = 0;

	public SerialPortStreamImpl(String port, int baud) {
		this.portName = port;
		this.baudRate = baud;
		this.serialPort = SerialPort.getCommPort(port);
		serialPort.setBaudRate(baud);
	}

	public SerialPortStreamImpl(String port) {
		this(port, 115200);
	}

	@Override
	public String[] getPortNames() {
		List<String> portNames = new ArrayList<>();
		SerialPort[] serialPorts = SerialPort.getCommPorts();
		for (SerialPort port : serialPorts) {
			portNames.add(port.getSystemPortName());
			System.out.println(port.getSystemPortName());
		}
		return portNames.toArray(new String[portNames.size()]);
	}
	@Override
	public String getPortName() {
		return this.portName;
	}

	@Override
	public void setBaudRate​(int newBaudRate) {
		serialPort.setBaudRate(newBaudRate);
	}

	@Override
	public void setComPortTimeouts​(int newReadTimeout, int newWriteTimeout) {
		readTimeout = newReadTimeout;
		writeTimeout = newWriteTimeout;
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, readTimeout, writeTimeout);
	}

	@Override
	public void setReadTimeout(int miliseconds) {
		readTimeout = miliseconds;
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, readTimeout, writeTimeout);
	}

	@Override
	public void setWriteTimeout(int miliseconds) {
		writeTimeout = miliseconds;
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, readTimeout, writeTimeout);
	}

	@Override
	public void open() {
		serialPort.openPort();
	}

	@Override
	public void close() {
		serialPort.closePort();
	}

	@Override
	public void setDtrEnable(boolean enable) {
		if (enable)
			serialPort.setDTR();
		else
			serialPort.clearDTR();
	}

	@Override
	public void setRtsEnable(boolean enable) {
		if (enable)
			serialPort.setRTS();
		else
			serialPort.clearRTS();
	}

	public void setNumDataBits​(int newDataBits) {
		serialPort.setNumDataBits(newDataBits);
	}

	@Override
	public void setNumStopBits​(int newStopBits) {
		serialPort.setNumStopBits(newStopBits);
	};

	@Override
	public void setParity​(int newParity) {
		serialPort.setParity(newParity);
	};

	@Override
	public int readBytes​(byte[] buffer, long bytesToRead) {
		return serialPort.readBytes(buffer, bytesToRead);
	}

	@Override
	public int readBytes​(byte[] buffer, long bytesToRead, long offset) {
		return serialPort.readBytes(buffer, bytesToRead, offset);
	}

	@Override
	public int writeBytes​(byte[] buffer, long bytesToWrite) {
		return serialPort.writeBytes(buffer, bytesToWrite);
	}

	@Override
	public int writeBytes​(byte[] buffer, long bytesToWrite, long offset) {
		return serialPort.writeBytes(buffer, bytesToWrite, offset);
	}

	@Override
	public void DiscardInBuffer() {
		// TODO Auto-generated method stub
		
	}

	

}
