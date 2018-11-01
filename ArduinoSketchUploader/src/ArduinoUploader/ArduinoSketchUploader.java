package ArduinoUploader;

import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.AVR109.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v1.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.*;
import ArduinoUploader.BootloaderProgrammers.ResetBehavior.*;
import ArduinoUploader.Config.*;
import ArduinoUploader.Hardware.*;
import CSharpStyle.IProgress;
import IntelHexFormatReader.*;
import IntelHexFormatReader.Model.*;
import IntelHexFormatReader.Utils.FileLineIterable;
import UsbSerialHelper.ISerialPortStream;
import csharpstyle.StringHelper;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ArduinoSketchUploader<E extends ISerialPortStream> {
	private static IArduinoUploaderLogger Logger;
	private Class<E> inferedClass;

	public static IArduinoUploaderLogger getLogger() {
		return Logger;
	}

	public static void setLogger(IArduinoUploaderLogger value) {
		Logger = value;
	}

	private ArduinoSketchUploaderOptions _options;
	private IProgress<Double> _progress;

	public ArduinoSketchUploader(ArduinoSketchUploaderOptions options, IArduinoUploaderLogger logger) {
		this(options, logger, null);
	}

	public ArduinoSketchUploader(ArduinoSketchUploaderOptions options) {
		this(options, null, null);
	}

	public ArduinoSketchUploader(ArduinoSketchUploaderOptions options, IArduinoUploaderLogger logger,
			IProgress<Double> progress) {
		setLogger(logger);
		if (getLogger() != null)
			getLogger().Info("Starting ArduinoSketchUploader...");
		_options = options;
		_progress = progress;
		if (inferedClass == null) {
			try {
				inferedClass = getGenericClass();
			} catch (ClassCastException e) {
				if (getLogger() != null)
					getLogger().Error("Mus created as anonymous implementation (new Generic<Integer>() {};)...", e);
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ArduinoSketchUploader(Class<E> clazz, ArduinoSketchUploaderOptions options, IArduinoUploaderLogger logger) {
		this(clazz, options, logger, null);
	}

	public ArduinoSketchUploader(Class<E> clazz, ArduinoSketchUploaderOptions options) {
		this(clazz, options, null, null);
	}

	public ArduinoSketchUploader(Class<E> clazz, ArduinoSketchUploaderOptions options, IArduinoUploaderLogger logger,
			IProgress<Double> progress) {
		setLogger(logger);
		if (getLogger() != null)
			getLogger().Info("Starting ArduinoSketchUploader...");
		_options = options;
		_progress = progress;
		inferedClass = clazz;

	}

	@SuppressWarnings("unchecked")
	public Class<E> getGenericClass() throws ClassNotFoundException {
		if (inferedClass == null) {
			Type mySuperclass = getClass().getGenericSuperclass();
			Type tType = ((ParameterizedType) mySuperclass).getActualTypeArguments()[0];
			String className = tType.toString().split(" ")[1];
			inferedClass = (Class<E>) Class.forName(className);
		}
		// this.inferedClass = ((Class<E>) ((ParameterizedType)
		// getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		return inferedClass;
	}

	public final void UploadSketch(String[] portNames) {
		String hexFileName = _options.getFileName();
		Iterable<String> hexFileContents = null;
		if (getLogger() != null)
			getLogger().Info(String.format("Starting upload process for file '%1$s'.", hexFileName));
		try {
			hexFileContents = new FileLineIterable(hexFileName);
		} catch (RuntimeException ex) {
			if (getLogger() != null)
				getLogger().Error(ex.getMessage(), ex);
			throw ex;
		} catch (IOException e) {
			e.printStackTrace();
		}

		UploadSketch(hexFileContents, portNames);
	}

	public final void UploadSketch(Iterable<String> hexFileContents, String[] allPortNames) {
		try {
			String serialPortName = _options.getPortName();

			Set<String> temp = new HashSet<String>(Arrays.asList(allPortNames));
			String[] uq = temp.toArray(new String[temp.size()]);
			List<String> distinctPorts = Arrays.asList(uq);
			// If we don't specify a COM port, automagically select one if there is only a
			// single match.

			final String portSingleOrDefault;
			if (distinctPorts.size() > 0) {
				portSingleOrDefault = distinctPorts.get(0);
			} else
				portSingleOrDefault = null;

			if (StringHelper.isNullOrWhiteSpace(serialPortName) && portSingleOrDefault != null) {
				if (getLogger() != null)
					getLogger().Info(String.format("Port autoselected: %1$s.", serialPortName));
				serialPortName = distinctPorts.get(0);
			}
			// Or else, check that we have an unambiguous match. Throw an exception
			// otherwise.
			else if ((allPortNames.length == 0) || !distinctPorts.contains(serialPortName)) {
				throw new ArduinoUploaderException(
						String.format("Specified COM port name '%1$s' is not valid.", serialPortName));
			}

			if (getLogger() != null)
				getLogger().Trace(String.format("Creating serial port '%1$s'...", serialPortName));
			ArduinoBootloaderProgrammer<E> programmer;
			IMcu mcu;
			String model = _options.getArduinoModel().toString();
			Configuration hardwareConfig = ReadConfiguration();
			Arduino modelOptions = null;
			Arduino[] tempOptions = hardwareConfig.getArduinos();
			for (Arduino arduino : tempOptions) {
				if (arduino.getModel().equalsIgnoreCase(model)) {
					modelOptions = arduino;
					break;
				}
			}
			if (modelOptions == null) {
				throw new ArduinoUploaderException(String.format("Unable to find configuration for '%1$s'!", model));
			}
			switch (modelOptions.getMcu()) {
			case AtMega1284:
				mcu = new AtMega1284();
				break;
			case AtMega2560:
				mcu = new AtMega2560();
				break;
			case AtMega32U4:
				mcu = new AtMega32U4();
				break;
			case AtMega328P:
				mcu = new AtMega328P();
				break;
			case AtMega168:
				mcu = new AtMega168();
				break;
			default:
				throw new ArduinoUploaderException(String.format("Unrecognized MCU: '%1$s'!", modelOptions.getMcu()));
			}

			IResetBehavior preOpenResetBehavior = ParseResetBehavior(modelOptions.getPreOpenResetBehavior());
			IResetBehavior postOpenResetBehavior = ParseResetBehavior(modelOptions.getPostOpenResetBehavior());
			IResetBehavior closeResetBehavior = ParseResetBehavior(modelOptions.getCloseResetBehavior());

			SerialPortConfig serialPortConfig = new SerialPortConfig(serialPortName, modelOptions.getBaudRate(),
					preOpenResetBehavior, postOpenResetBehavior, closeResetBehavior, modelOptions.getSleepAfterOpen(),
					modelOptions.getReadTimeout(), modelOptions.getWriteTimeout());

			switch (modelOptions.getProtocol()) {
			case Avr109:
				if (getLogger() != null)
					getLogger().Info("Protocol.Avr109");
				programmer = new Avr109BootloaderProgrammer<E>(serialPortConfig, mcu);
				break;
			case Stk500v1:
				if (getLogger() != null)
					getLogger().Info("Protocol.Stk500v1");
				programmer = new Stk500V1BootloaderProgrammer<E>(serialPortConfig, mcu);
				break;
			case Stk500v2:
				if (getLogger() != null)
					getLogger().Info("Protocol.Stk500v2");
				programmer = new Stk500V2BootloaderProgrammer<E>(serialPortConfig, mcu);
				break;
			default:
				throw new ArduinoUploaderException(
						String.format("Unrecognized protocol: '%1$s'!", modelOptions.getProtocol()));
			}

			try {
				if (getLogger() != null)
					getLogger().Info("Establishing memory block contents...");
				MemoryBlock memoryBlockContents = ReadHexFile(hexFileContents, mcu.getFlash().getSize());
				programmer.setClazz(inferedClass);
				programmer.Open();

				if (getLogger() != null)
					getLogger().Info("Establishing sync...");
				programmer.EstablishSync();
				if (getLogger() != null)
					getLogger().Info("Sync established.");

				if (getLogger() != null)
					getLogger().Info("Checking device signature...");
				programmer.CheckDeviceSignature();
				if (getLogger() != null)
					getLogger().Info("Device signature checked.");

				if (getLogger() != null)
					getLogger().Info("Initializing device...");
				programmer.InitializeDevice();
				if (getLogger() != null)
					getLogger().Info("Device initialized.");

				if (getLogger() != null)
					getLogger().Info("Enabling programming mode on the device...");
				programmer.EnableProgrammingMode();
				if (getLogger() != null)
					getLogger().Info("Programming mode enabled.");

				if (getLogger() != null)
					getLogger().Info("Programming device...");
				programmer.ProgramDevice(memoryBlockContents, _progress);
				if (getLogger() != null)
					getLogger().Info("Device programmed.");

				if (getLogger() != null)
					getLogger().Info("Verifying program...");
				programmer.VerifyProgram(memoryBlockContents, _progress);
				if (getLogger() != null)
					getLogger().Info("Verified program!");

				if (getLogger() != null)
					getLogger().Info("Leaving programming mode...");
				programmer.LeaveProgrammingMode();
				if (getLogger() != null)
					getLogger().Info("Left programming mode!");
			} finally {

				programmer.Close();

			}
			if (getLogger() != null)
				getLogger().Info("All done, shutting down!");
		} catch (RuntimeException ex) {
			if (getLogger() != null)
				getLogger().Error(ex.getMessage(), ex);
			throw ex;
		}
	}

	private static MemoryBlock ReadHexFile(Iterable<String> hexFileContents, int memorySize) {
		try {
			HexFileReader reader = new HexFileReader(hexFileContents, memorySize);
			return reader.Parse();
		} catch (RuntimeException ex) {
			if (getLogger() != null)
				getLogger().Error(ex.getMessage(), ex);
			throw ex;
		} catch (IOException e) {
			e.printStackTrace();

		}
		return null;
	}

	private static Configuration ReadConfiguration() {// Todo add new arduino dynamic
		Configuration hardwareConfig = new Configuration();
		List<Arduino> listArduino = new ArrayList<>();

		Arduino Leonardo = new Arduino(ArduinoModel.Leonardo.toString(), McuIdentifier.AtMega32U4, 57600,
				Protocol.Avr109);
		Leonardo.setPreOpenResetBehavior("1200bps");
		Leonardo.setSleepAfterOpen(0);
		Leonardo.setReadTimeout(1000);
		Leonardo.setWriteTimeout(1000);
		listArduino.add(Leonardo);

		Arduino Mega1284 = new Arduino(ArduinoModel.Mega1284.toString(), McuIdentifier.AtMega1284, 115200,
				Protocol.Stk500v1);
		Mega1284.setPreOpenResetBehavior("DTR;true");
		Mega1284.setCloseResetBehavior("DTR-RTS;250;50");
		Mega1284.setSleepAfterOpen(250);
		Mega1284.setReadTimeout(1000);
		Mega1284.setWriteTimeout(1000);
		listArduino.add(Mega1284);

		Arduino Mega2560 = new Arduino(ArduinoModel.Mega2560.toString(), McuIdentifier.AtMega2560, 115200,
				Protocol.Stk500v2);
		Mega2560.setPostOpenResetBehavior("DTR-RTS;50;250;true");
		Mega2560.setCloseResetBehavior("DTR-RTS;250;50;true");
		Mega2560.setSleepAfterOpen(250);
		Mega2560.setReadTimeout(1000);
		Mega2560.setWriteTimeout(1000);
		listArduino.add(Mega2560);

		Arduino Micro = new Arduino(ArduinoModel.Micro.toString(), McuIdentifier.AtMega32U4, 57600, Protocol.Avr109);
		Micro.setPreOpenResetBehavior("1200bps");
		Micro.setSleepAfterOpen(0);
		Micro.setReadTimeout(1000);
		Micro.setWriteTimeout(1000);
		listArduino.add(Micro);

		Arduino NanoR2 = new Arduino(ArduinoModel.NanoR2.toString(), McuIdentifier.AtMega168, 19200, Protocol.Stk500v1);
		NanoR2.setPreOpenResetBehavior("DTR;true");
		NanoR2.setCloseResetBehavior("DTR-RTS;250;50");
		NanoR2.setSleepAfterOpen(250);
		NanoR2.setReadTimeout(1000);
		NanoR2.setWriteTimeout(1000);
		listArduino.add(NanoR2);

		Arduino NanoR3 = new Arduino(ArduinoModel.NanoR3.toString(), McuIdentifier.AtMega328P, 57600,
				Protocol.Stk500v1);
		NanoR3.setPreOpenResetBehavior("DTR;true");
		NanoR3.setCloseResetBehavior("DTR-RTS;250;50");
		NanoR3.setSleepAfterOpen(250);
		NanoR3.setReadTimeout(1000);
		NanoR3.setWriteTimeout(1000);
		listArduino.add(NanoR3);

		Arduino UnoR3 = new Arduino(ArduinoModel.UnoR3.toString(), McuIdentifier.AtMega328P, 115200, Protocol.Stk500v1);
		UnoR3.setPreOpenResetBehavior("DTR;true");
		UnoR3.setCloseResetBehavior("DTR-RTS;50;250;false");
		UnoR3.setSleepAfterOpen(250);
		UnoR3.setReadTimeout(1000);
		UnoR3.setWriteTimeout(1000);
		listArduino.add(UnoR3);
		hardwareConfig.setArduinos(listArduino.toArray(new Arduino[listArduino.size()]));
		return hardwareConfig;
	}

	private IResetBehavior ParseResetBehavior(String resetBehavior) {
		if (resetBehavior == null) {
			return null;
		}
		if (resetBehavior.trim().equalsIgnoreCase("1200bps")) {
			return new ResetThrough1200BpsBehavior<E>(inferedClass);
		}

		String[] parts = resetBehavior.split(";", -1);
		int numberOfParts = parts.length;
		if (numberOfParts == 2 && parts[0].trim().equalsIgnoreCase("DTR")) {
			boolean flag = parts[1].trim().equalsIgnoreCase("true");
			return new ResetThroughTogglingDtrBehavior(flag);
		}

		if (numberOfParts < 3 || numberOfParts > 4) {
			throw new ArduinoUploaderException(
					String.format("Unexpected format (%1$s parts to '%2$s')!", numberOfParts, resetBehavior));
		}

		// Only DTR-RTS supported at this point...
		String type = parts[0];
		if (!type.equalsIgnoreCase("DTR-RTS")) {
			throw new ArduinoUploaderException(
					String.format("Unrecognized close reset behavior: '%1$s'!", resetBehavior));
		}

		int wait1, wait2;
		try {
			wait1 = Integer.parseInt(parts[1]);
		} catch (RuntimeException e) {
			throw new ArduinoUploaderException(String.format("Unrecognized Wait (1) in DTR-RTS: '%1$s'!", parts[1]));
		}

		try {
			wait2 = Integer.parseInt(parts[2]);
		} catch (RuntimeException e2) {
			throw new ArduinoUploaderException(String.format("Unrecognized Wait (2) in DTR-RTS: '%1$s'!", parts[2]));
		}

		boolean inverted = numberOfParts == 4 && parts[3].equalsIgnoreCase("true");
		return new ResetThroughTogglingDtrRtsBehavior(wait1, wait2, inverted);
	}

	private static IResetBehavior ParseCloseResetBehavior(String closeResetBehavior) {
		if (closeResetBehavior == null) {
			return null;
		}
		String[] parts = closeResetBehavior.split(";", -1);
		int numberOfParts = parts.length;
		if (numberOfParts < 3 || numberOfParts > 4) {
			throw new ArduinoUploaderException(
					String.format("Unexpected format (%1$s parts to '%2$s')!", numberOfParts, closeResetBehavior));
		}
		// Only DTR-RTS supported at this point...
		String type = parts[0];
		if (!type.equalsIgnoreCase("DTR-RTS")) {
			throw new ArduinoUploaderException(
					String.format("Unrecognized close reset behavior: '%1$s'!", closeResetBehavior));
		}

		int wait1, wait2;
		try {
			wait1 = Integer.parseInt(parts[1]);
		} catch (RuntimeException e) {
			throw new ArduinoUploaderException(String.format("Unrecognized Wait (1) in DTR-RTS: '%1$s'!", parts[1]));
		}

		try {
			wait2 = Integer.parseInt(parts[2]);
		} catch (RuntimeException e2) {
			throw new ArduinoUploaderException(String.format("Unrecognized Wait (2) in DTR-RTS: '%1$s'!", parts[2]));
		}

		boolean inverted = numberOfParts == 4 && parts[3].equalsIgnoreCase("true");
		return new ResetThroughTogglingDtrRtsBehavior(wait1, wait2, inverted);
	}

}