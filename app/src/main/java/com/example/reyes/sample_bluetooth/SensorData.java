package com.example.reyes.sample_bluetooth;

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
        int length = allData.length;

        for(int i = 0; i < length; i++) {
            GetAllBytes()[i] = allData[i];
        }
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

    public byte[] GetPM_1_0_ug() {
        return new byte[] {
                _Data1_H, _Data1_L,
        };
    }

    public void SetPM_1_0_ug(byte data1h, byte data1L) {
        _Data1_H = data1h;
        _Data1_L = data1L;
    }

    public byte[] GetPM_2_5_ug() {
        return new byte[] {
                _Data2_H, _Data2_L,
        };
    }
}
