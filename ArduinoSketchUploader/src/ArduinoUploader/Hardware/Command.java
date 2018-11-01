package ArduinoUploader.Hardware;

import ArduinoUploader.*;

public enum Command
{
	PgmEnable,
	ReadFlash,
	ReadEeprom;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static Command forValue(int value)
	{
		return values()[value];
	}
}