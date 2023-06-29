package ru.ibsqa.chameleon.utils.os;

import java.util.Locale;

public class OsUtils {

    public static OSTypeEnum getOperatingSystemType() {
        OSTypeEnum detectedOS;
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
            detectedOS = OSTypeEnum.MacOS;
        } else if (OS.equalsIgnoreCase("windows 2003")){
            detectedOS = OSTypeEnum.WindowsServer;
        } else if (OS.indexOf("win") >= 0) {
            detectedOS = OSTypeEnum.Windows;
        } else {
            detectedOS = OSTypeEnum.Other;
        }
        return detectedOS;
    }

    public static OSBitEnum getOSBit(){
        String bitOS = System.getProperty("sun.arch.data.model");
        switch (getOperatingSystemType()) {
            case Windows:
                if (bitOS.equals("32"))
                    return OSBitEnum.BIT32;
                else
                    return OSBitEnum.BIT64;
            case WindowsServer:
                return OSBitEnum.BIT32;
        }
        return OSBitEnum.BIT32;
    }

}
