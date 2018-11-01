package UsbSerialHelper;

public interface ISerialPortStream {

	// Parity Values
	static final public int NO_PARITY = 0;
	static final public int ODD_PARITY = 1;
	static final public int EVEN_PARITY = 2;
	static final public int MARK_PARITY = 3;
	static final public int SPACE_PARITY = 4;

	// Number of Stop Bits
	static final public int ONE_STOP_BIT = 1;
	static final public int ONE_POINT_FIVE_STOP_BITS = 2;
	static final public int TWO_STOP_BITS = 3;

	// Flow Control constants
	static final public int FLOW_CONTROL_DISABLED = 0x00000000;
	static final public int FLOW_CONTROL_RTS_ENABLED = 0x00000001;
	static final public int FLOW_CONTROL_CTS_ENABLED = 0x00000010;
	static final public int FLOW_CONTROL_DSR_ENABLED = 0x00000100;
	static final public int FLOW_CONTROL_DTR_ENABLED = 0x00001000;
	static final public int FLOW_CONTROL_XONXOFF_IN_ENABLED = 0x00010000;
	static final public int FLOW_CONTROL_XONXOFF_OUT_ENABLED = 0x00100000;

	// Sync for read and write with timeout

	String getPortName();// Comport is using

	String[] getPortNames();// List Comport name

	void setBaudRate​(int newBaudRate);

	void setComPortTimeouts​(int newReadTimeout, int newWriteTimeout);

	void setReadTimeout(int miliseconds);

	void setWriteTimeout(int miliseconds);

	void open();

	void close();

	void setDtrEnable(boolean enable);

	void setRtsEnable(boolean enable);

	void setNumDataBits​(int newDataBits);

	void setNumStopBits​(int newStopBits);

	void setParity​(int newParity);

	int readBytes​(byte[] buffer, long bytesToRead); // Reads up to bytesToRead raw data bytes from the serial port and
														// stores them in the buffer.

	int readBytes​(byte[] buffer, long bytesToRead, long offset); // Reads up to bytesToRead raw data bytes from the
																	// serial port and stores them in the buffer
																	// starting at the indicated offset.

	int writeBytes​(byte[] buffer, long bytesToWrite);// Writes up to bytesToWrite raw data bytes from the buffer
														// parameter to the serial port.

	int writeBytes​(byte[] buffer, long bytesToWrite, long offset);// Writes up to bytesToWrite raw data bytes from the
																	// buffer parameter to the serial port starting at
																	// the indicated offset.
	 void DiscardInBuffer();//clear buffer input
}
