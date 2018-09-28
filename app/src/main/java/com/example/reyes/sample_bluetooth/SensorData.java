package com.example.reyes.sample_bluetooth;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SensorData {
    private byte START_CHAR_1 = 0x42;
    private byte START_CHAR_2 = 0X4d;
    private byte _FrameLength_H, _FrameLength_L;
    private byte _Data1_H, _Data1_L;
    private byte _Data2_H, _Data2_L;
    private byte _Data3_H, _Data3_L;
    private byte _Data4_H, _Data4_L;
    private byte _Data5_H, _Data5_L;
    private byte _Data6_H, _Data6_L;
    private byte _Data7_H, _Data7_L;
    private byte _Data8_H, _Data8_L;
    private byte _Data9_H, _Data9_L;
    private byte _Data10_H, _Data10_L;
    private byte _Data11_H, _Data11_L;
    private byte _Data12_H, _Data12_L;
    private byte _Data13_H, _Data13_L;
    private byte _Data_Check_H, _Data_Check_L;


    SensorData () {
        _FrameLength_L = (byte)0xFF;
        _FrameLength_H = (byte)0xFF;
        _Data1_L = (byte)0xFF;
        _Data1_H = (byte)0xFF;
        _Data2_L = (byte)0xFF;
        _Data2_H = (byte)0xFF;
        _Data3_L = (byte)0xFF;
        _Data3_H = (byte)0xFF;
        _Data4_L = (byte)0xFF;
        _Data4_H = (byte)0xFF;
        _Data5_L = (byte)0xFF;
        _Data5_H = (byte)0xFF;
        _Data6_L = (byte)0xFF;
        _Data6_H = (byte)0xFF;
        _Data7_L = (byte)0xFF;
        _Data7_H = (byte)0xFF;
        _Data8_L = (byte)0xFF;
        _Data8_H = (byte)0xFF;
        _Data9_L = (byte)0xFF;
        _Data9_H = (byte)0xFF;
        _Data10_L = (byte)0xFF;
        _Data10_H = (byte)0xFF;
        _Data11_L = (byte)0xFF;
        _Data11_H = (byte)0xFF;
        _Data12_L = (byte)0xFF;
        _Data12_H = (byte)0xFF;
        _Data13_L = (byte)0xFF;
        _Data13_H = (byte)0xFF;
        _Data_Check_L = (byte)0xFF;
        _Data_Check_H = (byte)0xFF;
    }

    public byte[] GetAllBytes() {
        return new byte[]{
                START_CHAR_1, START_CHAR_2,
                _FrameLength_H, _FrameLength_L,
                _Data1_H, _Data1_L,
                _Data2_H, _Data2_L,
                _Data3_H, _Data3_L,
                _Data4_H, _Data4_L,
                _Data5_H, _Data5_L,
                _Data6_H, _Data6_L,
                _Data7_H, _Data7_L,
                _Data8_H, _Data8_L,
                _Data9_H, _Data9_L,
                _Data10_H, _Data10_L,
                _Data11_H, _Data11_L,
                _Data12_H, _Data12_L,
                _Data13_H, _Data13_L,
                _Data_Check_H, _Data_Check_L
        };
    }

    public void SetAllBytes(byte[] allData) {
        _FrameLength_L = allData[2];
        _FrameLength_H = allData[3];
        _Data1_L = allData[4];
        _Data1_H = allData[5];
        _Data2_L = allData[6];
        _Data2_H = allData[7];
        _Data3_L = allData[8];
        _Data3_H = allData[9];
        _Data4_L = allData[10];
        _Data4_H = allData[11];
        _Data5_L = allData[12];
        _Data5_H = allData[13];
        _Data6_L = allData[14];
        _Data6_H = allData[15];
        _Data7_L = allData[16];
        _Data7_H = allData[17];
        _Data8_L = allData[18];
        _Data8_H = allData[19];
        _Data9_L = allData[20];
        _Data9_H = allData[21];
        _Data10_L = allData[22];
        _Data10_H = allData[23];
        _Data11_L = allData[24];
        _Data11_H = allData[25];
        _Data12_L = allData[26];
        _Data12_H = allData[27];
        _Data13_L = allData[28];
        _Data13_H = allData[29];
        _Data_Check_L = allData[30];
        _Data_Check_H = allData[31];
    }

    public byte[] GetFrameLength() {
        return new byte[] {
                _FrameLength_H, _FrameLength_L
        };
    }

    public void SetFrameLength(byte frameLength_h, byte frameLength_l) {
        _FrameLength_H = frameLength_h;
        _FrameLength_L = frameLength_l;
    }

    public int GetPmData(byte[] data) {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.put((byte)0x00);
        byteBuffer.put((byte)0x00);
        byteBuffer.put(data);
        byteBuffer.flip();

        return byteBuffer.getInt();
    }

    private byte[] GetData1() {
        return new byte[] {
                _Data1_L, _Data1_H
        };
    }

    private byte[] GetData2() {
        return new byte[] {
                _Data2_L, _Data2_H
        };
    }

    private byte[] GetData3() {
        return new byte[] {
                _Data3_L, _Data3_H
        };
    }

    private byte[] GetData4() {
        return new byte[] {
                _Data4_L, _Data4_H
        };
    }

    private byte[] GetData5() {
        return new byte[] {
                _Data5_L, _Data5_H
        };
    }

    private byte[] GetData6() {
        return new byte[] {
                _Data6_L, _Data6_H
        };
    }

    private byte[] GetData7() {
        return new byte[] {
                _Data7_L, _Data7_H
        };
    }

    private byte[] GetData8() {
        return new byte[] {
                _Data8_L, _Data8_H
        };
    }

    private byte[] GetData9() {
        return new byte[] {
                _Data9_L, _Data9_H
        };
    }

    public String ToString() {
        return "PM 1.0 ug/m3 - (" + GetPmData(GetData1()) + ")" + "\n" +
                "PM 2.5 ug/m3 - (" + GetPmData(GetData2()) + ")" + "\n" +
                "PM 10.0 ug/m3 - (" + GetPmData(GetData3()) + ")" + "\n" +
                "*********************" + "\n" +
                "Under Atmospheric Env." + "\n" +
                "PM 1.0 ug/m3 - (" + GetPmData(GetData4()) + ")" + "\n" +
                "PM 2.5 ug/m3 - (" + GetPmData(GetData5()) + ")" + "\n" +
                "PM ug/m3 - (" + GetPmData(GetData6()) + ")" + "\n" +
                "PM 0.3 um/0.1L - (" + GetPmData(GetData7()) + ")" + "\n" +
                "PM 0.5 um/0.1L - (" + GetPmData(GetData8()) + ")" + "\n" +
                "PM 1.0 um/0.1L - (" + GetPmData(GetData9()) + ")" + "\n";

    }
}
