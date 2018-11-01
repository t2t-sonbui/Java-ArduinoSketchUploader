package ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.Messages;

import java.io.UnsupportedEncodingException;

import ArduinoUploader.*;
import ArduinoUploader.BootloaderProgrammers.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.*;
import ArduinoUploader.BootloaderProgrammers.Protocols.STK500v2.*;

public class GetSyncResponse extends Response {
    public final boolean getIsInSync() {
        return getBytes().length > 1 && getBytes()[0] == Constants.CmdSignOn && getBytes()[1] == Constants.StatusCmdOk;
    }

    public final String getSignature() {
        byte signatureLength = getBytes()[2];
        byte[] signature = new byte[signatureLength];
        System.arraycopy(getBytes(), 3, signature, 0, signatureLength);

        try {
            return new String(signature, 0, signature.length, "ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}