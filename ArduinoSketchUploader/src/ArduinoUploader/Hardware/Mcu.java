package ArduinoUploader.Hardware;

import ArduinoUploader.Hardware.Memory.*;
import ArduinoUploader.*;

import java.util.*;

public abstract class Mcu implements IMcu {

	public abstract byte getDeviceCode();

	public abstract String getDeviceSignature();

	public abstract byte getDeviceRevision();

	public abstract byte getLockBytes();

	public abstract byte getFuseBytes();

	public abstract byte getTimeout();

	public abstract byte getStabDelay();

	public abstract byte getCmdExeDelay();

	public abstract byte getSynchLoops();

	public abstract byte getByteDelay();

	public abstract byte getPollValue();

	public abstract byte getPollIndex();

	public byte getProgType() {
		return 0;
	}

	public byte getParallelMode() {
		return 0;
	}

	public byte getPolling() {
		return 1;
	}

	public byte getSelfTimed() {
		return 1;
	}

	public abstract Map<Command, byte[]> getCommandBytes();

	public abstract List<IMemory> getMemory();

	// public IMemory Flash => Memory.SingleOrDefault(x => x.Type ==
	// MemoryType.Flash);
	// public IMemory Eeprom => Memory.SingleOrDefault(x => x.Type ==
	// MemoryType.Eeprom);
	public final IMemory getFlash() {
		
		for (IMemory memory : getMemory()) {
			if (memory.getType() == MemoryType.Flash)
				return memory;
		}
		return null;
	}

	public final IMemory getEeprom() {

		for (IMemory memory : getMemory()) {
			if (memory.getType() == MemoryType.Eeprom)
				return memory;
		}
		return null;
	}
}