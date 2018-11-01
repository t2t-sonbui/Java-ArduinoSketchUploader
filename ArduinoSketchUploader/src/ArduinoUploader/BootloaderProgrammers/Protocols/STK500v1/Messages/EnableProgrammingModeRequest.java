package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.Messages;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.*;

public class EnableProgrammingModeRequest extends Request
{
	public EnableProgrammingModeRequest()
	{
		setBytes(new byte[] {Constants.CmdStkEnterProgmode, Constants.SyncCrcEop});
	}
}