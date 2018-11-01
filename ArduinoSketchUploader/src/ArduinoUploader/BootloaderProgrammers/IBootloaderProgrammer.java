package ArduinoUploader.BootloaderProgrammers;

import ArduinoUploader.Hardware.Memory.*;
import CSharpStyle.IProgress;
import IntelHexFormatReader.Model.*;
import UsbSerialHelper.ISerialPortStream;
import ArduinoUploader.*;

public interface IBootloaderProgrammer<E extends ISerialPortStream> {
    void Open() throws ClassNotFoundException;

    void Close() throws InterruptedException, ClassNotFoundException;

    void EstablishSync();

    void CheckDeviceSignature();

    void InitializeDevice();

    void EnableProgrammingMode();

    void LeaveProgrammingMode();

    void ProgramDevice(MemoryBlock memoryBlock);

    //C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: void ProgramDevice(MemoryBlock memoryBlock, IProgress<double> progress = null);
    void ProgramDevice(MemoryBlock memoryBlock, IProgress<Double> progress);//IProgress nơ is callback

    void LoadAddress(IMemory memory, int offset);

    void ExecuteWritePage(IMemory memory, int offset, byte[] bytes);

    byte[] ExecuteReadPage(IMemory memory);
}