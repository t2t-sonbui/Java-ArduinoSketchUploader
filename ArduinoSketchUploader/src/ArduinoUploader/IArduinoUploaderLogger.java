package ArduinoUploader;

public interface IArduinoUploaderLogger {
    void Error(String message, Exception exception);

    void Warn(String message);

    void Info(String message);

    void Debug(String message);

    void Trace(String message);
}