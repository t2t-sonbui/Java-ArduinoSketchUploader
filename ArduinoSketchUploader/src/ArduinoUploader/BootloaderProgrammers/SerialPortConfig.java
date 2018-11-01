package ArduinoUploader.BootloaderProgrammers;

import ArduinoUploader.BootloaderProgrammers.ResetBehavior.*;
import ArduinoUploader.*;

public class SerialPortConfig {
    private static final int DefaultTimeout = 1000;


    public SerialPortConfig(String portName, int baudRate, IResetBehavior preOpenResetBehavior, IResetBehavior postOpenResetBehavior, IResetBehavior closeResetAction, int sleepAfterOpen, int readTimeout) {
        this(portName, baudRate, preOpenResetBehavior, postOpenResetBehavior, closeResetAction, sleepAfterOpen, readTimeout, DefaultTimeout);
    }

    public SerialPortConfig(String portName, int baudRate, IResetBehavior preOpenResetBehavior, IResetBehavior postOpenResetBehavior, IResetBehavior closeResetAction, int sleepAfterOpen) {
        this(portName, baudRate, preOpenResetBehavior, postOpenResetBehavior, closeResetAction, sleepAfterOpen, DefaultTimeout, DefaultTimeout);
    }

    public SerialPortConfig(String portName, int baudRate, IResetBehavior preOpenResetBehavior, IResetBehavior postOpenResetBehavior, IResetBehavior closeResetAction) {
        this(portName, baudRate, preOpenResetBehavior, postOpenResetBehavior, closeResetAction, 0, DefaultTimeout, DefaultTimeout);
    }

    //ORIGINAL LINE: public SerialPortConfig(string portName, int baudRate, IResetBehavior preOpenResetBehavior, IResetBehavior postOpenResetBehavior, IResetBehavior closeResetAction, int sleepAfterOpen = 0, int readTimeout = DefaultTimeout, int writeTimeout = DefaultTimeout)
    public SerialPortConfig(String portName, int baudRate, IResetBehavior preOpenResetBehavior, IResetBehavior postOpenResetBehavior, IResetBehavior closeResetAction, int sleepAfterOpen, int readTimeout, int writeTimeout) {
        setPortName(portName);
        setBaudRate(baudRate);
        setPreOpenResetBehavior(preOpenResetBehavior);
        setPostOpenResetBehavior(postOpenResetBehavior);
        setCloseResetAction(closeResetAction);
        setSleepAfterOpen(sleepAfterOpen);
        setReadTimeOut(readTimeout);
        setWriteTimeOut(writeTimeout);
    }

    private String PortName;

    public final String getPortName() {
        return PortName;
    }

    public final void setPortName(String value) {
        PortName = value;
    }

    private int BaudRate;

    public final int getBaudRate() {
        return BaudRate;
    }

    public final void setBaudRate(int value) {
        BaudRate = value;
    }

    private IResetBehavior PreOpenResetBehavior;

    public final IResetBehavior getPreOpenResetBehavior() {
        return PreOpenResetBehavior;
    }

    public final void setPreOpenResetBehavior(IResetBehavior value) {
        PreOpenResetBehavior = value;
    }

    private IResetBehavior PostOpenResetBehavior;

    public final IResetBehavior getPostOpenResetBehavior() {
        return PostOpenResetBehavior;
    }

    public final void setPostOpenResetBehavior(IResetBehavior value) {
        PostOpenResetBehavior = value;
    }

    private IResetBehavior CloseResetAction;

    public final IResetBehavior getCloseResetAction() {
        return CloseResetAction;
    }

    public final void setCloseResetAction(IResetBehavior value) {
        CloseResetAction = value;
    }

    private int SleepAfterOpen;

    public final int getSleepAfterOpen() {
        return SleepAfterOpen;
    }

    public final void setSleepAfterOpen(int value) {
        SleepAfterOpen = value;
    }

    private int ReadTimeOut;

    public final int getReadTimeOut() {
        return ReadTimeOut;
    }

    public final void setReadTimeOut(int value) {
        ReadTimeOut = value;
    }

    private int WriteTimeOut;

    public final int getWriteTimeOut() {
        return WriteTimeOut;
    }

    public final void setWriteTimeOut(int value) {
        WriteTimeOut = value;
    }
}