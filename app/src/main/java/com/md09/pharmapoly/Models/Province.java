package com.md09.pharmapoly.Models;

import java.io.Serializable;

public class Province implements Serializable {
    private int ProvinceID;
    private String ProvinceName;

    public Province(int provinceID, String provinceName) {
        ProvinceID = provinceID;
        ProvinceName = provinceName;
    }

    public int getProvinceID() {
        return ProvinceID;
    }

    public void setProvinceID(int provinceID) {
        ProvinceID = provinceID;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    @Override
    public String toString() {
        return "Province{" +
                "ProvinceID=" + ProvinceID +
                ", ProvinceName='" + ProvinceName + '\'' +
                '}';
    }
}
