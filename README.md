# ArduinoSketchUploader
This is port from  [christophediericx .Net library](https://github.com/twinearthsoftware/ArduinoSketchUploader) using java code
Java library [IntelHexFormatReader](https://github.com/t2t-sonbui/IntelHexFormatReader/tree/java-port) 
This repository contains a Java library  that can be used to upload a compiled sketch (.HEX) directly to an Arduino board over USB. It talks to the boards bootloader over the serial (USB) connection, much like *avrdude* does (when invoked from the Arduino IDE, or from the command line).

![ArduinoSketchUploader](https://github.com/christophediericx/ArduinoSketchUploader/blob/master/Images/ArduinoSketchUploader.png)

## Compatibility ##
The library has been tested with the following configurations:

| Arduino Model | MCU           | Bootloader protocol                                |
| ------------- |:-------------:| -------:|
| Mega 2560     | ATMega2560    | STK500v2|
| Micro         | ATMega32U4    | AVR109  |
| Uno (R3)      | ATMega328P    | STK500v1|

## How to use in your application ##

Create object implement *ISerialPortStream* interface,then usage *anonymous* or passing *clazz* generics for **ArduinoSketchUploader** object :
**Anonymous:**
```
ArduinoSketchUploader<SerialPortStreamImpl> uploader = new ArduinoSketchUploader<SerialPortStreamImpl>(optionsMega,logger,progress) {};
```
**Clazz generics:** 
```
ArduinoSketchUploader<SerialPortStreamImpl> uploader = new ArduinoSketchUploader<SerialPortStreamImpl>(
			SerialPortStreamImpl.class, optionsUno, logger, progress) ;
```
Check "Test" for more example.

[![Arduino Hex Uploader Android App](https://play.google.com/intl/en_us/badges/images/badge_new.png)](https://play.google.com/store/apps/details?id=xyz.vidieukhien.embedded.arduinohexupload)
------
## Like this
- [Donate via Paypal](https://www.paypal.me/sonbuivn) (my paypal email is **thanhson1903** at google's mail)
- Donate via Bitcoin: 1NKBD6b6akaqiggNr3Sv9e6SsMXUFgZ8AX
![alt text][bitcoin]
-  Donate via ETH: 0xefF19390931F3A11DaEC5Db5A336d42148Ae84d1
![alt text][eth]

[bitcoin]: https://i.imgur.com/oD2K5uo.png "1NKBD6b6akaqiggNr3Sv9e6SsMXUFgZ8AX"
[eth]: https://i.imgur.com/MplPLS9.png "0xefF19390931F3A11DaEC5Db5A336d42148Ae84d1"
## Thanks
