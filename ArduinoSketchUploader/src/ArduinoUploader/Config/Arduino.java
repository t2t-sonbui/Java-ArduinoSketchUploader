package ArduinoUploader.Config;

import ArduinoUploader.*;

public class Arduino {

    private String Model;

    public final String getModel() {
        return Model;
    }

    public final void setModel(String value) {
        Model = value;
    }

    private McuIdentifier Mcu = McuIdentifier.values()[0];

    public final McuIdentifier getMcu() {
        return Mcu;
    }

    public final void setMcu(McuIdentifier value) {
        Mcu = value;
    }

    private int BaudRate;

    public final int getBaudRate() {
        return BaudRate;
    }

    public final void setBaudRate(int value) {
        BaudRate = value;
    }

    private String PreOpenResetBehavior;

    public final String getPreOpenResetBehavior() {
        return PreOpenResetBehavior;
    }

    public final void setPreOpenResetBehavior(String value) {
        PreOpenResetBehavior = value;
    }

    private String PostOpenResetBehavior;

    public final String getPostOpenResetBehavior() {
        return PostOpenResetBehavior;
    }

    public final void setPostOpenResetBehavior(String value) {
        PostOpenResetBehavior = value;
    }

    private String CloseResetBehavior;

    public final String getCloseResetBehavior() {
        return CloseResetBehavior;
    }

    public final void setCloseResetBehavior(String value) {
        CloseResetBehavior = value;
    }

    private int SleepAfterOpen = 0;

    public final int getSleepAfterOpen() {
        return SleepAfterOpen;
    }

    public final void setSleepAfterOpen(int value) {
        SleepAfterOpen = value;
    }

    private int ReadTimeout = 1000;

    public final int getReadTimeout() {
        return ReadTimeout;
    }

    public final void setReadTimeout(int value) {
        ReadTimeout = value;
    }

    private int WriteTimeout = 1000;

    public final int getWriteTimeout() {
        return WriteTimeout;
    }

    public final void setWriteTimeout(int value) {
        WriteTimeout = value;
    }

    private Protocol Protocol = getProtocol().values()[0];

    public final Protocol getProtocol() {
        return Protocol;
    }

    public final void setProtocol(Protocol value) {
        Protocol = value;
    }

    public Arduino(String model, McuIdentifier mcu, int baudRate, String preOpenResetBehavior, String postOpenResetBehavior, String closeResetBehavior, int sleepAfterOpen, int readTimeout, int writeTimeout, ArduinoUploader.Config.Protocol protocol) {
        Model = model;
        Mcu = mcu;
        BaudRate = baudRate;
        PreOpenResetBehavior = preOpenResetBehavior;
        PostOpenResetBehavior = postOpenResetBehavior;
        CloseResetBehavior = closeResetBehavior;
        SleepAfterOpen = sleepAfterOpen;
        ReadTimeout = readTimeout;
        WriteTimeout = writeTimeout;
        Protocol = protocol;
    }

    public Arduino(String model, McuIdentifier mcu, int baudRate, ArduinoUploader.Config.Protocol protocol) {
        Model = model;
        Mcu = mcu;
        BaudRate = baudRate;
        Protocol = protocol;
    }
}
