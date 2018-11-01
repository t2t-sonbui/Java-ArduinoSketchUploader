package ArduinoUploader.BootloaderProgrammers;

import java.util.Arrays;

import ArduinoUploader.ArduinoSketchUploader;
import ArduinoUploader.ArduinoUploaderException;
import ArduinoUploader.Hardware.IMcu;
import ArduinoUploader.Hardware.Memory.IMemory;
import ArduinoUploader.IArduinoUploaderLogger;
import CSharpStyle.BitConverter;
import CSharpStyle.IProgress;
import IntelHexFormatReader.Model.MemoryBlock;
import IntelHexFormatReader.Model.MemoryCell;
import UsbSerialHelper.ISerialPortStream;

public abstract class BootloaderProgrammer<E extends ISerialPortStream> implements IBootloaderProgrammer<E> {
	protected final IArduinoUploaderLogger getLogger() {
		return ArduinoSketchUploader.getLogger();
	}

	private IMcu Mcu;

	protected final IMcu getMcu() {
		return Mcu;
	}

	protected BootloaderProgrammer(IMcu mcu) {
		Mcu = mcu;
	}

	public abstract void Open();

	public abstract void Close() throws InterruptedException;

	public abstract void EstablishSync();

	public abstract void CheckDeviceSignature();

	public abstract void InitializeDevice();

	public abstract void EnableProgrammingMode();

	public abstract void LeaveProgrammingMode();

	public abstract void LoadAddress(IMemory memory, int offset);

	public abstract void ExecuteWritePage(IMemory memory, int offset, byte[] bytes);

	public abstract byte[] ExecuteReadPage(IMemory memory);

	public void ProgramDevice(MemoryBlock memoryBlock) {
		ProgramDevice(memoryBlock, null);
	}

	public void ProgramDevice(MemoryBlock memoryBlock, IProgress<Double> progress) {
		int sizeToWrite = memoryBlock.getHighestModifiedOffset() + 1;
		ArduinoUploader.Hardware.Memory.IMemory flashMem = getMcu().getFlash();
		int pageSize = flashMem.getPageSize();
		if (getLogger() != null)
			getLogger().Info(String.format("Preparing to write %1$s bytes...", sizeToWrite));
		if (getLogger() != null)
			getLogger().Info(String.format("Flash page size: %1$s.", pageSize));

		int offset;
		for (offset = 0; offset < sizeToWrite; offset += pageSize) {
			if (getLogger() != null)
				progress.Report((double) offset / (sizeToWrite * 2));

			boolean needsWrite = false;
			for (int i = offset; i < offset + pageSize; i++) {
				if (!memoryBlock.getCells()[i].getModified()) {
					continue;
				}
				needsWrite = true;
				break;
			}
			if (needsWrite) {
//              var bytesToCopy = memoryBlock.Cells.Skip(offset).Take(pageSize).Select(x => x.Value).ToArray();
				MemoryCell[] orgMemoryCells = memoryBlock.getCells();
				MemoryCell[] memoryCells;
				if ((offset + pageSize) > orgMemoryCells.length)
					memoryCells = Arrays.copyOfRange(orgMemoryCells, offset, orgMemoryCells.length);
				else
					memoryCells = Arrays.copyOfRange(orgMemoryCells, offset, offset + pageSize);
				byte[] bytesToCopy = new byte[memoryCells.length];
				for (int i = 0; i < memoryCells.length; i++) {
					bytesToCopy[i] = memoryCells[i].getValue();
				}
				if (getLogger() != null)
					getLogger().Trace(String.format("Writing page at offset %1$s.", offset));
				LoadAddress(flashMem, offset);
				ExecuteWritePage(flashMem, offset, bytesToCopy);
			} else {
				if (getLogger() != null)
					getLogger().Trace("Skip writing page...");
			}
		}
		if (getLogger() != null)
			getLogger().Info(String.format("%1$s bytes written to flash memory!", sizeToWrite));
	}

	public void VerifyProgram(MemoryBlock memoryBlock) {
		VerifyProgram(memoryBlock, null);
	}

	// public virtual void VerifyProgram(MemoryBlock memoryBlock, IProgress<double>
	// progress = null)
	public void VerifyProgram(MemoryBlock memoryBlock, IProgress<Double> progress) {
		int sizeToVerify = memoryBlock.getHighestModifiedOffset() + 1;
		ArduinoUploader.Hardware.Memory.IMemory flashMem = getMcu().getFlash();
		int pageSize = flashMem.getPageSize();
		if (getLogger() != null)
			getLogger().Info(String.format("Preparing to verify %1$s bytes...", sizeToVerify));
		if (getLogger() != null)
			getLogger().Info(String.format("Flash page size: %1$s.", pageSize));
		int offset;
		for (offset = 0; offset < sizeToVerify; offset += pageSize) {
			if (progress != null)
				progress.Report((double) (sizeToVerify + offset) / (sizeToVerify * 2));
			if (getLogger() != null)
				getLogger().Debug(String.format("Executing verification of bytes @ address %1$s (page size %2$s)...",
						offset, pageSize));

//          var bytesToVerify = memoryBlock.Cells.Skip(offset).Take(pageSize).Select(x => x.Value).ToArray();
			MemoryCell[] orgMemoryCells = memoryBlock.getCells();
			MemoryCell[] memoryCells;
			if ((offset + pageSize) > orgMemoryCells.length)
				memoryCells = Arrays.copyOfRange(orgMemoryCells, offset, orgMemoryCells.length);
			else
				memoryCells = Arrays.copyOfRange(orgMemoryCells, offset, offset + pageSize);
			byte[] bytesToVerify = new byte[memoryCells.length];
			for (int i = 0; i < memoryCells.length; i++) {
				bytesToVerify[i] = memoryCells[i].getValue();
			}
			LoadAddress(flashMem, offset);
			// var bytesPresent = ExecuteReadPage(flashMem);
			byte[] bytesPresent = ExecuteReadPage(flashMem);
			// var succeeded = bytesToVerify.SequenceEqual(bytesPresent);
			boolean succeeded = Arrays.equals(bytesToVerify, bytesPresent);
			if (succeeded) {
				continue;
			}
			if (getLogger() != null)
				getLogger().Info(String.format("Expected: %1$s.", BitConverter.toString(bytesToVerify))
						+ String.format("%1$sRead after write: %2$s", System.getProperty("line.separator"),
								BitConverter.toString(bytesPresent)));
			throw new ArduinoUploaderException("Difference encountered during verification!");
		}
		if (getLogger() != null)
			getLogger().Info(String.format("%1$s bytes verified!", sizeToVerify));
	}

}