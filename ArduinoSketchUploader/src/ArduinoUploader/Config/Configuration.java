package ArduinoUploader.Config;

import ArduinoUploader.*;

public class Configuration {
    private Arduino[] Arduinos;

    public final Arduino[] getArduinos() {
        return Arduinos;
    }

    public final void setArduinos(Arduino[] value) {
        Arduinos = value;
    }
}