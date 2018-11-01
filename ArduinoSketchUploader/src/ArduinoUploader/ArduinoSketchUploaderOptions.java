package ArduinoUploader;

import ArduinoUploader.Hardware.*;

public class ArduinoSketchUploaderOptions {
    private String FileName;

    public final String getFileName() {
        return FileName;
    }

    public final void setFileName(String value) {
        FileName = value;
    }

    private String PortName;

    public final String getPortName() {
        return PortName;
    }

    public final void setPortName(String value) {
        PortName = value;
    }

    private ArduinoModel ArduinoModel = getArduinoModel().values()[0];

    public final ArduinoModel getArduinoModel() {
        return ArduinoModel;
    }

    public final void setArduinoModel(ArduinoModel value) {
        ArduinoModel = value;
    }
}