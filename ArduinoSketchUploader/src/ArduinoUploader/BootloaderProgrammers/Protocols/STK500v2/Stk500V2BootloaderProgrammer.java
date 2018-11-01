package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2;

import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.Messages.*;
import ArduinoUploader.Hardware.*;
import ArduinoUploader.Hardware.Memory.*;
import UsbSerialHelper.ISerialPortStream;
import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;

import java.util.*;

public class Stk500V2BootloaderProgrammer<E extends ISerialPortStream> extends ArduinoBootloaderProgrammer<E> {
	private static byte _sequenceNumber;
	protected static byte LastCommandSequenceNumber;

	// private readonly IDictionary<MemoryType, byte> _readCommands = new
	// Dictionary<MemoryType, byte> { {MemoryType.Flash, Constants.CmdReadFlashIsp},
	// {MemoryType.Eeprom, Constants.CmdReadEepromIsp} };
	private final Map<MemoryType, Byte> _readCommands = new HashMap<MemoryType, Byte>() {
		{
			put(MemoryType.Flash, Constants.CmdReadFlashIsp);
			put(MemoryType.Eeprom, Constants.CmdReadEepromIsp);
		}
	};

	// private readonly IDictionary<MemoryType, byte> _writeCommands = new
	// Dictionary<MemoryType, byte> { {MemoryType.Flash,
	// Constants.CmdProgramFlashIsp}, {MemoryType.Eeprom,
	// Constants.CmdProgramEepromIsp} };
	private final Map<MemoryType, Byte> _writeCommands = new HashMap<MemoryType, Byte>() {
		{
			put(MemoryType.Flash, Constants.CmdProgramFlashIsp);
			put(MemoryType.Eeprom, Constants.CmdProgramEepromIsp);
		}
	};

	private String _deviceSignature;

	public Stk500V2BootloaderProgrammer(SerialPortConfig serialPortConfig, IMcu mcu) {
		super(serialPortConfig, mcu);
	}

	protected static byte getSequenceNumber() {
		if (_sequenceNumber == 255) {
			_sequenceNumber = 0;
		}
		return ++_sequenceNumber;
	}

	@Override
	protected void Send(IRequest request) {
		int requestBodyLength = request.getBytes().length;
		int totalMessageLength = requestBodyLength + 6;

		byte[] wrappedBytes = new byte[totalMessageLength];
		wrappedBytes[0] = Constants.MessageStart;
		wrappedBytes[1] = LastCommandSequenceNumber = getSequenceNumber();
		wrappedBytes[2] = (byte) (requestBodyLength >> 8);
		wrappedBytes[3] = (byte) (requestBodyLength & 0xFF);
		wrappedBytes[4] = Constants.Token;
		System.arraycopy(request.getBytes(), 0, wrappedBytes, 5, requestBodyLength);

		byte checksum = 0;
		for (int i = 0; i < totalMessageLength - 1; i++) {
			checksum ^= wrappedBytes[i];
		}
		wrappedBytes[totalMessageLength - 1] = checksum;

		request.setBytes(wrappedBytes);
		super.Send(request);
	}

	protected final <TResponse extends Response> TResponse Receive(TResponse responseType) {
//        var response = (TResponse) Activator.CreateInstance(typeof(TResponse));
		TResponse response = null;
		byte[] wrappedResponseBytes = new byte[300];
		// Discard bytes until we get a MessageStart
		final int maxRetries = 256;
		int retryCounter = 0;
		while (retryCounter++ < maxRetries) {
			int messageStart = ReceiveNext();
			if (messageStart == Constants.MessageStart) {
				break;
			}
		}
		if (retryCounter == maxRetries) {
			if (getLogger() != null)
				getLogger().Trace(String.format("No MESSAGE_START found after %1$s bytes!", maxRetries));
			return null;
		}
		wrappedResponseBytes[0] = Constants.MessageStart;

		int seqNumber = ReceiveNext();
		if (seqNumber != LastCommandSequenceNumber) {
			if (getLogger() != null)
				getLogger().Warn(CorruptWrapper(String.format("Wrong sequence number: %1$s - expected %2$s!", seqNumber,
						LastCommandSequenceNumber)));
			return null;
		}
		wrappedResponseBytes[1] = _sequenceNumber;
		int messageSizeHighByte = ReceiveNext();
		if (messageSizeHighByte == -1) {
			if (getLogger() != null)
				getLogger().Warn(CorruptWrapper("Timeout ocurred!"));
			return null;
		}
		wrappedResponseBytes[2] = (byte) messageSizeHighByte;
		int messageSizeLowByte = ReceiveNext();
		if (messageSizeLowByte == -1) {
			if (getLogger() != null)
				getLogger().Warn(CorruptWrapper("Timeout ocurred!"));
			return null;
		}
		wrappedResponseBytes[3] = (byte) messageSizeLowByte;
		int messageSize = (messageSizeHighByte << 8) + messageSizeLowByte;
		int token = ReceiveNext();
		if (token != Constants.Token) {
			if (getLogger() != null)
				getLogger().Warn(CorruptWrapper("Token not received!"));
			return null;
		}
		wrappedResponseBytes[4] = (byte) token;

		byte[] payload = ReceiveNext(messageSize);
		if (payload == null) {
			if (getLogger() != null)
				getLogger().Warn(CorruptWrapper("Inner message not received!"));
			return null;
		}

		System.arraycopy(payload, 0, wrappedResponseBytes, 5, messageSize);

		int responseCheckSum = ReceiveNext();
		if (responseCheckSum == -1) {
			if (getLogger() != null)
				getLogger().Warn(CorruptWrapper("Checksum not received!"));
			return null;
		}
		wrappedResponseBytes[5 + messageSize] = (byte) responseCheckSum;
		byte checksum = 0;
		for (int i = 0; i < 5 + messageSize; i++) {
			checksum ^= wrappedResponseBytes[i];
		}
		if (responseCheckSum != checksum) {
			if (getLogger() != null)
				getLogger().Warn(CorruptWrapper("Checksum incorrect!"));
			return null;
		}
		byte[] message = new byte[messageSize];
		System.arraycopy(wrappedResponseBytes, 5, message, 0, messageSize);
		try {
			Class clazz = responseType.getClass();
			response = (TResponse) clazz.newInstance();
			response.setBytes(message);
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			return response;
		}

	}

	@Override
	public void EstablishSync() {
		Send(new GetSyncRequest());
//        GetSyncResponse result = this.<GetSyncResponse>Receive();
		GetSyncResponse getSyncResponse = new GetSyncResponse();
		GetSyncResponse result = Receive(getSyncResponse);
		if (result == null || !result.getIsInSync()) {
			throw new ArduinoUploaderException("Unable to establish sync!");
		}
		_deviceSignature = result.getSignature();
	}

	@Override
	public void CheckDeviceSignature() {
		if (getLogger() != null)
			getLogger().Debug(String.format("Expecting to find '%1$s'...", getMcu().getDeviceSignature()));
		if (!_deviceSignature.equals(getMcu().getDeviceSignature())) {
			throw new ArduinoUploaderException(
					String.format("Unexpected device signature - found '%1$s'", _deviceSignature)
							+ String.format("- expected '%1$s'.", getMcu().getDeviceSignature()));
		}
	}

	@Override
	public void InitializeDevice() {
		int hardwareVersion = GetParameterValue(Constants.ParamHwVer);
		int softwareMajor = GetParameterValue(Constants.ParamSwMajor);
		int softwareMinor = GetParameterValue(Constants.ParamSwMinor);

		int vTarget = GetParameterValue(Constants.ParamVTarget);
		if (getLogger() != null)
			getLogger().Info(String.format("Retrieved software version: %1$s (hardware) ", hardwareVersion)
					+ String.format("- %1$s.%2$s (software).", softwareMajor, softwareMinor));
		if (getLogger() != null)
			getLogger().Info(String.format("Parameter VTarget: %1$s.", vTarget));
	}

	@Override
	public void EnableProgrammingMode() {
		Send(new EnableProgrammingModeRequest(getMcu()));
//        EnableProgrammingModeResponse response = this.<EnableProgrammingModeResponse>Receive();
		EnableProgrammingModeResponse enableProgrammingModeResponse = new EnableProgrammingModeResponse();
		EnableProgrammingModeResponse response = Receive(enableProgrammingModeResponse);
		if (response == null || response.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to enable programming mode on the device!");
		}
		// TODO: properly decode SPI instructions...
		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x30, 0x00, 0x00, 0x00 }));
//        ExecuteSpiCommandResponse spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		ExecuteSpiCommandResponse executeSpiCommandResponse = new ExecuteSpiCommandResponse();
		ExecuteSpiCommandResponse spiResponse = Receive(executeSpiCommandResponse);
		if (spiResponse == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}

		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x30, 0x00, 0x01, 0x00 }));
		// Tao cai moi
//        spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		spiResponse = Receive(executeSpiCommandResponse);
		if (response == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}

		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x30, 0x00, 0x02, 0x00 }));
//        spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		spiResponse = Receive(executeSpiCommandResponse);
		if (response == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}
		// --- Fuses
		// LFuse
		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x50, 0x00, 0x00, 0x00 }));
//        spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		spiResponse = Receive(executeSpiCommandResponse);
		if (spiResponse == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}

		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x50, 0x00, 0x00, 0x00 }));
//        spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		spiResponse = Receive(executeSpiCommandResponse);
		if (spiResponse == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}

		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x50, 0x00, 0x00, 0x00 }));
//        spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		spiResponse = Receive(executeSpiCommandResponse);
		if (spiResponse == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}

		// HFuse

		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x58, 0x08, 0x00, 0x00 }));
//        spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		spiResponse = Receive(executeSpiCommandResponse);
		if (spiResponse == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}

		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x58, 0x08, 0x00, 0x00 }));
//        spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		spiResponse = Receive(executeSpiCommandResponse);
		if (spiResponse == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}

		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x58, 0x08, 0x00, 0x00 }));
		// spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		spiResponse = Receive(executeSpiCommandResponse);
		if (spiResponse == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}

		// EFuse

		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x50, 0x08, 0x00, 0x00 }));
		// spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		spiResponse = Receive(executeSpiCommandResponse);
		if (spiResponse == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}

		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x50, 0x08, 0x00, 0x00 }));
		// spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		spiResponse = Receive(executeSpiCommandResponse);
		if (spiResponse == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}

		Send(new ExecuteSpiCommandRequest((byte) 4, (byte) 4, (byte) 0, new byte[] { 0x50, 0x08, 0x00, 0x00 }));
		// spiResponse = this.<ExecuteSpiCommandResponse>Receive();
		spiResponse = Receive(executeSpiCommandResponse);
		if (spiResponse == null || spiResponse.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Unable to execute SPI command!");
		}
	}

	@Override
	public void LeaveProgrammingMode() {
		Send(new LeaveProgrammingModeRequest());
//        LeaveProgrammingModeResponse response = this.<LeaveProgrammingModeResponse>Receive();
		LeaveProgrammingModeResponse leaveProgrammingModeResponse = new LeaveProgrammingModeResponse();
		LeaveProgrammingModeResponse response = Receive(leaveProgrammingModeResponse);
		if (response == null) {
			throw new ArduinoUploaderException("Unable to leave programming mode on the device!");
		}
	}

	@Override
	public void ExecuteWritePage(IMemory memory, int offset, byte[] bytes) {
		if (getLogger() != null)
			getLogger().Trace("Sending execute write page request for offset "
					+ String.format("%1$s (%2$s bytes)...", offset, bytes.length));
		byte writeCmd = _writeCommands.get(memory.getType());
		Send(new ExecuteProgramPageRequest(writeCmd, memory, bytes));
//        ExecuteProgramPageResponse response = this.<ExecuteProgramPageResponse>Receive();
		ExecuteProgramPageResponse executeProgramPageResponse = new ExecuteProgramPageResponse();
		ExecuteProgramPageResponse response = Receive(executeProgramPageResponse);

		if (response == null || response.getAnswerId() != writeCmd || response.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException(
					String.format("Executing write page request at offset %1$s failed!", offset));
		}
	}

	@Override
	public byte[] ExecuteReadPage(IMemory memory) {
		byte readCmd = _readCommands.get(memory.getType());
		Send(new ExecuteReadPageRequest(readCmd, memory));
//        ExecuteReadPageResponse response = this.<ExecuteReadPageResponse>Receive();
		ExecuteReadPageResponse executeReadPageResponse = new ExecuteReadPageResponse();
		ExecuteReadPageResponse response = Receive(executeReadPageResponse);
		if (response == null || response.getAnswerId() != readCmd || response.getStatus() != Constants.StatusCmdOk) {
			throw new ArduinoUploaderException("Executing read page request failed!");
		}

		byte[] responseBytes = new byte[memory.getPageSize()];
		System.arraycopy(response.getBytes(), 2, responseBytes, 0, responseBytes.length);
		return responseBytes;
	}

	@Override
	public void LoadAddress(IMemory memory, int offset) {
		if (getLogger() != null)
			getLogger().Trace(String.format("Sending load address request: %02X.", offset));
//C# TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
		offset = offset >> 1;
		Send(new LoadAddressRequest(memory, offset));
//        LoadAddressResponse response = this.<LoadAddressResponse>Receive();
		LoadAddressResponse loadAddressResponse = new LoadAddressResponse();
		LoadAddressResponse response = Receive(loadAddressResponse);
		if (response == null || !response.getSucceeded()) {
			throw new ArduinoUploaderException("Unable to execute load address!");
		}
	}

	private int GetParameterValue(byte param) {
		if (getLogger() != null)
			getLogger().Trace(String.format("Retrieving parameter '%02X'...", param));
		Send(new GetParameterRequest(param));
//        GetParameterResponse response = this.<GetParameterResponse>Receive();
		GetParameterResponse getParameterResponse = new GetParameterResponse();
		GetParameterResponse response = Receive(getParameterResponse);
		if (response == null || !response.getIsSuccess()) {
			throw new ArduinoUploaderException(String.format("Retrieving parameter '%1$s' failed!", param));
		}
		return response.getParameterValue();
	}

	private static String CorruptWrapper(String message) {
		return String.format("STK500V2 wrapper corrupted (%1$s)!", message);
	}
}