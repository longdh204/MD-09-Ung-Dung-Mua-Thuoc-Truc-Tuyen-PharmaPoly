package com.md09.pharmapoly.Models;

public class DistrictRequest {
    private int province_ID;

    public DistrictRequest(int province_ID) {
        this.province_ID = province_ID;
    }

    public int getProvince_ID() {
        return province_ID;
    }

    public void setProvince_ID(int province_ID) {
        this.province_ID = province_ID;
    }
}
