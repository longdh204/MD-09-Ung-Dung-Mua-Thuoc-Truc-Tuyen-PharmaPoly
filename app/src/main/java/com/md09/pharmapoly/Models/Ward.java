package com.md09.pharmapoly.Models;

import java.io.Serializable;

public class Ward implements Serializable {
    private String WardCode;
    private int DistrictID;
    private String WardName;

    public Ward(String wardCode, int districtID, String wardName) {
        WardCode = wardCode;
        DistrictID = districtID;
        WardName = wardName;
    }

    public String getWardCode() {
        return WardCode;
    }

    public void setWardCode(String wardCode) {
        WardCode = wardCode;
    }

    public int getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(int districtID) {
        DistrictID = districtID;
    }

    public String getWardName() {
        return WardName;
    }

    public void setWardName(String wardName) {
        WardName = wardName;
    }

    @Override
    public String toString() {
        return "Ward{" +
                "WardCode='" + WardCode + '\'' +
                ", DistrictID=" + DistrictID +
                ", WardName='" + WardName + '\'' +
                '}';
    }
}
