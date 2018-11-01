package Test;

import java.util.ArrayList;
import java.util.List;

import com.fazecast.jSerialComm.SerialPort;

import ArduinoUploader.ArduinoSketchUploader;
import ArduinoUploader.ArduinoSketchUploaderOptions;
import ArduinoUploader.ArduinoUploaderException;
import ArduinoUploader.IArduinoUploaderLogger;
import ArduinoUploader.Hardware.ArduinoModel;
import CSharpStyle.IProgress;

public class Test {

	public static void main(String[] args) {

		IArduinoUploaderLogger logger = new IArduinoUploaderLogger() {

			@Override
			public void Error(String message, Exception exception) {
				System.out.println("Error:" + message);
			}

			@Override
			public void Warn(String message) {
				System.out.println("Warn:" + message);
			}

			@Override
			public void Info(String message) {
				System.out.println("Info:" + message);
			}

			@Override
			public void Debug(String message) {
				System.out.println("Debug:" + message);
			}

			@Override
			public void Trace(String message) {
				System.out.println("Trace:" + message);
			}
		};
		ArduinoSketchUploaderOptions optionsMega = new ArduinoSketchUploaderOptions();
		optionsMega.setArduinoModel(ArduinoModel.Mega2560);
//		optionsMega.setPortName("COM9");// Real
		optionsMega.setFileName("C:\\Users\\sonbu\\OneDrive\\Desktop\\Eclipse upload\\Hex\\Blink.ino.mega.hex");

		ArduinoSketchUploaderOptions optionsUno = new ArduinoSketchUploaderOptions();
		optionsUno.setArduinoModel(ArduinoModel.UnoR3);
//			options.setPortName("COM9");// Real comport name or null for first auto
		
		optionsUno.setFileName("C:\\Users\\sonbu\\OneDrive\\Desktop\\Eclipse upload\\Hex\\Big.uno.hex");
		//optionsUno.setFileName("..\\..\\..\\Hex\\Big.uno.hex");
		IProgress<Double> progress = new IProgress<Double>() {

			@Override
			public void Report(Double value) {
				System.out.println(String.format("Upload progress: %1$,3.2f%%", value * 100));

			}
		};
//		ArduinoSketchUploader<SerialPortStreamImpl> uploader = new ArduinoSketchUploader<SerialPortStreamImpl>(optionsMega, logger, progress) {};

		ArduinoSketchUploader<SerialPortStreamImpl> uploader = new ArduinoSketchUploader<SerialPortStreamImpl>(
				SerialPortStreamImpl.class, optionsUno, logger, progress);
		try {

			List<String> portNames = new ArrayList<>();
			SerialPort[] serialPorts = SerialPort.getCommPorts();
			for (SerialPort port : serialPorts) {
				portNames.add(port.getSystemPortName());
				System.out.println(port.getSystemPortName());
			}
			uploader.UploadSketch(portNames.toArray(new String[portNames.size()]));

		} catch (ArduinoUploaderException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			System.out.println("Unexpected exception!");
			ex.printStackTrace();
			ex.printStackTrace();

		}

	}

}
