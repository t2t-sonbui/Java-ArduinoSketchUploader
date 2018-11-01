package CSharpStyle;

import csharpstyle.StringHelper;


public class BitConverter {

    public static String toString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < bytes.length; j++) {
            if (j > 0) sb.append("-");
            sb.append(String.format("%02X", bytes[j]));
        }
        return sb.toString();

//        String[] arrStr = new String[] {"A","B","C","D","E","F"};
//        String texts=StringHelper.join("-", arrStr);
    }


}
